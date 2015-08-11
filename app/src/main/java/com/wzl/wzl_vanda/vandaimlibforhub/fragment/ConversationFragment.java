package com.wzl.wzl_vanda.vandaimlibforhub.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.vandaimlibforhub.adapter.ConvRecyclerAdapter;
import com.wzl.wzl_vanda.vandaimlibforhub.data.DBHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMConv;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class ConversationFragment extends Fragment {

    @Bind(R.id.id_recyclerview_conversation)
    RecyclerView mRecyclerView;

    ConvRecyclerAdapter mConvRecyclerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.conv_layout_main, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mConvRecyclerAdapter = new ConvRecyclerAdapter(getActivity(), DBHelper.getInstance());
        this.mRecyclerView.setAdapter(this.mConvRecyclerAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        this.refreshData();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(IMConv newConv) {
        Log.d("IM", "onEventMainThread, newConv:" + newConv);

        this.refreshData();
    }

    private void refreshData() {
        this.mConvRecyclerAdapter.refreshFromDatabase();
        this.mConvRecyclerAdapter.notifyDataSetChanged();
    }
}
