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
import android.widget.Button;
import android.widget.EditText;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.bean.Order_Main;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;


public class ModifyReceiverDetailFragment extends Fragment {
    private Activity activity;
    private final static String TAG = "---ModRecDetailFrag---";
    private EditText etRecName, etRecPhone, etRecAddress;
    private Button btCancel, btSave;
    private CommonTask modifyRecInfoTask;
    private Order_Main orderMain = new Order_Main();
    private String recName, recPhone, recAddress;
    private  int orderID;

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
        if (bundle == null) {
            Common.showToast(activity, R.string.textnofound);
            navController.popBackStack();
            return;
        }else{
            orderID = bundle.getInt("orderID");
            String name = bundle.getString("name");
            String phone = bundle.getString("phone");
            String address = bundle.getString("address");
//        take the bundled words as hint strings
            etRecName.setText(name);
            etRecPhone.setText(phone);
            etRecAddress.setText(address);
        }

//        set button
        btCancel = view.findViewById(R.id.btCancelMRD);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });

        btSave = view.findViewById(R.id.btSaveMRD);
        btSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try{
                    String order_Main_Receiver = etRecName.getText().toString();
                    String order_Main_Phone = etRecPhone.getText().toString();
                    String order_Main_Address = etRecAddress.getText().toString();
                    Log.e(TAG, "getEditText -> "+order_Main_Receiver+"+"+order_Main_Phone+"+"+order_Main_Address);
                    orderMain.setReceiver(order_Main_Receiver, order_Main_Phone, order_Main_Address,orderID);
//                save to db
                    if (Common.networkConnected(activity)) {
                        String url = Common.URL_SERVER + "Orders_Servlet";//連server端先檢查網址
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "update");//case "update" in servlet(check)
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}