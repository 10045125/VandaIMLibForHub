package com.wzl.wzl_vanda.viewtypelibrary;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by wzl_vanda on 15/7/28.
 */
public abstract class DataCursorBindAdapter extends BaseAbstractRecycleCursorAdapter {

    public Context context;

    public DataCursorBindAdapter(Context context, Cursor c){
        super(context,c);
        this.context = context;
        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return getDataCursorBinder(viewType).newViewHolder(parent);
    }

//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
//        getDataBinder(viewHolder.getItemViewType()).bindViewHolder(viewHolder, position);
//    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor,int position) {
        getDataCursorBinder(viewHolder.getItemViewType()).bindViewHolder(viewHolder,cursor, position);
    }

    @Override
    public abstract int getItemViewType(int position);

    public abstract <T extends DataCursorBinder> T getDataCursorBinder(int viewType);
}
