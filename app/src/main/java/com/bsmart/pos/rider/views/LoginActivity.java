package com.bsmart.pos.rider.views;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.bsmart.pos.rider.R;
import com.bsmart.pos.rider.base.App;
import com.bsmart.pos.rider.base.BaseActivity;
import com.bsmart.pos.rider.base.LocationUtils;
import com.bsmart.pos.rider.base.utils.HeaderView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author: yoda
 * DateTime: 2020/3/6 23:06
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.header)
    HeaderView header;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        header.setTitle(getResources().getString(R.string.title_activity_Login));
        btnLogin.setOnClickListener(onLoginListener);

        requestPermissions();

    }

    public void requestPermissions() {

        new RxPermissions(this).request(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe();
    }

    private View.OnClickListener onLoginListener = view -> {

        new RxPermissions(this).request(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        performLogin(view);
                    } else {
                        ToastUtils.showShort("Please allow the permission");
                    }
                });
    };

    private void performLogin(View view) {
        view.setEnabled(false);
        if (TextUtils.isEmpty(etUsername.getText())) {
            ToastUtils.showShort("Username must not be empty");
            view.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(etPassword.getText())) {
            ToastUtils.showShort("Password must not be empty");
            view.setEnabled(true);
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sign in... Please wait.");
        progressDialog.show();

        Map<String, String> requestData = App.getMetaRequestData();
        requestData.put("officer_id", etUsername.getText().toString());
        requestData.put("officer_password", etPassword.getText().toString());

        //for test
        openMainActivity();

//        Api.getRectsEA().login(requestData)
//                .compose(new NetTransformer<>(LoginBean.class))
//                .subscribe(new NetSubscriber<>(bean -> {
//                    view.setEnabled(true);
//                    progressDialog.dismiss();
//
//                    ProfileUtils.saveProfile(bean.profile);
//                    ProfileUtils.saveIdAndPassword(etUsername.getText().toString(), etPassword.getText().toString());
//
//                    openMainActivity();
//                }, e -> {
//                    progressDialog.dismiss();
//
//                    if (e instanceof UpgradeHttpException) {
//                        Utils.showUpgradeDialog(this, ((UpgradeHttpException) e).getNewVersionName(), ((UpgradeHttpException) e).getDownloadUrl());
//                        return;
//                    }
//
//                    view.setEnabled(true);
//                    ToastUtils.showShort("Some error happened, Please try again later.");
//                }));
    }


    private void openMainActivity() {
        progressDialog.dismiss();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationUtils.getInstance(this).removeLocationUpdatesListener();
    }

}
