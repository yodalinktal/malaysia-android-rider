package com.bsmart.pos.rider.views;

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
import com.bsmart.pos.rider.base.api.Api;
import com.bsmart.pos.rider.base.api.NetSubscriber;
import com.bsmart.pos.rider.base.api.NetTransformer;
import com.bsmart.pos.rider.base.api.UpgradeHttpException;
import com.bsmart.pos.rider.base.api.bean.RiderBean;
import com.bsmart.pos.rider.base.utils.HeaderView;
import com.bsmart.pos.rider.base.utils.ProfileUtils;
import com.bsmart.pos.rider.base.utils.ToolUtils;
import com.bsmart.pos.rider.base.utils.Utils;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 登录页面
 * Author: yoda
 * DateTime: 2020/2/29 15:37
 */
public class RegisterActivity extends BaseActivity {

    @BindView(R.id.header)
    HeaderView header;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        header.setLeft(view->finish());
        header.setTitle(getResources().getString(R.string.title_activity_Register));
        btnRegister.setOnClickListener(onRegisterListener);

    }


    private View.OnClickListener onRegisterListener = view -> {
        performRegister(view);

    };

    private void performRegister(View view) {
        view.setEnabled(false);
        if (TextUtils.isEmpty(etUsername.getText())) {
            ToastUtils.showShort("Username must not be empty");
            view.setEnabled(true);
            return;
        }

        if (TextUtils.isEmpty(etEmail.getText())) {
            ToastUtils.showShort("Email must not be empty");
            view.setEnabled(true);
            return;
        }else if(!ToolUtils.isEmailValid(etEmail.getText().toString())){
            ToastUtils.showShort("Email Invalid");
            view.setEnabled(true);
            return;
        }

        if (TextUtils.isEmpty(etPassword.getText())) {
            ToastUtils.showShort("Password must not be empty");
            view.setEnabled(true);
            return;
        }

        if (TextUtils.isEmpty(etConfirmPassword.getText())) {
            ToastUtils.showShort("Confirm Password must not be empty");
            view.setEnabled(true);
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Register in... Please wait.");
        progressDialog.show();

        Map<String, String> requestData = new HashMap<>();
        requestData.put("username", etUsername.getText().toString());
        requestData.put("password", etPassword.getText().toString());
        requestData.put("email", etEmail.getText().toString());
        requestData.put("confirmPassword", etConfirmPassword.getText().toString());

        //for test
        //openLoginActivity();

        Api.getRectsEA().register(requestData)
                .compose(new NetTransformer<>(JsonObject.class))
                .subscribe(new NetSubscriber<>(bean -> {
                    view.setEnabled(true);
                    progressDialog.dismiss();

                    if (null != bean){
                        if (bean.get("errno").getAsInt()==0){
                            ProfileUtils.saveProfile(App.gson.fromJson(bean.get("data"),RiderBean.class));
                            ProfileUtils.saveIdAndPassword(etUsername.getText().toString(), etPassword.getText().toString());
                            openLoginActivity();
                        }else{
                            ToastUtils.showShort(bean.get("errmsg").getAsString());
                        }

                    }else{
                        ToastUtils.showShort("Some error happened, Please try again later.");
                    }

                }, e -> {
                    progressDialog.dismiss();

                    if (e instanceof UpgradeHttpException) {
                        Utils.showUpgradeDialog(this, ((UpgradeHttpException) e).getNewVersionName(), ((UpgradeHttpException) e).getDownloadUrl());
                        return;
                    }

                    view.setEnabled(true);
                    ToastUtils.showShort("Some error happened, Please try again later.");
                }));
    }


    private void openLoginActivity() {
        progressDialog.dismiss();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
