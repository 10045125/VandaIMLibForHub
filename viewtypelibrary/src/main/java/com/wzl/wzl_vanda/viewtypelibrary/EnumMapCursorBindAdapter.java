package com.wzl.wzl_vanda.viewtypelibrary;

import android.content.Context;
import android.database.Cursor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wzl_vanda on 15/7/28.
 */
public abstract class EnumMapCursorBindAdapter <E extends Enum<E>> extends DataCursorBindAdapter {

    private Map<E, DataCursorBinder> mBinderMap = new HashMap<>();

    public EnumMapCursorBindAdapter(Context context, Cursor c){
        super(context,c);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return getEnumFromPosition(position).ordinal();
    }

    @Override
    public <T extends DataCursorBinder> T getDataCursorBinder(int viewType) {
        return getDataCursorBinder(getEnumFromOrdinal(viewType));
    }

    public abstract E getEnumFromPosition(int position);

    public abstract E getEnumFromOrdinal(int ordinal);

//    public E getEnumFromBinder(DataBinder binder) {
//        for (Map.Entry<E, DataBinder> entry : mBinderMap.entrySet()) {
//            if (entry.getValue().equals(binder)) {
//                return entry.getKey();
//            }
//        }
//        throw new IllegalArgumentException("Invalid Data Binder");
//    }

    public <T extends DataCursorBinder> T getDataCursorBinder(E e) {
        return (T) mBinderMap.get(e);
    }

    public Map<E, DataCursorBinder> getBinderMap() {
        return mBinderMap;
    }

    public void putBinder(E e, DataCursorBinder binder) {
        mBinderMap.put(e, binder);
    }

    public void removeBinder(E e) {
        mBinderMap.remove(e);
    }

    public void clearBinderMap() {
        mBinderMap.clear();
    }
}
