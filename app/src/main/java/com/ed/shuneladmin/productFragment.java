package com.ed.shuneladmin;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.adapter.ProductAdapter;
import com.ed.shuneladmin.bean.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class productFragment extends Fragment {
    Activity activity;
    private List<Product> product;
    private List<Product> promotionProduct = new ArrayList<>();
    private List<Product> onsaleProduct = new ArrayList<>();
    private List<Product> shelvesProduct = new ArrayList<>();
    private List<Product> allproduct = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommonTask productGetAllTask;
    private RadioGroup productstatus;
    FloatingActionButton btAdd ;


    public productFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerview);
        productstatus = view.findViewById(R.id.productstatus);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);
        findViews(view);
        product = getProduct("getAll");
        for(Product p:product){
            allproduct.add(p);
        }


        showBooks(product);

        //============商品管理頁面的Radiobutton
        ProductAdapter productAdapter = (ProductAdapter) recyclerView.getAdapter();
        productstatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.shelvesProduct://單選 下架
                        Log.e("這是下架的allproduct",allproduct.size()+"");
                        for(int i = 0 ; i <= allproduct.size()-1 ; i++){
                            if(allproduct.get(i).getProduct_Status()==0){
                                shelvesProduct.add(allproduct.get(i));
                            }
                        }
                        product.clear();
                        product = shelvesProduct;
                        productAdapter.setProducts(product);
                        productAdapter.notifyDataSetChanged();
                        //showBooks(product);
                        break;
                    case R.id.onsaleProduct: //單選 上架
                        Log.e("這是上架的allproduct",allproduct.size()+"");
                        for(int i = 0 ; i <= allproduct.size()-1 ; i++){
                            if(allproduct.get(i).getProduct_Status()==1){
                                onsaleProduct.add(allproduct.get(i));
                            }
                        }
                        product.clear();
                        product = onsaleProduct;
                        //showBooks(product);
                        productAdapter.setProducts(product);
                        productAdapter.notifyDataSetChanged();
                        break;
                    case R.id.promotionProduct: //單選 促銷
                        Log.e("這是促銷的allproduct",allproduct.size()+"");
                        for(int i = 0 ; i <= allproduct.size()-1 ; i++){
                            if(allproduct.get(i).getProduct_Status()==2){
                                promotionProduct.add(allproduct.get(i));
                            }
                        }
                        product.clear();
                        product = promotionProduct;
                       // showBooks(product);
                         productAdapter.setProducts(product);
                        productAdapter.notifyDataSetChanged();

                        break;
                    case R.id.allProduct: //單選 所有商品
                        Log.e("這是所有商品的allproduct",allproduct.size()+"");
                       product.clear();
//                        for(Product p:allproduct){
//                            product.add(p);
//                        }

                        product = getProduct("getAll");
                        productAdapter.setProducts(product);
                        productAdapter.notifyDataSetChanged();
//                        showBooks(allproduct);
//                        recyclerView.setAdapter(productAdapter);

                        break;

                }

            }
        });
        //========================================


    }

    private void findViews(View view) {
        btAdd = view.findViewById(R.id.btAdd);
        showtest();
    }




    private void showtest() {

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.insertProductFragment);
            }
        });


    }


    private List<Product> getProduct(String action) {
        List<Product> products = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Prouct_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", action);
            productGetAllTask = new CommonTask(url, jsonObject.toString());
            try {
                String jsonIn = productGetAllTask.execute().get();
                Type listType = new TypeToken<List<Product>>() {
                }.getType();
                products = new Gson().fromJson(jsonIn, listType);

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        Log.e("--------------",products+"");
        return products;
    }

    private void showBooks(List<Product> product) {
        if (product == null || product.isEmpty()) {
            Common.showToast(activity, R.string.productempty);
        }
        ProductAdapter productAdapter = ( ProductAdapter) recyclerView.getAdapter();
        if (productAdapter == null) {
            recyclerView.setAdapter(new ProductAdapter(activity, product));
        } else {
            productAdapter.setProducts(product);
            productAdapter.notifyDataSetChanged();

        }
    }

}
