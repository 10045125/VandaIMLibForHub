package com.wzl.wzl_vanda.vandaimlibforhub.binder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.wzl.wzl_vanda.vandaimlibforhub.ChatSetActivity;
import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.viewtypelibrary.DataBindAdapter;
import com.wzl.wzl_vanda.viewtypelibrary.DataBinder;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wzl_vanda on 15/8/10.
 * 作者：wzl_vanda on 15/8/10 17:39
 * 邮箱：806594174@qq.com
 */
public class ChatSetBinder extends DataBinder<ChatSetBinder.ViewHolder> {




    public ChatSetBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.circle_imageview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        Glide.with(ChatSetActivity.instance)
                .load("http://www.feizl.com/upload2007/2015_04/1504021516764612.jpg")
                .into(holder.idChatSetCircleimage);
        Log.e("position -> ",""+position);
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.id_chat_set_circleimage)
        CircleImageView idChatSetCircleimage;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
