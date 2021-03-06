package com.bsmart.pos.rider.base;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ToastUtils;
import com.bsmart.pos.rider.views.ConfirmDeliveryActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.ByteArrayOutputStream;

/**
 * Author: yoda
 * DateTime: 2020/2/29 16:56
 */
public class BaseQRCodeFragment extends Fragment {

    public static final String ORDERINFO = "orderInfo";

    public static final int REQ_QR_CODE = 101;
    public static final int REQ_SEAL_PHOTO_CODE = 201;
    public static final int REQ_CARGO_PHOTO_CODE = 301;
    public static final int REQ_QR_CONFIRM = 401;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.VIBRATE)
                .subscribe(granted -> {
                    if (!granted) {
                        ToastUtils.showShort("please grant the permission.");
                    }
                });
    }

    public void performQRCode() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.VIBRATE)
                .subscribe(granted -> {
                    if (granted) {
                        Intent intent = new Intent(getContext(), CaptureActivity.class);
                        startActivityForResult(intent, REQ_QR_CODE);
                    } else {
                        ToastUtils.showShort("please grant the permission.");
                    }
                });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle bundle = null;
        if (null != data) {
            bundle = data.getExtras();
            if (bundle == null) {
                return;
            }
        }

        if (null == bundle){
            return;
        }

        if (requestCode == REQ_QR_CODE) {
            if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                String result = bundle.getString(CodeUtils.RESULT_STRING);
                Log.d("QRCode",result);

                Intent intent = new Intent(getContext(), ConfirmDeliveryActivity.class);
                intent.putExtra(ORDERINFO,result);
                getParentFragment().getActivity().startActivityForResult(intent,REQ_QR_CONFIRM);

            } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                ToastUtils.showShort("Some error happened");
            }
        }

        if (requestCode == REQ_QR_CONFIRM){
            Log.d("REQ_QR_CONFIRM",null!=data?data.toString():"REQ_QR_CONFIRM");
        }

    }


}
