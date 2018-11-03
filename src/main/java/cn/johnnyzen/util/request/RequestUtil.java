package cn.johnnyzen.util.request;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/10/7  15:51:28
 * @Description: ...
 */
 /* @Component:
  *      将本Bean置入Spring容器中，形成绑定
  *      把普通pojo实例化到spring容器中，相当于配置文件中的<bean id="" class=""/>
  *      定义Spring管理Bean.
  */
@Component
public class RequestUtil {
    @Autowired //从容器中取值，对类成员变量、方法及构造函数进行标注，完成自动装配的工作。
    private RequestProperties requestProperties;

    /**
     * 判断uri是否是api请求(返回json)
     *  即：路径中是否包含：/api/
     * @Author: xxxxx
     * @Description: 是否需要过滤
     * @Date: 2018-03-12 13:20:54
     * @param uri
     */
    public static boolean isApiURI(String uri){
        String [] dirs = uri.split("/");
        for(String dir:dirs){
            if(dir.equalsIgnoreCase("API")){
                return true;//API URI
            }
        }
        return false;
    }

    public Document getDocument(String url) throws IOException {
        Document doc = null;
        doc = Jsoup.connect(url)
                .header("Cache-Control", requestProperties.getCacheControl())
                .header("Connection", requestProperties.getConnection())
                .header("Accept", requestProperties.getAccept())
                .header("Accept-Encoding", requestProperties.getAcceptEncoding())
                .header("Accept-Language", requestProperties.getAcceptLanguage())
                .header("Set-Cookie", requestProperties.getCookie())
                .userAgent(requestProperties.getUserAgent())
                .get();
        return doc;
    }
}
