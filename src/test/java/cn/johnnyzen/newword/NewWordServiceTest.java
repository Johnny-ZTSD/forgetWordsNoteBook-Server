package cn.johnnyzen.newword;

import cn.johnnyzen.newWord.NewWord;
import cn.johnnyzen.newWord.NewWordRepository;
import cn.johnnyzen.newWord.NewWordService;
import cn.johnnyzen.util.request.RequestProperties;
import cn.johnnyzen.util.request.RequestUtil;
import cn.johnnyzen.word.ThirdWordResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
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

    @Autowired //从容器中取值，对类成员变量、方法及构造函数进行标注，完成自动装配的工作。
    private RequestProperties requestProperties;

    @Autowired
    private RequestUtil requestUtil;

    @Autowired
    private NewWordService newWordService;

    @Test
    public void testCalculateForgetRate(){
        String logPrefix = "[NewWordServiceTest.testCalculateForgetRate] ";
        Calendar now = Calendar.getInstance();
        double rate = newWordService.calculateForgetRate(now, now, 11);
        System.out.println(logPrefix + "rate:" + rate);
    }

    //@Test
    public void testFindAllByEmail(){
        Collection<NewWord> newWords = newWordRepository.findAllByEmail("1125418540@qq.com");
        for(NewWord item:newWords){
            System.out.println(item.toString());
        }
    }

//    @Test
    public void testRequestPropeties(){
        System.out.println(requestProperties.toString());
    }

//    @Test
    public void testRequestHtmlDocument() throws IOException {
        String englishWord = "family";
        StringBuilder url = new StringBuilder("http://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i=");
        url.append(englishWord);
        Document doc = requestUtil.getDocument(url.toString());

        //json to obejct
        ObjectMapper mapper = new ObjectMapper();
        ThirdWordResult thirdWordResult = mapper.readValue(doc.text(), ThirdWordResult.class);

        System.out.println(thirdWordResult.toString());
        System.out.println(doc.text());
    }


}
