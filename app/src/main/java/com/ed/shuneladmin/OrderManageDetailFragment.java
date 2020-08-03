package com.ed.shuneladmin;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.bean.Order_Detail;
import com.ed.shuneladmin.bean.Order_Main;
import com.google.gson.JsonObject;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderManageDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "---orderManageDet---";
//    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
//    private String mParam2;
//    routine work
    private Activity activity;
    private Integer counter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView svOrders;
    private TextView tvAccountIdDet, tvOrderIdDet, tvOrderDateDet, tvReceiverName, tvReceiverPhone, tvReceiverAddress;
    private Spinner spChangeStatus;
    private Button btCancel, btSave;
    private String status = "";
    List<Order_Main> orderMainList;
    List<Order_Detail> orderDetailList;
    RecyclerView rvOrderDetProduct;
    private CommonTask ordersListDetGetTask;


    public OrderManageDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_manage_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setting words
        tvAccountIdDet = view.findViewById(R.id.tvAccountIdDet);
        tvOrderIdDet = view.findViewById(R.id.tvOrderIdDet);
        tvOrderDateDet = view.findViewById(R.id.tvOrderDateDet);
        tvReceiverName = view.findViewById(R.id.tvReceiverName);
        tvReceiverPhone = view.findViewById(R.id.tvReceiverPhone);
        tvReceiverAddress = view.findViewById(R.id.tvReceiverAddress);

//        setting recycler view
        rvOrderDetProduct = view.findViewById(R.id.rvOrderDetProduct);
        rvOrderDetProduct.setLayoutManager(new LinearLayoutManager(activity));
        rvOrderDetProduct.setAdapter(new OrderManageDetAdapter(getContext(), orderMainList));

//        setting spinner
        spChangeStatus = view.findViewById(R.id.spChangeStatus);
        spChangeStatus.setSelection(0, true);
        String[] statusCategory = {"未付款", "未出貨", "已出貨", "已送達", "已取消", "已退貨"};
        ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, statusCategory);
        aAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spChangeStatus.setAdapter(aAdapter);
        spChangeStatus.setSelection(0,true);

//        setting buttons
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
                try {
                    if (Common.networkConnected(activity)) {
                        String url = Common.URL_SERVER + "Orders_Servlet";
                        JsonObject jsonObject = new JsonObject();
                        switch (status) { //modification needed
                            case "促銷訊息":
                                Log.e("促銷訊息", "===" + status);
                                jsonObject.addProperty("action", "updateStatus");
//                                jsonObject.addProperty("Title", Title);
//                                jsonObject.addProperty("Detail", Detail);
                                break;
                            case "系統訊息":
                                Log.e("系統訊息", "===" + status);
                                jsonObject.addProperty("action", "sendSystemN");
//                                jsonObject.addProperty("Title", Title);
//                                jsonObject.addProperty("Detail", Detail);
                                break;
                        }

                        ordersListDetGetTask = new CommonTask(url, jsonObject.toString());
                        String jsonIn = "";

                        try {
                            jsonIn = ordersListDetGetTask.execute().get();

                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                        Log.e("------------",jsonIn);
                    }
                }catch (Exception e){
                    Log.e(TAG, e.toString());
                }
            }
        });
    }

    private class OrderManageDetAdapter extends RecyclerView.Adapter {
        public OrderManageDetAdapter(Context context, List<Order_Main> orderMainList) {
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}