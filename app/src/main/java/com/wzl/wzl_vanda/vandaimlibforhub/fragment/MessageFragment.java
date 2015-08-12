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
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationQueryCallback;
import com.wangjie.androidbucket.support.recyclerview.layoutmanager.ABaseLinearLayoutManager;
import com.wangjie.androidbucket.support.recyclerview.listener.OnRecyclerViewScrollLocationListener;
import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.vandaimlibforhub.adapter.MessageListMapAdapter;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.ChatManager;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMConv;
import com.wzl.wzl_vanda.vandaimlibforhub.model.MessageEvent;
import com.wzl.wzl_vanda.vandaimlibforhub.model.Room;
import com.wzl.wzl_vanda.vandaimlibforhub.model.User;
import com.wzl.wzl_vanda.vandaimlibforhub.service.CacheService;
import com.wzl.wzl_vanda.vandaimlibforhub.service.ConversationManager;
import com.wzl.wzl_vanda.vandaimlibforhub.utils.Logger;
import com.wzl.wzl_vanda.vandaimlibforhub.view.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

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

    private int flag;


    public static MessageFragment newInstance(int flag) {
        MessageFragment mMessageFragment = new MessageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("flag", flag);
        mMessageFragment.setArguments(bundle);
        return mMessageFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        Bundle bundle = getArguments();
        if (bundle != null)
            flag = bundle.getInt("flag");
        eventBus = EventBus.getDefault();
        conversationManager = ConversationManager.getInstance();
        chatManager = ChatManager.getInstance();
//        chatManager.openClientWithSelfId(Utils.getLocalMacAddress(getActivity()), null);
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
        Log.e("onEvent", "" + event.getMessage().getContent());
        mMessageListMapAdapter.notifyDataSetChanged();
        System.out.println("size onE->>>> " + mMessageListMapAdapter.size());
    }


    public void onEvent(TextView event) {
//        refresh();
    }


    public void onEventMainThread(IMConv newConv) {
        refreshData();
    }

    public void refreshData() {
        mMessageListMapAdapter.refreshFromDatabase();
        mMessageListMapAdapter.notifyDataSetChanged();
    }

    public void firstCacheConversation() {
        final ArrayList<String> ids = new ArrayList<>();
        for (IMConv imConv : mMessageListMapAdapter.getList()) {
            ids.add(imConv.convId);
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    CacheService.cacheConvs(ids, new AVIMConversationCallback() {
                        @Override
                        public void done(AVException e) {
                        }
                    });
                } catch (AVException e) {
                    e.printStackTrace();
                    return null;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    private void initData() {
        mMessageListMapAdapter = new MessageListMapAdapter(getActivity());
        idRecyclerviewMessage.setHasFixedSize(true);
        idRecyclerviewMessage.setItemAnimator(null);//new DefaultItemAnimator());
        idRecyclerviewMessage.setAdapter(mMessageListMapAdapter);
        mABaseLinearLayoutManager = new ABaseLinearLayoutManager(getActivity()) {
//            @Override
//            protected int getExtraLayoutSpace(RecyclerView.State state) {
//                return 1200;
//            }
        };
//        mABaseLinearLayoutManager.setReverseLayout(true);
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
        if (flag == 1) {
            refreshData();
            firstCacheConversation();
//            refresh();
        }
    }


    public void refresh() {
        mMessageListMapAdapter.clear();
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected Void doInBackground(Void... voids) {

                if (AVUser.getCurrentUser() != null) {
                    conversationManager.findGroupConversationsIncludeMe(new AVIMConversationQueryCallback() {
                        @Override
                        public void done(List<AVIMConversation> list, AVException e) {

                            if (e == null && list != null) {
                                Log.e("List<AVIM ->> ", "" + list.size());
                                List<Room> rooms = new ArrayList<Room>();
                                for (AVIMConversation mAVIMConversation : list) {
                                    Room room = new Room();
                                    CacheService.registerConv(mAVIMConversation);
                                    ChatManager.getInstance().registerConversation(mAVIMConversation);
                                    room.setConversation(mAVIMConversation);
                                    room.setConversationId(mAVIMConversation.getConversationId());

                                    String name = mAVIMConversation.getName();
                                    room.setGroupName(name);
                                    try {
                                        room.setLastMessage(ConversationManager.getLastMessage(mAVIMConversation));
                                    } catch (AVException e1) {
                                        e1.printStackTrace();
                                    } catch (InterruptedException e1) {
                                        e1.printStackTrace();
                                    }
                                    rooms.add(room);
                                }
//                                mMessageListMapAdapter.addAll(rooms);
                            } else
                                Log.e("List<AVIM ->> ", "" + e.toString());
                        }

                    });
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mMessageListMapAdapter.notifyDataSetChanged();
                System.out.println("size onE->>>> " + mMessageListMapAdapter.size());
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }
        }.execute();
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
