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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.bean.Admin;


public class AdminNewDetailFragment extends Fragment {
    private Activity activity;
    private TextView tvAdminNo;
    private EditText etName, etAccountId, etNewPassword;
    private Button btCancel, btConfirm;
    private Admin admin;

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


        String url = Common.URL_SERVER + "Admin";
        int  id = admin.getAdmin_ID();
        tvAdminNo.setText(String.valueOf(id));
        etName.setText(admin.getAdmin_Name());
        etAccountId.setText(admin.getAdmin_User_Name());
        etNewPassword.setText(admin.getAdmin_User_Password());



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
}
