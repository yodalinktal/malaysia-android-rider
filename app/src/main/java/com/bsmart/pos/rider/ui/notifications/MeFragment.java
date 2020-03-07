package com.bsmart.pos.rider.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.ToastUtils;
import com.bsmart.pos.rider.R;
import com.bsmart.pos.rider.base.BaseFragment;
import com.bsmart.pos.rider.base.BaseQRCodeFragment;
import com.bsmart.pos.rider.base.utils.HeaderView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MeFragment extends BaseQRCodeFragment {

    private MeViewModel meViewModel;
    Unbinder unbinder;

    @BindView(R.id.header)
    HeaderView header;

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

        });
        header.setRightCustomView(customRightView);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}