package com.bsmart.pos.rider.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.ToastUtils;
import com.bsmart.pos.rider.R;
import com.bsmart.pos.rider.base.BaseFragment;
import com.bsmart.pos.rider.base.BaseQRCodeFragment;
import com.bsmart.pos.rider.base.utils.HeaderView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class OrderFragment extends BaseQRCodeFragment {

    /**
     * UNPAID(0,"unpaid"),
     *     WAITING(1,"Waiting"),
     *     ACCEPTED(2,"Accepted"),
     *     DELIVERING(3,"Delivering"),
     *     FINISHED(4,"Finished"),
     *     PAYMENTFAILED(98,"PaymentFailed"),
     *     CANCELED(99,"Canceled");
     */
    public final static Integer STATUS_WAITING = 1;
    public final static Integer STATUS_ACCEPTED = 2;
    public final static Integer STATUS_DELIVERING = 3;
    public final static Integer STATUS_FINISHED = 4;
    public final static Integer STATUS_CANCELED = 99;


    private OrderViewModel orderViewModel;
    @BindView(R.id.header) HeaderView header;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    Unbinder unbinder;
    private View rootView;

    private OrderAcceptedFragment orderAcceptedFragment = new OrderAcceptedFragment();
    private OrderDeliveringFragment orderDeliveringFragment = new OrderDeliveringFragment();
    private OrderFinishedFragment orderFinishedFragment = new OrderFinishedFragment();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderViewModel =
                ViewModelProviders.of(this).get(OrderViewModel.class);
        rootView = inflater.inflate(R.layout.fragment_order, container, false);

        unbinder = ButterKnife.bind(this, rootView);

        header.setTitle(getResources().getString(R.string.title_order));
        View customRightView = LayoutInflater.from(getContext()).inflate(R.layout.action_right, null);
        customRightView.findViewById(R.id.flRefresh).setOnClickListener(v -> {
            performQRCode();
        });
        header.setRightCustomView(customRightView);


        MyAdapter myAdapter = new MyAdapter(getChildFragmentManager());
        viewPager.setAdapter(myAdapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(myAdapter);

        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }

    private class MyAdapter extends FragmentPagerAdapter
            implements ViewPager.OnPageChangeListener {

        private List<BaseFragment> fragments = new ArrayList<>();

        public MyAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(orderAcceptedFragment);
            fragments.add(orderDeliveringFragment);
            fragments.add(orderFinishedFragment);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Accepted Jobs";
                case 1:
                    return "Delivery Jobs";
                case 2:
                    return "Finished Jobs";
                default:
                    return "";
            }
        }

        @Override
        public void onPageScrolled(int position,
                                   float positionOffset,
                                   int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            switch (position) {
                case 0:
                    OrderAcceptedFragment orderAcceptedFragment= (OrderAcceptedFragment)getItem(0);
                    orderAcceptedFragment.refresh();
                    break;
                case 1:
                    OrderDeliveringFragment orderDeliveringFragment= (OrderDeliveringFragment)getItem(1);
                    orderDeliveringFragment.refresh();
                    break;
                case 2:
                   OrderFinishedFragment orderFinishedFragment= (OrderFinishedFragment)getItem(2);
                   orderFinishedFragment.refresh();
                    break;
                default:
                    ;
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
