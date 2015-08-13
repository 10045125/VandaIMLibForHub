package com.wzl.wzl_vanda.vandaimlibforhub.binder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.vandaimlibforhub.adapter.ChatEnumMapAdater;
import com.wzl.wzl_vanda.vandaimlibforhub.controller.MessageHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsg;
import com.wzl.wzl_vanda.vandaimlibforhub.fragment.ChatFragment;
import com.wzl.wzl_vanda.vandaimlibforhub.view.PlayButton;
import com.wzl.wzl_vanda.viewtypelibrary.DataBindAdapter;
import com.wzl.wzl_vanda.viewtypelibrary.DataBinder;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by wzl_vanda on 15/7/28.
 */
public class ChatSoundBinder extends DataBinder<ChatSoundBinder.ViewHolder> {

//    private List<SampleData> mDataSet = new ArrayList<>();

    public ChatSoundBinder(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.chat_layout_sound, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(final ViewHolder holder, int position) {
        ChatEnumMapAdater chatEnumMapAdater = (ChatEnumMapAdater)getDataBindAdapter();
        holder.idChatTextTvName.setText(chatEnumMapAdater.nickNameForMe);
        Glide.with(ChatFragment.instance)
                .load(chatEnumMapAdater.faceUrlForMe)
//                .tag(getDataBindAdapter().context)
                .into(holder.idChatTextIvBg);

        IMMsg imMsg = chatEnumMapAdater.get(position);
        initPlayBtn(imMsg, holder.idPlayButton);
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

    private void initPlayBtn(IMMsg msg, PlayButton playBtn) {
        playBtn.setLeftSide(false);
        playBtn.setPath(MessageHelper.getFilePathForIMMsg(msg));
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
            ButterKnife.bind(this,view);
        }
    }
}
