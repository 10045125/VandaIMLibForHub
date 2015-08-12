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
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.vandaimlibforhub.adapter.ChatEnumMapAdater;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.MessageAgent;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsg;
import com.wzl.wzl_vanda.vandaimlibforhub.fragment.ChatFragment;
import com.wzl.wzl_vanda.vandaimlibforhub.model.MessageItem;
import com.wzl.wzl_vanda.vandaimlibforhub.model.User;
import com.wzl.wzl_vanda.vandaimlibforhub.service.UserService;
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
public class ChatOtherImageViewBinder2 extends DataBinder<ChatOtherImageViewBinder2.ViewHolder> {

//    private List<SampleData> mDataSet = new ArrayList<>();

    public ChatOtherImageViewBinder2(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.chat_layout_other_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(final ViewHolder holder, int position) {
        IMMsg imMsg = ((ChatEnumMapAdater)getDataBindAdapter()).get(position);

        Glide.with(ChatFragment.instance)
                .load(imMsg.getUrl())
//                .tag(getDataBindAdapter().context)
                .override(200, 200)
                .into(holder.mImageView);
        Map<String,Object> map = imMsg.getAttrs();
        if (map != null && map.get(MessageAgent.MAPKEY) != null)
            UserService.findUserInConversationAllInfo((String)map.get(MessageAgent.MAPKEY), new FindCallback<AVObject>() {

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

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.id_image)
        ImageView mImageView;
        @Bind(R.id.id_chat_text_iv_bg)
        CircleImageView idChatTextIvBg;
        @Bind(R.id.id_chat_text_tv_name)
        TextView idChatTextTvName;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
