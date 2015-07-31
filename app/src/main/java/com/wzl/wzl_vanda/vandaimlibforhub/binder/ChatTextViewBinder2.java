package com.wzl.wzl_vanda.vandaimlibforhub.binder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.vandaimlibforhub.view.JustifyTextView;
import com.wzl.wzl_vanda.viewtypelibrary.DataBindAdapter;
import com.wzl.wzl_vanda.viewtypelibrary.DataBinder;
import com.wzl.wzl_vanda.viewtypelibrary.bean.DemoItem;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wzl_vanda on 15/7/28.
 */
public class ChatTextViewBinder2 extends DataBinder<ChatTextViewBinder2.ViewHolder> {

//    private List<SampleData> mDataSet = new ArrayList<>();

    public ChatTextViewBinder2(DataBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.chat_layout_textview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder, int position) {

        DemoItem item = (DemoItem) getDataBindAdapter().get(position);
//
//        holder.idChatTextviewStatusOk.setVisibility(View.GONE);
//        holder.idChatTextviewStatusFail.setVisibility(View.GONE);
//        if (item.sendStatus){
//            holder.idChatTextviewStatusOk.setVisibility(View.VISIBLE);
//        }else{
//            holder.idChatTextviewStatusFail.setVisibility(View.VISIBLE);
//        }
        holder.idChatTextview.setText(item.title);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {


        @Bind(R.id.id_chat_textview_status_ok)
        TextView idChatTextviewStatusOk;
        @Bind(R.id.id_chat_textview_status_fail)
        TextView idChatTextviewStatusFail;
        @Bind(R.id.id_chat_textview)
        JustifyTextView idChatTextview;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
