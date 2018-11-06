package cn.johnnyzen.util.collection;

import org.hibernate.mapping.Collection;

import java.util.Iterator;
import java.util.List;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/4  22:08:16
 * @Description: 集合工具类
 */

public class CollectionUtil {

    public static <T> boolean isItemInList(List<T> list, T item){
        Iterator iterator = null;
        iterator = list.iterator();
        while(iterator.hasNext()){
            if(iterator.next().equals(item)){
                return true;
            }
        }
        return false;
    }

    /* 必须满足 一个字符串是另一个字符串的子集，如果不是子集，结果将出错 */
    public static String minus(String strA, String strB){
        int compare = strA.compareTo(strB);
        System.out.println("compare:" + (compare) + " <" + strA + " : " + strB + ">");
        int lenA = strA.length();
        int lenB = strB.length();
        if(compare == 0){ //相等
            return "";
        } else if(compare < 0){//A更小或者更短
            return strB.substring(lenB + compare);
        } else {//B更长或者更大
            return strA.substring(lenA - compare);
        }
    }
}
