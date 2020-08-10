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
import android.widget.SearchView;
import android.widget.TextView;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.bean.Order_Main;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersManagementFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "---OrdersManageFrag---"; //maximum 23 characters

    private Activity activity;
    private Integer counter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView svOrders;
//    private User_Account userAccount;
    List<Order_Main> orderMainList;
    RecyclerView rvOrderMain;
    private CommonTask ordersListGetTask;

    public OrdersManagementFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        if (getArguments() != null){
            counter = getArguments().getInt(TAG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_orders_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        get data
        orderMainList = getOrders();
        showOrders(orderMainList);
//        setting recycler view
        rvOrderMain = view.findViewById(R.id.rvOrderMain);
        rvOrderMain.setLayoutManager(new LinearLayoutManager(activity));
        rvOrderMain.setAdapter(new OrderMainAdapter(getContext(), orderMainList));
//        setting swipe refresh layout
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayoutOrder);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //讀取的圈圈 動畫
                swipeRefreshLayout.setRefreshing(true);
                showOrders(getOrders());//method in next paragraph
                //直到讀取完 結束
                swipeRefreshLayout.setRefreshing(false);
            }
        });

//        setting search view
        svOrders = view.findViewById(R.id.svOrders);
        svOrders.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                OrderMainAdapter adapter = (OrderMainAdapter) rvOrderMain.getAdapter(); //強迫子型recyclerview.getadapter轉型成父型friendadapter
                try{
                    if (adapter != null) { //先檢查是否為空值：空值會執行錯誤
                        // 如果搜尋條件為空字串，就顯示原始資料；否則就顯示搜尋後結果
                        if (newText.isEmpty()) { //空字串“” =/ 空值 null ＝/ 空白字串"/s"; 空值呼叫方法會造成執行錯誤：nullpointer exception
                            adapter.setOrders(orderMainList);
                        } else {
                            List<Order_Main> searchOrders = new ArrayList<>();
                            // 搜尋原始資料內有無包含關鍵字(不區別大小寫)
                            for (Order_Main orderMain : orderMainList) { //get searched data from origin data
//                            search by account id
                                if (orderMain.getAccount_ID().toUpperCase().contains(newText.toUpperCase())) { //ignore upper/lower case: all input change into upper case
                                    searchOrders.add(orderMain);
                                }
//                            search by orderId
                                if (orderMain.getOrder_ID() == Integer.parseInt(newText)) { //turn newtext into int and compare to orderid
                                    searchOrders.add(orderMain);
                                }
                            }
                            adapter.setOrders(searchOrders);
                        }
                        adapter.notifyDataSetChanged(); //重刷畫面
                        return true;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                return false;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    private void showOrders(List<Order_Main> orderMainList) {
        try{
            if (orderMainList == null || orderMainList.isEmpty()) {
                Common.showToast(activity, R.string.textnofound);
            }
            OrderMainAdapter orderMainAdapter = (OrderMainAdapter) rvOrderMain.getAdapter();
            // 如果spotAdapter不存在就建立新的，否則續用舊有的
            if (orderMainAdapter == null) {
                rvOrderMain.setAdapter(new OrderMainAdapter(activity, orderMainList));
            } else {
                orderMainAdapter.setOrders(getOrders());//get new
                orderMainAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Order_Main> getOrders() {//see notice detail fragments for times format
        List<Order_Main> orderMainList = new ArrayList<>();
        try {
            if (Common.networkConnected(activity)) {
//                get data from orders servlet
                String url = Common.URL_SERVER + "Orders_Servlet";
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getOrdersForManage");
                String jsonOut = jsonObject.toString();
                ordersListGetTask = new CommonTask(url, jsonOut);
                try {
                    String jsonIn = ordersListGetTask.execute().get();
                    Type listType = new TypeToken<List<Order_Main>>() {
                    }.getType();
                    orderMainList = gson.fromJson(jsonIn, listType);
                    Log.e(TAG, jsonIn);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            } else {
                Common.showToast(activity, R.string.textNoNetwork);
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return orderMainList;
    }

//    ---adapter---
    private class OrderMainAdapter extends RecyclerView.Adapter<OrderMainAdapter.PageViewHolder> {
        private LayoutInflater layoutInflater;
        Context context;
        List<Order_Main> orderMainList;

        public OrderMainAdapter(Context context, List<Order_Main> orderMainList) {
            this.context = context;
            layoutInflater = LayoutInflater.from(context);
            this.orderMainList = orderMainList;
        }

        @NonNull
        @Override
        public OrderMainAdapter.PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_orders_management_view, parent, false);
            return new OrderMainAdapter.PageViewHolder(view);
        }

    //    inner class PageViewHolder for the holding of recycler view
        class PageViewHolder extends RecyclerView.ViewHolder {
            TextView tvOrderDate, tvOrderModifyDate, tvOrderId, tvAccountIdOrders, tvTotalPrice, tvOrderStatus;
            public PageViewHolder(@NonNull View itemView) {
                super(itemView);
                tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
                tvOrderModifyDate = itemView.findViewById(R.id.tvOrderModifyDate);
                tvOrderId = itemView.findViewById(R.id.tvOrderId);
                tvAccountIdOrders = itemView.findViewById(R.id.tvAccountIdOrders);
                tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
                tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull OrderMainAdapter.PageViewHolder holder, int position){
            final Order_Main orderMain = orderMainList.get(position);
            holder.tvOrderDate.setText(orderMain.getOrder_Main_Order_Date().toString());
            holder.tvOrderModifyDate.setText(orderMain.getOrder_Main_Modify_Date().toString());
            holder.tvOrderId.setText(String.valueOf(orderMain.getOrder_ID()));
            holder.tvAccountIdOrders.setText(orderMain.getAccount_ID());
            holder.tvTotalPrice.setText(String.valueOf(orderMain.getOrder_Main_Total_Price()));
            holder.tvOrderStatus.setText(orderStatusText(orderMain.getOrder_Main_Order_Status()));//apply method orderStatusText
            Log.e(TAG,"--onBindViewHolder-->"+orderMain.getOrder_ID());



//            navigate to detail fragment
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Orders", orderMain);
                    Navigation.findNavController(v).navigate(R.id.action_ordersManagementFragment_to_orderManageDetailFragment, bundle);
                }
            });
        }
//            setting text for order status
    private String orderStatusText(int status) {
            String statusText = "";
            if (status == 0){
                statusText = "未付款";
            }else if (status == 1){
                statusText = "未出貨";
            }else if (status == 2){
                statusText = "已出貨";
            }else if (status == 3){
                statusText = "已送達";
            }else if (status == 4){
                statusText = "已取消";
            }else if (status == 5){
                statusText = "已退貨";
            }
            return statusText;
    }

    @Override
        public int getItemCount() {
            try {
                if (orderMainList != null) {
                    Log.e(TAG,"itemCount:"+orderMainList.size());
                    return orderMainList == null ? 0 : orderMainList.size();
                }
            }catch (Exception e){
                Log.e(TAG,"null list");
            }
            return orderMainList == null ? 0 : orderMainList.size();
        }

        public void setOrders(List<Order_Main> orderMainList) {
            this.orderMainList = orderMainList;
        }
    }
}