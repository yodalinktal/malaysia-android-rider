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
import com.bsmart.pos.rider.base.utils.HeaderView;

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
        header.setTitle(getResources().getString(R.string.title_activity_Register));
        btnRegister.setOnClickListener(onRegisterListener);

    }


    private View.OnClickListener onRegisterListener = view -> {


    };

    private void performRegister(View view) {
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

        if (TextUtils.isEmpty(etConfirmPassword.getText())) {
            ToastUtils.showShort("Confirm Password must not be empty");
            view.setEnabled(true);
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Register in... Please wait.");
        progressDialog.show();

        Map<String, String> requestData = App.getMetaRequestData();
        requestData.put("officer_id", etUsername.getText().toString());
        requestData.put("officer_password", etPassword.getText().toString());

        //for test
        openLoginActivity();

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


    private void openLoginActivity() {
        progressDialog.dismiss();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
