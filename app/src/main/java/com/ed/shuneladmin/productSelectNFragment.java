package com.ed.shuneladmin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Constraints;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.Task.ImageTask;
import com.ed.shuneladmin.bean.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class productSelectNFragment extends Fragment {
    private RecyclerView rvAllproductForN;
    private ProductChooseAdapter chooseAdapter;
    private productSelectNFragment productSelectNFragment;
    Activity activity;
    private List<Product> productList;
    private CommonTask noticeAdminTask;
    private NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_product_select_n, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Common.getPreherences(activity).edit()
                .remove("productName")
                .remove("product_ID")
                .remove("productColor")
                .apply();

        productList = getData();
        rvAllproductForN = view.findViewById(R.id.rvAllproductForN);


        rvAllproductForN.setLayoutManager(new LinearLayoutManager(activity));

        rvAllproductForN.setLayoutManager(new LinearLayoutManager(activity));

        showProductlist(getData());


    }

//    private void back(){
//        if (chooseAdapter.isOpen = true) {

//        }
//    }


    private List<Product> getData() {
        List<Product> proucts = new ArrayList<>();
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Prouct_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            noticeAdminTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = noticeAdminTask.execute().get();
                Type listType = new TypeToken<List<Product>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                proucts = gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(Constraints.TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return proucts;
    }


    private void showProductlist(List<Product> products) {
        if (products == null || products.isEmpty()) {
            Common.showToast(activity, R.string.noNotice);
        }
        chooseAdapter = (ProductChooseAdapter) rvAllproductForN.getAdapter();
        // 如果spotAdapter不存在就建立新的，否則續用舊有的
        if (chooseAdapter == null) {
            rvAllproductForN.setAdapter(new ProductChooseAdapter(activity, products));
        } else {
            Log.e(Constraints.TAG, "00000000000");
            chooseAdapter.setList(products);
            chooseAdapter.notifyDataSetChanged();
        }

    }

    private class ProductChooseAdapter extends RecyclerView.Adapter<ProductChooseAdapter.MyViewHolder> {
        Context context;
        List<Product> productList;
        private boolean isOpen = false;
        boolean[] checkboxList;

        public boolean getOpen() {
            return isOpen;
        }

        public ProductChooseAdapter(Context context, List<Product> productList) {
            this.context = context;
            this.productList = productList;
            checkboxList = new boolean[productList.size()];

        }


        void setList(List<Product> productList) {

            this.productList = productList;

        }

        void refresh(int position, ProductChooseAdapter.MyViewHolder holder) {
//            holder.cbNotice.setChecked();
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.noitce_adim_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductChooseAdapter.MyViewHolder holder, int position) {
            final Product product = productList.get(position);
            final int product_ID = product.getProduct_ID();

            int imageSize = getResources().getDisplayMetrics().widthPixels / 3;
            Bitmap bitmap = null;
            String url = Common.URL_SERVER + "Prouct_Servlet";

            try {
                bitmap = new ImageTask(url, product_ID, imageSize, holder.ivCategoryN).execute().get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }


            holder.tvNoticeT.setText("品名:" + product.getProduct_Name());
            holder.tvNoticeD.setText("ID:" + product.getProduct_ID());
            holder.tvDateN.setText("顏色:" + product.getProduct_Color());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String productName = product.getProduct_Name();
                    int product_ID = product.getProduct_ID();
                    String productColor = product.getProduct_Color();


                    Common.getPreherences(activity).edit()
                            .putString("productName", productName)
                            .putInt("product_ID", product_ID)
                            .putString("productColor", productColor)
                            .putString("returnFlag","Y" )
                            .apply();



                   Navigation.findNavController(v).popBackStack();




                }
            });


        }

        @Override
        public int getItemCount() {
            return productList == null ? 0 : productList.size();
        }

        private class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivCategoryN;
            TextView tvNoticeT;
            TextView tvNoticeD;
            TextView tvDateN;
            CheckBox cbNotice;
            Button btUpdateND;

            public MyViewHolder(@NonNull View view) {
                super(view);
                ivCategoryN = view.findViewById(R.id.ivCategoryN);
                tvNoticeT = view.findViewById(R.id.tvNoticeT);
                tvNoticeD = view.findViewById(R.id.tvNoticeD);
                tvDateN = view.findViewById(R.id.tvDateN);
                cbNotice = view.findViewById(R.id.cbNotice);
                btUpdateND = view.findViewById(R.id.btUpdateND);
                cbNotice.setVisibility(View.GONE);
                btUpdateND.setVisibility(View.GONE);


            }

        }


    }

}
