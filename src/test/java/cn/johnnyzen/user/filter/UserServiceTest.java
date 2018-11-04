package cn.johnnyzen.user.filter;

import cn.johnnyzen.user.User;
import cn.johnnyzen.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/4  14:37:55
 * @Description: ...
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    public void testLogin(){
        HttpSession session = null;
        userService.login(session, "johnny", "123456", "1125418540@qq.com");
    }
}
