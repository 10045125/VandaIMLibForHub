package com.wzl.wzl_vanda.viewtypelibrary;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Adapter class for managing a set of data binders
 * <p/>
 * Created by yqritc on 2015/03/01.
 */
abstract public class DataBindAdapter<E> extends ArrayRecyclerAdapter<E> {

    public Context context;

    public DataBindAdapter(Context context){
        this.context = context;
        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return getDataBinder(viewType).newViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        getDataBinder(viewHolder.getItemViewType()).bindViewHolder(viewHolder, position);
    }

    @Override
    public abstract int getItemViewType(int position);

    public abstract <T extends DataBinder> T getDataBinder(int viewType);

}
