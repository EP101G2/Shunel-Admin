package com.ed.shuneladmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ed.shuneladmin.R;
import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.ImageTask;
import com.ed.shuneladmin.bean.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.productmyviewholder> {

    private Context context;
    private List<Product> products;
    private ImageTask productimageTask;
    private int imageSize;

    public ProductAdapter(Context context, List<Product> product) {
        this.context = context;
        this.products = product;
        imageSize = context.getResources().getDisplayMetrics().widthPixels / 4;

    }
    @NonNull
    @Override
    public productmyviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_product, parent, false);
        return new productmyviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull productmyviewholder holder, int position) {
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
        holder.tvproductcategory.setText(category);
        holder.tvproductdetail.setText(product.getProduct_Ditail());

    }

    @Override
    public int getItemCount() {

        return products == null ? 0 : products.size();
    }



    public class productmyviewholder extends RecyclerView.ViewHolder{
        private TextView tvproductname,tvproductid,tvproductcolor,tvproductprice,tvproductstatus,tvproductcategory,tvproductdetail;
        private ImageView ivsearch;


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

        }
    }

    public void setProducts(List<Product> product) {
        this.products = product;
    }



}
