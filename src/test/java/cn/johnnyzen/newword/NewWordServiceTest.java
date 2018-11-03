package cn.johnnyzen.newword;

import cn.johnnyzen.newWord.NewWord;
import cn.johnnyzen.newWord.NewWordRepository;
import cn.johnnyzen.util.request.RequestProperties;
import cn.johnnyzen.util.request.RequestUtil;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
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

    //@Test
    public void testFindAllByEmail(){
        List<NewWord> newWords = newWordRepository.findAllByEmail("1125418540@qq.com");
        for(NewWord item:newWords){
            System.out.println(item.toString());
        }
    }

    @Test
    public void testRequestPropeties(){
        System.out.println(requestProperties.toString());
    }

    //@Test
    public void testRequestHtmlDocument() throws IOException {
        String url = "http://fanyi.youdao.com/translate?&doctype=json&type=AUTO&i=family";
        RequestUtil requestUtil = new RequestUtil();
        Document doc = requestUtil.getDocument(url);
        System.out.println(doc.body());
    }

}
