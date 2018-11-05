package cn.johnnyzen.authority;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/5  18:26:09
 * @Description: ...
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorityServiceTest {
    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    public void testHasOperationAuthorityOfUser(){
        String logPrefix = "[AuthorityServiceTest.testHasOperationAuthorityOfUser] ";
        String authorityCode = "57435347532423536236";
        String action = "saveNewWordOfUser/api";
        System.out.println(logPrefix + authorityService.hasOperationAuthorityOfUser(1,authorityCode,action));
    }

//    @Test
    public void testFindAuthorityTypeOfUserByUserIdAndAndAuthorityCode(){
        String logPrefix = "[AuthorityServiceTest.testFindAuthorityTypeOfUserByUserIdAndAndAuthorityCode] ";
        String authorityCode = "57435347532423536236";
        String action = "saveNewWordOfUser/api";
        System.out.println(logPrefix + "authorityType:" + authorityRepository.findAuthorityTypeOfUserByUserIdAndAndAuthorityCode(1,authorityCode));
    }
}
