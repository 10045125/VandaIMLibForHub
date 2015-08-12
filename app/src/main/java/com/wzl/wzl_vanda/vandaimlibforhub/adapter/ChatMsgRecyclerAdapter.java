package com.wzl.wzl_vanda.vandaimlibforhub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.wzl.wzl_vanda.vandaimlibforhub.data.DBHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsg;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsgStatus;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsgType;
import com.wzl.wzl_vanda.vandaimlibforhub.holder.ChatViewBaseHolder;
import com.wzl.wzl_vanda.vandaimlibforhub.holder.ChatViewType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Jam on 04/08/2015.
 */
public class ChatMsgRecyclerAdapter extends RecyclerView.Adapter<ChatViewBaseHolder> {

    int DEFAULT_LIMIT_PER_LOAD = 10;

    /**
     * 消息按照id从小到大排序
     */
    ArrayList<IMMsg> mMsgs;
    Context mContext;
    String mConvId;
    DBHelper mDbHelper;

    public ChatMsgRecyclerAdapter(Context context, DBHelper dbHelper, String convId) {
        this.mContext = context;
        this.mDbHelper = dbHelper;
        this.mConvId = convId;
        this.mMsgs = new ArrayList<>();
    }

    /**
     *
     * @return 添加的消息数
     */
    public int loadMoreMsgsFromDB() {
        long fromRecordId = 0;
        if (mMsgs.size() > 0) fromRecordId = mMsgs.get(0).recordId;

        List<IMMsg> loadedMsgs = this.mDbHelper.loadMsgs(mConvId, fromRecordId, DEFAULT_LIMIT_PER_LOAD);
        String logMsg = String.format("convId:%s, fromRecordId:%s, loadedMsgs.size:%s",
                mConvId, fromRecordId, loadedMsgs.size());
        Log.d("IM", "loadMoreMsgsFromDB, " + logMsg);

        Collections.reverse(loadedMsgs);
        mMsgs.addAll(0, loadedMsgs);

        return loadedMsgs.size();
    }

    public void addNewMsg(IMMsg msg) {
        this.mMsgs.add(msg);
    }

    @Override
    public int getItemViewType(int position) {
        IMMsg msg = this.getMsgByPosition(position);
        if (msg == null) {
            Log.e("IM", "ChatMsgRecyclerAdapter.getItemViewType no msg found at position:" + position);
            return ChatViewType.UNDEFINED.ordinal();
        }

        return ChatViewType.valueOf(msg.type).ordinal();
    }

    @Override
    public ChatViewBaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ChatViewType chatViewType = ChatViewType.valueOf(viewType);
        if (ChatViewType.UNDEFINED == chatViewType) {
            Log.e("IM", "ChatMsgRecyclerAdapter.onCreateViewHolder undefined viewType:" + viewType);
            return null;
        }

        return chatViewType.buildChatViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ChatViewBaseHolder holder, int position) {
        IMMsg msg = this.getMsgByPosition(position);
        if (msg == null) {
            Log.e("IM", "ChatMsgRecyclerAdapter.onBindViewHolder no msg found at position:" + position);
            return;
        }

        holder.onBindData(mContext, msg);
    }

    @Override
    public int getItemCount() {
        return this.mMsgs.size();
    }

    /**
     *
     * @return 无数据时返回 -1
     */
    public int getLastItemPosition() {
        return this.mMsgs.size() - 1;
    }

    private IMMsg getMsgByPosition(int position) {
        int size = mMsgs.size();
        if (position < 0 || position >= size) return null;

        return mMsgs.get(position);
    }

}
