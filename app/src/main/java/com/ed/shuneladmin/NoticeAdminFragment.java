package com.ed.shuneladmin;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Constraints;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.adapter.ProductAdapter;
import com.ed.shuneladmin.bean.Notice;
import com.ed.shuneladmin.bean.Product;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeAdminFragment extends Fragment {

    private Activity activity;
    private EditText edNoticeTitle, edNoticeDetail, edNStart, edNEnd;
    //    private Spinner spNoticeCategory;
    private Button btSendNotice;
    private String Title, Detail;
    //    private String choice = "";
    Notice notice;

    private CommonTask noticeAdminTask;
    private TextView tvAddNPageT;
    private NavController navController;
    private RadioGroup rgSchedule;
    private RadioButton rbAllProduct, rbOneProduct;
    private ProductChooseAdapter chooseAdapter;
    private RecyclerView rvAllproductForN;
    private ImageView ivCategoryN;
    private TextView tvNoticeT, tvNoticeD, tvDateN;
    private CheckBox cbNotice;
    private Button btUpdateND;
    private List<Product> productList;

    private int productType = 0;// 0 = 全部商品推送, 1= 單一商品推送

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notice_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        findViews(view);
        /* 初始化資料,包含從其他Activity傳來的Bundle資料 ,Preference資枓 */

        initData();
        /* 設置必要的系統服務元件如: Services、BroadcastReceiver */

        /* 設置View元件對應的linstener事件,讓UI可以與用戶產生互動 */
        setLinstener();


    }


    private void findViews(View view) {
        edNoticeTitle = view.findViewById(R.id.edNoticeTitle);
        edNoticeDetail = view.findViewById(R.id.edNoticeDetail);
        edNStart = view.findViewById(R.id.etNStart);
        edNEnd = view.findViewById(R.id.etNEnd);
//        spNoticeCategory = view.findViewById(R.id.spNoticeCategory);//spinner
        btSendNotice = view.findViewById(R.id.btSendNotice);
        tvAddNPageT = view.findViewById(R.id.tvAddNPageT);
        rgSchedule = view.findViewById(R.id.rgSchedule);
        rvAllproductForN = view.findViewById(R.id.rvAllproductForN);
        rvAllproductForN.setLayoutManager(new LinearLayoutManager(activity));
//        rvAllproductForN.setLayoutManager(new MyLinearLayoutManager(activity,false));


    }


    private void initData() {
        Bundle bundle;
        switch (MainActivity.flag) {
            case 0:
                String textSale = "新增促銷訊息";
                tvAddNPageT.append(textSale);
                bundle = getArguments();
                edNStart.setVisibility(View.GONE);
                edNEnd.setVisibility(View.GONE);
                if (bundle != null) {
                    notice = (Notice) bundle.getSerializable("NoitceAdim");
                    edNoticeTitle.setText(notice.getNotice_Title());
                    edNoticeDetail.setText(notice.getNotice_Content());
                }
                break;

            case 1:
                String textSystem = "新增系統訊息";
                tvAddNPageT.append(textSystem);
                rgSchedule.setVisibility(View.GONE);
                edNStart.setVisibility(View.GONE);
                edNEnd.setVisibility(View.GONE);
                bundle = getArguments();
                if (bundle != null) {
                    notice = (Notice) bundle.getSerializable("NoitceAdim");
                    edNoticeTitle.setText(notice.getNotice_Title());
                    edNoticeDetail.setText(notice.getNotice_Content());
                }
                break;

            case 2:
                String textUpdateSale = "修改促銷訊息";
                tvAddNPageT.setText(textUpdateSale);
                 edNStart.setVisibility(View.GONE);
                 edNEnd.setVisibility(View.GONE);
                bundle = getArguments();
                if (bundle != null) {
                    notice = (Notice) bundle.getSerializable("NoitceAdim");
                    edNoticeTitle.setText(notice.getNotice_Title());
                    edNoticeDetail.setText(notice.getNotice_Content());
                }
                break;

            case 3:
                String textUpdateSystem = "修改系統訊息";
                tvAddNPageT.setText(textUpdateSystem);
                 rgSchedule.setVisibility(View.GONE);
                 edNStart.setVisibility(View.GONE);
                 bundle = getArguments();
                 edNEnd.setVisibility(View.GONE);
                 if (bundle != null) {
                    notice = (Notice) bundle.getSerializable("NoitceAdim");
                    edNoticeTitle.setText(notice.getNotice_Title());
                    edNoticeDetail.setText(notice.getNotice_Content());
                }
                break;

            case 4:
                String textSchedule = "新增排程促銷訊息";
                tvAddNPageT.setText(textSchedule);

                break;

            case 5:
                String textUpdateSchedule = "修改排程促銷訊息";
                tvAddNPageT.setText(textUpdateSchedule);

                break;
        }


    }


    private void setLinstener() {

//        spNoticeCategory.setOnItemSelectedListener(listener);//spinner


        rgSchedule.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.rbAllProduct:
                        productType = 0;
                        rvAllproductForN.setVisibility(View.GONE);

                        break;
                    case R.id.rbOneProduct:

                        productType = 1;
                        productList = getData();
                        showProductlist(getData());
                        rvAllproductForN.setVisibility(View.VISIBLE);
                        break;

                }


            }
        });


        btSendNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Title = edNoticeTitle.getText().toString();
                Detail = edNoticeDetail.getText().toString();
