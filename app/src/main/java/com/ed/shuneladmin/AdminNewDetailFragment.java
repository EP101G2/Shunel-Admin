package com.ed.shuneladmin;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class AdminNewDetailFragment extends Fragment {
    private final static String TAG = "---NewDeFragment---";
    private Activity activity;
    private TextView tvAdminNo;
    private EditText etName, etAccountId, etNewPassword;
    private Button btCancel, btConfirm;
    private Admin admin;
    private Spinner spPosition;
    private String result;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_new_detail, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NavController navController = Navigation.findNavController(view);
        tvAdminNo = view.findViewById(R.id.tvAdminNo);
        etName = view.findViewById(R.id.etName);
        etAccountId = view.findViewById(R.id.etAccountId);
        etNewPassword = view.findViewById(R.id.etNewPassword);
        btCancel = view.findViewById(R.id.btLogin);
        btConfirm = view.findViewById(R.id.btConfirm);
        spPosition= view.findViewById(R.id.spPosition);




        Bundle bundle = getArguments();
        if (bundle == null || bundle.getSerializable("admin") == null) {

            navController.popBackStack();
            return;
        }
        admin = (Admin) bundle.getSerializable("admin");

        String[] postions = {"管理員", "一般職員"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_spinner_item, postions);
        /* 指定點選時彈出來的選單樣式 */
        arrayAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
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


        showAdmin();


        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String name = etName.getText().toString();
                String accountId = etAccountId.getText().toString();
                String password = etNewPassword.getText().toString();

                Log.e("1234567890",name+"");
                admin.setAdmin_Name(name);
                admin.setAdmin_User_Name(accountId);
                admin.setAdmin_User_Password(password);
                admin.setAdmin_ID(admin.getAdmin_ID());
                admin.setAdmin_User_Position(result);


                if (Common.networkConnected(activity)) {
                    String url = Common.URL_SERVER + "Admin";//連server端先檢查網址
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "Update");//變作ＪＳＯＮ自串
                    jsonObject.addProperty("admin", new Gson().toJson(admin));

                    int count = 0;
                    try {
                        String result = new CommonTask(url, jsonObject.toString()).execute().get();
                        count = Integer.parseInt(result);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if (count == 0) {
                        Common.showToast(activity, R.string.textUpdateFail);
                    } else {
                        savePreferences();
                        Common.showToast(activity, R.string.textUpdateSuccess);
                    }
                } else {
                    Common.showToast(activity, R.string.textNoNetwork);
                }
                /* 回前一個Fragment */
                navController.popBackStack();
            }



        });


        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* 回前一個Fragment */
                navController.popBackStack();
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

    private void showAdmin() {
        tvAdminNo.setText(String.valueOf(admin.getAdmin_ID()));
        etName.setText(admin.getAdmin_Name());
        etAccountId.setText(admin.getAdmin_User_Name());
        etNewPassword.setText(admin.getAdmin_User_Password());
        String sp = admin.getAdmin_User_Position();

        switch(sp){

            case "管理員":
                spPosition.setSelection(0);
                break;
            case"一般職員":
                spPosition.setSelection(1);
                 break;
        }

    }

}
