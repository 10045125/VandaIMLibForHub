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

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.wangjie.androidbucket.support.recyclerview.layoutmanager.ABaseLinearLayoutManager;
import com.wangjie.androidbucket.support.recyclerview.listener.OnRecyclerViewScrollLocationListener;
import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.vandaimlibforhub.adapter.MessageListMapAdapter;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.ChatManager;
import com.wzl.wzl_vanda.vandaimlibforhub.model.Room;
import com.wzl.wzl_vanda.vandaimlibforhub.service.ConversationManager;
import com.wzl.wzl_vanda.vandaimlibforhub.view.SimpleDividerItemDecoration;

import java.util.List;

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


    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


    private void initData() {
        mMessageListMapAdapter = new MessageListMapAdapter(getActivity());
//        loadItems();
//        try {
//            System.out.println("size1 ->>>> " + mMessageListMapAdapter.size());
////            mMessageListMapAdapter.addAll(conversationManager.findAndCacheRooms());
//            System.out.println("size ->>>> " + mMessageListMapAdapter.size());
//        } catch (AVException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


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

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (AVUser.getCurrentUser() != null) {
                        List<Room> messageList = conversationManager.findAndCacheRooms();
                        Log.e("messageList ->> ", "" + messageList.size());
                        mMessageListMapAdapter.addAll(messageList);
                    }
                } catch (AVException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
