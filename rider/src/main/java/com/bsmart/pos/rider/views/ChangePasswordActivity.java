package com.bsmart.pos.rider.views;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.bsmart.pos.rider.base.utils.Utils;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * reset password页面
 * Author: yoda
 * DateTime: 2020/2/29 15:37
 */
public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.header)
    HeaderView header;
    @BindView(R.id.etOrgPassword)
    EditText etOrgPassword;
    @BindView(R.id.etNewPassword)
    EditText etNewPassword;
    @BindView(R.id.etConfirmPassword)
    EditText etConfirmPassword;
    @BindView(R.id.btnConfirm)
    Button btnConfirm;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.addActivity(this);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        header.setLeft(view->finish());
        header.setTitle(getResources().getString(R.string.title_activity_reset_password));
        btnConfirm.setOnClickListener(onConfirmListener);

    }

    private View.OnClickListener onConfirmListener = view -> {
        performConfirm(view);
    };

    private void performConfirm(View view) {
        view.setEnabled(false);
        if (TextUtils.isEmpty(etOrgPassword.getText())) {
            ToastUtils.showShort("old password must not be empty");
            view.setEnabled(true);
            return;
        }
        if (TextUtils.isEmpty(etNewPassword.getText())) {
            ToastUtils.showShort("new password must not be empty");
            view.setEnabled(true);
            return;
        }

        if (TextUtils.isEmpty(etConfirmPassword.getText())) {
            ToastUtils.showShort("Confirm Password must not be empty");
            view.setEnabled(true);
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Reset... Please wait.");
        progressDialog.show();

        Map<String, String> requestData = new HashMap<>();

        requestData.put("token", ProfileUtils.getToken());
        requestData.put("orgPassword", etOrgPassword.getText().toString());
        requestData.put("newPassword", etNewPassword.getText().toString());
        requestData.put("confirmPassword", etConfirmPassword.getText().toString());

        Api.getRectsEA().changePassword(requestData)
                .compose(new NetTransformer<>(JsonObject.class))
                .subscribe(new NetSubscriber<>(bean -> {
                    progressDialog.dismiss();
                    view.setEnabled(true);
                    if (null != bean){

                        if (bean.get("errno").getAsInt()==0){
                            finish();
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
