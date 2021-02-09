package com.wl3321.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2020/9/22 12:16
 * desc   :
 */
public class DateUtils {
    //一分钟
    public static final long BASE_MIN = 1000*60;
    //一小时
    public static final long BASE_HOUR = 1000*60*60;
    //一天
    public static final long BASE_DAY = 1000*60*60*24;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 将时间戳转换为时间
     */
    public static String stampToDate(Long lt){
        String res;
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }


    /**
     * 计算时间  转换日期
     */
    public static String calcDistanceTime(String date) {
        String str = date.split(" ")[0];
        try {
            long dtime = simpleDateFormat.parse(date).getTime();
            Long time = System.currentTimeMillis()-dtime;
            //刚刚发布  1分钟以内
            if (time<=BASE_MIN){
                str = "1分钟前";
                //几分钟前 一小时以内  并且 大于1分钟
            }else if(time>BASE_MIN&&time<BASE_HOUR){

                str = (int) Math.floor(time/BASE_MIN)+"分钟前";

                //几小时前 一天以内  并且  大于1小时
            }else if (time>=BASE_HOUR&&time<BASE_DAY){

                str  = (int) Math.floor(time/BASE_HOUR)+"小时前";

                //几天前   大于1天  小于30天
            }else if (time>=BASE_DAY&&time<BASE_DAY*2){
                str = (int) Math.floor(time/BASE_DAY)+"天前";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
