package com.ed.shuneladmin;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final int CARD_ITEM_SIZE = 3;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        //帶建立的fragment
        switch (position){
            case 0: //促銷
                return new  AdminFragment();
//                        LabelFragment();
            case 1: //系統
                return new noticeListAdimFragment();
            case 2: //訊息
                return new Member_newsFragment();
        }
        return LabelFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return CARD_ITEM_SIZE;
    }
}
