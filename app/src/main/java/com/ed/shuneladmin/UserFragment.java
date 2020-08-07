package com.ed.shuneladmin;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.bean.User_Account;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class UserFragment extends Fragment {
    private static final String TAG = "---UserFragment---";
    private Activity activity;
    private RecyclerView rvUser;
    private CommonTask UserTask;
    private List<User_Account> data;
    private SearchView searchView;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_user, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.searchView);
        rvUser = view.findViewById(R.id.rvUser);
        data = user_accounts();

        Log.e("_______", data + "");
        rvUser.setAdapter(new UserAdapter(activity, data));
        rvUser.setLayoutManager(new LinearLayoutManager(activity));
        showUserAccount(data);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // 如果搜尋條件為空字串，就顯示原始資料；否則就顯示搜尋後結果
                if (newText.isEmpty()) {
                    showUserAccount(data);
                } else {
                    List<User_Account> searchUserAccount = new ArrayList<>();
                    // 搜尋原始資料內有無包含關鍵字(不區別大小寫)
                    for (User_Account user_account : data) {
//                        Log.e("1234567890",user_account.getAccount_User_Name());
                        if (user_account.getAccount_ID().toUpperCase().contains(newText.toUpperCase()) || user_account.getAccount_User_Name().toUpperCase().contains(newText.toUpperCase())) {
                            searchUserAccount.add(user_account);
                        }
                    }
                    showUserAccount(searchUserAccount);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });
    }

    private List<User_Account> user_accounts() {

        User_Account test = null;
        List<User_Account> user_account = null;

        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "User_Account_Servlet";//連server端先檢查網址
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");//變作ＪＳＯＮ自串
            String jsonOut = jsonObject.toString();
            UserTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = UserTask.execute().get();
                Type listType = new TypeToken<List<User_Account>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//                參考Android web範例：jsonex
                user_account = gson.fromJson(jsonIn, listType);

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            Log.e("---------8", user_account + "");
            return user_account;
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        Log.e("12345", user_account + "");
        return user_account;

    }

    private class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
        private boolean[] userExpanded;
        LayoutInflater layoutInflater;
        Context context;
        List<User_Account> userAccounts;

        void setUserAccounts(List<User_Account> userAccounts) {
            this.userAccounts = userAccounts;
        }

        public UserAdapter(Context context, List<User_Account> user_accounts) {

            layoutInflater = LayoutInflater.from(context);    //加載ＬＡＹＯＵＴＩＮＦＬＡＴＯＲ
            this.context = context;
            this.userAccounts = user_accounts;
            userExpanded = new boolean[user_accounts.size()];
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.member_item_view, parent, false);                                   //呈現畫面

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder holder,int position) {
            final User_Account user_account = userAccounts.get(position);
            holder.tvId.setText(user_account.getAccount_ID());              //setText()裡塞我要使用的方法
            holder.tvName.setText(user_account.getAccount_User_Name());

            holder.tvPhone.setText(user_account.getAccount_Phone());

            holder.tvAddress.setText(user_account.getAccount_Address());

//            holder.btOpen.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    holder.clDetail.setVisibility(userExpanded[position] ? v.VISIBLE : v.GONE);
//                }
//            });


            holder.clDetail.setVisibility(
                    userExpanded[position] ? View.VISIBLE : View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expand(holder.getAdapterPosition());

                }

            });


            if (userExpanded[position]){
                holder.btOpen.setImageResource(R.drawable.up);
            }else {
                holder.btOpen.setImageResource(R.drawable.down);
            }


        }


        private void expand(int position) {
            userExpanded[position] = !userExpanded[position];
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return userAccounts == null ? 0 : userAccounts.size();
        }


        private class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tvId, tvName, tvPhone, tvAddress;
            private ImageButton btOpen;
            private ConstraintLayout clDetail;

            public MyViewHolder(View view) {
                super(view);
                tvId = view.findViewById(R.id.tvId);
                tvName = view.findViewById(R.id.tvName);
                tvPhone = view.findViewById(R.id.tvPhone);
                tvAddress = view.findViewById(R.id.tvAddress);
                btOpen = view.findViewById(R.id.btOpen);
                clDetail = view.findViewById(R.id.clDetail);
            }
        }
    }

    private void showUserAccount(List<User_Account> user_accounts) {
        if (user_accounts == null || user_accounts.isEmpty()) {
            Common.showToast(activity, R.string.textNoUserAccountFound);
        }
        UserAdapter Adapter = (UserAdapter) rvUser.getAdapter();
        // 如果spotAdapter不存在就建立新的，否則續用舊有的
        if (Adapter == null) {
            rvUser.setAdapter(new UserAdapter(activity, user_accounts));
        } else {
            Adapter.setUserAccounts(user_accounts);
            Adapter.notifyDataSetChanged();
        }
    }


}
