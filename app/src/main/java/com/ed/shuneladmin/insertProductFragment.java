package com.ed.shuneladmin;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.bean.Product;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class insertProductFragment extends Fragment {
    Activity activity;
    EditText nameOfProduct,colorOfProduct,priceOfProduct,detailOfProduct,categoryOfProduct,statusOfProduct;
    Product product;
    Button btaddproduct;
    Common common;
    CommonTask insertProduct;



    public insertProductFragment() {
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
        return inflater.inflate(R.layout.fragment_insert_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameOfProduct = view.findViewById(R.id.nameOfProduct);
        colorOfProduct = view.findViewById(R.id.colorOfProduct);
        priceOfProduct = view.findViewById(R.id.priceOfProduct);
        detailOfProduct = view.findViewById(R.id.detailOfProduct);
        categoryOfProduct = view.findViewById(R.id.categoryOfProduct);
        statusOfProduct = view.findViewById(R.id.statusOfProduct);
        btaddproduct = view.findViewById(R.id.btaddproduct);









        btaddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.networkConnected(activity)){
                    product = new Product();
                    product.setProduct_Name(nameOfProduct.getText().toString());
                    product.setProduct_Color(colorOfProduct.getText().toString());
                    product.setProduct_Price(Integer.parseInt(priceOfProduct.getText().toString()));
                    product.setProduct_Ditail(detailOfProduct.getText().toString());
                    product.setProduct_Category_ID(Integer.parseInt(categoryOfProduct.getText().toString()));
                    product.setProduct_Status(Integer.parseInt(statusOfProduct.getText().toString()));




                    String url = Common.URL_SERVER + "Prouct_Servlet";
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "insertProduct");
                    jsonObject.addProperty("product",new Gson().toJson(product));

                    insertProduct = new CommonTask(url, jsonObject.toString());

                    try {
                        String rp =  insertProduct.execute().get();
                        int count = Integer.parseInt(rp);

                        if (count == 1) {

                            Toast.makeText(activity, R.string.insertsuccess, Toast.LENGTH_SHORT).show();

                        }else {
                            Toast.makeText(activity, R.string.insertfail, Toast.LENGTH_SHORT).show();
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
}
