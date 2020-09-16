package com.ed.shuneladmin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.Task.ImageTask;
import com.ed.shuneladmin.bean.Order_Detail;
import com.ed.shuneladmin.bean.Order_Main;
import com.ed.shuneladmin.bean.Product;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderManageDetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    private static final String TAG = "---orderManageDet---";
    // TODO: Rename and change types of parameters
    private String mParam1;
//    private String mParam2;
//    routine work
    private Activity activity;
    private TextView tvAccountIdDet, tvOrderIdDet, tvOrderDateDet, tvReceiverName, tvReceiverPhone, tvReceiverAddress;
    private Spinner spChangeStatus;
    private Button btCancel, btSave, btModifyOrderData;
//    private int status = 0;
    private Order_Main orderMain;
    private Order_Detail orderDetail;
    List<Order_Main> orderMainList;
    List<Order_Detail> orderDetailList;
    List<Product> orderProductList;
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
//        bundle data from last page
        String[] statusCategory = {"未付款", "未出貨", "已出貨", "已送達", "已取消", "已退貨"};
        final NavController navController = Navigation.findNavController(view);
        Bundle bundle = getArguments();
        if (bundle == null || bundle.getSerializable("Orders") == null) {
            Common.showToast(activity, R.string.textnofound);
            navController.popBackStack();
            return;
        }else {
            orderMain = (Order_Main) bundle.getSerializable("Orders");
            spChangeStatus = view.findViewById(R.id.spChangeStatus);
//        spChangeStatus.setSelection(oriStatus, true);

//        spinner's adapter
            ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, statusCategory);
            aAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spChangeStatus.setAdapter(aAdapter);
            showOrderDetails();
        }
//        tvReceiverName.setText(orderMain.getOrder_Main_Receiver());
//        tvReceiverAddress.setText(orderMain.getOrder_Main_Address());
//        tvReceiverPhone.setText(orderMain.getOrder_Main_Phone());

//        get data for recycle view
        orderDetailList = getOrderedProducts();
//        showOrderedProducts(orderDetailList);
//        setting recycler view
        rvOrderDetProduct = view.findViewById(R.id.rvOrderDetProduct);
        rvOrderDetProduct.setLayoutManager(new LinearLayoutManager(activity));
        rvOrderDetProduct.setAdapter(new OrderManageDetAdapter(getContext(), orderDetailList));

//        setting spinner, change status
//        int oriStatus = orderMain.getOrder_Main_Order_Status();

        spChangeStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        setting buttons
        btCancel = view.findViewById(R.id.btCancelOMD);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });
//save changed status and upload to server
        btSave = view.findViewById(R.id.btSaveOMD);
        btSave.setOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int orderId = Integer.parseInt(tvOrderIdDet.getText().toString());
                int status = spChangeStatus.getSelectedItemPosition();
                String jsonIn = "";
                try {
                    if (Common.networkConnected(activity)) {
                        String url = Common.URL_SERVER + "Orders_Servlet";
                        JsonObject jsonObject = new JsonObject();
//                        Log.e("updateStatus", "==" + status);
                        jsonObject.addProperty("action", "updateStatus");
                        jsonObject.addProperty("status", new Gson().toJson(status));
                        jsonObject.addProperty("orderId", new Gson().toJson(orderId));
                        ordersListDetGetTask = new CommonTask(url, jsonObject.toString());
                        try {
                            jsonIn = ordersListDetGetTask.execute().get();

                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }else {
                        Common.showToast(activity, R.string.textNoNetwork);
                    }
                }catch (Exception e){
                    Log.e(TAG, e.toString());
                }
                Log.e("--updateStatus--", jsonIn);
                navController.popBackStack();
            }
        });

        btModifyOrderData = view.findViewById(R.id.btModifyOrderData);
