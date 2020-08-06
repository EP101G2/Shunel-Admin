package com.ed.shuneladmin;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class AdminNewDetailFragment extends Fragment {
private Activity activity;
private TextView tvAdminNo,tvAdminName,tvAdminId,tvAdminIdPassword,tvAdminIdPosition;
private Button btCancel,btModify;
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
        tvAdminNo=view.findViewById(R.id.tvAdminNo);
        tvAdminName=view.findViewById(R.id.tvAdminName);
        tvAdminId=view.findViewById(R.id.tvAccountId);
        tvAdminIdPassword=view.findViewById(R.id.tvAdminIdPassword);
        tvAdminIdPosition=view.findViewById(R.id.tvAdminIdPosition);
        btCancel=view.findViewById(R.id.btCancel);
        btModify= view.findViewById(R.id.btModify);


        btModify.setOnClickListener(new View.OnClickListener() {
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
