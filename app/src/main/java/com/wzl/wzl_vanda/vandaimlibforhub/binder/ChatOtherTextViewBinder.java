package com.wzl.wzl_vanda.vandaimlibforhub.binder;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.viewtypelibrary.DataCursorBindAdapter;
import com.wzl.wzl_vanda.viewtypelibrary.DataCursorBinder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by wzl_vanda on 15/7/27.
 */
public class ChatOtherTextViewBinder extends DataCursorBinder<ChatOtherTextViewBinder.ViewHolder> {

//    private List<SampleData> mDataSet = new ArrayList<>();

    public ChatOtherTextViewBinder(DataCursorBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.chat_layout_other_textview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder,Cursor cursor, int position) {
//        holder.mImageView.setImageResource(R.drawable.bird);
//        Picasso.with(holder.mImageView.getContext())
//                .load(R.drawable.bird)
//                .into(holder.mImageView);
//        SampleData item = (SampleData) getDataBindAdapter().get(position);
//        holder.idChatTextviewStatusOk.setVisibility(View.GONE);
//        holder.idChatTextviewStatusFail.setVisibility(View.GONE);
//        if (item.sendStatus){
//            holder.idChatTextviewStatusOk.setVisibility(View.VISIBLE);
//        }else{
//            holder.idChatTextviewStatusFail.setVisibility(View.VISIBLE);
//        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.id_chat_textview_status_ok)
        TextView idChatTextviewStatusOk;
        @Bind(R.id.id_chat_textview_status_fail)
        TextView idChatTextviewStatusFail;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
//            mTitleText = (TextView) view.findViewById(R.id.title_type1);
//            mImageView = (ImageView) view.findViewById(R.id.image_type1);
//            mContent = (TextView) view.findViewById(R.id.content_type1);
        }
    }
}
