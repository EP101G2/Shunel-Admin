package com.ed.shuneladmin;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.bean.Order_Main;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


public class ModifyReceiverDetailFragment extends Fragment {
    private Activity activity;
    private final static String TAG = "---ModRecDetailFrag---";
    private EditText etRecName, etRecPhone, etRecAddress;
    private Button btCancel, btSave;
    private CommonTask modifyRecInfoTask;
    private Order_Main orderMain;
    private String recName, recPhone, recAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modify_receiver_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setting text
        etRecName = view.findViewById(R.id.etRecName);
        etRecPhone = view.findViewById(R.id.etRecPhone);
        etRecAddress = view.findViewById(R.id.etRecAddress);

        final NavController navController = Navigation.findNavController(view);
        Bundle bundle = getArguments();
        if (bundle == null || bundle.getSerializable("Receiver") == null) {
            Common.showToast(activity, R.string.textnofound);
            navController.popBackStack();
            return;
        }

        orderMain = (Order_Main) bundle.getSerializable("Receiver");
        String url = Common.URL_SERVER + "Orders_Servlet";
        int  orderId = orderMain.getOrder_ID();

        etRecName.setText(orderMain.getOrder_Main_Receiver());
        etRecPhone.setText(orderMain.getOrder_Main_Phone());
        etRecAddress.setText(orderMain.getOrder_Main_Address());

//        set button
        btCancel = view.findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });

        btSave = view.findViewById(R.id.btSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recName = etRecName.getText().toString();
                recPhone = etRecPhone.getText().toString();
                recAddress = etRecAddress.getText().toString();
                orderMain.setOrder_Main_Receiver(recName);
                orderMain.setOrder_Main_Phone(recPhone);
                orderMain.setOrder_Main_Address(recAddress);
//                save to db
                if (Common.networkConnected(activity)) {
                    String url = Common.URL_SERVER + "Orders_Servlet";//連server端先檢查網址
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "Update");//變作ＪＳＯＮ自串 //build case "update" in servlet
                    jsonObject.addProperty("Receiver", new Gson().toJson(orderMain));
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

                        Common.showToast(activity, R.string.textUpdateSuccess);
                    }
                } else {
                    Common.showToast(activity, R.string.textNoNetwork);
                }
                /* 回前一個Fragment */
                navController.popBackStack();
            }
        });
    }
}