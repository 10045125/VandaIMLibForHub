package com.wzl.wzl_vanda.vandaimlibforhub.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.wzl.wzl_vanda.vandaimlibforhub.BuildConfig;
import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.vandaimlibforhub.adapter.ChatMsgRecyclerAdapter;
import com.wzl.wzl_vanda.vandaimlibforhub.data.DBHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsg;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

public class ChatRoomFragment extends Fragment {


    @Bind(R.id.id_recyclerview_main)
    RecyclerView mRecyclerView;

    ChatMsgRecyclerAdapter mMsgRecyclerAdapter;
    LinearLayoutManager mLayoutManager;

    EventBus mEventBus;

    private AVIMClient mClient;
    private AVIMConversation mConv;

    public ChatRoomFragment(AVIMClient client, AVIMConversation conv) {
        this.mClient = client;
        this.mConv = conv;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.chat_layout_main, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMsgRecyclerAdapter = new ChatMsgRecyclerAdapter(getActivity(), DBHelper.getInstance(), mConv.getConversationId());
        mRecyclerView.setAdapter(mMsgRecyclerAdapter);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.scrollToPosition(0);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState != RecyclerView.SCROLL_STATE_IDLE) return;
                if (isViewOnTop()) loadDBMsgInBackground();
            }
        });

        this.loadDBMsgInBackground();

        mEventBus = EventBus.getDefault();
        mEventBus.register(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void loadDBMsgInBackground() {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                return mMsgRecyclerAdapter.loadMoreMsgsFromDB();
            }


            @Override
            protected void onPostExecute(Integer addedMsgsSize) {
                mMsgRecyclerAdapter.notifyDataSetChanged();
                if (addedMsgsSize > 1) {
                    Log.d("IM", "loadMoreMsgsFromDB, scrollToPosition:" + addedMsgsSize);
                    mRecyclerView.scrollToPosition(addedMsgsSize);
                }
            }
        }.execute();
    }


    public void onEventMainThread(Object event) {
        Log.d("IM", "onEventMainThread, event:" + event);

        if (event == null) return;

        if (event instanceof IMMsg) this.handleNewAddedMsg((IMMsg) event);
    }

    private void handleNewAddedMsg(IMMsg newMsg) {
        boolean isViewOnBottom = isViewOnBottom();

        this.mMsgRecyclerAdapter.addNewMsg(newMsg);
        this.mMsgRecyclerAdapter.notifyDataSetChanged();

        Log.e("IM", "handleNewAddedMsg, isViewOnBottom:" + isViewOnBottom);

        if (isViewOnBottom) this.mRecyclerView.scrollToPosition(this.mMsgRecyclerAdapter.getLastItemPosition());
    }

    private boolean isViewOnBottom() {
        int dataIdx = mMsgRecyclerAdapter.getLastItemPosition();
        int viewIdx = mLayoutManager.findLastCompletelyVisibleItemPosition();
        return dataIdx == viewIdx;
    }

    private boolean isViewOnTop() {
        return mLayoutManager.findFirstCompletelyVisibleItemPosition() == 0;
    }
}
