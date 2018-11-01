package cn.johnnyzen.newword;

import cn.johnnyzen.newWord.NewWord;
import cn.johnnyzen.newWord.NewWordRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/1  22:52:06
 * @Description: ...
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class NewWordServiceTest {
    @Autowired
    private NewWordRepository newWordRepository;

    @Test
    public void testFindAllByEmail(){
        List<NewWord> newWords = newWordRepository.findAllByEmail("1125418540@qq.com");
        for(NewWord item:newWords){
            System.out.println(item.toString());
        }
    }


}
