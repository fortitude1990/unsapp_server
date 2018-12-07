package java_all.main_flow.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lizhijing on 2018/10/12.
 */
public class DateUtils {

    public static final String nowDate(){
        return nowDate(null);
    }

    public static final String nowDate(String format){

        if (format == null){
            format = "yyyyMMddHHmmss";
        }

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);

    }

    public static final String transferFormat(String format, String toFormat, String dateString){

        if (format == null || toFormat == null || dateString == null){
            return null;
        }

        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(format);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(toFormat);
        try {
            Date date = simpleDateFormat1.parse(dateString);
            String newDateString;
            if (date != null){
               newDateString = simpleDateFormat2.format(date);
            }else {
                newDateString = null;
            }
            return newDateString;
        }catch (Exception e){
          e.printStackTrace();
        }
        return null;

    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds,String format) {
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
            return "";
        }
        if(format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds+"000")));
    }

    /**
     * 日期格式字符串转换成时间戳
     * @param date_str 字符串日期
     * @param format 如：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String date2TimeStamp(String date_str,String format){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return String.valueOf(sdf.parse(date_str).getTime()/1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 取得当前时间戳（精确到秒）
     * @return
     */
    public static String timeStamp(){
        long time = System.currentTimeMillis();
        String t = String.valueOf(time/1000);
        return t;
    }

}
