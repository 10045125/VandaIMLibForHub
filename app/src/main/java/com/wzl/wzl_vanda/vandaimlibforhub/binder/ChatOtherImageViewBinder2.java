package com.wzl.wzl_vanda.vandaimlibforhub.binder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.viewtypelibrary.DataBindAdapter;
import com.wzl.wzl_vanda.viewtypelibrary.DataBinder;
import com.wzl.wzl_vanda.viewtypelibrary.bean.DemoItem;

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
    public void bindViewHolder(ViewHolder holder, int position) {
//        holder.mImageView.setImageResource(R.drawable.bird);
//        Picasso.with(holder.mImageView.getContext())
//                .load(R.drawable.bird)
//                .into(holder.mImageView);
        DemoItem item = (DemoItem) getDataBindAdapter().get(position);

//        DemoItem item = DemoItem.fromCursor(cursor);
//
        Picasso.with(getDataBindAdapter().context)
                .load(item.url)
//                .placeholder(R.drawable.placeholder)
//                .error(R.drawable.error)
//                .resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
//                .centerInside()
                .tag(getDataBindAdapter().context)
                .into(holder.mImageView);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTitleText;
        ImageView mImageView;
        TextView mContent;

        public ViewHolder(View view) {
            super(view);
//            mTitleText = (TextView) view.findViewById(R.id.title_type1);
            mImageView = (ImageView) view.findViewById(R.id.id_image);
//            mContent = (TextView) view.findViewById(R.id.content_type1);
        }
    }
}
