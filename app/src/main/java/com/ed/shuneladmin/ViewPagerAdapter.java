package com.ed.shuneladmin;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final int CARD_ITEM_SIZE = 4;
    public ViewPagerAdapter(@NonNull Activity fragmentActivity) {
        super((FragmentActivity) fragmentActivity);
    }
    @NonNull @Override public Fragment createFragment(int position) {

        //帶建立的fragment
        switch (position){
            case 0: //系統
                return new LabelFragment();
            case 1: //熱門
                return new UserFragment();
            case 2: //促銷
                return new LabelFragment();
            case 3: //訊息
                return new Member_newsFragment();

        }
        return LabelFragment.newInstance(position);
    }
    @Override public int getItemCount() {
        return CARD_ITEM_SIZE;
    }
}
