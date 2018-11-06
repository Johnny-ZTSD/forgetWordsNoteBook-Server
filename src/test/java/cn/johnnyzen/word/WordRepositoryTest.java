package cn.johnnyzen.word;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/6  00:48:28
 * @Description: ...
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WordRepositoryTest {

        @Autowired
        private WordRepository wordRepository;

//        @Test
        public void testWordRepository(){
            System.out.println(wordRepository.findFirstByEnglishWord("family").toString());
        }

        @Test
        public void testFindAllNewWordsOfNotUserByUserIdAndFuzzyEnglishWord(){
                System.out.println(wordRepository.findAllNewWordsOfNotUserByUserIdAndFuzzyEnglishWord(1,"fa").toString());
        }

}