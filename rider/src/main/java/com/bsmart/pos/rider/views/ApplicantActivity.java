package com.bsmart.pos.rider.views;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

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
 * 申请新的骑手页面
 * Author: yoda
 * DateTime: 2020/2/29 15:37
 */
public class ApplicantActivity extends BaseActivity {

    @BindView(R.id.header)
    HeaderView header;

    @BindView(R.id.etFullName)
    EditText etFullName;

    @BindView(R.id.etEmail)
    EditText etEmail;

    @BindView(R.id.etContactNumber)
    EditText etContactNumber;

    @BindView(R.id.etIcNumber)
    EditText etIcNumber;

    @BindView(R.id.etAddress)
    EditText etAddress;

    @BindView(R.id.btnApplicant)
    Button btnApplicant;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant);
        ButterKnife.bind(this);
        header.setLeft(view->finish());
        header.setTitle(getResources().getString(R.string.title_activity_applicant));
        btnApplicant.setOnClickListener(onApplicantListener);
    }


    private View.OnClickListener onApplicantListener = view -> {
        performRegister(view);

    };

    private void performRegister(View view) {
        view.setEnabled(false);

        if (TextUtils.isEmpty(etFullName.getText())) {
            ToastUtils.showShort("Confirm FullName must not be empty");
            view.setEnabled(true);
            return;
        }

        if (TextUtils.isEmpty(etIcNumber.getText())) {
            ToastUtils.showShort("IC Number must not be empty");
            view.setEnabled(true);
            return;
        }

        if (TextUtils.isEmpty(etAddress.getText())) {
            ToastUtils.showShort("Address must not be empty");
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


        if (TextUtils.isEmpty(etContactNumber.getText())) {
            ToastUtils.showShort("Confirm Contact Number must not be empty");
            view.setEnabled(true);
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing... Please wait.");
        progressDialog.show();

        Map<String, String> requestData = new HashMap<>();

        requestData.put("fullName", etFullName.getText().toString());
        requestData.put("icNumber", etIcNumber.getText().toString());
        requestData.put("address", etAddress.getText().toString());
        requestData.put("email",etEmail.getText().toString());
        requestData.put("contactNumber", etContactNumber.getText().toString());

        Api.getRectsEA().applicant(requestData)
                .compose(new NetTransformer<>(JsonObject.class))
                .subscribe(new NetSubscriber<>(bean -> {
                    view.setEnabled(true);
                    progressDialog.dismiss();

                    if (null != bean){
                        if (bean.get("errno").getAsInt()==0){

                            //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                            AlertDialog.Builder builder = new AlertDialog.Builder(ApplicantActivity.this);
                            //    设置Title的内容
                            builder.setTitle("Info");
                            //    设置Content来显示一个信息
                            builder.setMessage("Your application has been submitted successfully");
                            //    设置一个PositiveButton
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    openLoginActivity();
                                }
                            });
                            //    显示出该对话框
                            builder.show();


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
