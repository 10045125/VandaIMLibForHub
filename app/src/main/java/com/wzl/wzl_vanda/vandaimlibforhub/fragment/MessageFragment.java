package com.wzl.wzl_vanda.vandaimlibforhub.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wangjie.androidbucket.support.recyclerview.layoutmanager.ABaseLinearLayoutManager;
import com.wangjie.androidbucket.support.recyclerview.listener.OnRecyclerViewScrollLocationListener;
import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.vandaimlibforhub.adapter.MessageListMapAdapter;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.ChatManager;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMConv;
import com.wzl.wzl_vanda.vandaimlibforhub.model.MessageEvent;
import com.wzl.wzl_vanda.vandaimlibforhub.service.ConversationManager;
import com.wzl.wzl_vanda.vandaimlibforhub.view.SimpleDividerItemDecoration;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by wzl_vanda on 15/7/29.
 */
public class MessageFragment extends Fragment implements ChatManager.ConnectionListener {

    public static MessageFragment instance;

    @Bind(R.id.id_recyclerview_message)
    RecyclerView idRecyclerviewMessage;
    private EventBus eventBus;
    private ConversationManager conversationManager;
    private ChatManager chatManager;
    ABaseLinearLayoutManager mABaseLinearLayoutManager;
    MessageListMapAdapter mMessageListMapAdapter;



    public static MessageFragment newInstance() {
        MessageFragment mMessageFragment = new MessageFragment();
        Bundle bundle = new Bundle();
        mMessageFragment.setArguments(bundle);
        return mMessageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        Bundle bundle = getArguments();
//        if (bundle != null)
//            flag = bundle.getInt("flag");
        eventBus = EventBus.getDefault();
        conversationManager = ConversationManager.getInstance();
        chatManager = ChatManager.getInstance();
        chatManager.setConnectionListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.message_main, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }


    @Override
    public void onResume() {
        super.onResume();
        eventBus.register(this);
        if (mMessageListMapAdapter != null){
            mMessageListMapAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        eventBus.unregister(this);
    }

    public void onEvent(MessageEvent event) {
        mMessageListMapAdapter.notifyDataSetChanged();
    }


    public void onEvent(TextView event) {
    }


    public void onEventMainThread(IMConv newConv) {
        refreshData();
    }

    public void refreshData() {
        mMessageListMapAdapter.refreshFromDatabase();
        mMessageListMapAdapter.notifyDataSetChanged();
    }


    private void initData() {
        mMessageListMapAdapter = new MessageListMapAdapter(getActivity());
        idRecyclerviewMessage.setHasFixedSize(true);
        idRecyclerviewMessage.setItemAnimator(null);//new DefaultItemAnimator());
        idRecyclerviewMessage.setAdapter(mMessageListMapAdapter);
        mABaseLinearLayoutManager = new ABaseLinearLayoutManager(getActivity()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 1200;
            }
        };
        mABaseLinearLayoutManager.setOnRecyclerViewScrollLocationListener(idRecyclerviewMessage, new OnRecyclerViewScrollLocationListener() {
            @Override
            public void onTopWhenScrollIdle(RecyclerView recyclerView) {

            }

            @Override
            public void onBottomWhenScrollIdle(RecyclerView recyclerView) {
            }
        });

        mABaseLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        idRecyclerviewMessage.setLayoutManager(mABaseLinearLayoutManager);
        idRecyclerviewMessage.addItemDecoration(new SimpleDividerItemDecoration(getResources()));
            refreshData();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onConnectionChanged(boolean connect) {

    }

}
