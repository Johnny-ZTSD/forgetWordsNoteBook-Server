package cn.johnnyzen.user;

import cn.johnnyzen.util.code.CodeUtil;
import cn.johnnyzen.util.reuslt.Result;
import cn.johnnyzen.util.reuslt.ResultCode;
import cn.johnnyzen.util.reuslt.ResultUtil;
import com.sun.org.apache.regexp.internal.REUtil;
import org.eclipse.jetty.client.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/10/6  20:02:26
 * @Description: ...
 */
@Controller
public class UserController {
    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    private UserService userService;

    @Value("${file.staticRealRootPath}")
    private String staticRealRootPath;

    /*
     * @param token
     *          登陆校验过滤器校验登陆时，由于上传文件的form-data形式无法读取token；
     *          过滤器会从header中获取token；
     *          也即提醒：本方法的特殊处在于，获取token需要从header中获取
     * @param logo
     * */
    @PostMapping(value = "/updateUserLogo/api")
    @ResponseBody
    public Result updateUserLogo(HttpServletRequest request,
                                 @RequestParam(value = "logo") MultipartFile logoFile
    ){
//        String filePath= null;
//        try {
//            filePath = ResourceUtils.getURL("classpath:").getPath().toString();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        //即 实际地址：publicAccesssRootPath + "/user/logo" + imgName,such as:C:/Users/千千寰宇/Desktop/public/user/logo/a.jpg
        int code=userService.updateUserLogoUrl(request,logoFile, staticRealRootPath + "/user/logo/");
        switch (code){
            case 1:
                return ResultUtil.success("上传头像成功！");
            case -1:
                return ResultUtil.error(ResultCode.FAIL,"文件类型为空，请重新选择");
            case -2:
                return ResultUtil.error(ResultCode.FAIL,"文件类型不符合，请重新选择");
            default:
                return ResultUtil.error(ResultCode.FAIL,"上传失败，原因未知");
        }
        //return ResultUtil.error(ResultCode.FAIL, "[UserController.updateUserLogo] 接口暂未开发");
    }

    @PostMapping(value = "/updateUserInfo/api")
    @ResponseBody
    public Result updateUserInfo(HttpServletRequest request,
                                 @RequestParam(value = "username",required = false) String username,
                                 @RequestParam(value = "sex",required = false) String sex,
                                 @RequestParam(value = "token",required = true) String token){
        int code=userService.upudateUserInfo(request,username,sex);
        switch (code){
            case 1:
                return ResultUtil.success("信息更新成功！");
            case -1:
                return ResultUtil.error(ResultCode.FAIL,"用户名格式不正确，请重新输入！");
            default:
                return ResultUtil.error(ResultCode.FAIL,"更新失败，原因未知");
        }
    }


    /*
     * 重置密码
     *
     * 注：必须带token更改密码
     */
    @PostMapping(value = "/resetPassword/api")
    @ResponseBody
    public Result resetPassword(HttpServletRequest request,
                                @RequestParam(value = "token",required = true) String token,
                                @RequestParam(value = "oldPswd",required = true) String oldPswd,
                                @RequestParam(value = "newPswd",required = true) String newPswd){
        String logPrefix = "[UserController.resetPassword()] ";
        int result = userService.resetPassword(request,token,oldPswd,newPswd);
        switch(result){
            case 1:
                return ResultUtil.success("密码重置成功~");
            case -1:
                return ResultUtil.error(ResultCode.NOT_LOGIN_NO_ACCESS,"重置密码失败,原因：未登录,无权限");
            case -2:
                return ResultUtil.error(ResultCode.FAIL, "密码长度(6至18位数)不符合要求，重置失败。");
            case -3:
                return ResultUtil.error(ResultCode.FAIL, "原密码输入错误，重置失败。");
            case -4:
                return ResultUtil.error(ResultCode.FAIL, "新密码与原密码一致，重置失败。");
            default:
                return ResultUtil.error(ResultCode.FAIL, "重置失败，原因未知。");
        }
    }

