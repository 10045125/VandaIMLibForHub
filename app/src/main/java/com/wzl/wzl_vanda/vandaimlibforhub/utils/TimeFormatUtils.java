/***********************************************************************************************************************
 * 
 * Copyright (C) 2015 by koogroup (http://www.koogroup.co)
 * http://www.koogroup.co/
 *
 ***********************************************************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 **********************************************************************************************************************/
package com.wzl.wzl_vanda.vandaimlibforhub.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Jam
 *
 */
public class TimeFormatUtils {

    static SimpleDateFormat DATE_TIME = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    static SimpleDateFormat HOUR_MINUTE = new SimpleDateFormat("HH:mm");
    
    final static long ONE_DAY_MILLS = 24 * 60 * 60 * 1000;
    
    final static long ONE_WEEK_MILLS = ONE_DAY_MILLS * 7;
    /**
     * 0 - 星期天
     * 1 - 星期一
     * ...
     * 6 - 星期六
     */
    static String[] DATE_OF_WEEK_MAP = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    
    /**
     * @param args
     */
//    public static void main(String[] args) {
//        long now = System.currentTimeMillis();
//        now += ONE_DAY_MILLS * 5;
//        System.out.println(formatTime(now, now - ONE_DAY_MILLS * 2));
//
//    }
    
    public static String formatTime(long now, long toBeFormatedMills) {
        Date toBeFormated = new Date(toBeFormatedMills);
        
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(now);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        
        // 今天
        long todayStartMills = cal.getTimeInMillis();
        if (todayStartMills < toBeFormatedMills) return HOUR_MINUTE.format(toBeFormated);
        
        // 昨天
        long yesterdayStartMills = todayStartMills - ONE_DAY_MILLS;
        if (yesterdayStartMills < toBeFormatedMills) return "昨天 " + HOUR_MINUTE.format(toBeFormated);
        
        // 本周
        if (isSameWeek(now, toBeFormatedMills)) return formatWeekTime(toBeFormatedMills);
        
        return DATE_TIME.format(toBeFormated);
    }

    static boolean isSameWeek(long time1, long time2) {
        long diff = Math.abs(time1 - time2);
        if (diff > ONE_WEEK_MILLS) return false;
        
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(time1);
        
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(time2);
        
        System.out.println(cal1.get(Calendar.WEEK_OF_YEAR));
        System.out.println(cal2.get(Calendar.WEEK_OF_YEAR));
        
        return cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR);
    }
    
    static String formatWeekTime(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        
        int week = cal.get(Calendar.DAY_OF_WEEK);
        return DATE_OF_WEEK_MAP[week - 1] + " " + HOUR_MINUTE.format(cal.getTime());
    }
}
