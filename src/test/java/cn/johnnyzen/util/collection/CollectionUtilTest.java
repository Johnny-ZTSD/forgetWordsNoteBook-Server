package cn.johnnyzen.util.collection;

import cn.johnnyzen.user.User;
import org.junit.Test;

import java.util.Arrays;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/4  22:13:44
 * @Description: ...
 */

public class CollectionUtilTest {
    @Test
    public void testIsItemInList(){
        String[] list = {"apple", "test", "hi","word","world","child"};
        String itemA = "word";
        String itemB = "will";
        System.out.println(CollectionUtil.isItemInList(Arrays.asList(list),itemA));//true
        System.out.println(CollectionUtil.isItemInList(Arrays.asList(list),itemB));//false
    }

    @Test
    public void testIsOnlyContainsChineseAndNumberAndAlpha(){
//        String str = "都不超过ni78hao";//true
//        String str = "！都不超过ni78hao";//false
        String str = "686#￥%……ni78hao";//false
        System.out.println(CollectionUtil.isOnlyContainsChineseAndNumberAndEnglishAlpha(str));
    }

    @Test
    public void testIsOnlyContainsNumberAndEnglishAlpha(){
        String str1 = "35535SDFxcsdas";
        String str2 = "v!~sff234";
        String str3 = "";
        String str4 = "你好";
        String str5 = null;
        System.out.println(str1 + ":" + CollectionUtil.isOnlyContainsNumberAndEnglishAlpha(str1));
        System.out.println(str2 + ":" + CollectionUtil.isOnlyContainsNumberAndEnglishAlpha(str2));
        System.out.println(str3 + ":" + CollectionUtil.isOnlyContainsNumberAndEnglishAlpha(str3));
        System.out.println(str4 + ":" + CollectionUtil.isOnlyContainsNumberAndEnglishAlpha(str4));
        System.out.println(str5+ ":" + CollectionUtil.isOnlyContainsNumberAndEnglishAlpha(str5));
    }

    @Test
    public void testIsPhoneNumber(){
        String phone = "15202843189";
        String phone2 = "242354235235";
        System.out.println(phone + ":" + CollectionUtil.isPhoneNumberOfChina((phone)));//
        System.out.println(CollectionUtil.isPhoneNumberOfChina(phone2));//false
    }

    @Test
    public void testIsLegalEmail(){
//        String email = "3535@rd.com";//4:right
//        String email = "352@.rd.com";//2:right
        String email = "353t75787578478484785738254378254738572@rd.com";//4:right
        System.out.println("email's format is :" + CollectionUtil.isLegalEmail(email));
    }
}