//                Log.e("T+D",Title+Detail);
                if (Common.networkConnected(activity)) {
                    String url = Common.URL_SERVER + "Notice_Servlet";
                    JsonObject jsonObject = new JsonObject();
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    String title;
                    String detail;
                    switch (MainActivity.flag) {
                        case 0:
                            Log.e("促銷訊息", "===" + MainActivity.flag);
                            jsonObject.addProperty("action", "sendSaleN");
                            jsonObject.addProperty("title", Title);
                            jsonObject.addProperty("msg", Detail);
                            jsonObject.addProperty("productType", productType);
                            Log.e("促銷訊息", "===" + jsonObject.toString());
                            Common.showToast(activity, "促銷訊息已送出");
                            break;


                        case 1:
                            Log.e("系統訊息", "===" + MainActivity.flag);
                            jsonObject.addProperty("action", "sendSystemN");
                            jsonObject.addProperty("title", Title);
                            jsonObject.addProperty("msg", Detail);
                            Log.e("系統訊息", "===" + jsonObject.toString());
                            Common.showToast(activity, "系統訊息已送出");
                            break;

                        case 2:
                            title = edNoticeTitle.getText().toString();
                            detail = edNoticeDetail.getText().toString();
                            notice.setNotice_Title(title);
                            notice.setNotice_Content(detail);
                            Log.e("修改促消訊息", "===" + MainActivity.flag);
                            jsonObject.addProperty("action", "update");
                            jsonObject.addProperty("notice", gson.toJson(notice));
                            Log.e("系統訊息", "===" + jsonObject.toString());
                            Common.showToast(activity, "修改促銷訊息已送出");
                            break;

                        case 3:
                            title = edNoticeTitle.getText().toString();
                            detail = edNoticeDetail.getText().toString();
                            notice.setNotice_Title(title);
                            notice.setNotice_Content(detail);
                            Log.e("修改促消訊息", "===" + MainActivity.flag);
                            jsonObject.addProperty("action", "update");
                            jsonObject.addProperty("notice", gson.toJson(notice));
                            Log.e("系統訊息", "===" + jsonObject.toString());
                            Common.showToast(activity, "修改系統訊息已送出");
                            break;


                    }
                    noticeAdminTask = new CommonTask(url, jsonObject.toString());
                    String jsonIn = "";

                    try {
                        jsonIn = noticeAdminTask.execute().get();

                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    Log.e("------------", jsonIn);
                }
                navController.popBackStack();
            }
        });

        edNStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        edNStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        

//        spNoticeCategory.setSelection(0, true); //spinner
//        String[] noticeCategory = {"促銷訊息", "系統訊息"};
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity,
////                提供textView元件,提供一個當模板，提供預設樣式
//                android.R.layout.simple_spinner_item, noticeCategory);
//
//        arrayAdapter.setDropDownViewResource(
//                android.R.layout.simple_spinner_dropdown_item);
//        spNoticeCategory.setAdapter(arrayAdapter);
////        Spinner的管家，把多個view跟data結合
//        spNoticeCategory.setSelection(0, true);
////        設定預選


    }

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


    public class MyLinearLayoutManager extends LinearLayoutManager {
        private final String TAG = MyLinearLayoutManager.class.getSimpleName();

        private boolean isScrollEnabled = true;

        public MyLinearLayoutManager(Context context, boolean isScrollEnabled) {
            super(context);
            this.isScrollEnabled = isScrollEnabled;
        }

        public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public boolean canScrollVertically() {
            //設定是否禁止滑動
            return isScrollEnabled && super.canScrollVertically();
        }
    }

    private class ProductChooseAdapter extends RecyclerView.Adapter<ProductChooseAdapter.MyViewHolder> {
        Context context;
        List<Product> productList;
        boolean isOpen;

        public ProductChooseAdapter(Context context, List<Product> productList) {
            this.context = context;
            this.productList = productList;

        }


        void setList(List<Product> productList) {

            this.productList = productList;

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
            holder.tvNoticeT.setText("品名:" + product.getProduct_Name());
            holder.tvNoticeD.setText("ID:" + product.getProduct_ID());
            holder.tvDateN.setText("顏色:" + product.getProduct_Color());
            holder.cbNotice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        productList.removeAll(productList);
                        productList.add(product);

                    } else {
                        productList.remove(product);
                    }

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
                cbNotice.setVisibility(View.VISIBLE);


            }

        }


    }


//    Spinner.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {//spinner
//        @Override
//        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//            parent.setVisibility(View.VISIBLE);
//            choice = parent.getItemAtPosition(position).toString();
//            Log.e("CHOICE", "===" + choice);
//
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> parent) {
//            parent.setVisibility(View.VISIBLE);
//        }
//    };


}
