/*******************************************************************************
 * Copyright (c) 2006-2010 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Util {
    
    public final static long HRS_24_MS = (1000*(60*60*24));
    public final static long HRS_1_MS = (1000*(60*60));
    
    public static boolean isToday (Date date) {
        Date now = new Date();
        if (date.getYear() == now.getYear() 
                && date.getMonth() == now.getMonth() 
                && date.getDate() == now.getDate()) 
            return true;
        return false;
    }

    public static long getLastHour (Date now) {
        Date date = new Date(now.getTime());
        //wind the clock back to the hour
        date.setMinutes(0);
        date.setSeconds(0);
        long time = date.getTime();
        return time;
    }

    public static long getLastCompletedHour (Date now) {
        Date date = new Date(now.getTime());
        //wind the clock back to the hour
        date.setMinutes(0);
        date.setSeconds(0);
        long time = date.getTime();
        //wind the clock back to the previous hour
        time = time - HRS_1_MS;
        return time;
    }
    
    public static long get24HrsPrevious (long time) {
        return time - HRS_24_MS;
    }
    
    public static int[] getAvailableHours (Date date) {
        int max = 24;
        if (Util.isToday(date)) {
            Date now = new Date();
                max = now.getHours() + 1; //include current hour
        }
        int[] hours = new int[max];
        for (int i=0;i<max;i++) {
            hours[i]=i;
        }
        return hours;
    }
    
    public static int[] getAvailableHours (long timestamp) {
        return getAvailableHours(new Date(timestamp));
    }
    
    public static <E extends Enum<E>> List<E> convertToEnum(String str, Class<E> clazz) {

        if (str == null || str.length() == 0)
            return Collections.emptyList();

        String[] metrics = str.split(",");
        List<String> names = Arrays.asList(metrics);
        return convertToEnumFromCamelCase(names, clazz);
    }

    public static <E extends Enum<E>> List<E> convertToEnumFromCamelCase (List<String> names,Class<E> clazz) {
        if (names == null || names.size() == 0)
            return Collections.emptyList();

        List<E> list = new ArrayList<E>();

        for (String m:names) {
            char[] chars = m.trim().toCharArray();
            chars[0] = Character.toUpperCase(chars[0]);
            try {
                E e = Enum.valueOf(clazz ,new String(chars));
                list.add(e);
            } catch (IllegalArgumentException e) {
                //Ignore as it might be metric names that aren't relevant for this presenter
            } catch (NullPointerException e) {
                //Ignore
            }
        }

        return list;
    }
    
    public static <E extends Enum<E>> List<String> convertFromEnumToCamelCase (List<E> metrics) {
        if (metrics == null || metrics.size() == 0)
            return Collections.emptyList();
        
        List<String> list = new ArrayList<String>();
        for (E e:metrics) {
            char[] chars = e.toString().toCharArray();
            chars[0] = Character.toLowerCase(chars[0]);
            list.add(new String(chars));
        }
        return list;
    }
    
    
    
}
