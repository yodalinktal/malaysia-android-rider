package com.bsmart.pos.rider.base.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class NoScrollViewPager extends ViewPager {

  public NoScrollViewPager(Context context) {
    super(context);
  }

  public NoScrollViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    return false;
  }
}
