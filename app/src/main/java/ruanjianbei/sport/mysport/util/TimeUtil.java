package ruanjianbei.sport.mysport.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by li on 2018/8/19.
 */

public class TimeUtil {
    /*
     * 格式yyyy-MM-dd
     * 之前返回true,之后false
     */
    public static Boolean getDayBetweenDate(String startDateStr) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String time = null;

        Date date = new Date();
        Date startDate = simpleDateFormat.parse(startDateStr);
        //之前返回true,之后false
        boolean flag = startDate.after(date);
        if (startDateStr.equals(simpleDateFormat.format(date))) {
            return true;
        }


        return flag;
    }

    /**
     * 比较时间大小如果str1早于str2返回true，否则返回false
     * @param str1 格式HH:mm
     * @param str2 格式HH:mm
     * @return
     */
    public static Boolean getshijiandaxiao(String str1, String str2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date a=sdf.parse(str1);
        Date b=sdf.parse(str2);
        //Date类的一个方法，如果a早于b返回true，否则返回false
        if(a.before(b))
            return true;
        else
            return false;
    }

    /**
     * 日期转星期
     *
     * @param datetime
     * @return
     */
    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 给时间加上分钟
     *
     * @param day 当前时间 格式：HH:mm
     * @param min 需要加的时间
     * @return
     */
    public static String addDateMinut(String day, int min) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = format.parse(day);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (date == null)
            return "";
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, min);// 24小时制
        date = cal.getTime();
        System.out.println("after:" + format.format(date));  //显示更新后的日期
        cal = null;
        return format.format(date);
    }

    /**
     * 两个时间相差距离多少天多少小时多少分多少秒
     *
     * @param str1 时间参数 1 格式：1990-01-01 12:00
     * @param str2 时间参数 2 格式：2009-01-01 12:00
     * @return long[] 返回值为：{天, 时, 分, 秒}
     */
    public static long[] getDistanceTimes(String str1, String str2) {
        System.out.println("第一个时间：" + str1 + "第二个时间：" + str2);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date one;
        Date two;
        long day = 0;
        long hour = 0;
        long min = 0;
        long sec = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            diff = time2 - time1;
            min = ((diff / (60 * 1000)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long[] times = {min};
        return times;
    }
    /**
     * 获取两个日期之间的所有日期（yyyy-MM-dd）
     * @Description TODO
     * @param begintime
     * @param endtime
     * @return
     */
    public static List<String> getBetweenDates(String begintime, String endtime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        List<String> result = new ArrayList<String>();
        Calendar tempStart = Calendar.getInstance();
        try {
            Date begin = f.parse(begintime);
            Date end = f.parse(endtime);
            tempStart.setTime(begin);
            while(begin.getTime()<=end.getTime()){
                result.add(f.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
                begin = tempStart.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
            /* Calendar tempEnd = Calendar.getInstance();
            tempStart.add(Calendar.DAY_OF_YEAR, 1);
            tempEnd.setTime(end);
            while (tempStart.before(tempEnd)) {
                result.add(tempStart.getTime());
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }*/
        return result;
    }
}
