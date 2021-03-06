package cn.johnnyzen.newword;

import cn.johnnyzen.newWord.NewWord;
import cn.johnnyzen.newWord.NewWordRepository;
import cn.johnnyzen.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
    public void testIsNewWordsOfForgetRateTopByUserId(){
        System.out.println("testIsNewWordsOfForgetRateTopByUserId:" + newWordRepository.isNewWordsOfForgetRateTopByUserId(1,300,"test"));
    }

   @Test
    public void testFindNewWordsOfForgetRateTopByUserId(){
        Pageable pageable = new PageRequest(0,50);
        List<NewWord> newWordList = newWordRepository.findNewWordsOfForgetRateTopByUserId(1, pageable).getContent();
        Iterator<NewWord> nwIter = null;
        nwIter = newWordList.iterator();
        while(nwIter.hasNext()){
            System.out.println(nwIter.next().toString());
        }
    }

//    @Test
//    public void testFindAllBySortRuleOfUser(){
//        Pageable pageable = new PageRequest(0,50);
//        List<NewWord> newWordList = newWordRepository.findAllBySortRuleOfUser(1,"last_forgot_datetime", "DESC", pageable).getContent();
//        Iterator<NewWord> nwIter = null;
//        nwIter = newWordList.iterator();
//        while(nwIter.hasNext()){
//            System.out.println(nwIter.next().toString());
//        }
//    }

    //@Test
    public void testFindAllBySortRuleOfUser(){
        Pageable pageable = new PageRequest(0,50);
        List<NewWord> newWordList = newWordRepository.findAllByUserIdOrderByForgetCount(1, pageable).getContent();
        Iterator<NewWord> nwIter = null;
        nwIter = newWordList.iterator();
        while(nwIter.hasNext()){
            System.out.println(nwIter.next().toString());
        }
    }

    //@Test
    public void testFindDisorderedNewWordsByUserId(){
        Pageable pageable = new PageRequest(0,50);
        List<NewWord> newWordList = newWordRepository.findDisorderedNewWordsByUserId(1,pageable).getContent();
        Iterator<NewWord> nwIter = null;
        nwIter = newWordList.iterator();
        while(nwIter.hasNext()){
            System.out.println(nwIter.next().toString());
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

//    @Test
    public void testFindDistinctFirstByUserIdAndEnglishWord(){
        NewWord newWord = null;
        String englishWord = "family";
        newWord = newWordRepository.findDistinctFirstByUserIdAndEnglishWord(1, englishWord);
        System.out.println("NewWordRepository.testFindDistinctFirstByUserIdAndEnglishWord:");
        System.out.println(newWord.toString());
    }

//    @Test
    public void testFindNewWordById(){
        String logPrefix = "[NewWordRepositoryTest.testFindNewWordById] ";
        System.out.println(logPrefix + newWordRepository.findNewWordById(1).toString());
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

//    @Test
    public void testFindNewWordsOfLastDaysOfUser(){
        Pageable pageable = new PageRequest(1,2);
//        page，第几页，从0开始，默认为第0页
//        size，每一页的大小，默认为20
        System.out.println(newWordRepository.findNewWordsOfLastDaysOfUser(1,1, pageable).iterator().next());
    }
}
