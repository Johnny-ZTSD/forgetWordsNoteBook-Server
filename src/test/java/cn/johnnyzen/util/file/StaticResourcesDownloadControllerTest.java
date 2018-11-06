package cn.johnnyzen.util.file;

import cn.johnnyzen.util.collection.CollectionUtil;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/6  15:33:15
 * @Description: ...
 */
public class StaticResourcesDownloadControllerTest {
//    @Test
    public void testMiunsOfStrings(){
        String strA = "35344/35345436/cxxfd";
        String strB = "35344/35345436";
        System.out.println(strA.compareTo(strB));
        System.out.println("minus:" + CollectionUtil.minus(strA, strB));
        System.out.println("minus:" + CollectionUtil.minus(strB, strA));
    }

    @Test
    public void test(){
        String fileName = "1.jpg";
        String realPath = "C:/Users/千千寰宇/Desktop/public/1.jpg";
        int index = realPath.indexOf(fileName);
        System.out.println(realPath.substring(0,index));
        realPath.substring(0, realPath.indexOf(fileName));
    }

//    @Test
    public void testStr(){
        String str = "32535";
        System.out.println(str.substring(1));
    }

//    @Test
    public void testUrlAccessPathForDownloadFile(){
        //demo request url:http://localhost:8080/news/main/list.jsp
        //contextPath:request.getContextPath():/news
        //request.getServletPath():/main/list.jsp
        //request.getRequestURI():/news/main/list.jsp
        //request.getRealPath("/"):F:\Tomcat 6.0\webapps\news\test
//        HttpServletRequest request = null;
//        String requestURI = "/MedicineMS/login";//request.getRequestURI()
        String requestUrl = "http://localhost:8080/forget-words-notebook/public/user/logo/common.jpg";
        String requestURI = "/forget-words-notebook/public/user/logo/common.jpg";
        //        String realPath = "C://Users/千千寰宇/Desktop/public/user/logo/common.jpg";
        String fileName = "common.jpg";
        String contextPath = "/forget-words-notebook";
        String staticPath = "/public";
        String accessPath = "";
        String servletPath = "/public/user/logo/common.jpg";

//        String tmp = CollectionUtil.minus(requestURI, contextPath);
//        System.out.println("tmp:" + tmp);
//        tmp += contextPath + staticPath;
        System.out.println(CollectionUtil.minus(servletPath,staticPath));
//        System.out.println("aim:" + CollectionUtil.minus(fileName, ));
//        System.out.println(minus(new String [] {requestUrl},new String [] {fileName})[0]);
//        request.getContextPath()
    }

}
