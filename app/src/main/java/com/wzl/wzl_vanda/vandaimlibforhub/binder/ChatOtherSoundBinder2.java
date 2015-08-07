package com.wzl.wzl_vanda.vandaimlibforhub.binder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.squareup.picasso.Picasso;
import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.MessageAgent;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.MessageHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.model.MessageItem;
import com.wzl.wzl_vanda.vandaimlibforhub.model.User;
import com.wzl.wzl_vanda.vandaimlibforhub.service.UserService;
import com.wzl.wzl_vanda.vandaimlibforhub.view.PlayButton;
import com.wzl.wzl_vanda.viewtypelibrary.DataBindAdapter;
import com.wzl.wzl_vanda.viewtypelibrary.DataBinder;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by wzl_vanda on 15/7/28.
 */
public class ChatOtherSoundBinder2 extends DataBinder<ChatOtherSoundBinder2.ViewHolder> {


    public ChatOtherSoundBinder2(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.chat_layout_other_sound, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(final ViewHolder holder, int position) {
        AVIMTypedMessage msg = ((MessageItem)getDataBindAdapter().get(position)).avimTypedMessage;
        initPlayBtn(msg, holder.idPlayButton);


        Map<String,Object> map = ((AVIMAudioMessage)msg).getAttrs();
        if (map != null && map.get(MessageAgent.MAPKEY) != null)
            UserService.findUserInConversationAllInfo((String)map.get(MessageAgent.MAPKEY), new FindCallback<AVObject>() {

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

       /* try {
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

    private void initPlayBtn(AVIMTypedMessage msg, PlayButton playBtn) {
        playBtn.setLeftSide(true);
        playBtn.setPath(MessageHelper.getFilePath(msg));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.id_play_button)
        PlayButton idPlayButton;
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
