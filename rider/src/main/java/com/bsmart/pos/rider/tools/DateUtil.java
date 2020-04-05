package com.bsmart.pos.rider.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Author: yoda
 * DateTime: 2020/4/4 21:28
 */
public class DateUtil {

    public static Date formatChinese(String var){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(var);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static String formatEnglish(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy HH:mm", Locale.ENGLISH);
        return sdf.format(date);
    }

    public static  String fromCN_English(String var){
        try{
            Date date = formatChinese(var);
            return formatEnglish(date);
        }catch(Exception e){

        }
        return var;
    }

    /**
     * 获取日期date的day天后的日期，如果day小于0，则获取day天前的日期
     * @param day
     * @param date
     * @return
     */
    public static Date getDateDateByDay(int day, Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, day);
        return c.getTime();
    }

    public static List<String> monthList(int day, Date date){

        Date after = getDateDateByDay(day,date);

        Calendar endDate = Calendar.getInstance();
        endDate.setTime(after);

        List<String> months = new ArrayList<>();
        Calendar startDate = Calendar.getInstance();
        Integer currentMonth = startDate.get(Calendar.MONTH);
        Integer endMonth = endDate.get(Calendar.MONTH);

        if (endMonth>=currentMonth){

            for (int i = currentMonth; i <= endMonth; i++) {
                if (i<10){
                    months.add("0"+i);
                }else{
                    months.add(""+i);
                }
            }

        }else{
            for (int i = currentMonth; i < 12; i++) {
                if (i<10){
                    months.add("0"+i);
                }else{
                    months.add(""+i);
                }
            }

            for (int i = 0; i < endMonth; i++) {
                if (i<10){
                    months.add("0"+i);
                }else{
                    months.add(""+i);
                }
            }
        }

        return months;
    }

}
