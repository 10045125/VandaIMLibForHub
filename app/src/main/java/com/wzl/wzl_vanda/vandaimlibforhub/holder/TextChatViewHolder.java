package com.wzl.wzl_vanda.vandaimlibforhub.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rockerhieu.emojicon.EmojiconTextView;
import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsg;
import com.wzl.wzl_vanda.vandaimlibforhub.view.JustifyTextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Jam on 04/08/2015.
 */
public class TextChatViewHolder extends ChatViewBaseHolder {

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

    public static TextChatViewHolder build(ViewGroup viewGroup) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat_layout_textview, null);
        return new TextChatViewHolder(v);
    }

    public TextChatViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onBindData(Context context, IMMsg msg) {
        idChatTextview.setText(msg.text);
        idChatTextviewStatusOk.setVisibility(View.GONE);
        idChatTextviewStatusFail.setVisibility(View.GONE);
    }
}
