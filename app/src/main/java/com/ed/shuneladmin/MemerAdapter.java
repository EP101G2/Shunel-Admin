package com.ed.shuneladmin;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*----------------------------------建立Adaptee------------------------------------------------------------*/
  class MemerAdapter extends RecyclerView.Adapter<MemerAdapter.memberViewHoder> {

    Context context;
    List<String>  MemberList;

    MemerAdapter(Context context,List<String>  MemberList ) {
        this.context = context;
        this.MemberList =MemberList;
    }


    @Override
    public int getItemCount() {
            Log.e("----","======"+MemberList.size()+"/t"+MemberList.toString());
        return MemberList.size()== 0?  1 :MemberList.size();
    }


    @NonNull
    @Override
    public MemerAdapter.memberViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.member_item, parent, false);

        return new memberViewHoder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MemerAdapter.memberViewHoder holder, int position) {

//        final String member = MemberList.get(position);
//        holder.tvFriendName.setText(member);
//        // 點選聊天清單上的user即開啟聊天頁面
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });

    }


    public class memberViewHoder extends RecyclerView.ViewHolder {
        TextView tvFriendName;
        public memberViewHoder(View itemView) {
            super(itemView);
            tvFriendName = itemView.findViewById(R.id.tvFrinedName);


        }
    }
}
