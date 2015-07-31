package com.wzl.wzl_vanda.viewtypelibrary;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Class for binding view and data
 *
 * Created by yqritc on 2015/03/01.
 */
abstract public class DataBinder<T extends RecyclerView.ViewHolder> {

    private DataBindAdapter mDataBindAdapter;

    public DataBinder(DataBindAdapter dataBindAdapter) {
        mDataBindAdapter = dataBindAdapter;
    }

    abstract public T newViewHolder(ViewGroup parent);

    abstract public void bindViewHolder(T holder, int position);

    public DataBindAdapter getDataBindAdapter(){
        return mDataBindAdapter;
    }

    public final void notifyDataSetChanged() {
        mDataBindAdapter.notifyDataSetChanged();
    }

}