//        send name, phone, address to the next page
        btModifyOrderData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                int orderID = orderMain.getOrder_ID();

                String name = tvReceiverName.getText().toString().trim();
                String phone = tvReceiverPhone.getText().toString().trim();
                String address = tvReceiverAddress.getText().toString();
                bundle.putInt("orderID",orderID);
                bundle.putString("name", name);
                bundle.putString("phone", phone);
                bundle.putString("address", address);

                Navigation.findNavController(v).navigate(R.id.action_orderManageDetailFragment_to_modifyReceiverDetailFragment, bundle);
            }
        });
    }

    private void showOrderedProducts(List<Order_Detail> orderDetailList) {
//        Log.e(TAG, "orderDetList: "+orderDetailList);//get ok
        try{
            if (orderDetailList == null || orderDetailList.isEmpty()) {
                Common.showToast(activity, R.string.textnofound);
            }
            OrderManageDetAdapter orderManageDetAdapter = (OrderManageDetAdapter) rvOrderDetProduct.getAdapter();
//            null pointer exception  //no mind?
            // 如果spotAdapter不存在就建立新的，否則續用舊有的
            if (orderManageDetAdapter == null) {
                rvOrderDetProduct.setAdapter(new OrderManageDetailFragment.OrderManageDetAdapter(activity, orderDetailList));
            } else {
                orderManageDetAdapter.setOrderedProduct(getOrderedProducts());//get new
                orderManageDetAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<Order_Detail> getOrderedProducts() {
        List<Order_Detail> orderDetailList = new ArrayList<>();
        int orderId = Integer.parseInt(tvOrderIdDet.getText().toString());
//        Log.e(TAG, "get orderId: "+orderId);
        try {
            if (Common.networkConnected(activity)) {
//                get data from orders servlet
                String url = Common.URL_SERVER + "Orders_Servlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "getOrderedProducts");
                jsonObject.addProperty("order_Id", orderId);
                String jsonOut = jsonObject.toString();
                ordersListDetGetTask = new CommonTask(url, jsonOut);
                Log.e(TAG, "getOrderedProducts: out -> "+jsonOut);
                try {
                    String jsonIn = ordersListDetGetTask.execute().get();
                    Type listType = new TypeToken<List<Order_Detail>>() {
                    }.getType();
                    orderDetailList = new Gson().fromJson(jsonIn, listType);
//                    Log.e(TAG, "getOrderedProducts: in -> "+jsonIn);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
            } else {
                Common.showToast(activity, R.string.textNoNetwork);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderDetailList;
    }//get ok


    private void showOrderDetails() {
        int id = orderMain.getOrder_ID();
        tvOrderIdDet.setText(String.valueOf(id));
        tvAccountIdDet.setText(orderMain.getAccount_ID());
        tvOrderDateDet.setText(String.valueOf(orderMain.getOrder_Main_Order_Date()));
        tvReceiverName.setText(orderMain.getOrder_Main_Receiver());
        tvReceiverPhone.setText(orderMain.getOrder_Main_Phone());
        tvReceiverAddress.setText(orderMain.getOrder_Main_Address());
        int status = orderMain.getOrder_Main_Order_Status(); // 拿訂單狀態碼
        Log.e("status===========",status+"");
        switch (status){
            case 0 :
                spChangeStatus.setSelection(0);
                break;
            case 1 :
                spChangeStatus.setSelection(1);
                break;
            case 2 :
                spChangeStatus.setSelection(2);
                break;
            case 3 :
                spChangeStatus.setSelection(3);
                break;
            case 4 :
                spChangeStatus.setSelection(4);
                break;
            case 5 :
                spChangeStatus.setSelection(5);
                break;

        }

    }//show ok

    //    Adapter for rvOrderDetProduct
    private class OrderManageDetAdapter extends RecyclerView.Adapter<OrderManageDetAdapter.PageViewHolder> {
        private LayoutInflater inflater;
        Context context;
        List<Order_Detail> orderDetailList;
        private ImageTask orderDetProdImgTask;
        private int imageSize;

        public OrderManageDetAdapter(Context context, List<Order_Detail> orderDetailList) {
            this.context = context;
            this.orderDetailList = orderDetailList;
            imageSize = context.getResources().getDisplayMetrics().widthPixels / 4;
            inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public OrderManageDetAdapter.PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_order_manage_detail_view, parent, false);
            return new OrderManageDetAdapter.PageViewHolder(view);
        }

        public void setOrderedProduct(List<Order_Detail> orderDetailList) {
            this.orderDetailList = orderDetailList;
        }

        class PageViewHolder extends RecyclerView.ViewHolder {
            TextView tvProductName, tvProductPrice, tvOrderedProductId;
            ImageView ivOrderProductPic;

            public PageViewHolder(@NonNull View itemView) {
                super(itemView);
                tvProductName = itemView.findViewById(R.id.tvProductNameOM);
                tvProductPrice = itemView.findViewById(R.id.tvProductPriceOM);
//                tvOrderedProductId = itemView.findViewById(R.id.tvOrderedProductId);
                ivOrderProductPic = itemView.findViewById(R.id.ivOrderProductPic);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull OrderManageDetAdapter.PageViewHolder holder, int position) {
            final Order_Detail orderDetail = orderDetailList.get(position);
            Log.e(TAG, "orderDetail: "+orderDetail.getorderDetProductId());

            holder.tvProductName.setText(orderDetail.getorderDetProductName()); //
            holder.tvProductPrice.setText("$" + orderDetail.getOrder_Detail_Buy_Price());

//            get product pic through product ID
            try {
                String url = Common.URL_SERVER + "Prouct_Servlet";
                int productIdOMD = orderDetail.getorderDetProductId();
                orderDetProdImgTask = new ImageTask(url, productIdOMD, imageSize, holder.ivOrderProductPic);
                orderDetProdImgTask.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            try {
                if (orderDetailList != null) {
                    Log.e(TAG, "itemCount:" + orderDetailList.size());
                    return orderDetailList == null ? 0 : orderDetailList.size();
                }
            } catch (Exception e) {
                Log.e(TAG, "null list");
            }
            return orderDetailList == null ? 0 : orderDetailList.size();
        } //count correct
    }
}