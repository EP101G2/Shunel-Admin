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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class SystemNFragment extends Fragment {

    Activity activity;
    TextView tvDeleteSysN;
    ImageView ivAddSysN;
    Button  checkSysN;
    private CommonTask noticeAdimGetAllTask;
    private List<Notice> noticeAdimSystemList, noticeCopySystemList;
    RecyclerView rvAdimSysN;
    SearchView SearchSysN;
    private adimSysAdapter noticeAdimSystemAdapter;
    Notice notice;
    List<Notice> noticeArrayList = new ArrayList<>();

    boolean flag = false;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_system_n, container, false);

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
        ivAddSysN = view.findViewById(R.id.ivAddSystemN);
        tvDeleteSysN = view.findViewById(R.id.tvdeleteSystemN);
        rvAdimSysN = view.findViewById(R.id.rvAdimSystemN);
        SearchSysN = view.findViewById(R.id.SearchSystemN);
        checkSysN = view.findViewById(R.id.checksysN);
        rvAdimSysN.setLayoutManager(new LinearLayoutManager(activity));


    }

    private void initData() {
        noticeAdimSystemList = getData();
        noticeCopySystemList = noticeAdimSystemList;//複製一個List取值

        showSalelist(getData());
//        noticeAdimSaleList = getData();


    }

    private void setLinstener() {

        SearchSysN.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // 如果搜尋條件為空字串，就顯示原始資料；否則就顯示搜尋後結果
                adimSysAdapter adapter = (adimSysAdapter) rvAdimSysN.getAdapter();
                try {
                    if (adapter != null) {
                        if (newText.isEmpty()) {
                            noticeAdimSystemList = noticeCopySystemList;//再把值傳回來
                            showSalelist(noticeAdimSystemList);
                        } else {
                            List<Notice> SearchSystemAll = new ArrayList<>();
                            // 搜尋原始資料內有無包含關鍵字(不區別大小寫)
                            for (Notice notice : noticeAdimSystemList) {
                                if (notice.getNotice_Content().toUpperCase().contains(newText.toUpperCase()) || notice.getNotice_Title().toUpperCase().contains(newText.toUpperCase())) {
                                    SearchSystemAll.add(notice);
                                }
                            }
                            noticeAdimSystemList = SearchSystemAll;
                            adapter.setList(noticeAdimSystemList);
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

        checkSysN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Common.URL_SERVER + "Notice_Servlet";
                JsonObject jsonObject = new JsonObject();
                if (Common.networkConnected(activity)) {

                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//                    Log.e("", ));
                    jsonObject.addProperty("action", "delete");
                    jsonObject.addProperty("delete",gson.toJson(noticeArrayList));
                    Log.e("", String.valueOf(jsonObject));
                }

                boolean[] Expanded = noticeAdimSystemAdapter.getExpanded();
                boolean isOpen = noticeAdimSystemAdapter.getOpen();
                for (int i = 0; i < Expanded.length; i++) {
                    Expanded[i] = !Expanded[i];
                }

                noticeAdimGetAllTask = new CommonTask(url, jsonObject.toString());
                String jsonIn = "";

                try {
                    jsonIn = noticeAdimGetAllTask.execute().get();

                } catch (Exception e) {
                    Log.e(ContentValues.TAG, e.toString());
                }
                Log.e("------------",jsonIn);
                if(!jsonIn.equals("0")){
                    Common.showToast(activity,"刪除成功");
                    initData();
                    if (flag) {
                        tvDeleteSysN.setText(R.string.delet);
                        flag = !flag;
                    }
                }
            }
        });



        tvDeleteSysN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!flag) {
                    tvDeleteSysN.setText(R.string.cancel);
                    flag = !flag;
                }else {
                    tvDeleteSysN.setText(R.string.delet);
                    flag = false;
                }

                noticeAdimSystemAdapter = (adimSysAdapter) rvAdimSysN.getAdapter();
                boolean[] Expanded = noticeAdimSystemAdapter.getExpanded();

                for (int i = 0; i < Expanded.length; i++) {
                    Expanded[i] = !Expanded[i];
                }

                if (noticeAdimSystemAdapter.getOpen() == true ){
                    noticeArrayList.remove(notice);
                }

                noticeAdimSystemAdapter.setExpanded(Expanded);
                noticeAdimSystemAdapter.notifyDataSetChanged();
                checkSysN.setVisibility(View.GONE);
                noticeArrayList.remove(notice);

            }


        });


        ivAddSysN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.flag = 1;
                NavController navController = Navigation.findNavController(activity, R.id.homeFragment);
                navController.navigate(R.id.noticeAdminFragment);
            }


        });

    }

    private void showSalelist(List<Notice> nList) {
        if (nList == null || nList.isEmpty()) {
            Common.showToast(activity, R.string.noNotice);
        }
        noticeAdimSystemAdapter = (adimSysAdapter) rvAdimSysN.getAdapter();
        // 如果spotAdapter不存在就建立新的，否則續用舊有的
        if (noticeAdimSystemAdapter == null) {
            rvAdimSysN.setAdapter(new adimSysAdapter(activity, nList));
        } else {
            noticeAdimSystemAdapter.setList(nList);
            noticeAdimSystemAdapter.notifyDataSetChanged();
        }

    }


    private List<Notice> getData() {
        List<Notice> noticeList = new ArrayList<>();
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Notice_Servlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getSystemAll");
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


    private class adimSysAdapter extends RecyclerView.Adapter<adimSysAdapter.MyViewHolder> {
        Context context;
        List<Notice> noticeList;
        private boolean[] Expanded;
        private boolean isOpen;



        public adimSysAdapter(Context context, List<Notice> noticeList) {
            this.context = context;
            this.noticeList = noticeList;
            Expanded = new boolean[noticeList.size()];
        }

        void setList(List<Notice> noticeList) {

            this.noticeList = noticeList;

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
//        private  void  expand(int position){
//            Expanded[position] = noticeList.get(position).isOpen();
//            Expanded[position] = !Expanded[position];
//            Log.e("Expanded", String.valueOf(Expanded[position]));
//            notifyDataSetChanged();
//        }

        @NonNull
        @Override
        public adimSysAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.noitce_adim_item, parent, false);
            return new adimSysAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull adimSysAdapter.MyViewHolder holder, final int position) {
            final Notice notice = noticeAdimSystemList.get(position);
            final int notice_id = notice.getNotice_ID();

            holder.tvNoticeT.setText(notice.getNotice_Title());
            holder.tvNoticeD.setText(notice.getNotice_Content());
            Log.e("---------", notice.getNotice_time().toString() + "---");
            holder.tvDateN.setText(notice.getNotice_time().toString());
            holder.cbNotice.setVisibility(Expanded[position] ? View.VISIBLE : View.GONE);
            if(!Expanded[position]){
                holder.cbNotice.setChecked(false);
            }


            holder.cbNotice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    if (isChecked) {
                        noticeArrayList.add(notice);
                        checkSysN.setVisibility(View.VISIBLE);

                    } else  {
                        noticeArrayList.remove(notice);
                        if (noticeArrayList.size()==0){
                            checkSysN.setVisibility(View.GONE);
                        }

                    }
                    Log.e(TAG, "數量：" + noticeArrayList.size());
                }
            });

            holder.btUpdateND.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.flag = 3;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("NoitceAdim", notice);
                    NavController navController = Navigation.findNavController(activity, R.id.homeFragment);
                    navController.navigate(R.id.noticeAdminFragment, bundle);
                }
            });

        }


        @Override
        public int getItemCount() {
            return noticeAdimSystemList == null ? 0 : noticeAdimSystemList.size();
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


