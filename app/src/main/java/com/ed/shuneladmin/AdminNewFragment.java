package com.ed.shuneladmin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.bean.Admin;
import com.ed.shuneladmin.bean.User_Account;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class AdminNewFragment extends Fragment {
    private final static String TAG = "---AdminNewFragment---";
    private Admin admin;
    private Activity activity;
private EditText etName,etAccountId,etNewPassword,etReTypeNewPassword;
private Spinner spPosition;
private Button btConfirm,btCancel;
private String result;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }
//public class MyArrrayAdapter extends ArrayAdapter
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
    etName= view.findViewById(R.id.etName);
    etAccountId= view.findViewById(R.id.etAccountId);
    etNewPassword=view.findViewById(R.id.etNewPassword);
    etReTypeNewPassword=view.findViewById(R.id.etReTypeNewPassword);
    btCancel=view.findViewById(R.id.btCancel);

    btConfirm=view.findViewById(R.id.btConfirm);
    spPosition=view.findViewById(R.id.spPosition);
        /* 也可使用List */
        final String[] postions = {"管理員", "一般職員"};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_spinner_item, postions);
        /* 指定點選時彈出來的選單樣式 */
        arrayAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        spPosition.setSelection(0, true);
        spPosition.setAdapter(arrayAdapter);
        spPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Admin admin =(Admin) parent.getItemAtPosition(position);
                 result =  parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


btCancel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        /* 回前一個Fragment */
        navController.popBackStack();
    }
});

btConfirm.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        if (!etNewPassword.getText().toString().equals(etReTypeNewPassword.getText().toString())) {
            Common.showToast(activity, "輸入密碼與再次輸入密碼不吻合");
            return;
        }



    if (etName.length() == 0) {
        etName.setError("請輸入中英文");
    } else if (etNewPassword.length() == 0) {
        etNewPassword.setError("請輸入15位數內英文或數字");
    } else if (etAccountId.length() == 0) {
            etAccountId.setError("請輸入15位數內英文或數字");
    } else if (etReTypeNewPassword.length() == 0) {
        etReTypeNewPassword.setError("請和密碼輸入相同");
    }



    if (Common.networkConnected(activity)) {
        String url = Common.URL_SERVER + "Admin";
        String status = btConfirm.getText().toString();
        String name = etName.getText().toString();
        String password = etNewPassword.getText().toString();
        String accountId = etAccountId.getText().toString();

        admin = new Admin(name, password,accountId,result);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "Register");
        jsonObject.addProperty("admin", new Gson().toJson(admin));

        try {
            String result = new CommonTask(url, jsonObject.toString()).execute().get();//巷ＳＥＶＥＲ提出註冊請球，彈出的是註冊節過0貨1
            int resultType = Integer.parseInt(result);

            if (resultType == 0) {



                Common.showToast(activity, R.string.textInsertFail);
            } else {

                savePreferences();

//                AlertDialog.Builder builder = new AlertDialog.Builder(activity);    //當你在使用物件後還有其他動作要執行，補充資料在JAVA-slide-ch0805
//                LayoutInflater inflater = LayoutInflater.from(activity);
//                final View view =inflater.inflate(R.layout.registersuccess,null);
//                builder.setView(view);
//                builder.create().show();


                Navigation.findNavController(v)
                        .navigate(R.id.action_adminNewFragment_to_adminNewDetailFragment);




            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());


        }


    } else {
        Common.showToast(activity, R.string.textNoNetwork);
    }

    }
});




    }
    private void savePreferences() {

        //置入name屬性的字串
        Common.getPreherences(activity).edit().putString("name",etName.getText().toString()).apply();
        Common.getPreherences(activity).edit().putString("id",etAccountId.getText().toString()).apply();
        Common.getPreherences(activity).edit().putString("password",etNewPassword.getText().toString()).apply();
        Common.getPreherences(activity).edit().putString("position",spPosition.toString()).apply();

        Log.i(TAG, "-------------------------------------------------------------");

    }


//
//    Spinner.OnItemSelectedListener listener = new Spinner.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(
//                AdapterView<?> parent, View view, int pos, long id) {
//            position.setText(parent.getItemAtPosition(pos).toString());
//        }
//
//    };

}
