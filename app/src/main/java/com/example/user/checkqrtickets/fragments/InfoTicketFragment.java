package com.example.user.checkqrtickets.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.user.checkqrtickets.R;
import com.example.user.checkqrtickets.asynctasks.AsyncCheckTicket;
import com.example.user.checkqrtickets.entities.Ticket;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by alexey on 05.07.16.
 */
public class InfoTicketFragment extends Fragment {

    private static final String TAG_ARG = InfoTicketFragment.class.getName();


    private Button mCheckTicketBtn;
    private TextView mIdTicketTxtView, mNumPlaceTxtView,mRowPlaceTxtView, mFIOTxtView, mNameRepresentTxtView;

    private Ticket mTicket;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null) {
            mTicket = (Ticket) getArguments().getSerializable(TAG_ARG);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_ticket_fragment, container, false);

        mIdTicketTxtView = (TextView) view.findViewById(R.id.id_ticket_text_view);
        mNumPlaceTxtView = (TextView) view.findViewById(R.id.num_place_txt_view);
        mRowPlaceTxtView = (TextView) view.findViewById(R.id.row_place_txt_view);
        mFIOTxtView = (TextView) view.findViewById(R.id.fio_txt_view);
        mNameRepresentTxtView = (TextView) view.findViewById(R.id.name_represent_txt_view);

        updateInfo();

        mCheckTicketBtn = (Button) view.findViewById(R.id.btn_check_ticket);
        mCheckTicketBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AsyncCheckTicket asyncCheckTicket = new AsyncCheckTicket();
                    String jsonString = asyncCheckTicket.execute(mTicket.getHashCode()).get();

                    if(jsonString == null) {
                        Toast.makeText(getActivity(), getString(R.string.msg_not_connect_to_internet),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    boolean result = new JSONObject(jsonString).getBoolean("value");
                    if(result)
                        showInfoDialog();
                    else
                        showWarningDialog();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return view;
    }

    private void updateInfo() {
        mIdTicketTxtView.setText(String.valueOf(mTicket.getID()));
        mNameRepresentTxtView.setText(mTicket.getNameRepresentation());
        mFIOTxtView.setText(mTicket.getFIO());
        mRowPlaceTxtView.setText(String.valueOf(mTicket.getRow()));
        mNumPlaceTxtView.setText(String.valueOf(mTicket.getNumber()));
    }

    public static InfoTicketFragment newInstance(Ticket ticket) {
        Bundle b = new Bundle();
        b.putSerializable(TAG_ARG, ticket);
        InfoTicketFragment fragment = new InfoTicketFragment();
        fragment.setArguments(b);
        return fragment;
    }

    private void showWarningDialog() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.title_warning_dialog)
                .titleColorRes(R.color.colorWarning)
                .content(R.string.msg_warning_check)
                .positiveColorRes(R.color.colorWarning)
                .positiveText(android.R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        getActivity().finish();
                    }
                })
                .show();
    }


    private void showInfoDialog() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.title_success_dialog)
                .titleColorRes(R.color.colorPrimaryDark)
                .content(R.string.msg_success_check)
                .positiveColorRes(R.color.colorPrimaryDark)
                .positiveText(android.R.string.ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        getActivity().finish();
                    }
                })
                .show();
    }
}
