//package com.ed.shuneladmin.adapter;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.ed.shuneladmin.MainActivity;
//import com.ed.shuneladmin.R;
//import com.ed.shuneladmin.Task.Common;
//import com.ed.shuneladmin.Task.CommonTask;
//import com.ed.shuneladmin.bean.Notice;
//import com.ed.shuneladmin.noticeListAdimFragment;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonObject;
//import com.google.gson.reflect.TypeToken;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//
//import static androidx.constraintlayout.widget.Constraints.TAG;
//
//public class AdimSaleNAdapter extends RecyclerView.Adapter<AdimSaleNAdapter.MyViewHolder> {
//    private List<Notice> noticeAdimSaleList;
//    private CommonTask noticeAdimGetAllTask;
//    Context context;
//    List<Notice> noticeList;
//    private boolean[] Expanded;
//    Activity activity ;
//
//    public AdimSaleNAdapter(Context context, List<Notice> noticeList) {
//        this.context = context;
//        this.noticeList = noticeList;
//        Expanded = new boolean[noticeList.size()];
//    }
//
//    void setList(List<Notice> noticeList) {
//
//
//        this.noticeList = noticeList;
//
//    }
//
//    boolean[] getExpanded(){
//        return  this.Expanded;
//    }
//
//    void setExpanded(boolean[] Expanded){
//        this.Expanded = Expanded;
//    }
////        private  void  expand(int position){
////            Expanded[position] = noticeList.get(position).isOpen();
////            Expanded[position] = !Expanded[position];
////            Log.e("Expanded", String.valueOf(Expanded[position]));
////            notifyDataSetChanged();
////        }
//
//    @NonNull
//    @Override
//    public AdimSaleNAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.noitce_adim_item, parent, false);
//        return new AdimSaleNAdapter.MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull AdimSaleNAdapter.MyViewHolder holder, int position) {
//        final Notice notice = noticeAdimSaleList.get(position);
//        noticeAdimSaleList = getData();
//        int notice_id = notice.getNotice_ID();
//        holder.tvNoticeT.setText(notice.getNotice_Title());
//        holder.tvNoticeD.setText(notice.getNotice_Content());
//        Log.e("---------", notice.getNotice_time().toString() + "---");
//        holder.tvDateN.setText(notice.getNotice_time().toString());
//        holder.cbNotice.setVisibility(Expanded[position] ? View.VISIBLE : View.GONE);
//        holder.btUpdateND.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MainActivity.flag = 2;
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("NoitceAdim",notice);
//                NavController navController = Navigation.findNavController(Activity,R.id.homeFragment);
//                navController.navigate(R.id.noticeAdminFragment, bundle);
//            }
//        });
//
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return noticeAdimSaleList == null ? 0 : noticeAdimSaleList.size();
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//        ImageView ivCategoryN;
//        TextView tvNoticeT;
//        TextView tvNoticeD;
//        TextView tvDateN;
//        CheckBox cbNotice;
//        Button btUpdateND;
//
//        public MyViewHolder(View view) {
//            super(view);
//            ivCategoryN = view.findViewById(R.id.ivCategoryN);
//            tvNoticeT = view.findViewById(R.id.tvNoticeT);
//            tvNoticeD = view.findViewById(R.id.tvNoticeD);
//            tvDateN = view.findViewById(R.id.tvDateN);
//            cbNotice = view.findViewById(R.id.cbNotice);
//            btUpdateND =view.findViewById(R.id.btUpdateND);
//
//
//        }
//
//
//    }
//
//    private List<Notice> getData() {
//        List<Notice> noticeList = new ArrayList<>();
//        if (Common.networkConnected(activity)) {
//            String url = Common.URL_SERVER + "Notice_Servlet";
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("action", "getSaleAll");
//            String jsonOut = jsonObject.toString();
//            noticeAdimGetAllTask = new CommonTask(url, jsonOut);
//            try {
//                String jsonIn = noticeAdimGetAllTask.execute().get();
//                Type listType = new TypeToken<List<Notice>>() {
//                }.getType();
//                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//                noticeList = gson.fromJson(jsonIn, listType);
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//        } else {
//            Common.showToast(Activity, R.string.textNoNetwork);
//        }
//        return noticeList;
//    }
//}

