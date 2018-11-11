package cn.johnnyzen.util;

import cn.johnnyzen.util.datetime.DatetimeUtil;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;

public class DateTimeUtilTest {
    @Test
    public void testTime(){
        String date="2018-11-04 08:11:00";
        System.out.println(date);
        Long l=DatetimeUtil.stringToTimestamp(date).getTime();
        String formatDate=DatetimeUtil.millisecondToDateString(Math.abs(System.currentTimeMillis()-l));
        System.out.println(formatDate);
        String day=formatDate.substring(0,formatDate.indexOf("天"));
        String hour=formatDate.substring(formatDate.indexOf("天")+1,formatDate.indexOf("时"));
        String minute=formatDate.substring(formatDate.indexOf("时")+1,formatDate.indexOf("分"));
        String second=formatDate.substring(formatDate.indexOf("分")+1,formatDate.indexOf("秒"));
        System.out.println(day);
        System.out.println(hour);
        System.out.println(minute);
        System.out.println(second);
        if(Integer.parseInt(day)==0){
            if(Integer.parseInt(hour)>0||Integer.parseInt(minute)>0||Integer.parseInt(second)>0){
                System.out.println(1);

            }else {
                System.out.println(0);
            }
        }
        if(Integer.parseInt(day)>0){
            if (Integer.parseInt(hour)>0||Integer.parseInt(minute)>0||Integer.parseInt(second)>0){
                System.out.println(Integer.parseInt(day)+1);

            }else {
                System.out.println(day);}

        }

    }
}
