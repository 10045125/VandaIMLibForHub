package com.wzl.wzl_vanda.vandaimlibforhub.binder;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.viewtypelibrary.DataCursorBindAdapter;
import com.wzl.wzl_vanda.viewtypelibrary.DataCursorBinder;


/**
 * Created by wzl_vanda on 15/7/27.
 */
public class ChatOtherSoundBinder extends DataCursorBinder<ChatOtherSoundBinder.ViewHolder> {

//    private List<SampleData> mDataSet = new ArrayList<>();

    public ChatOtherSoundBinder(DataCursorBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.chat_layout_other_sound, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder,Cursor cursor, int position) {
//        holder.mImageView.setImageResource(R.drawable.bird);
//        Picasso.with(holder.mImageView.getContext())
//                .load(R.drawable.bird)
//                .into(holder.mImageView);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleText;
        ImageView mImageView;
        TextView mContent;

        public ViewHolder(View view) {
            super(view);
//            mTitleText = (TextView) view.findViewById(R.id.title_type1);
//            mImageView = (ImageView) view.findViewById(R.id.image_type1);
//            mContent = (TextView) view.findViewById(R.id.content_type1);
        }
    }
}
