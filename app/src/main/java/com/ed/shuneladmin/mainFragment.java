package com.ed.shuneladmin;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;
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

import static android.content.Context.INPUT_METHOD_SERVICE;
import static androidx.constraintlayout.widget.Constraints.TAG;


public class mainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    Activity activity;
    TextView tvUpdateSaleN;
    ImageView ivAddSaleN;
    Button btUpdaeSaleN;
    private CommonTask noticeAdimGetAllTask;
    private List<Notice> noticeAdimSaleList,noticeCopyList;
    RecyclerView rvAdimSaleN;
    SearchView SearchSaleN;
    private adimSaleNAdapter noticeAdimSaleAdapter;


    public mainFragment() {
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
        return inflater.inflate(R.layout.fragment_main, container, false);
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
        rvAdimSaleN.setLayoutManager(new LinearLayoutManager(activity));
    }

    private void initData() {
        noticeAdimSaleList = getData();
        noticeCopyList =noticeAdimSaleList;//複製一個List取值

        showSalelist(getData());
//        noticeAdimSaleList = getData();


    }

    private void setLinstener() {

        SearchSaleN.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // 如果搜尋條件為空字串，就顯示原始資料；否則就顯示搜尋後結果
                adimSaleNAdapter adapter = (adimSaleNAdapter) rvAdimSaleN.getAdapter();
                try {
                    if (adapter != null) {
                        if (newText.isEmpty()) {
                            noticeAdimSaleList = noticeCopyList;//再把值傳回來
                            showSalelist(noticeAdimSaleList);
                        } else {
                            List<Notice> SearchSaleAll = new ArrayList<>();
                            // 搜尋原始資料內有無包含關鍵字(不區別大小寫)
                            for (Notice notice : noticeAdimSaleList) {
                                if (notice.getNotice_Content().toUpperCase().contains(newText.toUpperCase()) || notice.getNotice_Title().toUpperCase().contains(newText.toUpperCase())) {
                                    Log.e(" SearchSaleN", "---------------------" + notice.getNotice_Content());
                                    SearchSaleAll.add(notice);
                                }
                            }
                            noticeAdimSaleList = SearchSaleAll;
                            adapter.setList(noticeAdimSaleList);
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


        tvUpdateSaleN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noticeAdimSaleAdapter = (adimSaleNAdapter) rvAdimSaleN.getAdapter();
                boolean[] Expanded = noticeAdimSaleAdapter.getExpanded();
                for (int i = 0; i < Expanded.length; i++) {
                    Expanded[i] = !Expanded[i];
                }
                noticeAdimSaleAdapter.setExpanded(Expanded);
                noticeAdimSaleAdapter.notifyDataSetChanged();

            }
        });


        ivAddSaleN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.flag = 0;
                NavController navController = Navigation.findNavController(activity, R.id.homeFragment);
                navController.navigate(R.id.noticeAdminFragment);
            }


        });

    }

    private void showSalelist(List<Notice> nList) {
        if (nList == null || nList.isEmpty()) {
            Common.showToast(activity, R.string.noNotice);
        }
        noticeAdimSaleAdapter = (adimSaleNAdapter) rvAdimSaleN.getAdapter();
        // 如果spotAdapter不存在就建立新的，否則續用舊有的
        if (noticeAdimSaleAdapter == null) {
            rvAdimSaleN.setAdapter(new adimSaleNAdapter(activity, nList));
        } else {
            Log.e(TAG, "00000000000");
            noticeAdimSaleAdapter.setList(nList);
            noticeAdimSaleAdapter.notifyDataSetChanged();
        }

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

        void setList(List<Notice> noticeList) {

            this.noticeList = noticeList;

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
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.noitce_adim_item, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull adimSaleNAdapter.MyViewHolder holder, int position) {
            final Notice notice = noticeAdimSaleList.get(position);
            int notice_id = notice.getNotice_ID();
            holder.tvNoticeT.setText(notice.getNotice_Title());
            holder.tvNoticeD.setText(notice.getNotice_Content());
            Log.e("---------", notice.getNotice_time().toString() + "---");
            holder.tvDateN.setText(notice.getNotice_time().toString());
            holder.cbNotice.setVisibility(Expanded[position] ? View.VISIBLE : View.GONE);
            holder.btUpdateND.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.flag = 2;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("NoitceAdim", notice);
                    NavController navController = Navigation.findNavController(activity, R.id.homeFragment);
                    navController.navigate(R.id.noticeAdminFragment, bundle);
                }
            });

        }


        @Override
        public int getItemCount() {
            Log.e("TAG", "數量" + noticeAdimSaleList.size() + "-----------");
            return noticeAdimSaleList == null ? 0 : noticeAdimSaleList.size();
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
