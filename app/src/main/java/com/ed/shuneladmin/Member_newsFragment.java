package com.ed.shuneladmin;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.bean.StateMessage;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static androidx.navigation.Navigation.findNavController;
import static com.ed.shuneladmin.CommonTwo.loadUserName;
import static com.ed.shuneladmin.CommonTwo.showToast;


/**
 * A simple {@link Fragment} subclass.
 */
public class Member_newsFragment extends Fragment {

    private static final String TAG = "TAG_Member_newsFragment";
    private Activity activity;
    private RecyclerView rvMember;
    private String user;
    private List<String> MemberList;
    private LocalBroadcastManager broadcastManager;
    private CommonTask chatTask;
    private int chat_ID;
    public Member_newsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        if (activity == null) {
            return;
        }
        // 初始化聊天清單
        MemberList = new ArrayList<>();
        // 取得user name
//        user = loadUserName(activity);
        user = "Shunel";
        // 初始化LocalBroadcastManager並註冊BroadcastReceiver
        broadcastManager = LocalBroadcastManager.getInstance(activity);
        registerFriendStateReceiver();
        CommonTwo.connectServer(activity, user);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_member_news, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvMember = view.findViewById(R.id.rvMember);
        rvMember.setLayoutManager(new LinearLayoutManager(activity));
        Log.e(TAG, "test" + MemberList.size());
        rvMember.setAdapter(new Member_newsFragment.MemerAdapter(activity));


    }



    /*----------------------------------方法區------------------------------------------------------------*/


    // 攔截user連線或斷線的Broadcast
    private void registerFriendStateReceiver() {
        IntentFilter openFilter = new IntentFilter("open");
        IntentFilter closeFilter = new IntentFilter("close");
        broadcastManager.registerReceiver(memberStateReceiver, openFilter);
        broadcastManager.registerReceiver(memberStateReceiver, closeFilter);

    }

    // 攔截user連線或斷線的Broadcast，並在RecyclerView呈現
    private BroadcastReceiver memberStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            Log.e(TAG, "11111111" + message);
            StateMessage stateMessage = new Gson().fromJson(message, StateMessage.class);
            String type = stateMessage.getType();
            String friend = stateMessage.getUser();
            switch (type) {
                // 有user連線
                case "open":
                    // 上線的是好友而非自己就顯示該好友user name
                    if (!friend.equals(user)) {
                        Log.e(TAG, "--------------------" + friend);
                        showToast(activity, friend + " is online");
                    }
                    break;
                // 有user斷線
                case "close":
                    // 斷線的是好友而非自己就顯示該好友user name
                    if (!friend.equals(user)) {
                        showToast(activity, friend + " is offline");
                    }
                    break;
            }
            // 取得server上的所有user
            MemberList = new ArrayList<>(stateMessage.getUsers());
            // 將自己從聊天清單中移除，否則會看到自己在聊天清單上
            MemberList.remove(user);
            Log.e(TAG, "222222222222" + MemberList.size());
            // 重刷好友清單
            if (rvMember.getAdapter() != null) {
                rvMember.getAdapter().notifyDataSetChanged();
            }
            Log.d(TAG, message);
        }
    };


    /*----------------------------------生命週期------------------------------------------------------------*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Fragment頁面切換時解除註冊，同時關閉WebSocket
        broadcastManager.unregisterReceiver(memberStateReceiver);
        CommonTwo.disconnectServer();
    }

    private class MemerAdapter extends RecyclerView.Adapter<MemerAdapter.mViewHoder> {
        Context context;

        public MemerAdapter(Context context) {
            this.context = context;

        }

        @NonNull
        @Override
        public mViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.member_item, parent, false);

            return new mViewHoder(itemView);
        }


        @Override
        public void onBindViewHolder(@NonNull mViewHoder holder, int position) {

            final String member = MemberList.get(position);
//            Log.e(TAG,"選擇=-----------------------"+member);
            holder.tvFriendName.setText(member);
            // 點選聊天清單上的user即開啟聊天頁面
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    findRoomId(member);
                    Bundle bundle = new Bundle();
                    bundle.putString("member", member);
                    bundle.putInt("chatRoom",chat_ID);
                    findNavController(view).navigate(R.id.action_member_newsFragment_to_customerServiceFragment,bundle);
                }
            });

        }

        @Override
        public int getItemCount() {
            Log.e(TAG, "getItemCount" + MemberList.size());
            return MemberList.size();
        }

        private class mViewHoder extends RecyclerView.ViewHolder {
            TextView tvFriendName;

            public mViewHoder(View itemView) {
                super(itemView);
                tvFriendName = itemView.findViewById(R.id.tvFrinedName);


            }

        }
    }

    private void findRoomId(String member_ID) {


        /********************************建立聊天室 Jack*****************************************/

//        user_Id = Common.getPreherences(activity).getString("id","");

        String url = Common.URL_SERVER + "Chat_Servlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "findRoomID");
        jsonObject.addProperty("admin_Id", "1");
        jsonObject.addProperty("user_ID", member_ID);

//        Log.e(TAG, "0000000000000000000"+jsonObject.toString());
        try {
            chatTask = new CommonTask(url, jsonObject.toString());



            String result = chatTask.execute().get();
            chat_ID = Integer.parseInt(result);
//            Log.e(TAG, "1111111111111111111="+result);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }



        /********************************建立聊天室 Jack*****************************************/


    }


}
