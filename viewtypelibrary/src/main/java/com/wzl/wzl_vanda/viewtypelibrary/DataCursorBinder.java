package com.wzl.wzl_vanda.viewtypelibrary;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by wzl_vanda on 15/7/28.
 */
public abstract class DataCursorBinder <T extends RecyclerView.ViewHolder> {

    private DataCursorBindAdapter mDataCursorBindAdapter;

    public DataCursorBinder(DataCursorBindAdapter dataCursorBindAdapter) {
        mDataCursorBindAdapter = dataCursorBindAdapter;
    }

    abstract public T newViewHolder(ViewGroup parent);

    abstract public void bindViewHolder(T holder,Cursor cursor, int position);

    public DataCursorBindAdapter getDataBindAdapter(){
        return mDataCursorBindAdapter;
    }

    public final void notifyDataSetChanged() {
        mDataCursorBindAdapter.notifyDataSetChanged();
    }
}
