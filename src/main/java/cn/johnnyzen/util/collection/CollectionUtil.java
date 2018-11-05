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
}
