package com.ed.shuneladmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ed.shuneladmin.Task.Common;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static int flag;
    private ImageView ivAdmin, ivLogout;
    private Activity activity;
    private final static String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //建立bottom
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        final NavController navController = Navigation.findNavController(this, R.id.homeFragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        ivLogout = findViewById(R.id.ivLogout);
        ivAdmin = findViewById(R.id.ivAdmin);


        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });


//        ivAdmin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                NavController navController = Navigation.findNavController(MainActivity.this, R.id.adminFragment);
//                navController.navigate(R.id.adminFragment);

//            }
//        });
    }



//    /**
//     * 递归调用，对所有的子Fragment生效
//     *
//     * @param fragment
//     * @param requestCode
//     * @param resultCode
//     * @param data
//     */
//    private void handleResult(Fragment fragment, int requestCode, int resultCode, Intent data) {
//        fragment.onActivityResult(requestCode, resultCode, data);//调用每个Fragment的onActivityResult
////        Log.e(TAG, "MyBaseFragmentActivity");
//        List<Fragment> childFragment = fragment.getChildFragmentManager().getFragments(); //找到第二层Fragment
//        if (childFragment != null)
//            for (Fragment f : childFragment)
//                if (f != null) {
//                    handleResult(f, requestCode, resultCode, data);
//                }
//        if (childFragment == null)
//            Log.e(TAG, "MyBaseFragmentActivity1111");
//    }


    private void Logout() {
        Common.getPreherences(activity).edit().clear().apply();
        Intent intent = new Intent();
        intent.setClass(activity, LoginActivity.class);   //前放目前ＡＣＴＩＶＩＴＹ，後放目標的ＡＣＴ
        startActivity(intent);
//        if (MainActivity.preferences.edit())


    }

}
