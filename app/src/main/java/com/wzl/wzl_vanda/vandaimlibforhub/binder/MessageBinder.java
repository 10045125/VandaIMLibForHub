package com.wzl.wzl_vanda.vandaimlibforhub.binder;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wzl.wzl_vanda.vandaimlibforhub.MainActivity;
import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.ChatManager;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.MessageHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.model.Room;
import com.wzl.wzl_vanda.vandaimlibforhub.service.CacheService;
import com.wzl.wzl_vanda.viewtypelibrary.DataBindAdapter;
import com.wzl.wzl_vanda.viewtypelibrary.DataBinder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wzl_vanda on 15/7/29.
 */
public class MessageBinder extends DataBinder<MessageBinder.ViewHolder> {

//    private List<SampleData> mDataSet = new ArrayList<>();

    public MessageBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {
        final Room item = (Room) getDataBindAdapter().get(position);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("ConvName", item.getConversation().getName());
                bundle.putString("ConvId", item.getConversationId());
                Intent it = new Intent(getDataBindAdapter().context, MainActivity.class);
                it.putExtras(bundle);
                CacheService.registerConv(item.getConversation());
                ChatManager.getInstance().registerConversation(item.getConversation());
                getDataBindAdapter().context.startActivity(it);
            }
        });
        holder.idMessageGroupName.setText(item.getGroupName());
        if (item.getLastMessage() != null)
            holder.idMessageGroupLastMessage.setText(MessageHelper.outlineOfMsg(item.getLastMessage()));
//        holder.idMessageGroupLastName.setText(item.);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.showVoiceBtn)
        ImageView showVoiceBtn;
        @Bind(R.id.chatBottomLeftLayout)
        LinearLayout chatBottomLeftLayout;
        @Bind(R.id.id_message_group_name)
        TextView idMessageGroupName;
        @Bind(R.id.id_message_group_time)
        TextView idMessageGroupTime;
        @Bind(R.id.id_message_group_last_message)
        TextView idMessageGroupLastMessage;
        @Bind(R.id.id_message_group_last_name)
        TextView idMessageGroupLastName;
        View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }
    }


}
