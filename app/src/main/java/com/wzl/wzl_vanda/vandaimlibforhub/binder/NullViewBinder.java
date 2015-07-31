package com.wzl.wzl_vanda.vandaimlibforhub.binder;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wzl.wzl_vanda.viewtypelibrary.DataCursorBindAdapter;
import com.wzl.wzl_vanda.viewtypelibrary.DataCursorBinder;


/**
 * Created by wzl_vanda on 15/7/28.
 */
public class NullViewBinder extends DataCursorBinder<NullViewBinder.ViewHolder> {


    public NullViewBinder(DataCursorBindAdapter dataBindAdapter) {
        super(dataBindAdapter);
    }

    @Override
    public ViewHolder newViewHolder(ViewGroup parent) {
        View view = new View(getDataBindAdapter().context);
        return new ViewHolder(view);
    }

    @Override
    public void bindViewHolder(ViewHolder holder,Cursor cursor, int position) {
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
        }
    }
}
