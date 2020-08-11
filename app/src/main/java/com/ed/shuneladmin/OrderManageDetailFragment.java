package com.ed.shuneladmin;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.Task.ImageTask;
import com.ed.shuneladmin.bean.Order_Detail;
import com.ed.shuneladmin.bean.Order_Main;
import com.ed.shuneladmin.bean.Product;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
    private TextView tvAccountIdDet, tvOrderIdDet, tvOrderDateDet, tvReceiverName, tvReceiverPhone, tvReceiverAddress;
    private Spinner spChangeStatus;
    private Button btCancel, btSave, btModifyOrderData;
//    private int status = 0;
    private Order_Main orderMain;
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
//        bundle data from last page
        final NavController navController = Navigation.findNavController(view);
        Bundle bundle = getArguments();
        if (bundle == null || bundle.getSerializable("Orders") == null) {
            Common.showToast(activity, R.string.textnofound);
            navController.popBackStack();
            return;
        }
        orderMain = (Order_Main) bundle.getSerializable("Orders");
        showOrderDetails();


        tvReceiverName.setText(orderMain.getOrder_Main_Receiver());
        tvReceiverAddress.setText(orderMain.getOrder_Main_Address());
        tvReceiverPhone.setText(orderMain.getOrder_Main_Phone());

//        setting recycler view
        rvOrderDetProduct = view.findViewById(R.id.rvOrderDetProduct);
        rvOrderDetProduct.setLayoutManager(new LinearLayoutManager(activity));
        rvOrderDetProduct.setAdapter(new OrderManageDetAdapter(getContext(), orderDetailList));

//        setting spinner, change status
//        int oriStatus = orderMain.getOrder_Main_Order_Status();
        spChangeStatus = view.findViewById(R.id.spChangeStatus);
//        spChangeStatus.setSelection(oriStatus, true);
        String[] statusCategory = {"未付款", "未出貨", "已出貨", "已送達", "已取消", "已退貨"};
//        spinner's adapter
        ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, statusCategory);
        aAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spChangeStatus.setAdapter(aAdapter);
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
                        Log.e("updateStatus", "==" + status);
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

                String name = tvReceiverName.getText().toString().trim();
                String phone = tvReceiverPhone.getText().toString().trim();
                String address = tvReceiverAddress.getText().toString();

                bundle.putString("name", name);
                bundle.putString("phone", phone);
                bundle.putString("address", address);

                Navigation.findNavController(v).navigate(R.id.action_orderManageDetailFragment_to_modifyReceiverDetailFragment, bundle);
            }
        });
    }

    private void showOrderDetails() {
//        List<Order_Main> orderDetList = new ArrayList<>();
        int id = orderMain.getOrder_ID();
        tvOrderIdDet.setText(String.valueOf(id));
        tvAccountIdDet.setText(orderMain.getAccount_ID());
        tvOrderDateDet.setText(String.valueOf(orderMain.getOrder_Main_Order_Date()));
        tvReceiverName.setText(orderMain.getOrder_Main_Receiver());
        tvReceiverPhone.setText(orderMain.getOrder_Main_Phone());
        tvReceiverAddress.setText(orderMain.getOrder_Main_Address());

////        get receiver name, phone, address by orderId from server(not working, can't see Logcat)
//        String url = Common.URL_SERVER + "Orders_Servlet";
//        final Gson gson = new Gson();
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("action", "getOrderMain");
//        jsonObject.addProperty("orderID", new Gson().toJson(id));
//        ordersListDetGetTask = new CommonTask(url, jsonObject.toString());
//
//        try {
//            String jsonIn = ordersListDetGetTask.execute().get();
//            Log.e(TAG, jsonIn);
//
////            Order_Mai orderDetail =
////            Type listType = new TypeToken<List<Order_Main>>() {
////            }.getType();
////            orderDetList = gson.fromJson(jsonIn, listType);
////            Log.e(TAG, jsonIn);
//
////            JsonObject jsonObjectRec = gson.fromJson(jsonIn, JsonObject.class);
////            final String result = jsonObjectRec.get("orderMain").getAsString();
//            final Order_Main orderMainRec = gson.fromJson(jsonIn, Order_Main.class);
//
//            tvReceiverName.setText(orderMainRec.getOrder_Main_Receiver());
//            tvReceiverPhone.setText(orderMainRec.getOrder_Main_Phone());
//            tvReceiverAddress.setText(orderMainRec.getOrder_Main_Address());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    //    Adapter for rvOrderDetProduct
    private class OrderManageDetAdapter extends RecyclerView.Adapter<OrderManageDetAdapter.PageViewHolder> {
        private LayoutInflater inflater;
        Context context;
        List<Order_Detail> orderDetailList;
        List<Product> productList;
        private ImageTask orderDetProdImgTask;
        private int imageSize;

        public OrderManageDetAdapter(Context context, List<Order_Detail> orderDetailList) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.orderDetailList = orderDetailList;
            imageSize = context.getResources().getDisplayMetrics().widthPixels / 4;
        }

        @NonNull
        @Override
        public OrderManageDetAdapter.PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.fragment_order_manage_detail_view, parent, false);
            return new OrderManageDetAdapter.PageViewHolder(view);
        }

        class PageViewHolder extends RecyclerView.ViewHolder {
            TextView tvProductName, tvProductPrice;
            ImageView ivOrderProductPic;

            public PageViewHolder(@NonNull View itemView) {
                super(itemView);
                tvProductName = itemView.findViewById(R.id.tvProductName);
                tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
                ivOrderProductPic = itemView.findViewById(R.id.ivOrderProductPic);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull OrderManageDetAdapter.PageViewHolder holder, int position) {
            final Order_Detail orderDetail = orderDetailList.get(position);
            final Product product = productList.get(position);

//            get productDetail through product ID
            String url = Common.URL_SERVER + "Prouct_Servlet";
            int productId = orderDetail.getProduct_ID(); //get product id in order detail
            orderDetProdImgTask = new ImageTask(url, productId, imageSize, holder.ivOrderProductPic);
            orderDetProdImgTask.execute();
            holder.tvProductName.setText(product.getProduct_Name()); // --delete ".get(position)"
            holder.tvProductPrice.setText("$" + product.getProduct_Price());
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("product", product);
//                    Navigation.findNavController(v).navigate(R.id.productDetailFragment, bundle);
//                }
//            });
        }

        @Override
        public int getItemCount() {
            try {
                if (productList != null) {
                    Log.e(TAG, "itemCount:" + productList.size());
                    return productList == null ? 0 : productList.size();
                }
            } catch (Exception e) {
                Log.e(TAG, "null list");
            }
            return productList == null ? 0 : productList.size();
        }
    }
}