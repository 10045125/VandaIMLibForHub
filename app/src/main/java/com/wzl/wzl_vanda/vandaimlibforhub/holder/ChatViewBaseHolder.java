package com.wzl.wzl_vanda.vandaimlibforhub.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsg;

/**
 * Created by Jam on 04/08/2015.
 */
public abstract class ChatViewBaseHolder extends RecyclerView.ViewHolder{

    public ChatViewBaseHolder(View view) {
        super(view);
    }

    public abstract void onBindData(Context context, IMMsg msg);
}
