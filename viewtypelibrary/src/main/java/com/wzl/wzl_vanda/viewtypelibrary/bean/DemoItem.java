package com.wzl.wzl_vanda.viewtypelibrary.bean;

import android.database.Cursor;
import android.util.Log;

import com.wzl.wzl_vanda.viewtypelibrary.db.ItemsDataHelper;


public class DemoItem {
    public int id;
    public String title;
    public int type;
    public String url;

    public DemoItem() {
    }

    public DemoItem(int id, String title, int type, String url) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.url = url;
    }

    public static DemoItem fromCursor(Cursor cursor) {
        DemoItem demoItem = new DemoItem();
        Log.e("cursor.", "" + cursor.getPosition());
//        if (cursor != null && cursor.getPosition() == -1) {
//            if (cursor.moveToFirst()) {
//
//                demoItem.id = cursor.getInt(cursor.getColumnIndex(ItemsDataHelper.ItemsDBInfo.ID));
//                demoItem.title = cursor.getString(cursor.getColumnIndex(ItemsDataHelper.ItemsDBInfo.TITLE));
//                demoItem.type = cursor.getInt(cursor.getColumnIndex(ItemsDataHelper.ItemsDBInfo.TYPE));
//                demoItem.url = cursor.getString(cursor.getColumnIndex(ItemsDataHelper.ItemsDBInfo.URL));
//            }
//        } else {
            demoItem.id = cursor.getInt(cursor.getColumnIndex(ItemsDataHelper.ItemsDBInfo.ID));
            demoItem.title = cursor.getString(cursor.getColumnIndex(ItemsDataHelper.ItemsDBInfo.TITLE));
            demoItem.type = cursor.getInt(cursor.getColumnIndex(ItemsDataHelper.ItemsDBInfo.TYPE));
            demoItem.url = cursor.getString(cursor.getColumnIndex(ItemsDataHelper.ItemsDBInfo.URL));
//        }
        return demoItem;
    }
}
