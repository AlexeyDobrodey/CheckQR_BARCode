package com.example.user.checkqrtickets.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.user.checkqrtickets.R;
import com.example.user.checkqrtickets.activities.InfoTicketActivity;
import com.example.user.checkqrtickets.asynctasks.AsyncFindTicket;
import com.example.user.checkqrtickets.entities.Json;
import com.example.user.checkqrtickets.entities.Client;
import com.example.user.checkqrtickets.entities.Ticket;
import com.example.user.checkqrtickets.utils.Utils;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.SymbolSet;

import java.io.IOException;

/**
 * Created by User on 23.06.2016.
 */
public class SearchQRCodeFragment extends Fragment{

    private static final String KEY_ID_REPRESENTATION = "id_representation";
    private int mIdRepresentation;

    private SurfaceView mSV;
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private HolderCallBack mHolderCallBack;


    private Handler mHandler;
    private boolean previewing;


    private final int CAMERA_ID = 0;

    private Button mButtonSendIdTicket;
    private TextInputLayout mCodeTicketWrapper;
    private EditText mInputCodeTicket;




    //ZBAR lib
    private ImageScanner mIMGScanner;
    private Image mCodeImage;

    static {
        System.loadLibrary("iconv");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            mIdRepresentation = getArguments().getInt(KEY_ID_REPRESENTATION, -1);
        }
        Utils.hideKeyboard(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_qr_code_fragment, container, false);

        mSV = (SurfaceView) view.findViewById(R.id.camera_surface_view);
        mHolder = mSV.getHolder();

        mButtonSendIdTicket = (Button) view.findViewById(R.id.btn_send_id_ticket);
        mButtonSendIdTicket.setEnabled(false);
        mButtonSendIdTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToServer(mInputCodeTicket.getText().toString());
            }
        });

        mCodeTicketWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_id_ticket);
        mCodeTicketWrapper.setHint("Ticket Code");
        mInputCodeTicket = (EditText) view.findViewById(R.id.input_code_ticket);
        mInputCodeTicket.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String textCodeTicket = s.toString();
                if(textCodeTicket.length() == 10)
                    mButtonSendIdTicket.setEnabled(true);
                else
                    mButtonSendIdTicket.setEnabled(false);
            }
        });


        mHolderCallBack = new HolderCallBack();
        mHolder.addCallback(mHolderCallBack);

        mIMGScanner = new ImageScanner();

        mHandler = new Handler();

        mIMGScanner = new ImageScanner();
        mIMGScanner.setConfig(0, Config.X_DENSITY, 3);
        mIMGScanner.setConfig(0, Config.Y_DENSITY, 3);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraResume();
    }



    @Override
    public void onPause() {
        super.onPause();
        cameraRelease();
    }

    private void cameraResume() {
        mCamera = Camera.open(CAMERA_ID);
        Camera.Size size = mCamera.getParameters().getPreviewSize();
        mCodeImage = new Image(size.width, size.height, "Y800");
        previewing = true;
        mSV.refreshDrawableState();

    }

    private void cameraRelease() {
        if(mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.cancelAutoFocus();
            mCamera.stopPreview();
            mCamera.release();

            previewing = false;
        }
        mCamera = null;
    }

    public static SearchQRCodeFragment newInstance(int id_representation) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ID_REPRESENTATION, id_representation);
        SearchQRCodeFragment fragment = new SearchQRCodeFragment();
        fragment.setArguments(bundle);
        return  fragment;
    }



    class HolderCallBack implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            try {
                mCamera.stopPreview();
                mCamera.setPreviewCallbackWithBuffer(null);
                mCamera.setPreviewDisplay(holder);
                correctPreviewSize();

                byte[] cameraBuffer;
                int imageFormat = mCamera.getParameters().getPreviewFormat();
                int bufferSize = mCamera.getParameters().getPreviewSize().width * mCamera.getParameters().getPreviewSize().height * ImageFormat.getBitsPerPixel(imageFormat) / 8;
                cameraBuffer = new byte[bufferSize];

                mCamera.setPreviewCallbackWithBuffer(previewCallback);
                mCamera.addCallbackBuffer(cameraBuffer);

                mCamera.startPreview();
                mCamera.autoFocus(autoFocusCallback);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

    private void correctPreviewSize() {
        Camera.Size previewSize = mCamera.getParameters().getPreviewSize();
        float aspect = (float) previewSize.width / previewSize.height;

        int previewSurfaceWidth = mSV.getWidth();
        int previewSurfaceHeight = mSV.getHeight();


        ViewGroup.LayoutParams lp = mSV.getLayoutParams();

        if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            mCamera.setDisplayOrientation(90);
            lp.height = previewSurfaceHeight;
            lp.width = (int) (previewSurfaceHeight / aspect);
        }
        else {
            mCamera.setDisplayOrientation(0);
            lp.width = previewSurfaceWidth;
            lp.height = (int) (previewSurfaceWidth / aspect);
        }
        mSV.setLayoutParams(lp);
    }

    Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            mCodeImage.setData(data);
            int result = mIMGScanner.scanImage(mCodeImage);
            if(result != 0) {
                SymbolSet symbols = mIMGScanner.getResults();
                String lastScannedCode = null;

                lastScannedCode = symbols.iterator().next().getData();
                sendDataToServer(lastScannedCode);
            }
            mCamera.addCallbackBuffer(data);
        }
    };


    private Runnable doAutoFocus = new Runnable() {
        @Override
        public void run() {
            if(mCamera != null && previewing)
                mCamera.autoFocus(autoFocusCallback);
        }
    };

    Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            mHandler.postDelayed(doAutoFocus, 1000);
        }
    };




    private void sendDataToServer(String textCode) {
        try {
            AsyncFindTicket asyncHash = new AsyncFindTicket();
            String jsonString = asyncHash.execute(textCode).get();

            if(jsonString == null) {
                createWarningDialog(getString(R.string.msg_not_found_ticket));
            }
            else if(jsonString.equals(Client.NOT_CONNECT_TO_INTERNET)) {
                createWarningDialog(getString(R.string.msg_not_connect_to_internet));
            }
            else {
                Ticket ticket = Json.getTicket(jsonString);
                if(ticket == null) {
                    createWarningDialog(getString(R.string.msg_error_parsing_json));
                }
                else if(ticket.getID() != mIdRepresentation) {
                    createWarningDialog(getString(R.string.msg_warning_ticket));
                }
                else {
                    Intent intent = InfoTicketActivity.newIntent(getActivity(), ticket);
                    startActivity(intent);
                }
            }
        } catch(Exception ex) {
            Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    private void createWarningDialog(String msg) {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.title_warning_dialog)
                .titleColorRes(R.color.colorWarning)
                .content(msg)
                .positiveColorRes(R.color.colorPrimaryDark)
                .positiveText(android.R.string.ok)
                .show();
    }


}
