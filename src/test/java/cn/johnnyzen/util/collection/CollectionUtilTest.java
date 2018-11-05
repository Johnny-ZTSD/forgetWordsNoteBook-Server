package cn.johnnyzen.util.collection;

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
}
