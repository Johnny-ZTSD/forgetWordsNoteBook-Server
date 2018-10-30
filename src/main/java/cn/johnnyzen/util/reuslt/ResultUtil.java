package cn.johnnyzen.util.reuslt;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/9/30  21:03:59
 * @Description: ...
 */

public class ResultUtil {

    public static Result success(Object object){
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS);
        result.setMessage("request success!");
        result.setData(object);
        return result;
    }

    public static Result success(String message){
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    public static Result success(String message, Object object){
        Result result = new Result();
        result.setCode(ResultCode.SUCCESS);
        result.setMessage(message);
        result.setData(object);
        return result;
    }

    public static Result success() {
        return success(null);
    }

    public static Result error(ResultCode code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }
}
