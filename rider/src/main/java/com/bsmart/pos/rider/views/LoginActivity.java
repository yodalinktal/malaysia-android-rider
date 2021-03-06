package com.bsmart.pos.rider.views;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.bsmart.pos.rider.R;
import com.bsmart.pos.rider.base.App;
import com.bsmart.pos.rider.base.BaseActivity;
import com.bsmart.pos.rider.base.LocationUtils;
import com.bsmart.pos.rider.base.api.Api;
import com.bsmart.pos.rider.base.api.NetSubscriber;
import com.bsmart.pos.rider.base.api.NetTransformer;
import com.bsmart.pos.rider.base.api.UpgradeHttpException;
import com.bsmart.pos.rider.base.api.bean.RiderServerBean;
import com.bsmart.pos.rider.base.utils.HeaderView;
import com.bsmart.pos.rider.base.utils.ProfileUtils;
import com.bsmart.pos.rider.base.utils.Utils;
import com.bsmart.pos.rider.tools.StringUtil;
import com.google.gson.JsonObject;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

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
    @BindView(R.id.registerGuide)
    TextView registerGuide;
    @BindView(R.id.resetGuide)
    TextView resetGuide;
    @BindView(R.id.applicantGuide)
    TextView applicantGuide;
    @BindView(R.id.usernameZone)
    LinearLayout usernameZone;
    @BindView(R.id.passwordZone)
    LinearLayout passwordZone;
    ProgressDialog progressDialog;

    @BindView(R.id.showPassword)
    ImageView showPassword;

    boolean isChecked = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        header.setTitle(getResources().getString(R.string.title_activity_Login));
        btnLogin.setOnClickListener(onLoginListener);
        registerGuide.setOnClickListener(onRegisterGuideListener);
        resetGuide.setOnClickListener(onResetListener);
        applicantGuide.setOnClickListener(onApplicantListener);
        requestPermissions();
        checkToken();
        showPassword.setOnClickListener(view ->{
            if (isChecked){
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }else{
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
            isChecked = !isChecked;
        });
    }

    private void setViewCtrl(int ctrl){
        header.setVisibility(ctrl);
        usernameZone.setVisibility(ctrl);
        passwordZone.setVisibility(ctrl);
        btnLogin.setVisibility(ctrl);
        registerGuide.setVisibility(ctrl);
        resetGuide.setVisibility(ctrl);
        applicantGuide.setVisibility(ctrl);
    }

    private void checkToken(){

        String token = ProfileUtils.getToken();

        if (StringUtil.isNotEmpty(token)){

            Map<String, String> requestData = new HashMap<>();
            requestData.put("token",token);
            //start valid token is ok
            Api.getRectsEA().validate(requestData)
                    .compose(new NetTransformer<>(JsonObject.class))
                    .subscribe(new NetSubscriber<>(bean -> {

                                if (null != bean){

                                    if (bean.get("errno").getAsInt()==0){

                                        openMainActivity();

                                    }else{

                                        //需要进行登录
                                        setViewCtrl(View.VISIBLE);
                                    }

                                }else{
                                    setViewCtrl(View.VISIBLE);
                                    ToastUtils.showShort("Some error happened, Please try again later.");
                                }



                            }, e -> {
                                setViewCtrl(View.VISIBLE);
                                ToastUtils.showShort("Some error happened, Please try again later.");
                            }
                            )
                    );


        }else{
            setViewCtrl(View.VISIBLE);
        }
    }

    public void requestPermissions() {

        new RxPermissions(this).request(Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe();
    }

    private View.OnClickListener onRegisterGuideListener = view -> {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    };

    private View.OnClickListener onResetListener = view -> {
        Intent intent = new Intent(this,ResetPasswordActivity.class);
        startActivity(intent);
    };

    private View.OnClickListener onApplicantListener = view -> {
        Intent intent = new Intent(this,ApplicantActivity.class);
        startActivity(intent);
    };

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

        //这里开始调用一下定位
        Location mLocation = LocationUtils.getInstance(this.getApplication()).showLocation();
        Log.d("mLocation", ""+mLocation);
        if (null == mLocation){
            LocationUtils.getInstance(this.getApplication()).refreshLocation();
            App.resetLocation();
        }

        Map<String, String> requestData = App.getMetaRequestData();
        requestData.put("username", etUsername.getText().toString());
        requestData.put("password", etPassword.getText().toString());

        Api.getRectsEA().login(requestData)
                .compose(new NetTransformer<>(RiderServerBean.class))
                .subscribe(new NetSubscriber<>(bean -> {
                            view.setEnabled(true);
                            progressDialog.dismiss();

                            if (null != bean){

                                if (bean.getErrno()==0){

                                    ProfileUtils.saveProfile(bean.getData());
                                    ProfileUtils.saveIdAndPassword(etUsername.getText().toString(), etPassword.getText().toString());
                                    openMainActivity();

                                }else{
                                    ToastUtils.showShort(bean.getErrmsg());
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
                        }
                        )
                );
    }


    private void openMainActivity() {
        if (null != progressDialog){
            progressDialog.dismiss();
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //LocationUtils.getInstance(this).removeLocationUpdatesListener();
    }

}
