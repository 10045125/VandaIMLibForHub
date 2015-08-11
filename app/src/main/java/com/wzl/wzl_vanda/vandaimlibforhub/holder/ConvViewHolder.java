package com.wzl.wzl_vanda.vandaimlibforhub.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMConv;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jam on 04/08/2015.
 */
public class ConvViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.id_conv_icon)
    ImageView idTextViewIcon;
    @Bind(R.id.id_conv_header)
    TextView idTextViewHeader;
    @Bind(R.id.id_conv_time)
    TextView idTextViewConvTime;
    @Bind(R.id.id_conv_text)
    TextView idTextViewText;
    @Bind(R.id.id_conv_footer)
    TextView idTextViewFooter;


    public static ConvViewHolder build(ViewGroup viewGroup) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.conv_layout_item, null);

        return new ConvViewHolder(v);
    }

    public ConvViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void onBindData(Context context, IMConv conv) {

        this.idTextViewHeader.setText(this.formatHeader(conv) + "_" + conv.unreadCount);
        this.idTextViewConvTime.setText(this.formatLatestMsgTime(conv));
        this.idTextViewText.setText(this.formatText(conv));
        this.idTextViewFooter.setText(this.formatFooter(conv));
    }

    private String formatHeader(IMConv conv) {
        String header = (String) conv.getAttr(IMConv.ATTR_TITLE);
        if (header == null) header = "NO-TITLE, " + conv.convId;

        return header;
    }

    private String formatLatestMsgTime(IMConv conv) {
        return String.valueOf(conv.latestMsgTime);
    }

    private String formatText(IMConv conv) {
        String text = (String) conv.getAttr(IMConv.ATTR_TEXT);
        if (text == null) text = "NO-TEXT, " + conv.convId;
        return text;
    }

    private String formatFooter(IMConv conv) {
        String senderId = (String) conv.getAttr(IMConv.ATTR_SENDER_ID);
        return senderId == null ? "" : senderId;
    }
}
