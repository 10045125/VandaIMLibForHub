package com.wzl.wzl_vanda.vandaimlibforhub.binder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.vandaimlibforhub.adapter.ChatEnumMapAdater;
import com.wzl.wzl_vanda.vandaimlibforhub.adapter.SampleEnumMapAdapter;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsg;
import com.wzl.wzl_vanda.vandaimlibforhub.fragment.ChatFragment;
import com.wzl.wzl_vanda.vandaimlibforhub.model.MessageItem;
import com.wzl.wzl_vanda.vandaimlibforhub.model.User;
import com.wzl.wzl_vanda.vandaimlibforhub.service.UserService;
import com.wzl.wzl_vanda.viewtypelibrary.DataBindAdapter;
import com.wzl.wzl_vanda.viewtypelibrary.DataBinder;
import com.wzl.wzl_vanda.viewtypelibrary.bean.DemoItem;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by wzl_vanda on 15/7/28.
 */
public class ChatImageViewBinder2 extends DataBinder<ChatImageViewBinder2.ViewHolder> {

//    private List<SampleData> mDataSet = new ArrayList<>();

    public ChatImageViewBinder2(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.chat_layout_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(final ViewHolder holder, int position) {

        ChatEnumMapAdater chatEnumMapAdater = (ChatEnumMapAdater) getDataBindAdapter();
        holder.idChatTextTvName.setText(chatEnumMapAdater.nickNameForMe);
        Glide.with(ChatFragment.instance)
                .load(chatEnumMapAdater.faceUrlForMe)
//                .tag(getDataBindAdapter().context)
                .into(holder.idChatTextIvBg);
        IMMsg imMsg = chatEnumMapAdater.get(position);

        Glide.with(getDataBindAdapter().context)
                .load(imMsg.getUrl())
//                .tag(getDataBindAdapter().context)
                .override(400,400)
                .into(holder.mImageView);

        /*try {
            UserService.findUserUrl(msg.getFrom(), new FindCallback<AVObject>() {

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

        @Bind(R.id.id_image)
        ImageView mImageView;
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
