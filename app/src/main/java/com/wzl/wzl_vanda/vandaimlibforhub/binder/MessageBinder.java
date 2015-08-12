package com.wzl.wzl_vanda.vandaimlibforhub.binder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.FindCallback;
import com.bumptech.glide.Glide;
import com.wzl.wzl_vanda.vandaimlibforhub.MainActivity;
import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.vandaimlibforhub.adapter.MessageListMapAdapter;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.ChatManager;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.MessageAgent;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMConv;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsgType;
import com.wzl.wzl_vanda.vandaimlibforhub.fragment.ChatFragment;
import com.wzl.wzl_vanda.vandaimlibforhub.fragment.MessageFragment;
import com.wzl.wzl_vanda.vandaimlibforhub.model.User;
import com.wzl.wzl_vanda.vandaimlibforhub.service.CacheService;
import com.wzl.wzl_vanda.vandaimlibforhub.service.UserService;
import com.wzl.wzl_vanda.vandaimlibforhub.utils.TimeFormatUtils;
import com.wzl.wzl_vanda.vandaimlibforhub.view.DraggableFlagView;
import com.wzl.wzl_vanda.viewtypelibrary.DataBindAdapter;
import com.wzl.wzl_vanda.viewtypelibrary.DataBinder;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

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
    public void bindViewHolder(final ViewHolder holder, int position) {
        final IMConv item = (IMConv) getDataBindAdapter().get(position);

        if (item == null) return;

        if (item.unreadCount > 0) {
            holder.idMessageUnreadcount.setVisibility(View.VISIBLE);
            holder.idMessageUnreadcount.setText(item.unreadCount + "");
        } else {
            holder.idMessageUnreadcount.setVisibility(View.GONE);
            holder.idMessageUnreadcount.setText("");
            holder.idMessageUnreadcount.resetAfterDismiss();
        }

        final String groupName = formatHeader(item);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("ConvName", groupName);
                bundle.putString("ConvId", item.convId);
                Intent intent = new Intent(getDataBindAdapter().context, MainActivity.class);
                intent.putExtras(bundle);
                ((MessageListMapAdapter) getDataBindAdapter()).getmDbHelper().resetConvUnreadCount(item.convId);
                item.unreadCount = 0;
                getDataBindAdapter().context.startActivity(intent);//,options.toBundle());
            }
        });
        holder.idMessageGroupName.setText(groupName);
        holder.idMessageGroupLastMessage.setText(formatText(item));
        holder.idMessageGroupTime.setText(TimeFormatUtils.formatTime(System.currentTimeMillis(),formatLatestMsgTime(item)));


        UserService.findUserInConversationAllInfo(formatFooter(item), new FindCallback<AVObject>() {

            @Override
            public void done(List<AVObject> list, AVException e) {
                if (list != null && list.size() > 0) {
                    holder.idMessageGroupLastName.setText(list.get(0).getString(User.NICKNAME));
                }
            }
        });

        Glide.with(MessageFragment.instance)
                .load("http://www.feizl.com/upload2007/2015_04/1504021516764612.jpg")
                .into(holder.idMessageListFace);

    }


    private String formatHeader(IMConv conv) {
        String header = (String) conv.getAttr(IMConv.ATTR_TITLE);
        if (header == null) header = "...";

        return header;
    }

    private long formatLatestMsgTime(IMConv conv) {
        return conv.latestMsgTime;
    }

    private String formatText(IMConv conv) {
        int imMsgType = (int)conv.getAttr(IMConv.ATTR_IM_MSG_TYPE);
        IMMsgType type = IMMsgType.valueOf(imMsgType);
        String text = "";
        switch (type) {
            case TEXT_OTHERS:
            case TEXT_MINE:
                text = (String) conv.getAttr(IMConv.ATTR_TEXT);
                if (text == null) text = "...";
                return text;
            case IMAGE_OTHERS:
            case IMAGE_MINE:
                text = "[图片]";
                return text;
            case AUDIO_OTHERS:
            case AUDIO_MINE:
                text = "[声音]";
                return text;
            default:
                return "";
        }
    }

    private String formatFooter(IMConv conv) {
        String senderId = (String) conv.getAttr(MessageAgent.MAPKEY);
        return senderId == null ? "" : senderId;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        @Bind(R.id.id_message_list_face)
        CircleImageView idMessageListFace;
        @Bind(R.id.id_message_unreadcount)
        DraggableFlagView idMessageUnreadcount;
        //        @Bind(R.id.chatBottomLeftLayout)
//        RelativeLayout chatBottomLeftLayout;
        @Bind(R.id.id_message_group_name)
        TextView idMessageGroupName;
        @Bind(R.id.id_message_group_time)
        TextView idMessageGroupTime;
        @Bind(R.id.id_message_group_last_message)
        TextView idMessageGroupLastMessage;
        @Bind(R.id.id_message_group_last_name)
        TextView idMessageGroupLastName;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }
    }


}
