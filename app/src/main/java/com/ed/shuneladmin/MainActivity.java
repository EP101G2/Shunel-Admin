package com.ed.shuneladmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.bean.ChatMessage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.List;

import static com.ed.shuneladmin.CommonTwo.loadUserName;

public class MainActivity extends AppCompatActivity {

    public static int flag;
    private ImageView ivAdmin, ivLogout;

    private Activity activity;
    private final static String TAG = "MainActivity";


    //廣播接收
    private LocalBroadcastManager broadcastManager;
    //通知訊息
    private final static int NOTIFICATION_ID = 0;
    private final static String NOTIFICATION_CHANNEL_ID = "Channel01";
    private NotificationManager notificationManager;
    static AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* jack-------------------------------------------------------------------------------------------*/
        //一開APP就連上聊天室
        CommonTwo.connectServer(this, loadUserName(this));
        //通知
        notificationManager = (NotificationManager) MainActivity.this.getSystemService(NOTIFICATION_SERVICE);

        // 初始化LocalBroadcastManager並註冊BroadcastReceiver
        broadcastManager = LocalBroadcastManager.getInstance(MainActivity.this);
        registerChatReceiver();

        /* jack-------------------------------------------------------------------------------------------*/





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

    private void registerChatReceiver() {

        IntentFilter chatFilter = new IntentFilter("chat");
        broadcastManager.registerReceiver(chatReceiver, chatFilter);


    }



    // 接收到聊天訊息會在TextView呈現
    private BroadcastReceiver chatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            ChatMessage chatMessage = new Gson().fromJson(message, ChatMessage.class);

            chatMessage.setFlag(1);
            // 接收到聊天訊息，若發送者與目前聊天對象相同，就將訊息顯示在TextView

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // 重要性越高，提示(打擾)user方式就越明確，設為IMPORTANCE_HIGH會懸浮通知並發出聲音
                NotificationChannel notificationChannel = new NotificationChannel(
                        NOTIFICATION_CHANNEL_ID,
                        "MyNotificationChannel",
                        NotificationManager.IMPORTANCE_HIGH);
                // 如果裝置有支援，開啟指示燈
                notificationChannel.enableLights(true);
                // 設定指示燈顏色
                notificationChannel.setLightColor(Color.RED);
                // 開啟震動
                notificationChannel.enableVibration(true);
                // 設定震動頻率
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationManager.createNotificationChannel(notificationChannel);
            }


            Notification notification = new NotificationCompat.Builder(MainActivity.this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_dialog_email)
                    .setContentTitle(chatMessage.getSender())
                    .setContentText(chatMessage.getMessage())
                    .setAutoCancel(true)
//                .setContentIntent(pendingIntent) // 若無開啟頁面可不寫
                    .build();
            notificationManager.notify(NOTIFICATION_ID, notification);


            Log.d("=============", message);
        }
    };



    private void Logout() {
        Common.getPreherences(activity).edit().clear().apply();
        Intent intent = new Intent();
        intent.setClass(activity, LoginActivity.class);   //前放目前ＡＣＴＩＶＩＴＹ，後放目標的ＡＣＴ
        startActivity(intent);

    }

    /*JACK 生命週期 接收消息------------------------------------------------------------------------------------------------------------------*/

    @Override
    protected void onPause() {
        super.onPause();
        CommonTwo.connectServer(this, loadUserName(this));
        // 初始化LocalBroadcastManager並註冊BroadcastReceiver
        broadcastManager = LocalBroadcastManager.getInstance(MainActivity.this);
        registerChatReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        CommonTwo.connectServer(this, loadUserName(this));
        // 初始化LocalBroadcastManager並註冊BroadcastReceiver
        broadcastManager = LocalBroadcastManager.getInstance(MainActivity.this);
        registerChatReceiver();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}
