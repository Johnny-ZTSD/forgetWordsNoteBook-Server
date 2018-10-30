package cn.johnnyzen.util.request;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/10/7  15:51:28
 * @Description: ...
 */

public class RequestUtil {
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
}
