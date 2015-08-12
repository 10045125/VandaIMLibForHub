package com.wzl.wzl_vanda.viewtypelibrary;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public abstract class EnumMapBindAdapter<B,E extends Enum<E>> extends DataBindAdapter<B> {

    private Map<E, DataBinder> mBinderMap = new HashMap<>();

    public EnumMapBindAdapter(Context context){
        super(context);
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
    public <T extends DataBinder> T getDataBinder(int viewType) {
        return getDataBinder(getEnumFromOrdinal(viewType));
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

    public <T extends DataBinder> T getDataBinder(E e) {
        return (T) mBinderMap.get(e);
    }

    public Map<E, DataBinder> getBinderMap() {
        return mBinderMap;
    }

    public void putBinder(E e, DataBinder binder) {
        mBinderMap.put(e, binder);
    }

    public void removeBinder(E e) {
        mBinderMap.remove(e);
    }

    public void clearBinderMap() {
        mBinderMap.clear();
    }
}
