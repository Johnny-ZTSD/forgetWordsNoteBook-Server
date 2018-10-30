package cn.johnnyzen.user.filter;

import org.junit.Test;

import java.util.Calendar;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/10/7  15:11:50
 * @Description: ...
 */

public class LoginFilterTest {
    @Test
    public void differenceMillis(){
        Calendar lastLoginDateTime = Calendar.getInstance();
        System.out.println(lastLoginDateTime.toString());
        lastLoginDateTime.set(Calendar.SECOND,0);

        Calendar cal2 = Calendar.getInstance();
        cal2.set(Calendar.SECOND,23);
        long diferenceMillis = cal2.getTimeInMillis() - lastLoginDateTime.getTimeInMillis();
        System.out.println(diferenceMillis/1000);
    }
}
