package com.wzl.wzl_vanda.vandaimlibforhub.utils;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.JSONHelper;

import org.json.JSONException;

import java.util.Map;

/**
 * Created by Jam on 05/08/2015.
 */
public class JsonUtils {
    public static String toJsonString(Object obj) {
        return JSON.toJSONString(obj);
    }

    public static <T> T fromJsonString(String jsonStr, Class<T> clazz){
        return JSON.parseObject(jsonStr, clazz);
    }
}
