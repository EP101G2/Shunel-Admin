package com.ed.shuneladmin;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.bean.Notice;
import com.ed.shuneladmin.bean.Notice_Schedule;
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
public class NoticeScheduleFragment extends Fragment {

    Activity activity;
    TextView tvDeleteScheduleN;
    ImageView ivAddScheduleN;
    Button checkScheduleN;
    private CommonTask ScheduleNGetAllTask;
    private List<Notice_Schedule> noticeAdimScheduleList, noticeCopyScheduleList;
    RecyclerView rvAdimScheduleN;
    SearchView SearchScheduleN;
    private ScheduleNApapter noticeScheduleApapter;
    Notice notice;
    Notice_Schedule notice_schedule;
    List<Notice_Schedule> scheduleArrayList = new ArrayList<>();

    boolean flag = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notice_schedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /* 初始化資料,包含從其他Activity傳來的Bundle資料 ,Preference資枓 */
        findViews(view);
        initData();

        /* 設置必要的系統服務元件如: Services、BroadcastReceiver */
        /* 設置View元件對應的linstener事件,讓UI可以與用戶產生互動 */
        setLinstener();
    }

    private void findViews(View view) {

        ivAddScheduleN = view.findViewById(R.id.ivAddScheduleN);
        tvDeleteScheduleN = view.findViewById(R.id.tvdeleteSchduleN);
        rvAdimScheduleN = view.findViewById(R.id.rvAdimScheduleN);
        SearchScheduleN = view.findViewById(R.id.SearchScheduleN);
        checkScheduleN = view.findViewById(R.id.checkScheduleN);
        rvAdimScheduleN.setLayoutManager(new LinearLayoutManager(activity));

    }

    private void initData() {
        noticeAdimScheduleList = getData();
        noticeCopyScheduleList = noticeAdimScheduleList;//複製一個List取值

        showSchedulelist(getData());

    }

    private void setLinstener() {
        SearchScheduleN.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // 如果搜尋條件為空字串，就顯示原始資料；否則就顯示搜尋後結果
                ScheduleNApapter adapter = (ScheduleNApapter) rvAdimScheduleN.getAdapter();
                try {
                    if (adapter != null) {
                        if (newText.isEmpty()) {
                            noticeAdimScheduleList = noticeCopyScheduleList;//再把值傳回來
                            showSchedulelist(noticeAdimScheduleList);
                        } else {
                            List<Notice_Schedule> SearchScheduleNAll = new ArrayList<>();
                            // 搜尋原始資料內有無包含關鍵字(不區別大小寫)
                            for (Notice_Schedule notice_schedule : noticeAdimScheduleList) {
                                if (notice_schedule.getNOTICE_SCHEDULE_T().toUpperCase().contains(newText.toUpperCase()) || notice_schedule.getNOTICE_SCHEDULE_D().toUpperCase().contains(newText.toUpperCase())) {
                                    SearchScheduleNAll.add(notice_schedule);
                                }
                            }
                            noticeAdimScheduleList = SearchScheduleNAll;
                            adapter.setList(noticeAdimScheduleList);
                        }

                        adapter.notifyDataSetChanged();
                        return true;

                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }


        });

        checkScheduleN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Common.URL_SERVER + "Notice_Schedule_Servlet";
                JsonObject jsonObject = new JsonObject();
                if (Common.networkConnected(activity)) {

                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//                    Log.e("", ));
                    jsonObject.addProperty("action", "deleteSchedule");
                    jsonObject.addProperty("delete", gson.toJson(scheduleArrayList));
                    Log.e("", String.valueOf(jsonObject));
                }

                boolean[] Expanded = noticeScheduleApapter.getExpanded();
                boolean isOpen = noticeScheduleApapter.getOpen();
                for (int i = 0; i < Expanded.length; i++) {
                    Expanded[i] = !Expanded[i];
                }

                ScheduleNGetAllTask= new CommonTask(url, jsonObject.toString());
                String jsonIn = "";

                try {
                    jsonIn = ScheduleNGetAllTask.execute().get();

                } catch (Exception e) {
                    Log.e(ContentValues.TAG, e.toString());
                }
                Log.e("------------", jsonIn);
                if (!jsonIn.equals("0")) {
                    Common.showToast(activity, "刪除成功");
                    initData();
                    if (flag) {
                        tvDeleteScheduleN.setText(R.string.delet);
                        flag = !flag;
                    }
                }
            }
        });

        tvDeleteScheduleN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!flag) {
                    tvDeleteScheduleN.setText(R.string.cancel);
                    flag = !flag;
                }else {
                    tvDeleteScheduleN.setText(R.string.delet);
                    flag = false;
                }

                noticeScheduleApapter = (ScheduleNApapter) rvAdimScheduleN.getAdapter();
                boolean[] Expanded = noticeScheduleApapter.getExpanded();

                for (int i = 0; i < Expanded.length; i++) {
                    Expanded[i] = !Expanded[i];
                }

                if (noticeScheduleApapter.getOpen() == true ){
                    scheduleArrayList.remove(notice);
                }

                noticeScheduleApapter.setExpanded(Expanded);
                noticeScheduleApapter.notifyDataSetChanged();
                checkScheduleN.setVisibility(View.GONE);
                scheduleArrayList.remove(notice);
            }

        });


        ivAddScheduleN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.flag = 4;
                NavController navController = Navigation.findNavController(activity, R.id.homeFragment);
                navController.navigate(R.id.noticeAdminFragment);
            }


        });
    }


    private void showSchedulelist(List<Notice_Schedule> noticeScheduleList) {
        if (noticeScheduleList == null || noticeScheduleList.isEmpty()) {
            Common.showToast(activity, R.string.noNotice);
        }
        noticeScheduleApapter = (ScheduleNApapter) rvAdimScheduleN.getAdapter();
        // 如果spotAdapter不存在就建立新的，否則續用舊有的
        if (noticeScheduleApapter == null) {
            rvAdimScheduleN.setAdapter(new ScheduleNApapter(activity, noticeScheduleList));
        } else {
            noticeScheduleApapter.setList(noticeScheduleList);
            noticeScheduleApapter.notifyDataSetChanged();
        }

    }


    private List<Notice_Schedule> getData() {
        List<Notice_Schedule> scheduleNList = new ArrayList<>();
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Notice_Schedule_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getScheduleNAll");
            String jsonOut = jsonObject.toString();
            ScheduleNGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = ScheduleNGetAllTask.execute().get();
                Type listType = new TypeToken<List<Notice_Schedule>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                scheduleNList = gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return scheduleNList;
    }

    private class ScheduleNApapter extends RecyclerView.Adapter<ScheduleNApapter.MyViewHolder> {

        Context context;
        List<Notice_Schedule> notice_scheduleList;
        private boolean[] Expanded;
        private boolean isOpen;


        public ScheduleNApapter(Context context, List<Notice_Schedule> notice_scheduleList) {
            this.context = context;
            this.notice_scheduleList = notice_scheduleList;
            Expanded = new boolean[notice_scheduleList.size()];
        }

        void setList(List<Notice_Schedule> noticeScheduleList) {

            this.notice_scheduleList = noticeScheduleList;

        }

        public boolean getOpen() {
            return isOpen;
        }

        public void setOpen(boolean open) {
            isOpen = open;
        }

        boolean[] getExpanded() {
            return this.Expanded;
        }

        void setExpanded(boolean[] Expanded) {
            this.Expanded = Expanded;
        }


        @NonNull
        @Override
        public ScheduleNApapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.noitce_adim_item, parent, false);
            return new ScheduleNApapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ScheduleNApapter.MyViewHolder holder, final int position) {
            final Notice_Schedule notice_schedule = noticeAdimScheduleList.get(position);
            final int noticeSchedule_ID = notice_schedule.getNOTICE_SCHEDULE_ID();

            holder.tvNoticeT.setText(notice_schedule.getNOTICE_SCHEDULE_T());
            holder.tvNoticeD.setText(notice_schedule.getNOTICE_SCHEDULE_D());
            holder.tvDateN.setText(notice_schedule.getNOTICE_SCHEDUL_STARTTIME().toString());
            holder.cbNotice.setVisibility(Expanded[position] ? View.VISIBLE : View.GONE);
            if (!Expanded[position]) {
                holder.cbNotice.setChecked(false);
            }


            holder.cbNotice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    if (isChecked) {
                        scheduleArrayList.add(notice_schedule);
                        checkScheduleN.setVisibility(View.VISIBLE);

                    } else {
                        scheduleArrayList.remove(notice_schedule);
                        if (scheduleArrayList.size() == 0) {
                            checkScheduleN.setVisibility(View.GONE);
                        }

                    }
                    Log.e(TAG, "數量：" + notice_scheduleList.size());
                }
            });

            holder.btUpdateND.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.flag = 5;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("NoitceSchedule", notice_schedule);
                    NavController navController = Navigation.findNavController(activity, R.id.homeFragment);
                    navController.navigate(R.id.noticeAdminFragment, bundle);
                }
            });

        }


        @Override
        public int getItemCount() {
            return noticeAdimScheduleList == null ? 0 : noticeAdimScheduleList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivCategoryN;
            TextView tvNoticeT;
            TextView tvNoticeD;
            TextView tvDateN;
            CheckBox cbNotice;
            Button btUpdateND;

            public MyViewHolder(View view) {
                super(view);
                ivCategoryN = view.findViewById(R.id.ivCategoryN);
                tvNoticeT = view.findViewById(R.id.tvNoticeT);
                tvNoticeD = view.findViewById(R.id.tvNoticeD);
                tvDateN = view.findViewById(R.id.tvDateN);
                cbNotice = view.findViewById(R.id.cbNotice);
                btUpdateND = view.findViewById(R.id.btUpdateND);


            }


        }
    }
}

