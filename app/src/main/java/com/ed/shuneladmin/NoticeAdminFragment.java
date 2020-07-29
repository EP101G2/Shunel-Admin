package com.ed.shuneladmin;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.google.gson.JsonObject;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeAdminFragment extends Fragment {

    private Activity activity;
    private EditText edNoticeTitle, edNoticeDetail;
    private Spinner spNoticeCategory;
    private Button btSendNotice;
    private String Title, Detail;
    private String choice = "";
    private CommonTask noticeAdminTask;

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
        findViews(view);
        /* 初始化資料,包含從其他Activity傳來的Bundle資料 ,Preference資枓 */

        initData();
        /* 設置必要的系統服務元件如: Services、BroadcastReceiver */
        setSystemServices();
        /* 設置View元件對應的linstener事件,讓UI可以與用戶產生互動 */
        setLinstener();

    }


    private void findViews(View view) {
        edNoticeTitle = view.findViewById(R.id.edNoticeTitle);
        edNoticeDetail = view.findViewById(R.id.edNoticeDetail);
        spNoticeCategory = view.findViewById(R.id.spNoticeCategory);
        btSendNotice = view.findViewById(R.id.btSendNotice);


    }

    private void initData() {


    }

    private void setLinstener() {

        spNoticeCategory.setOnItemSelectedListener(listener);

        btSendNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Title = edNoticeTitle.getText().toString();
                Detail = edNoticeDetail.getText().toString();
//                Log.e("T+D",Title+Detail);
                if (Common.networkConnected(activity)) {
                    String url = Common.URL_SERVER + "Notice_Servlet";
                    JsonObject jsonObject = new JsonObject();
                    switch (choice) {
                        case "促銷訊息":
//                            Log.e("促銷訊息", "===" + choice);
                            jsonObject.addProperty("action", "sendSaleN");
                            jsonObject.addProperty("title", Title);
                            jsonObject.addProperty("msg", Detail);
                            Log.e("系統訊息", "===" + jsonObject.toString());
                            break;


                        case "系統訊息":
                            Log.e("系統訊息", "===" + choice);
                            jsonObject.addProperty("action", "sendSystemN");
                            jsonObject.addProperty("title", Title);
                            jsonObject.addProperty("msg", Detail);
                            Log.e("系統訊息", "===" + jsonObject.toString());
                            break;
                    }
                    noticeAdminTask = new CommonTask(url, jsonObject.toString());
                    String jsonIn = "";

                    try {
                        jsonIn = noticeAdminTask.execute().get();

                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    Log.e("------------",jsonIn);
                }

            }
        });


        spNoticeCategory.setSelection(0, true);
        String[] noticeCategory = {"促銷訊息", "系統訊息"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity,
//                提供textView元件,提供一個當模板，提供預設樣式
                android.R.layout.simple_spinner_item, noticeCategory);

        arrayAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spNoticeCategory.setAdapter(arrayAdapter);
//        Spinner的管家，把多個view跟data結合
        spNoticeCategory.setSelection(0, true);
//        設定預選


    }

    private void setSystemServices() {

    }

    Spinner.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            parent.setVisibility(View.VISIBLE);
            choice = parent.getItemAtPosition(position).toString();
            Log.e("CHOICE", "===" + choice);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            parent.setVisibility(View.VISIBLE);
        }
    };


}
