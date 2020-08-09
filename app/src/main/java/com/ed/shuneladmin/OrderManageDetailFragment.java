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
import com.google.gson.JsonObject;

import java.util.List;

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
    private TextView tvAccountIdDet, tvOrderIdDet, tvOrderDateDet, tvReceiverName, tvReceiverPhone, tvReceiverAddress;
    private Spinner spChangeStatus;
    private Button btCancel, btSave, btModifyOrderData;
    private String status = "";
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
//        bundle data from last page
        final NavController navController = Navigation.findNavController(view);
        Bundle bundle = getArguments();
        if (bundle == null || bundle.getSerializable("Orders") == null) {
            Common.showToast(activity, R.string.textnofound);
            navController.popBackStack();
            return;
        }
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
        rvOrderDetProduct.setAdapter(new OrderManageDetAdapter(getContext(), orderDetailList));

//        setting spinner
        spChangeStatus = view.findViewById(R.id.spChangeStatus);
        spChangeStatus.setSelection(0, true);
        String[] statusCategory = {"未付款", "未出貨", "已出貨", "已送達", "已取消", "已退貨"};
        ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, statusCategory);
        aAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spChangeStatus.setAdapter(aAdapter);
        spChangeStatus.setSelection(0,true);

//        setting buttons
        btCancel = view.findViewById(R.id.btLogin);
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
                        Log.e("updateStatus", "===" + status);
                        jsonObject.addProperty("action", "updateStatus");
                        ordersListDetGetTask = new CommonTask(url, jsonObject.toString());
                        String jsonIn = "";

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
            }
        });

        btModifyOrderData = view.findViewById(R.id.btModifyOrderData);
        btModifyOrderData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("Receiver", orderMain);
                Navigation.findNavController(v).navigate(R.id.action_orderManageDetailFragment_to_modifyReceiverDetailFragment, bundle);
            }
        });
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

        class PageViewHolder extends RecyclerView.ViewHolder{
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
            holder.tvProductPrice.setText("$"+product.getProduct_Price());
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
            return 0;
        }
    }
}