package cn.johnnyzen.mail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/10/8  00:37:05
 * @Description: ...
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailTest {
    @Autowired
    private MailUtil mailUtil;
    @Test
    public void mailTest001(){
        System.out.print(mailUtil.getMailProperties().toString());
    }

    @Test
    public void substring(){
        String str = "/34536436";
        System.out.println(str.substring(1));
    }
}
