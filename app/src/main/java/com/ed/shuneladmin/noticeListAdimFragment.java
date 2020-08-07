package com.ed.shuneladmin;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ed.shuneladmin.Task.CommonTask;
import com.ed.shuneladmin.bean.Notice;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class noticeListAdimFragment extends Fragment {

    Activity activity;
    RecyclerView noticeRecyclerView;
    private CommonTask noticeAdimGetAllTask;
    private List<Notice> noticeAdimList;
    private NoticeAdimAdapter noticeDetailAdapter;


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


    }

    private class NoticeAdimAdapter {
    }
}
