package com.ed.shuneladmin.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.ed.shuneladmin.R;
import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.ImageTask;
import com.ed.shuneladmin.bean.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.productmyviewholder> {
    private boolean[] userExpanded;
    private Context context;
    private List<Product> products = new ArrayList<>();
    private ImageTask productimageTask;
    private int imageSize;

    public ProductAdapter(Context context, List<Product> product) {

        this.context = context;

        this.products.addAll(product);
        //this.products = product;
        imageSize = context.getResources().getDisplayMetrics().widthPixels / 4;
        userExpanded = new boolean[product.size()];

    }
    @NonNull
    @Override
    public productmyviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_product, parent, false);
        return new productmyviewholder(view);
    }



    void setlistProduct(List<Product> productlist){

        products.addAll(productlist);
    };
    @Override
    public void onBindViewHolder(@NonNull final productmyviewholder holder, int position) {
        final Product product = products.get(position);
        String url = Common.URL_SERVER + "Prouct_Servlet";
        int id_product = product.getProduct_ID();
        productimageTask = new ImageTask(url, id_product, imageSize, holder.ivsearch);
        productimageTask.execute();
        holder.tvproductname.setText(products.get(position).getProduct_Name());
        holder.tvproductid.setText(String.valueOf(product.getProduct_ID()));
        holder.tvproductcolor.setText(products.get(position).getProduct_Color());
        holder.tvproductprice.setText(String.valueOf(product.getProduct_Price()));
        String status = "";
        switch (product.getProduct_Status()){
            case 0 : {
               status = "未上架";
                break;
            }
            case 1 : {
                status = "已上架";
                break;
            } case 2 : {
                status = "促銷商品";
                break;
            }
        }
        holder.tvproductstatus.setText(status);
        String category = "";
        switch (product.getProduct_Category_ID()){
            case 1 : {
                category = "戒指";
                break;
            }
            case 2 : {
                category = "項鍊";
                break;
            }
            case 3 : {
                category = "耳環";
                break;
            }
            case 4 : {
                category = "香氛項鍊";
                break;
            }
            case 5 : {
                category = "香氛耳環";
                break;
            }
        }

        // =================隱藏及顯示詳細資訊=========================
        holder.tvproductcategory.setText(category);
        holder.tvproductdetail.setText(product.getProduct_Ditail());


        holder.cldetail.setVisibility(
                userExpanded[position] ? View.VISIBLE : View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expand(holder.getAdapterPosition());

            }

        });


        if (userExpanded[position]){
            holder.ivbutton.setImageResource(R.drawable.up);
        }else {
            holder.ivbutton.setImageResource(R.drawable.down);
        }
        // ==========================================================
        holder.ivupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("product", product);
                Navigation.findNavController(v).navigate(R.id.insertProductFragment, bundle);
            }
        });


    }

    @Override
    public int getItemCount() {

        return products == null ? 0 : products.size();
    }



    public class productmyviewholder extends RecyclerView.ViewHolder{
        private TextView tvproductname,tvproductid,tvproductcolor,tvproductprice,tvproductstatus,tvproductcategory,tvproductdetail;
        private ImageView ivsearch,ivbutton,ivupdate;
        private ConstraintLayout cldetail;
        public productmyviewholder(@NonNull View view) {
            super(view);
            tvproductname = view.findViewById(R.id.tvproductname);
            tvproductid = view.findViewById(R.id.tvproductid);
            tvproductcolor = view.findViewById(R.id.tvproductcolor);
            tvproductprice = view.findViewById(R.id.tvproductprice);
            tvproductstatus = view.findViewById(R.id.tvproductstatus);
            tvproductcategory = view.findViewById(R.id.tvproductcategory);
            tvproductdetail = view.findViewById(R.id.tvproductdetail);
            ivsearch = view.findViewById(R.id.ivsearch);
            cldetail = view.findViewById(R.id.cldetail);
            ivbutton =view.findViewById(R.id.ivbutton);
            ivupdate = view.findViewById(R.id.ivupdate);
        }
    }
    public void setProducts(List<Product> product) {
        this.products = product;
    }

    private void expand(int position) {
        userExpanded[position] = !userExpanded[position];
        notifyDataSetChanged();
    }



}
