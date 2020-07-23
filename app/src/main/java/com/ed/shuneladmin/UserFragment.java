package com.ed.shuneladmin;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ed.shuneladmin.bean.User_Account;

import java.util.List;


public class UserFragment extends Fragment {
    private Activity activity;
    private RecyclerView rvUser;
    private UserAdapter userAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    private class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
        Context context;
        List<User_Account> userAccounts;


        public UserAdapter(Context context, List<User_Account> user_accounts) {
            this.context = context;
            this.userAccounts = user_accounts;
        }








        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.member_item_view, parent, false);                                   //呈現畫面

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final User_Account user_account = userAccounts.get(position);
            holder.tvId.setText(user_account.getAccount_ID());              //setText()裡塞我要使用的方法
            holder.tvName.setText(user_account.getAccount_User_Name());
            holder.btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

        @Override
        public int getItemCount()  {
            return userAccounts.size();
        }


        private class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tvId, tvName;
            private Button btEdit;

            public MyViewHolder(View view) {
                super(view);
                tvId = view.findViewById(R.id.tvId);
                tvName = view.findViewById(R.id.tvName);
                btEdit = view.findViewById(R.id.btEdit);
            }
        }
    }
}
