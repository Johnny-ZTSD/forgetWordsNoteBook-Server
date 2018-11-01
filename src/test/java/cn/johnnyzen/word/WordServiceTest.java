package cn.johnnyzen.word;

import cn.johnnyzen.newWord.NewWordRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/1  23:37:01
 * @Description: ...
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WordServiceTest {
    @Autowired
    private WordRepository wordRepository;

    @Test
    public void testWordRepository(){
    }
}
