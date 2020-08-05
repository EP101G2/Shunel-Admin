package com.ed.shuneladmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ed.shuneladmin.Task.Common;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private ImageView  ivLogout;
    private Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //建立bottom
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        final NavController navController = Navigation.findNavController(this, R.id.homeFragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
//        ivLogout = findViewById(R.id.ivLogout);
//
//
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
//                Intent intent = new Intent();
//                NavController navController = Navigation.findNavController(MainActivity.this, R.id.adminFragment);
//                navController.navigate(R.id.adminFragment);

//            }
//        });
    }


//    private void Logout() {
//        Common.getPreherences(activity).edit().clear().apply();
//        Intent intent = new Intent();
//        intent.setClass(activity, LoginActivity.class);   //前放目前ＡＣＴＩＶＩＴＹ，後放目標的ＡＣＴ
//        startActivity(intent);
////        if (MainActivity.preferences.edit())
//
//
//    }

}
