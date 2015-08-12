package com.wzl.wzl_vanda.vandaimlibforhub.binder;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.bumptech.glide.Glide;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.squareup.picasso.Picasso;
import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.vandaimlibforhub.adapter.ChatEnumMapAdater;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.ChatManager;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.EmotionHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.MessageAgent;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsg;
import com.wzl.wzl_vanda.vandaimlibforhub.fragment.ChatFragment;
import com.wzl.wzl_vanda.vandaimlibforhub.model.MessageItem;
import com.wzl.wzl_vanda.vandaimlibforhub.model.User;
import com.wzl.wzl_vanda.vandaimlibforhub.model.UserInfo;
import com.wzl.wzl_vanda.vandaimlibforhub.service.UserService;
import com.wzl.wzl_vanda.vandaimlibforhub.view.JustifyTextView;
import com.wzl.wzl_vanda.viewtypelibrary.DataBindAdapter;
import com.wzl.wzl_vanda.viewtypelibrary.DataBinder;
import com.wzl.wzl_vanda.viewtypelibrary.bean.DemoItem;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wzl_vanda on 15/7/28.
 */
public class ChatOtherTextViewBinder2 extends DataBinder<ChatOtherTextViewBinder2.ViewHolder> {

    public ChatOtherTextViewBinder2(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.chat_layout_other_textview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(final ViewHolder holder, int position) {

        IMMsg imMsg = ((ChatEnumMapAdater)getDataBindAdapter()).get(position);
        holder.idChatTextview.setText(imMsg.text);

        Map<String,Object> map = imMsg.getAttrs();
        if (map != null && map.get(MessageAgent.MAPKEY) != null) {
            Log.e("map", "" + map.get(MessageAgent.MAPKEY));
            UserService.findUserInConversationAllInfo((String) map.get(MessageAgent.MAPKEY), new FindCallback<AVObject>() {

                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (list != null && list.size() > 0) {
                        Glide.with(ChatFragment.instance)
                                .load(list.get(0).getAVFile(User.AVATAR).getUrl())
//                                .tag(getDataBindAdapter().context)
                                .into(holder.idChatTextIvBg);
                        holder.idChatTextTvName.setText(list.get(0).getString(User.NICKNAME));
                    }
                }
            });
        }

        /*try {
            UserService.findUserUrl(item.avimTypedMessage.getFrom(), new FindCallback<AVObject>() {

                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (list != null && list.size() > 0) {
                        Picasso.with(getDataBindAdapter().context)
                                .load(list.get(0).getAVFile(User.AVATAR).getUrl())
                                .tag(getDataBindAdapter().context)
                                .into(holder.idChatTextIvBg);
                        holder.idChatTextTvName.setText(list.get(0).getString(User.NICKNAME));
                    }
                }
            });
        } catch (AVException e) {
            e.printStackTrace();
            return;
        }*/
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.id_chat_textview_status_ok)
        TextView idChatTextviewStatusOk;
        @Bind(R.id.id_chat_textview_status_fail)
        TextView idChatTextviewStatusFail;
        @Bind(R.id.id_chat_textview)
        EmojiconTextView idChatTextview;
        @Bind(R.id.id_chat_text_iv_bg)
        CircleImageView idChatTextIvBg;
        @Bind(R.id.id_chat_text_tv_name)
        TextView idChatTextTvName;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
