package com.example.user.checkqrtickets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.user.checkqrtickets.R;
import com.example.user.checkqrtickets.activities.ListPresentationActivity;
import com.example.user.checkqrtickets.asynctasks.AsyncLogin;
import com.example.user.checkqrtickets.entities.Client;
import com.example.user.checkqrtickets.utils.Utils;

import java.util.concurrent.ExecutionException;

/**
 * Created by User on 21.06.2016.
 */
public class LoginFragment extends Fragment {

    private Button mBtnLogin;
    private TextInputLayout mUsernameWrapper, mPasswordWrapper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        loadHttp();
        Utils.hideKeyboard(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        mUsernameWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_full_name);
        mPasswordWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_password);

        mUsernameWrapper.setHint(getResources().getString(R.string.hint_username));
        mPasswordWrapper.setHint(getResources().getString(R.string.hint_password));

        mBtnLogin = (Button) view.findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = mUsernameWrapper.getEditText().getText().toString();
                String password = mPasswordWrapper.getEditText().getText().toString();

                boolean doLogin = true;
                if(username.isEmpty()) {
                    mUsernameWrapper.setError("input username!!!");
                    doLogin = false;
                }

                if(password.isEmpty()) {
                    mPasswordWrapper.setError("input password!!!");
                    doLogin = false;
                }

                if(doLogin) {
                    String paramsLogin[] = new String[]{username, password};
                    AsyncLogin asyncLogin = new AsyncLogin();

                    Boolean isLogin = new Boolean(false);

                    try {
                        isLogin = asyncLogin.execute(paramsLogin).get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }

                    if(isLogin) {doLogin();}
                    else Toast.makeText(getActivity(), "Ошибка Авторизации", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;

    }

    private void doLogin() {
        mUsernameWrapper.setErrorEnabled(false);
        mPasswordWrapper.setErrorEnabled(false);

        Intent intent = ListPresentationActivity.newIntent(getActivity());
        startActivity(intent);
    }




    private void loadHttp() {
        Utils.readHTTPFromFile(getActivity());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_login, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.item_setting_network: {
                createInputHttpsDialog();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }

    }

    private void createInputHttpsDialog() {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.title_input_path_site_dialog)
                .titleColorRes(R.color.colorPrimaryDark)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(null, Client.HTTP_SITE, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence inputHttps) {
                        Client.HTTP_SITE = inputHttps.toString();
                        Utils.writeHTTPToFile(getActivity(), Client.HTTP_SITE);
                    }
                }).show();
    }
}
