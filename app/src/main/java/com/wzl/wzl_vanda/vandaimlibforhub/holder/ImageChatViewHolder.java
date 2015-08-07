package com.wzl.wzl_vanda.vandaimlibforhub.holder;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wzl.wzl_vanda.vandaimlibforhub.R;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMMsg;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jam on 04/08/2015.
 */
public class ImageChatViewHolder extends ChatViewBaseHolder {

    @Bind(R.id.id_image)
    ImageView mImage;

    public static ImageChatViewHolder build(ViewGroup viewGroup) {
        Log.i("IM", "ImageChatViewHolder.build");

        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat_layout_image, null);

        return new ImageChatViewHolder(v);
    }

    public ImageChatViewHolder(View view) {
        super(view);

        Log.i("IM", "ImageChatViewHolder()");
        ButterKnife.bind(this, view);
    }

    @Override
    public void onBindData(Context context, IMMsg msg) {
        String imageUrl = msg.getUrl();
        Log.i("IM", "onBindData, imageUrl:" + imageUrl);

        Picasso.with(context)
                .load(imageUrl)
                .into(mImage);
    }
}
