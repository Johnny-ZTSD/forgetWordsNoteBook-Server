package cn.johnnyzen.util.code;

import org.junit.Test;

import java.util.Calendar;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/10/7  16:51:08
 * @Description: ...
 */

public class CodeUtilTest {
    @Test
    public void MD5(){
        System.out.print(CodeUtil.MD5(Long.toString(Calendar.getInstance().getTimeInMillis())));
    }

}
