package cn.johnnyzen.user.filter;

import cn.johnnyzen.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/9  00:23:01
 * @Description: ...
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testisExistsThisUsername(){
//        String username="johnny";//1
        String username="jake";//0
        System.out.println(username + "in database: " + userRepository.isExistsThisUsername(username));
    }
}
