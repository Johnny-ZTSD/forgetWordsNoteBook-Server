package cn.johnnyzen.newword;

import cn.johnnyzen.word.ThirdWordResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/3  10:32:40
 * @Description: ...
 */

public class TranslateTest {
    @Test
    public void testTranlate() throws IOException {
        String text = "{\"type\":\"EN2ZH_CN\",\"errorCode\":0,\"elapsedTime\":0,\"translateResult\":[[{\"src\":\"family\",\"tgt\":\"家庭\"}]]}";

        //json to obejct
        ObjectMapper mapper = new ObjectMapper();
        ThirdWordResult thirdWordResult = mapper.readValue(text, ThirdWordResult.class);

        System.out.println(thirdWordResult.toString());
        System.out.println(text);
    }

    @Test
    public void test001(){
        System.out.println("result:" + (1 == 1 && 2 == 2));
    }
}
