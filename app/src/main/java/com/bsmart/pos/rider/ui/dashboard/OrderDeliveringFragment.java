package com.bsmart.pos.rider.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.bsmart.pos.rider.R;
import com.bsmart.pos.rider.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author: yoda
 * DateTime: 2020/3/7 17:19
 */
public class OrderDeliveringFragment extends BaseFragment {

    private View rootView;
    Unbinder unbinder;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_order_delivering, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
