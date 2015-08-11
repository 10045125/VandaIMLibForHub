package com.wzl.wzl_vanda.vandaimlibforhub.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.wzl.wzl_vanda.vandaimlibforhub.data.DBHelper;
import com.wzl.wzl_vanda.vandaimlibforhub.data.IMConv;
import com.wzl.wzl_vanda.vandaimlibforhub.holder.ConvViewHolder;

import java.util.ArrayList;

/**
 * Created by Jam on 04/08/2015.
 */
public class ConvRecyclerAdapter extends RecyclerView.Adapter<ConvViewHolder> {

    ArrayList<IMConv> mConvs;
    Context mContext;
    DBHelper mDbHelper;

    public ConvRecyclerAdapter(Context context, DBHelper dbHelper) {
        this.mContext = context;
        this.mDbHelper = dbHelper;

        this.mConvs = new ArrayList<>();
    }

    public void refreshFromDatabase() {
        mConvs = this.mDbHelper.loadConvs();
    }

    @Override
    public ConvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ConvViewHolder.build(parent);
    }

    @Override
    public void onBindViewHolder(ConvViewHolder holder, int position) {
        IMConv conv = this.getConvByPosition(position);
        if (conv == null) {
            Log.e("IM", "ConvRecyclerAdapter.onBindViewHolder no msg found at position:" + position);
            return;
        }

        holder.onBindData(mContext, conv);
    }

    @Override
    public int getItemCount() {
        return this.mConvs.size();
    }

    private IMConv getConvByPosition(int position) {
        int size = mConvs.size();
        if (position < 0 || position >= size) return null;

        return mConvs.get(position);
    }

}
