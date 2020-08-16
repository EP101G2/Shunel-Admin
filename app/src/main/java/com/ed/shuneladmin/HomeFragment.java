package com.ed.shuneladmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
//<<<<<<< HEAD
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
//=======


import androidx.fragment.app.FragmentActivity;

import androidx.navigation.Navigation;


import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;

//>>>>>>> 596b02847cb064046f97b2c5033dd5f892efa7ea
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ed.shuneladmin.Task.Common;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class HomeFragment extends Fragment {

    private Activity activity;
    private ImageView ivLogout, ivAdmin;





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


        //建立bottom
        BottomNavigationView bottomNavigationView = view.findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(activity, R.id.fragment);//推播的基底頁面設置
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
//        ivLogout = view.findViewById(R.id.ivLogout);
//        ivAdmin = view.findViewById(R.id.ivAdmin);

//        ivAdmin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_adminFragment);
//            }
//        });
//
//        ivLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Logout();
//            }
//        });
        ivLogout = view.findViewById(R.id.ivLogout);



        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });


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

    }

    private void initData() {
    }

    private void setSystemServices() {
    }

    private void setLinstener() {




    }


    private ViewPagerAdapter createCardAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter((FragmentActivity) activity);
        return adapter;

    }


}
