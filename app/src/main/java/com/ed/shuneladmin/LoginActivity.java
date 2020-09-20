package com.ed.shuneladmin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.bean.Admin;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    private final static String TAG = "TAG_LoginFragment";

    private Button btLogin;
    private EditText etPw, etId;
    private String id,password,position;
    private int number;
    private CommonTask loginTask;
    private ImageView imageView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        Log.e("1", "1111111111111");

        etId = findViewById(R.id.etId);
        etPw = findViewById(R.id.etPw);
        btLogin = findViewById(R.id.btLogin);
        imageView2 = findViewById(R.id.imageView2);

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etId.setText("Admin001");
                etPw.setText("1111");

            }
        });





        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = etId.getText().toString();
                password = etPw.getText().toString();


                if (networkConnected()) {
                    String url = Common.URL_SERVER + "Admin";                           //connect servlet(eclipse)
                    Gson gson = new Gson();
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "getLogin");
                    jsonObject.addProperty("id", id);
                    jsonObject.addProperty("password", password);
                    loginTask = new CommonTask(url, jsonObject.toString());
                    String jsonIn = "";
                    try {
                        jsonIn = loginTask.execute().get();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    JsonObject jsonObject2 = gson.fromJson(jsonIn, JsonObject.class);


                    String result = jsonObject2.get("result").getAsString();
                    Log.i(TAG, result);

                    if (etId.length() == 0) {
                        etId.setError("請輸入15位數內英文或數字 ");
                    } else if (etPw.length() == 0) {
                        etPw.setError("請輸入15位數內英文或數字密碼");
                    }
                    switch (result) {
                        case "fail":

                            Toast.makeText(LoginActivity.this, "失敗", Toast.LENGTH_SHORT).show();


                            break;
                        case "success":


                            String userJstr = jsonObject2.get("admin").getAsString();
                            if (userJstr != null) {
                                Admin admin = gson.fromJson(userJstr, Admin.class);
                                number = admin.getAdmin_ID();
                                id = admin.getAdmin_User_Name();
                                position = admin.getAdmin_User_Position();
                                password= admin.getAdmin_User_Password();
                                savePreferences();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("Admin", admin);
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);    //當你在使用物件後還有其他動作要執行，補充資料在JAVA-slide-ch0805
                                LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
                                final View view = inflater.inflate(R.layout.loginsuccess, null);
                                builder.setView(view);
                                builder.create().show();

                                Intent intent = new Intent();
                                intent.putExtras(bundle);
                                intent.setClass(LoginActivity.this, MainActivity.class);   //前放目前ＡＣＴＩＶＩＴＹ，後放目標的ＡＣＴ
                                startActivity(intent);  //啟動跳頁動作
                                LoginActivity.this.finish();//把自己關掉
                            }
                            break;
                    }


                } else {
                    Common.showToast(LoginActivity.this, R.string.textNoNetwork);
                }
            }
        });
//
    }


    private void savePreferences() {

        //置入name屬性的字串
        Common.getPreherences(LoginActivity.this).edit()
                .putString("id", id)
                .putString("password", password)
                .putString("position",position)
                .putInt("number",number)
                .apply();


        Log.i(TAG, "-------------------------------------------------------------");
        Log.i(TAG, Common.getPreherences(LoginActivity.this).getString("id", id));
    }

    private boolean networkConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // API 23支援getActiveNetwork()
                Network network = connectivityManager.getActiveNetwork();
                // API 21支援getNetworkCapabilities()
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                if (networkCapabilities != null) {
                    String msg = String.format(Locale.getDefault(),
                            "TRANSPORT_WIFI: %b%nTRANSPORT_CELLULAR: %b%nTRANSPORT_ETHERNET: %b%n",
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI),
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR),
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
                    Log.d(TAG, msg);
                    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
                }
            } else {
                // API 29將NetworkInfo列為deprecated
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
        }
        return false;
    }
}
