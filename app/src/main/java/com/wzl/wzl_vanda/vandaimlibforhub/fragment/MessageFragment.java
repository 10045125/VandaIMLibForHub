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
        refresh();
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
            refresh();
        }

//        if (chatManager.getImClient() != null){
//            Log.e("ImClient -> ","not null");
//            refresh();
//        }else{
//            Log.e("ImClient -> ","null");
//        }
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
//                        final CountDownLatch latch = new CountDownLatch(1);
//                    try {
//                        Log.e("size -> ", "" + conversationManager.findAndCacheRooms().size());
//                    } catch (AVException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
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
//                                    List<String> list1 = mAVIMConversation.getMembers();
//                                    if (list1 != null) {
//                                        int count = list1.size();
//                                        for (int i = 0; i < count; i++) {
//                                            if (i > 0 && i < count - 1) {
//                                                name += "、" + list1.get(i);
//                                            } else {
//                                                name += list1.get(i);
//                                            }
//                                        }
//                                    }
                                    Log.e("name-> ", "" + name);
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
                                mMessageListMapAdapter.addAll(rooms);
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

    private void loadItems() {
//        ArrayList<Room> demoItems = new ArrayList<>();
//        int count = 0;
//        for (int i = 0; i <= 2; i++) {
//            count++;
//            demoItems.add(new Room(i, "me " + i, 1, "http://img3.fengniao.com/forum/attachpics/855/31/34166186.jpg"));
//            if (count == 6) {
//                count = 0;
//            }
//        }
//        mMessageListMapAdapter.addAll(demoItems);
//        mMessageListMapAdapter.notifyDataSetChanged();
    }

}
