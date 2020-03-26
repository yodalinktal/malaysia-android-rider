package com.bsmart.pos.rider.views;

import android.app.ProgressDialog;
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
import com.bsmart.pos.rider.base.utils.HeaderView;
import com.bsmart.pos.rider.base.utils.ProfileUtils;
import com.bsmart.pos.rider.base.utils.ToolUtils;
import com.google.gson.JsonObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * reset password
 * Author: yoda
 * DateTime: 2020/2/29 15:37
 */
public class ResetPasswordActivity extends BaseActivity {

    @BindView(R.id.header)
    HeaderView header;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.btnReset)
    Button btnReset;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);
        header.setLeft(view->finish());
        header.setTitle(getResources().getString(R.string.title_activity_forget_password));
        btnReset.setOnClickListener(onResetListener);

    }


    private View.OnClickListener onResetListener = view -> {

        performReset(view);

    };

    private void performReset(View view) {
        view.setEnabled(false);

        if (TextUtils.isEmpty(etEmail.getText())) {
            ToastUtils.showShort("Email must not be empty");
            view.setEnabled(true);
            return;
        }else if(!ToolUtils.isEmailValid(etEmail.getText().toString())){
            ToastUtils.showShort("Email Invalid");
            view.setEnabled(true);
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing... Please wait.");
        progressDialog.show();

        Map<String, String> requestData = App.getMetaRequestData();
        requestData.put("token", ProfileUtils.getToken());
        requestData.put("email", etEmail.getText().toString());

        //for test
        //openLoginActivity();

        Api.getRectsEA().resetPassword(requestData)
                .compose(new NetTransformer<>(JsonObject.class))
                .subscribe(new NetSubscriber<>(bean -> {
                    view.setEnabled(true);
                    progressDialog.dismiss();
                    if (null != bean){

                        if (bean.get("errno").getAsInt()==0){
                            openLoginActivity();
                        }else{
                            ToastUtils.showShort(bean.get("errmsg").getAsString());
                        }


                    }else{
                        ToastUtils.showShort("Some error happened, Please try again later.");
                    }

                }, e -> {
                    progressDialog.dismiss();
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
