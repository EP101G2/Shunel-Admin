package com.ed.shuneladmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.tv.TvContentRating;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import android.widget.TimePicker;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.applandeo.materialcalendarview.utils.CalendarProperties;
import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.Task.ImageTask;
import com.ed.shuneladmin.adapter.ProductAdapter;
import com.ed.shuneladmin.bean.Notice;
import com.ed.shuneladmin.bean.Notice_Schedule;
import com.ed.shuneladmin.bean.Product;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeAdminFragment extends Fragment implements OnSelectDateListener, TimePickerDialog.OnTimeSetListener {

    private Activity activity;
    private EditText edNoticeTitle, edNoticeDetail;
    //    private Spinner spNoticeCategory;
    private Button btSendNotice;
    private Date dateStart, dateEnd;
    private String title, detail, DateStr, DateStartStr, DateEndStr;
    //    private String choice = "";
    Notice notice;
    Notice_Schedule notice_schedule;
    Product product;
    //    private int sendProduct;
    private ConstraintLayout cvPraductCard;
    Timestamp start;
    Timestamp end;


    private CommonTask noticeAdminTask;
    private TextView tvAddNPageT, tvSelect;
    private NavController navController;
    private RadioGroup rgSchedule;
    private RadioButton rbAllProduct, rbOneProduct;
    private RecyclerView rvAllproductForN;
    private ImageView ivCategoryN;
    private TextView tvNoticeT, tvNoticeD, tvDateN, edNStart, edNEnd;

    private CheckBox cbNotice;
    private Button btUpdateND;
    private List<Product> productList;
    private AlertDialog dialog;
    String productName, productColor;
    int product_ID, notice_schedule_ID;
    private int imageSize;


    private int dateSatatus = 0;
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
        edNoticeTitle = view.findViewById(R.id.edN);
        edNoticeDetail = view.findViewById(R.id.edNotice);
        edNStart = view.findViewById(R.id.tvStart);
        edNEnd = view.findViewById(R.id.tvEnd);
        tvSelect = view.findViewById(R.id.tvSelect);
        cvPraductCard = view.findViewById(R.id.cvPraductCard);
        tvNoticeT = view.findViewById(R.id.tvproductT);
        tvNoticeD = view.findViewById(R.id.tvProductD);
        tvDateN = view.findViewById(R.id.tvDateN);

//        spNoticeCategory = view.findViewById(R.id.spNoticeCategory);//spinner
        btSendNotice = view.findViewById(R.id.btSendNotice);
        tvAddNPageT = view.findViewById(R.id.tvAddNPageT);
        rgSchedule = view.findViewById(R.id.rgSchedule);
        rbAllProduct = view.findViewById(R.id.rbAllProduct);
        rbOneProduct = view.findViewById(R.id.rbOneProduct);
        ivCategoryN = view.findViewById(R.id.ivCategoryN);


//        rvAllproductForN = view.findViewById(R.id.rvAllproductForN);
//        rvAllproductForN.setLayoutManager(new LinearLayoutManager(activity));
//        rvAllproductForN.setLayoutManager(new MyLinearLayoutManager(activity,false));

//        edNoticeTitle.setInputType(InputType.TYPE_NULL);
//        edNoticeDetail.setInputType(InputType.TYPE_NULL);

    }


    private void initData() {
        productName = Common.getPreherences(activity).getString("productName", null);
        product_ID = Common.getPreherences(activity).getInt("product_ID", 0);
        productColor = Common.getPreherences(activity).getString("productColor", null);

        Log.e("product_ID", "product_ID:getPreherences" + product_ID);


        Bundle bundle = getArguments();

        Log.e("MainActivity.flag", "MainActivity.flag" + MainActivity.flag);





        switch (MainActivity.flag) {
            case 0:
                String textSale = "新增促銷訊息";
                tvAddNPageT.append(textSale);
                bundle = getArguments();
                edNStart.setVisibility(View.GONE);
                edNEnd.setVisibility(View.GONE);

//                if (bundle != null) {
//                    notice = (Notice) bundle.getSerializable("NoitceAdim");
//                    edNoticeTitle.setText(notice.getNotice_Title());
//                    edNoticeDetail.setText(notice.getNotice_Content());
//                }
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
                cvPraductCard.setVisibility(View.GONE);
                bundle = getArguments();
                if (bundle != null) {
                    notice = (Notice) bundle.getSerializable("NoitceAdim");
                    edNoticeTitle.setText(notice.getNotice_Title());
                    edNoticeDetail.setText(notice.getNotice_Content());
                }
                Log.e("lllllll", String.valueOf(notice.getCATEGORY_MESSAGE_ID()));

                if (notice.getCATEGORY_MESSAGE_ID() != 0) {
                    Log.e("ddddddd", "dddddd");
                    rbOneProduct.setChecked(true);
                    int product_ID = notice.getCATEGORY_MESSAGE_ID();

                    if (Common.networkConnected(activity)) {
                        String url = Common.URL_SERVER + "Prouct_Servlet";
                        JsonObject jsonObject = new JsonObject();
                        jsonObject.addProperty("action", "findById");
                        jsonObject.addProperty("PRODUCT_Id", product_ID);
                        String jsonOut = jsonObject.toString();
                        noticeAdminTask = new CommonTask(url, jsonOut);
                        String jsonIn = null;
                        try {
                            jsonIn = noticeAdminTask.execute().get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        product = gson.fromJson(jsonIn, Product.class);
                    } else {
                        Common.showToast(activity, R.string.textNoNetwork);
                    }
                }


                break;

            case 3:
                String textUpdateSystem = "修改系統訊息";
                tvAddNPageT.setText(textUpdateSystem);
                rgSchedule.setVisibility(View.GONE);
                edNStart.setVisibility(View.GONE);
                cvPraductCard.setVisibility(View.GONE);
                bundle = getArguments();
                edNEnd.setVisibility(View.GONE);

// 將取得的Bundle資料設定
                if (bundle != null) {
                    String result = bundle.getString("username");
                    String result2 = bundle.getString("spinner");
                    notice = (Notice) bundle.getSerializable("NoitceAdim");
                    edNoticeTitle.setText(notice.getNotice_Title());
                    edNoticeDetail.setText(notice.getNotice_Content());
                }
                break;

            case 4:
                String textSchedule = "新增排程促銷訊息";
                cvPraductCard.setVisibility(View.GONE);
                tvSelect.setVisibility(View.GONE);
                tvAddNPageT.setText(textSchedule);

                break;

            case 5:
                String textUpdateSchedule = "修改排程促銷訊息";
                tvAddNPageT.setText(textUpdateSchedule);
                if (bundle != null) {
                    notice_schedule = (Notice_Schedule) bundle.getSerializable("NoitceSchedule");
                    edNoticeTitle.setText(notice_schedule.getNOTICE_SCHEDULE_T());
                    edNoticeDetail.setText(notice_schedule.getNOTICE_SCHEDULE_D());
                    String startStr = notice_schedule.getNOTICE_SCHEDUL_STARTTIME().toString();
                    String endStr = notice_schedule.getNOTICE_SCHEDUL_ENDTIME().toString();
                    Log.e("startStr", "startStr:" + startStr);
                    edNStart.setText(startStr.substring(0, startStr.length() - 5));
                    edNEnd.setText(endStr.substring(0, endStr.length() - 5));

                    if (notice_schedule.getPRODUCT_ID() != 0) {
                        rbOneProduct.setChecked(true);
                        int product_ID = notice_schedule.getPRODUCT_ID();


                        if (Common.networkConnected(activity)) {
                            String url = Common.URL_SERVER + "Prouct_Servlet";
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.addProperty("action", "findById");
                            jsonObject.addProperty("PRODUCT_Id", product_ID);
                            String jsonOut = jsonObject.toString();
                            noticeAdminTask = new CommonTask(url, jsonOut);
                            String jsonIn = null;
                            try {
                                jsonIn = noticeAdminTask.execute().get();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                            product = gson.fromJson(jsonIn, Product.class);

                            tvNoticeT.setText(product.getProduct_Name());
                            tvNoticeD.setText(String.valueOf(product_ID));
                            tvDateN.setText(product.getProduct_Color());
                            tvDateN.setTextColor(Color.BLACK);

                        } else {
                            Common.showToast(activity, R.string.textNoNetwork);
                        }
                    }
                }


                break;




        }




        //如果有進選擇商品頁面的話，就會把偏好設定的值塞進來，用returnFlag＝Ｙ做判斷
        if (Common.getPreherences(activity).getInt("product_ID", 0) != 0) {
            cvPraductCard.setVisibility(View.VISIBLE);
            tvSelect.setVisibility(View.VISIBLE);
            rbOneProduct.setChecked(true);
            if (Common.getPreherences(activity).getString("returnFlag", "N").equals("Y")) {
                tvNoticeT.setText(productName);
                tvNoticeD.setText(String.valueOf(product_ID));
                tvDateN.setText(productColor);

                if(product_ID != 0){
                    int imageSize = getResources().getDisplayMetrics().widthPixels / 3;
                    Bitmap bitmap = null;
                    String url = Common.URL_SERVER + "Prouct_Servlet";
                    try {
                        bitmap = new ImageTask(url, product_ID, imageSize).execute().get();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    if (bitmap != null) {
                        ivCategoryN.setImageBitmap(bitmap);
                    }

                }


            }

        } else {
            rbAllProduct.setChecked(true);

        }


    }


    private void setLinstener() {

//        spNoticeCategory.setOnItemSelectedListener(listener);//spinner


//        edNoticeDetail.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//            }
//        });

        tvAddNPageT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (MainActivity.flag) {
                    case 0:
                        edNoticeTitle.setText("開學季促銷！");
                        edNoticeDetail.setText("開學了，換個個打扮，換個心情，現在有多樣商品在商城中促銷！");
                        break;

                    case 1:
                        edNoticeTitle.setText("app更新");
                        edNoticeDetail.setText("app已更新至1.2.5版，請至app商城進行更新");
                        break;


                    case 4:
                        edNoticeTitle.setText("中秋特別促銷來了！");
                        edNoticeDetail.setText("一年一度的中秋節，除了送月餅、柚子、烤肉外，也可以有其他的選擇！目前商城有多樣商品促銷中，歡迎選購");
                        break;


                }
            }
        });


        rgSchedule.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.rbAllProduct:
                        productType = 0;
                        cvPraductCard.setVisibility(View.GONE);
                        tvSelect.setVisibility(View.GONE);
                        product_ID = 0;

                        break;
                    case R.id.rbOneProduct:

                        cvPraductCard.setVisibility(View.VISIBLE);
                        tvSelect.setVisibility(View.VISIBLE);


                        break;

                }


            }
        });

        cvPraductCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_noticeAdminFragment_to_productSelectNFragment);

            }
        });

        edNStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateSatatus = 1;


                openOneDayPicker();


            }
        });

        edNEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateSatatus = 2;
                openOneDayPicker();

            }
        });


        btSendNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                title = edNoticeTitle.getText().toString();
                detail = edNoticeDetail.getText().toString();

                if (!title.equals("") && !detail.equals("")) {
                    String url;

                    if (Common.networkConnected(activity)) {
                        if (MainActivity.flag == 4 || MainActivity.flag == 5) {

                            url = Common.URL_SERVER + "Notice_Schedule_Servlet";


                        } else {

                            url = Common.URL_SERVER + "Notice_Servlet";

                        }

                        JsonObject jsonObject = new JsonObject();
                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                        String startTime, endTime;
                        switch (MainActivity.flag) {
                            case 0:
                                Log.e("促銷訊息", "===" + MainActivity.flag);
                                jsonObject.addProperty("action", "sendSaleN");
                                jsonObject.addProperty("title", title);
                                jsonObject.addProperty("msg", detail);
                                jsonObject.addProperty("productType", product_ID);
                                Log.e("productType", "productType:" + product_ID);


                                Common.showToast(activity, "促銷訊息已送出");
                                break;


                            case 1:
                                Log.e("系統訊息", "===" + MainActivity.flag);
                                jsonObject.addProperty("action", "sendSystemN");
                                jsonObject.addProperty("title", title);
                                jsonObject.addProperty("msg", detail);
                                Log.e("系統訊息", "===" + jsonObject.toString());
                                Common.showToast(activity, "系統訊息已送出");
                                break;

                            case 2:

                                notice.setNotice_Title(title);
                                notice.setNotice_Content(detail);
                                Log.e("修改促消訊息", "===" + MainActivity.flag);
                                jsonObject.addProperty("action", "update");
                                jsonObject.addProperty("notice", gson.toJson(notice));
                                jsonObject.addProperty("productType", product_ID);
//                                Log.e("系統訊息", "===" + jsonObject.toString());
                                Log.e("productType", "productType:" + product_ID);
                                Common.showToast(activity, "修改促銷訊息已送出");

                                break;

                            case 3:
                                notice.setNotice_Title(title);
                                notice.setNotice_Content(detail);

                                jsonObject.addProperty("action", "update");
                                jsonObject.addProperty("notice", gson.toJson(notice));

                                Common.showToast(activity, "修改系統訊息已送出");
                                break;

                            case 4:
                                if (Pass()) {
                                    startTime = String.valueOf(edNStart.getText());
                                    endTime = String.valueOf(edNEnd.getText());

//                                notice_schedule.setNOTICE_SCHEDULE_T("hi");
//                                notice_schedule.setNOTICE_SCHEDULE_D(detail);
//                                notice_schedule.setNOTICE_SCHEDUL_STARTTIME(start);
//                                notice_schedule.setNOTICE_SCHEDUL_ENDTIME(end);
//                                notice_schedule.setPRODUCT_ID(sendProduct);
                                    notice_schedule = new Notice_Schedule(0, title, detail, start, end, 0, product_ID);
                                    jsonObject.addProperty("action", "insert");
                                    jsonObject.addProperty("productType", product_ID);
                                    jsonObject.addProperty("notice_Schedule", gson.toJson(notice_schedule));
                                    Common.showToast(activity, "排程訊息已送出");
                                }


                                break;


                            case 5:
                                if (Pass()) {
                                    notice_schedule_ID = notice_schedule.getNOTICE_SCHEDULE_ID();
                                    notice_schedule = new Notice_Schedule(notice_schedule_ID, title, detail, start, end, product_ID);
                                    jsonObject.addProperty("action", "update");
                                    jsonObject.addProperty("productType", product_ID);
                                    jsonObject.addProperty("notice_Schedule", gson.toJson(notice_schedule));
                                    Common.showToast(activity, "修改排程訊息已送出");
                                }

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
                    Navigation.findNavController(v).popBackStack();
//                    NavController navController = Navigation.findNavController(activity,R.id.homeFragment);
//
//                    navController.navigate(R.id.action_noticeAdminFragment_to_homeFragment);
                } else {
                    Toast.makeText(activity, "訊息輸入未完成", Toast.LENGTH_SHORT).show();
                }
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

    private boolean Pass() {

        boolean defaltPass = true;

        if (DateStartStr != null && DateEndStr != null) {

            if (DateStartStr.equals("") || DateEndStr.equals("")) {
                Toast.makeText(activity, "請填入排程日期", Toast.LENGTH_SHORT).show();
                return false;
            }

            //日期輸入錯誤防呆裝置
            start = Timestamp.valueOf(DateStartStr + ":00");
            end = Timestamp.valueOf(DateEndStr + ":00");
            if (start.getTime() > end.getTime()) {
                Toast.makeText(activity, "排程日期有誤", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            start = Timestamp.valueOf(edNStart.getText().toString() + ":00");
            end = Timestamp.valueOf(edNEnd.getText().toString() + ":00");
        }

        return defaltPass;
    }

    private void openOneDayPicker() {
        Calendar today = Calendar.getInstance();
        Calendar max = Calendar.getInstance();
        Calendar min = Calendar.getInstance();
        max.add(Calendar.MONTH, 12);
        min.add(Calendar.DATE, -1);


        DatePickerBuilder oneDayBuilder = new DatePickerBuilder(getContext(), this)
                .setPickerType(CalendarView.ONE_DAY_PICKER)
                .setDate(min)
                .setHeaderColor(R.color.sampleLighter)
                .setHeaderLabelColor(R.color.currentMonthDayColor)
                .setSelectionColor(R.color.daysLabelColor)
                .setTodayLabelColor(R.color.colorAccent)
                .setDialogButtonsColor(R.color.sampleLighter)
                .setDisabledDaysLabelsColor(R.color.disabledDialogButtonColor)
                .setPreviousButtonSrc(R.drawable.ic_arrow_left)
                .setForwardButtonSrc(R.drawable.ic_arrow_right)
                .setMinimumDate(min)
                .setMaximumDate(max)
                .setTodayColor(R.color.sampleLighter)
                .setHeaderVisibility(View.VISIBLE);


        DatePicker oneDayPicker = oneDayBuilder.build();
        oneDayPicker.show();


    }


//    private List<Product> getData() {
//        List<Product> proucts = new ArrayList<>();
//        if (Common.networkConnected(activity)) {
//            String url = Common.URL_SERVER + "Prouct_Servlet";
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("action", "getAll");
//            String jsonOut = jsonObject.toString();
//            noticeAdminTask = new CommonTask(url, jsonOut);
//            try {
//                String jsonIn = noticeAdminTask.execute().get();
//                Type listType = new TypeToken<List<Product>>() {
//                }.getType();
//                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//                proucts = gson.fromJson(jsonIn, listType);
//            } catch (Exception e) {
//                Log.e(Constraints.TAG, e.toString());
//            }
//        } else {
//            Common.showToast(activity, R.string.textNoNetwork);
//        }
//        return proucts;
//    }


//    private void showProductlist(List<Product> products) {
//        if (products == null || products.isEmpty()) {
//            Common.showToast(activity, R.string.noNotice);
//        }
//        chooseAdapter = (ProductChooseAdapter) rvAllproductForN.getAdapter();
//        // 如果spotAdapter不存在就建立新的，否則續用舊有的
//        if (chooseAdapter == null) {
//            rvAllproductForN.setAdapter(new ProductChooseAdapter(activity, products));
//        } else {
//            Log.e(Constraints.TAG, "00000000000");
//            chooseAdapter.setList(products);
//            chooseAdapter.notifyDataSetChanged();
//        }
//
//    }

    //取得時間
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String hourStr = String.valueOf(hourOfDay).length() == 1 ? "0" + hourOfDay : String.valueOf(hourOfDay);
        String minuteStr = String.valueOf(minute).length() == 1 ? "0" + minute : String.valueOf(minute);

//        DateStr = DateStr + " " + hourStr + ":" + minuteStr;
        Log.e("dateSatatus", "dateSatatus:" + dateSatatus);

        switch (dateSatatus) {
            case 1:

                DateStartStr = DateStr + " " + hourStr + ":" + minuteStr;

                edNStart.setText(DateStartStr);


                break;

            case 2:

                DateEndStr = DateStr + " " + hourStr + ":" + minuteStr;
                edNEnd.setText(DateEndStr);
                break;

        }
    }


    //選擇日期
    @Override
    public void onSelect(@NotNull List<Calendar> list) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = list.get(0);
        DateStr = df.format(c.getTime());

        TimePickerDialog timePickerDialog =
                new TimePickerDialog(
                        activity,
                        this,
                        0, 0, false);

        timePickerDialog.show();

    }


//    public class MyLinearLayoutManager extends LinearLayoutManager {
//        private final String TAG = MyLinearLayoutManager.class.getSimpleName();
//
//        private boolean isScrollEnabled = true;
//
//        public MyLinearLayoutManager(Context context, boolean isScrollEnabled) {
//            super(context);
//            this.isScrollEnabled = isScrollEnabled;
//        }
//
//        public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
//            super(context, orientation, reverseLayout);
//        }
//
//        @Override
//        public boolean canScrollVertically() {
//            //設定是否禁止滑動
//            return isScrollEnabled && super.canScrollVertically();
//        }
//    }

//    private class ProductChooseAdapter extends RecyclerView.Adapter<ProductChooseAdapter.MyViewHolder> {
//        Context context;
//        List<Product> productList;
//        boolean isOpen;
//        boolean[] checkboxList;
//
//        public ProductChooseAdapter(Context context, List<Product> productList) {
//            this.context = context;
//            this.productList = productList;
//            checkboxList = new boolean[productList.size()];
//
//        }
//
//
//        void setList(List<Product> productList) {
//
//            this.productList = productList;
//
//        }
//
//        void refresh(int position, ProductChooseAdapter.MyViewHolder holder) {
////            holder.cbNotice.setChecked();
//        }
//
//
////       void runOnUiThread(new Runnable() {
////           public void run () {
////
////
////
////           }
////       });
//
//
//        @NonNull
//        @Override
//        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(context).inflate(R.layout.noitce_adim_item, parent, false);
//            return new MyViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull ProductChooseAdapter.MyViewHolder holder, int position) {
//            final Product product = productList.get(position);
//            final int product_ID = product.getProduct_ID();
//            holder.tvNoticeT.setText("品名:" + product.getProduct_Name());
//            holder.tvNoticeD.setText("ID:" + product.getProduct_ID());
//            holder.tvDateN.setText("顏色:" + product.getProduct_Color());
////            holder.cbNotice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
////                @Override
////                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                    if(isChecked){
////                        checkboxList[position] = !checkboxList[position];
////                        chooseAdapter.checkboxList.
////                    }
////
////                }
////            });
//
//            boolean ifCheck = checkboxList[position];
//            holder.cbNotice.setChecked(ifCheck);
//
//
//            holder.cbNotice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//
//                    if (isChecked) {
//
//                        checkboxList[position] = !checkboxList[position];
//
//
//                        if (checkboxList[position]) {
//                            sendProduct = product.getProduct_ID();
//                            for (int i = 0; i < checkboxList.length; i++) {
//                                if (i != position && checkboxList[i]) {
//                                    checkboxList[i] = false;
//                                }
//                            }
//
//                        }
//
//                    }
//
//
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            chooseAdapter = (ProductChooseAdapter) rvAllproductForN.getAdapter();
//                            chooseAdapter.notifyDataSetChanged();//做畫面刷新
//                        }
//                    }, 1000);
//
//
//                }
//
//            });
//
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return productList == null ? 0 : productList.size();
//        }
//
//        private class MyViewHolder extends RecyclerView.ViewHolder {
//            ImageView ivCategoryN;
//            TextView tvNoticeT;
//            TextView tvNoticeD;
//            TextView tvDateN;
//            CheckBox cbNotice;
//            Button btUpdateND;
//
//            public MyViewHolder(@NonNull View view) {
//                super(view);
//                ivCategoryN = view.findViewById(R.id.ivCategoryN);
//                tvNoticeT = view.findViewById(R.id.tvNoticeT);
//                tvNoticeD = view.findViewById(R.id.tvNoticeD);
//                tvDateN = view.findViewById(R.id.tvDateN);
//                cbNotice = view.findViewById(R.id.cbNotice);
//                btUpdateND = view.findViewById(R.id.btUpdateND);
//                cbNotice.setVisibility(View.VISIBLE);
//                btUpdateND.setVisibility(View.GONE);
//
//
//            }
//
//        }
//
//
//    }


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
