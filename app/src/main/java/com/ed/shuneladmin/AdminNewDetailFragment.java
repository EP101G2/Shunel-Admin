package com.ed.shuneladmin;

import android.app.Activity;
import android.graphics.Bitmap;
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
import com.ed.shuneladmin.bean.Admin;


public class AdminNewDetailFragment extends Fragment {
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
        btCancel = view.findViewById(R.id.btCancel);
        btConfirm = view.findViewById(R.id.btConfirm);
        spPosition= view.findViewById(R.id.spPosition);




        Bundle bundle = getArguments();
        if (bundle == null || bundle.getSerializable("admin") == null) {

            navController.popBackStack();
            return;
        }
        admin = (Admin) bundle.getSerializable("admin");
        showAdmin();

        final String[] postions = {"管理員", "一般職員"};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity,
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

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
