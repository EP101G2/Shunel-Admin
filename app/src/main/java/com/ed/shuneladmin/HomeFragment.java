package com.ed.shuneladmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
//<<<<<<< HEAD
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
//=======


import androidx.fragment.app.FragmentActivity;

import androidx.navigation.Navigation;


import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;

//>>>>>>> 596b02847cb064046f97b2c5033dd5f892efa7ea
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ed.shuneladmin.Task.Common;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class HomeFragment extends Fragment {

    private Activity activity;
    private ImageView ivLogout, ivAdmin;
    /*建立viewPage 7.22*/
    TabLayout tabLayout;
    ViewPager2 viewPager;

<<<<<<< HEAD
    private int[] label={R.string.system,R.string.Promotion,R.string.chat};
//=======
//
//    private int[] label={R.string.system,R.string.Product,R.string.Promotion,R.string.msg,R.string.notice};
//
//>>>>>>> d10b0c31dd01221f3d499117e1b7cadfbaa888ef
//=======
//    private int[] label={R.string.system,R.string.Promotion,R.string.chat};
=======

>>>>>>> b92e13f5e63b782b5b12aa5835abc051e8f9f08b


    private int[] label={R.string.system,R.string.Promotion,R.string.chat};


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


<<<<<<< HEAD
//        ivLogout = view.findViewById(R.id.ivLogout);
//        ivAdmin = view.findViewById(R.id.ivAdmin);
//
//        ivLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Logout();
//            }
//        });
//        ivAdmin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_adminFragment);
//            }
//        });
=======
>>>>>>> b92e13f5e63b782b5b12aa5835abc051e8f9f08b

        ivLogout = view.findViewById(R.id.ivLogout);
        ivAdmin = view.findViewById(R.id.ivAdim);


        ivAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_adminFragment);
            }
        });


        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });

<<<<<<< HEAD
=======

>>>>>>> b92e13f5e63b782b5b12aa5835abc051e8f9f08b


        findViews(view);
        /* 初始化資料,包含從其他Activity傳來的Bundle資料 ,Preference資枓 */
        initData();
        /* 設置必要的系統服務元件如: Services、BroadcastReceiver */
        setSystemServices();
        /* 設置View元件對應的linstener事件,讓UI可以與用戶產生互動 */
        setLinstener();


    }

    private void Logout() {
        Common.getPreherences(activity).edit().clear().apply();
        Intent intent = new Intent();
        intent.setClass(activity, LoginActivity.class);   //前放目前ＡＣＴＩＶＩＴＹ，後放目標的ＡＣＴ
        startActivity(intent);
//        if (MainActivity.preferences.edit())
    }

    private void findViews(View view) {

        /*viewPage*/
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tabs);
        /*viewPage*/
    }

    private void initData() {
    }

    private void setSystemServices() {
    }

    private void setLinstener() {

        /*viewPage*/
        viewPager.setAdapter(createCardAdapter());
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(label[position]);
                    }
                }).attach();
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
        /*viewPage*/
    }


    private ViewPagerAdapter createCardAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter((FragmentActivity) activity);
        return adapter;

    }


    /*viewPage 滑動特效 Jack 7.22*/
    private class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }

    }
    /*viewPage 滑動特效 Jack 7.22*/
}
