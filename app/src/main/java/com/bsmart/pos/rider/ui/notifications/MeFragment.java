package com.bsmart.pos.rider.ui.notifications;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.ToastUtils;
import com.bsmart.pos.rider.BuildConfig;
import com.bsmart.pos.rider.R;
import com.bsmart.pos.rider.base.App;
import com.bsmart.pos.rider.base.BaseFragment;
import com.bsmart.pos.rider.base.BaseQRCodeFragment;
import com.bsmart.pos.rider.base.utils.HeaderView;
import com.bsmart.pos.rider.base.utils.ProfileUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MeFragment extends BaseQRCodeFragment {

    private MeViewModel meViewModel;
    Unbinder unbinder;

    @BindView(R.id.header)
    HeaderView header;

    @BindView(R.id.txUsername)
    TextView txUsername;

    @BindView(R.id.versionName)
    TextView versionName;

    @BindView(R.id.logoutZone)
    RelativeLayout logoutZone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        meViewModel =
                ViewModelProviders.of(this).get(MeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_me, container, false);
        unbinder = ButterKnife.bind(this, root);

        header.setTitle(getResources().getString(R.string.title_me));
        View customRightView = LayoutInflater.from(getContext()).inflate(R.layout.action_right, null);
        customRightView.findViewById(R.id.flRefresh).setOnClickListener(v -> {
            performQRCode();
        });
        header.setRightCustomView(customRightView);

        txUsername.setText(ProfileUtils.getUsername());
        versionName.setText("Version:"+BuildConfig.VERSION_NAME);

        logoutZone.setOnClickListener(view ->{
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Confirm");
            builder.setMessage("Are you sure Logout?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //todo:
                    App.exit();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.show();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
