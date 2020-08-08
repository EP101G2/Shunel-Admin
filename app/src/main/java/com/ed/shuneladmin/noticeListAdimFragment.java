package com.ed.shuneladmin;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.bean.Notice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class noticeListAdimFragment extends Fragment {

    Activity activity;
    TextView tvUpdateSaleN;
    ImageView ivAddSaleN;
    Button btUpdaeSaleN;
    private CommonTask noticeAdimGetAllTask;
    private List<Notice> noticeAdimSaleList;
    RecyclerView rvAdimSaleN;
    SearchView SearchSaleN;
    private adimSaleNAdapter noticeAdimSaleAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notice_list_adim, container, false);
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
        ivAddSaleN = view.findViewById(R.id.ivAddSaleN);
        tvUpdateSaleN = view.findViewById(R.id.tvUpdateSaleN);
        rvAdimSaleN = view.findViewById(R.id.rvAdimSaleN);
        SearchSaleN = view.findViewById(R.id.SearchSaleN);
    }

    private void initData() {
        noticeAdimSaleList = getData();

        for(Notice notice : noticeAdimSaleList){
            notice.setOpen(false);
        }
        rvAdimSaleN.setLayoutManager(new LinearLayoutManager(activity));
        rvAdimSaleN.setAdapter(new adimSaleNAdapter(activity, noticeAdimSaleList));


    }

    private void setLinstener() {



        tvUpdateSaleN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Notice notice : noticeAdimSaleList){
                    notice.setOpen(!notice.isOpen());
                }
                noticeAdimSaleAdapter = (adimSaleNAdapter) rvAdimSaleN.getAdapter();
                noticeAdimSaleAdapter.setList(noticeAdimSaleList);
                noticeAdimSaleAdapter.notifyDataSetChanged();

            }
        });


        ivAddSaleN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(activity, R.id.homeFragment);
                navController.navigate(R.id.noticeAdminFragment);
            }


        });

    }


    private List<Notice> getData() {
        List<Notice> noticeList = new ArrayList<>();
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Notice_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getSaleAll");
            String jsonOut = jsonObject.toString();
            noticeAdimGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = noticeAdimGetAllTask.execute().get();
                Type listType = new TypeToken<List<Notice>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                noticeList = gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return noticeList;
    }



    private class adimSaleNAdapter extends RecyclerView.Adapter<adimSaleNAdapter.MyViewHolder> {
        Context context;
        List<Notice> noticeList;
        private boolean[] Expanded;

        public adimSaleNAdapter(Context context, List<Notice> noticeList) {
            this.context = context;
            this.noticeList = noticeList;
            Expanded = new boolean[noticeList.size()];
        }

        void setList( List<Notice> noticeList){
            this.noticeList = noticeList;
        }

//        private  void  expand(int position){
//            Expanded[position] = noticeList.get(position).isOpen();
//            Expanded[position] = !Expanded[position];
//            Log.e("Expanded", String.valueOf(Expanded[position]));
//            notifyDataSetChanged();
//        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.noitce_adim_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Expanded[position] = noticeList.get(position).isOpen();
            final Notice notice = noticeAdimSaleList.get(position);
            int notice_id = notice.getNotice_ID();
            holder.tvNoticeT.setText(notice.getNotice_Title());
            holder.tvNoticeD.setText(notice.getNotice_Content());
            Log.e("---------",notice.getNotice_time().toString()+"---");
            holder.tvDateN.setText(notice.getNotice_time().toString());
            holder.cbNotice.setVisibility(Expanded[position]? View.VISIBLE:View.GONE);
        }

        @Override
        public int getItemCount() {
            return noticeAdimSaleList == null ? 0 : noticeAdimSaleList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivCategoryN;
            TextView tvNoticeT;
            TextView tvNoticeD;
            TextView tvDateN;
            CheckBox cbNotice;

            public MyViewHolder(View view) {
                super(view);
                ivCategoryN = view.findViewById(R.id.ivCategoryN);
                tvNoticeT = view.findViewById(R.id.tvNoticeT);
                tvNoticeD = view.findViewById(R.id.tvNoticeD);
                tvDateN = view.findViewById(R.id.tvDateN);
                cbNotice = view.findViewById(R.id.cbNotice);

            }


        }
    }
}
