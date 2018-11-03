package cn.johnnyzen.newword;

import cn.johnnyzen.newWord.NewWord;
import cn.johnnyzen.newWord.NewWordRepository;
import cn.johnnyzen.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/3  11:40:36
 * @Description: ...email
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class NewWordRepositoryTest {
    @Autowired
    private NewWordRepository newWordRepository;

    //@Test ok
    public void testFindAllByUserId(){
        Collection<NewWord> newWords = newWordRepository.findAllByUserId(1);
        System.out.println("NewWordRepository.testFindAllByUserId:");
        for(NewWord item : newWords){
            System.out.println(item.toString());
        }
    }

    //@Test
    public void testFindAllByEmail(){
        Collection<NewWord> newWords = newWordRepository.findAllByEmail("1125418540@qq.com");
        System.out.println("NewWordRepository.testFindAllByEmail:");
        for(NewWord item : newWords){
            System.out.println(item.toString());
        }
    }

    //@Test
    public void testFindNewWordsLikeByUserIdAndEnglishWord(){
        String englishWord = "ch"; //fam
        Collection<NewWord> newWords = newWordRepository.findNewWordsLikeByUserIdAndEnglishWord(1, englishWord);
        System.out.println("NewWordRepository.testFindNewWordsLikeByUserIdAndEnglishWord:");
        for(NewWord item : newWords){
            System.out.println(item.toString());
            System.out.println(item.getWord().toString());
        }
    }

    @Test
    public void testFindDistinctFirstByUserIdAndEnglishWord(){
        NewWord newWord = null;
        String englishWord = "family";
        newWord = newWordRepository.findDistinctFirstByUserIdAndEnglishWord(1, englishWord);
        System.out.println("NewWordRepository.testFindDistinctFirstByUserIdAndEnglishWord:");
        System.out.println(newWord.toString());
    }

//    @Test ok
    public void testFindAllByUser(){
        User user = new User();
        user.setId(1);
        Collection<NewWord> newWords = newWordRepository.findAllByUser(user);
        System.out.println("NewWordRepository.testFindAllByUser:");
        for(NewWord item : newWords){
            System.out.println(item.toString());
        }
    }
}
