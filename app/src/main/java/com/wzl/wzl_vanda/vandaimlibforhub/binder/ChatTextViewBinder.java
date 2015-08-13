package com.wzl.wzl_vanda.vandaimlibforhub.binder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rockerhieu.emojicon.EmojiconTextView;
import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.vandaimlibforhub.adapter.ChatEnumMapAdater;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsg;
import com.wzl.wzl_vanda.vandaimlibforhub.fragment.ChatFragment;
import com.wzl.wzl_vanda.viewtypelibrary.DataBindAdapter;
import com.wzl.wzl_vanda.viewtypelibrary.DataBinder;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wzl_vanda on 15/7/28.
 */
public class ChatTextViewBinder extends DataBinder<ChatTextViewBinder.ViewHolder> {


    public ChatTextViewBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.chat_layout_textview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(final ViewHolder holder, int position) {
        ChatEnumMapAdater chatEnumMapAdater = (ChatEnumMapAdater)getDataBindAdapter();
        IMMsg imMsg = chatEnumMapAdater.get(position);

        holder.idChatTextview.setText(imMsg.text);
        holder.idChatTextTvName.setText(chatEnumMapAdater.nickNameForMe);
        Glide.with(ChatFragment.instance)
                .load(chatEnumMapAdater.faceUrlForMe)
//                .tag(getDataBindAdapter().context)
                .into(holder.idChatTextIvBg);
       /* try {
            UserService.findUserUrl(item.avimTypedMessage.getFrom(), new FindCallback<AVObject>() {

                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (list != null && list.size() > 0) {
                        Picasso.with(getDataBindAdapter().context)
                                .load(list.get(0).getAVFile(User.AVATAR).getUrl())
                                .tag(getDataBindAdapter().context)
                                .into(holder.idChatTextIvBg);
                        holder.idChatTextTvName.setText(list.get(0).getString(User.NICKNAME));
                    }else{
                        if (BuildConfig.DEBUG)
                        Log.e("AVE ->> ",e.toString()+"");
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
