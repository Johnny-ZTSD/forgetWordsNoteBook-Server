package cn.johnnyzen.util.page;

import cn.johnnyzen.user.User;
import cn.johnnyzen.user.UserRepository;
import cn.johnnyzen.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/4  13:29:23
 * @Description: 分页测试
 * @Reference:
 *      0 Springboot 之 使用JPA进行分页操作
 *              https://blog.csdn.net/zsl129/article/details/52884571
 *      1 Spring Data JPA —— 分页PageRequest、PageImpl
 *              https://blog.csdn.net/Jae_Wang/article/details/80630776
 *      2 Spring Data JPA: 分页和排序
 *              https://blog.csdn.net/u011848397/article/details/52151673
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PageableTest {
    @Autowired
    private UserRepository userRepository;

//    @Test
    public void testPageImpl(){
        List<User> userList = new ArrayList<User>();
        for(int i=0;i<4;i++){
            User user = new User();
            user.setId(i+1);
            userList.add(user);
        }
        Pageable pageable = new PageRequest(2,2);
//        Pageable pageable = new PageRequest(2,2, Sort.Direction.DESC);
        Page<User> users = new PageImpl<User>(userList, pageable, 4);
        Iterator<User> iterator = users.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next().toString());
        }
    }

    @Test
    public void testFindUsers(){
        System.out.println(userRepository.findAll().get(0).toString());
        System.out.println(userRepository.findAll(new PageRequest(1,1)).iterator().next());
    }
//    @Test
//    public void testPageableTools() {
//        String logPrefix = this.getClass().getName().concat(Thread.currentThread().getStackTrace()[1].getMethodName().concat(" "));
//        Pageable pageable = null;
//        pageable = PageableTools.basicPage(1, 5);
//        if(pageable == null){
//            System.err.println(logPrefix + " pageable is null.");
//        } else {
//            Page<User> datas = userRepository.findAll(pageable);
//            PageableTools.print(datas);
//        }
//    }
}
