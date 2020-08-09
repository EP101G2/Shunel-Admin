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

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.adapter.ProductAdapter;
import com.ed.shuneladmin.bean.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class productFragment extends Fragment {
    Activity activity;
    private List<Product> product;
    private RecyclerView recyclerView;
    private CommonTask productGetAllTask;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        product = getProduct();
        showBooks(product);
        recyclerView.setAdapter(new ProductAdapter(getContext(),product));
        findViews(view);



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


    private List<Product> getProduct() {
        List<Product> products = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Prouct_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
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
