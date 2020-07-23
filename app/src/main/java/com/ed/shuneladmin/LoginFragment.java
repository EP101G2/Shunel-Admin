package com.ed.shuneladmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.bean.User_Account;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import static com.ed.shuneladmin.Task.Common.showToast;


public class LoginFragment extends Fragment {
    private Activity activity;
    private final static String TAG = "TAG_LoginFragment";
private EditText etId,etPw;
private Button btLogin;
    private CommonTask loginTask;
    private  String id,password;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }



    @Override
    public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etId=view.findViewById(R.id.etId);
        etPw=view.findViewById(R.id.etPw);
        btLogin=view.findViewById(R.id.btLogin);

        btLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                id = etId.getText().toString();
                password = etPw.getText().toString();

                if (Common.networkConnected(activity)) {
                    String url = Common.URL_SERVER + "Admin";                           //connect servlet(eclipse)
                    Gson gson = new Gson();
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "getLogin");
                    jsonObject.addProperty("id", id);
                    jsonObject.addProperty("password", password);
                    loginTask = new CommonTask(url, jsonObject.toString());
                    String jsonIn = "";//宣告字串去承接ＴＡＳＫ傳回的值

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

                        Toast.makeText(activity, "失敗", Toast.LENGTH_SHORT).show();


                        break;
                    case "success":


                        String userJstr = jsonObject2.get("user").getAsString();
                        if (userJstr != null) {
                            User_Account user_account = gson.fromJson(userJstr, User_Account.class);
                            savePreferences();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("User", user_account);

                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);    //當你在使用物件後還有其他動作要執行，補充資料在JAVA-slide-ch0805
                            LayoutInflater inflater = LayoutInflater.from(activity);
                            final View view = inflater.inflate(R.layout.loginsuccess, null);
                            builder.setView(view);
                            builder.create().show();

                            Intent intent = new Intent();
                            intent.putExtras(bundle);
                            intent.setClass(activity, MainActivity.class);   //前放目前ＡＣＴＩＶＩＴＹ，後放目標的ＡＣＴ
                            startActivity(intent);  //啟動跳頁動作
                            activity.finish();//把自己關掉
                        }
                }


            } else {
                 showToast(activity, R.string.textNoNetwork);
            }
        }
    });


    }
    private void savePreferences() {

        //置入name屬性的字串
        Common.getPreherences(activity).edit().putString("id", id)
                .putString("password", password)
                .apply();


        Log.i(TAG, "-------------------------------------------------------------");
        Log.i(TAG, Common.getPreherences(activity).getString("id", id));
    }
}