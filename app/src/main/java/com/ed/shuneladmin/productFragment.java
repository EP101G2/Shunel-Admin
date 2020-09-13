package com.ed.shuneladmin;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
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

import static com.ed.shuneladmin.MainActivity.flag;


/**
 * A simple {@link Fragment} subclass.
 */
public class productFragment extends Fragment {
    Activity activity;
    private List<Product> product;
    private List<Product> promotionProduct = new ArrayList<>();
    private List<Product> onsaleProduct = new ArrayList<>();
    private List<Product> shelvesProduct = new ArrayList<>();
    private List<Product> searchProduct = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommonTask productGetAllTask,insertAddress;
    private RadioGroup productstatus;
    private RadioButton allProduct;
    FloatingActionButton btAdd;
    private SearchView searchView3;
    private ImageView ivmap;
    private AlertDialog dialog;
    private Button submit;
    private EditText enterAddress;
    private String address;


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
        ivmap = view.findViewById(R.id.ivmap);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);
        findViews(view);
        product = getProduct("getAll");
        searchProduct.clear();
        searchProduct.addAll(product);
        showBooks(product);
        allProduct.setChecked(true);

        //設定店家位址
        ivmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                v = LayoutInflater.from(activity).inflate(R.layout.dialog_set_map, null);
                alertDialog.setView(v);

                dialog = alertDialog.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                dialog.getWindow().setLayout(1000, 1000);

                submit = v.findViewById(R.id.submit);
                enterAddress = v.findViewById(R.id.enterAddress);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        address = enterAddress.getText().toString();
                        JsonObject jsonObject = new JsonObject();
                        if (Common.networkConnected(activity)) {
                            String url = Common.URL_SERVER + "Prouct_Servlet";
                            jsonObject.addProperty("action", "Address");
                            jsonObject.addProperty("Address", address);



                            insertAddress = new CommonTask(url, jsonObject.toString());

                            try {
                                String rp = insertAddress.execute().get();
                                int count = Integer.parseInt(rp);
                                Log.e("--------", count + "+++++");
                                if (count == 0) {
                                    Common.showToast(activity,"修改失敗");
                                } else {
                                dialog.dismiss();
                                    Common.showToast(activity,"修改位址成功");
                                }
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });





            }
        });


        //============商品管理頁面的Radiobutton
        ProductAdapter productAdapter = (ProductAdapter) recyclerView.getAdapter();
        productstatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.shelvesProduct://單選 下架
                        shelvesProduct.clear();
                            Log.e("這是下架的allproduct", product.size() + "");
                            for (int i = 0; i <= product.size() - 1; i++) {
                                if (product.get(i).getProduct_Status() == 0) {
                                    shelvesProduct.add(product.get(i));
                                }
                            }
                        searchProduct.clear();
                            searchProduct.addAll(shelvesProduct);
                            productAdapter.setProducts(shelvesProduct);
                            productAdapter.notifyDataSetChanged();

                        //showBooks(product);
                        break;
                    case R.id.onsaleProduct: //單選 上架

                        onsaleProduct.clear();
                        for(int i = 0 ; i <= product.size()-1 ; i++){
                            if(product.get(i).getProduct_Status()==1){
                                onsaleProduct.add(product.get(i));
                            }
                        }
                        searchProduct.clear();
                        searchProduct.addAll(onsaleProduct);
                        //showBooks(product);
                        productAdapter.setProducts(onsaleProduct);
                        productAdapter.notifyDataSetChanged();
                        break;
                    case R.id.promotionProduct: //單選 促銷
                        promotionProduct.clear();
                        for(int i = 0 ; i <= product.size()-1 ; i++){
                            if(product.get(i).getProduct_Status()==2){
                                promotionProduct.add(product.get(i));
                            }
                        }
                        searchProduct.clear();
                        searchProduct.addAll(promotionProduct);
                       // showBooks(product);
                         productAdapter.setProducts(promotionProduct);
                        productAdapter.notifyDataSetChanged();

                        break;
                    case R.id.allProduct: //單選 所有商品
                        product.clear();
                        product = getProduct("getAll");
                        searchProduct.clear();
                        searchProduct.addAll(product);
                        productAdapter.setProducts(product);
                        productAdapter.notifyDataSetChanged();
//                        showBooks(allproduct);
//                        recyclerView.setAdapter(productAdapter);

                        break;

                }

            }
        });
        //========================================


        //=============快速搜尋===========================
        searchView3.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {   //沒有輸入東西 就是輸出所有的東西
                    showBooks(searchProduct);
                } else {
                    List<Product> products = new ArrayList<>();
                    // 搜尋原始資料內有無包含關鍵字(不區別大小寫)
                    for (Product product : searchProduct) {
                        if (product.getProduct_Name().toUpperCase().contains(newText.toUpperCase()) || String.valueOf(product.getProduct_ID()).contains(newText)) {   //toUpperCase()全部轉成大寫 就可以達到不分大小寫
                            //contains這個是一個比對的方法
                            //再由傳入的值(newText) 改為全大寫 與全部的好友資訊做比對
                            products.add(product);  //把達到條件的 加入  products
                        }
                    }
                    showBooks(products);
                }
                return true;
            }
        });
        //========================================
    }

    private void findViews(View view) {
        btAdd = view.findViewById(R.id.btAdd);
        allProduct = view.findViewById(R.id.allProduct);
        searchView3 = view.findViewById(R.id.searchView3);
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