    /* 退出登陆 */
    @RequestMapping(value = "/exitLogin/api")
    @ResponseBody
    public Result exitLogin(HttpServletRequest request,
                            @RequestParam(value = "token",required = true) String token){
        int handle = userService.exitLogin(request,token);
        if(handle == 1){
            return ResultUtil.success("退出成功!");
        } else {
            return ResultUtil.error(ResultCode.FAIL, "未曾登陆，退出失败.");
        }
    }

    /*
     * 从session中获取登陆用户基本信息
     * token参数的检查，已移交LoginFilter
     * */
    @RequestMapping("/viewLoginUserInfo/api")
    @ResponseBody
    public Result viewLoginUserInfo(HttpServletRequest request,
                                    @RequestParam(value = "token", required = true) String token){
        User user = null;
        logger.info("[UserController.viewLoginUserInfo] token" + token);
        user = userService.findOneByLoginUsersMap(request);
        if(user != null){
//            user.setPassword("");
            return ResultUtil.success("用户信息获取成功！", user);
        } else {//未曾登录
            return ResultUtil.error(ResultCode.NOT_LOGIN_NO_ACCESS, "用户信息获取失败！");
        }
    }

    /*
     * 1.初次登陆[数据库查询 username|email + password]
     * 2.非初次登陆，带token登陆
     * 3.非初次登陆，带username|email登陆
     *
     * 登陆成功：返回 用户基本信息
     * 登陆失败：返回 空
     *
     * 注：登陆校验工作已移交LoginFilter，无需关注此点.
     **/
    @PostMapping("/login/api")
    @ResponseBody
    public Result login(HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestParam(value = "token", required = false) String token,
                        @RequestParam(value = "username",required = false) String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "email",required = false) String email){
        String sessionId = request.getSession().getId();
//        response.addHeader("JSESSIONIDLOGINAPI", sessionId);
        if(userService.loginCheck(request) == 5){ //已登录过,并刷新活跃时间
            User user = null;
            user = userService.findOneByLoginUsersMap(request);
            if(user != null){
                user.setSessionId(sessionId);
                return ResultUtil.success("已登录", user);
            }
        }
        User user = null;
        user = userService.login(request.getSession(), username,password, email);
        if(user != null){
//            user.setPassword(""); //注：返回给前端时，密码屏蔽掉
            user.setSessionId(sessionId);
            return  ResultUtil.success("登陆成功!", user);
        } else {
            return  ResultUtil.error(ResultCode.USERNAME_ERROR_OR_PASSWORD_ERROR, "用户名、邮箱或密码错误！");
        }
    }

    /*
     * 用户注册
     * 1.防止：email|username重复注册
     **/
    @PostMapping("/register/api")
    @ResponseBody
    public Result register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("email") String email
    ){
        int state = userService.register(username, password, email);
        if(state == 1){
            return ResultUtil.success("注册中，请查看注册邮箱(" + email + ")确认。");
        } else if(state == -1){
            return ResultUtil.error(
                    ResultCode.FAIL,
                    "注册失败，邮箱格式不正确！");
        }  else if(state == -2){
            return ResultUtil.error(
                    ResultCode.FAIL,
                    "用户注册失败，该 email 已被注册！");
        }  else if(state == -3){
            return ResultUtil.error(
                    ResultCode.FAIL,
                    "用户注册失败，该 username 已被注册！");
        } else {
            return ResultUtil.error(
                    ResultCode.FAIL,
                    "注册失败，未知异常。[错误代码：ERROR-USER999]");
        }
    }

    /*  激活来自于用户邮箱的html，一般不使用api/json
     *  1.防止：用户多次激活，激活后销毁激活码
     * */
    @RequestMapping(value={"/register-activate"})
    public String registerActivate(
            HttpSession session,
            @RequestParam(value = "code",required = true) String code){
        int handleState = userService.registerActivate(session, code);
        if(handleState == 1){
            return "register/activate_success";
        } else {
            return "register/activate_fail";
        }
    }
}
