package com.wzl.wzl_vanda.vandaimlibforhub.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsg;
import com.wzl.wzl_vanda.vandaimlibforhub.view.JustifyTextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jam on 04/08/2015.
 */
public class TextOthersChatViewHolder extends ChatViewBaseHolder {

    @Bind(R.id.id_chat_textview_status_ok)
    TextView idChatTextviewStatusOk;
    @Bind(R.id.id_chat_textview_status_fail)
    TextView idChatTextviewStatusFail;
    @Bind(R.id.id_chat_textview)
    JustifyTextView idChatTextview;


    public static TextOthersChatViewHolder build(ViewGroup viewGroup) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat_layout_other_textview, null);

        return new TextOthersChatViewHolder(v);
    }

    public TextOthersChatViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onBindData(Context context, IMMsg msg) {
        idChatTextview.setText(msg.text);

        idChatTextviewStatusOk.setVisibility(View.VISIBLE);
        idChatTextviewStatusOk.setText(msg.readCount + "已读");

        idChatTextviewStatusFail.setVisibility(View.GONE);
    }
}
