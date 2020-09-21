package com.ed.shuneladmin;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

import com.ed.shuneladmin.Task.Common;
import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.bean.Admin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.List;

import static com.ed.shuneladmin.MainActivity.builder;


public class AdminFragment extends Fragment {
    private static final String TAG = "---AdminFragment---";
    private Activity activity;
    private RecyclerView rvAdmin;
    private CommonTask adminTask;
    private List<Admin> data;
    private SearchView searchView;
    private ImageView ivAdd, ivCustomer;
    private CommonTask adminDeleteTask;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (MainActivity.builder != null) {
            Log.e("))))", "9999");
            builder.create().cancel();
            builder.create().dismiss();
        }

        searchView = view.findViewById(R.id.searchView2);
        rvAdmin = view.findViewById(R.id.rvAdmin);
        ivAdd = view.findViewById(R.id.ivAdd);
        ivCustomer = view.findViewById(R.id.ivCustomer);

        data = getAdmins();
        Log.e("_______", data + "");

        rvAdmin.setAdapter(new AdminAdapter(activity, data));        //控制所有元件
        rvAdmin.setLayoutManager(new LinearLayoutManager(activity));

        showAdmin(data);

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_adminFragment_to_adminNewFragment);
            }
        });

        ivCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).popBackStack();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                // 如果搜尋條件為空字串，就顯示原始資料；否則就顯示搜尋後結果
                if (newText.isEmpty()) {
                    showAdmin(data);
                    Log.e(TAG, data + "");
                } else {
                    List<Admin> searchAdmin = new ArrayList<>();
                    // 搜尋原始資料內有無包含關鍵字(不區別大小寫)
                    for (Admin admin : data) {
//
                        if (admin.getAdmin_Name().toUpperCase().contains(newText.toUpperCase())/*|| String.valueOf(admin.getAdmin_ID().contains(newText))*/) {
                            searchAdmin.add(admin);
                        }
                    }
                    showAdmin(searchAdmin);
                }
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
        });

    }


    private List<Admin> getAdmins() {


        List<Admin> admin = null;

        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "Admin";//連server端先檢查網址
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");//變作ＪＳＯＮ自串
            String jsonOut = jsonObject.toString();
            adminTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = adminTask.execute().get();
                Type listType = new TypeToken<List<Admin>>() {
                }.getType();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
//                參考Android web範例：jsonex
                admin = gson.fromJson(jsonIn, listType);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

            return admin;
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        Log.e("12345", admin + "");
        return admin;

    }


    private class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.MyViewHolder> {
        LayoutInflater layoutInflater;
        Context context;
        List<Admin> admins;

        void setAdmins(List<Admin> admins) {
            this.admins = admins;
        }

        public AdminAdapter(Context context, List<Admin> admins) {
            layoutInflater = LayoutInflater.from(context);    //加載ＬＡＹＯＵＴＩＮＦＬＡＴＯＲ
            this.context = context;
            this.admins = getAdmins();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.admin_itemview, parent, false);                                   //呈現畫面


            return new MyViewHolder(view);
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            private TextView tvId, tvName;
            private Button btEdit;

            public MyViewHolder(View view) {
                super(view);
                tvId = view.findViewById(R.id.tvId);
                tvName = view.findViewById(R.id.etAccountId);
                btEdit = view.findViewById(R.id.btEdit);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final Admin admin = admins.get(position);
            holder.tvId.setText(String.valueOf(admin.getAdmin_ID()));              //setText()裡塞我要使用的方法
            holder.tvName.setText(admin.getAdmin_Name());
            String pos = Common.getPreherences(activity).getString("position", "def");

            Log.e("-------pos--------", pos);
            Log.e("0000", pos + "");
            if (pos.equals("管理員")) {
                holder.btEdit.setVisibility(View.VISIBLE);
            } else {
                holder.btEdit.setVisibility(View.GONE);
            }



            holder.btEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("admin", admin);
                    NavController navController = Navigation.findNavController(activity, R.id.homeFragment);
                    navController.navigate(R.id.adminNewDetailFragment, bundle);

                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public boolean onLongClick(final View view) {
                    PopupMenu popupMenu = new PopupMenu(activity, view, Gravity.END);
                    popupMenu.inflate(R.menu.popup_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
//                            case R.id.update:
//                                Bundle bundle = new Bundle();
//                                bundle.putSerializable("spot", spot);
//                                Navigation.findNavController(view)
//                                        .navigate(R.id.action_spotListFragment_to_spotUpdateFragment, bundle);
//                                break;
                                case R.id.delete:
                                    if (Common.networkConnected(activity)) {
                                        String url = Common.URL_SERVER + "Admin";
                                        JsonObject jsonObject = new JsonObject();
                                        jsonObject.addProperty("action", "Delete");
                                        jsonObject.addProperty("adminId", admin.getAdmin_ID());
                                        int count = 0;
                                        try {
                                            adminDeleteTask = new CommonTask(url, jsonObject.toString());
                                            String result = adminDeleteTask.execute().get();
                                            count = Integer.parseInt(result);
                                        } catch (Exception e) {
                                            Log.e(TAG, e.toString());
                                        }
                                        if (count == 0) {
                                            Common.showToast(activity, R.string.textDeleteFail);
                                        } else {
                                            admins.remove(admin);
                                            AdminAdapter.this.notifyDataSetChanged();
                                            // 外面spots也必須移除選取的spot
                                            AdminFragment.this.data.remove(admin);
                                            Common.showToast(activity, R.string.textDeleteSuccess);
                                        }
                                    } else {
                                        Common.showToast(activity, R.string.textNoNetwork);
                                    }
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return admins == null ? 0 : admins.size();
        }


    }


    private void showAdmin(List<Admin> admins) {
        if (admins == null || admins.isEmpty()) {
            Common.showToast(activity, R.string.textNoUserAccountFound);
        }
        AdminAdapter Adapter = (AdminAdapter) rvAdmin.getAdapter();
        // 如果adminAdapter不存在就建立新的，否則續用舊有的
        if (Adapter == null) {
            rvAdmin.setAdapter(new AdminAdapter(activity, admins));
        } else {
            Adapter.setAdmins(admins);
            Adapter.notifyDataSetChanged();
        }
    }


}
