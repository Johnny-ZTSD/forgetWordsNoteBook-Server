package cn.johnnyzen.user.filter;

import cn.johnnyzen.user.User;
import cn.johnnyzen.user.UserService;
import cn.johnnyzen.util.request.RequestUtil;
import cn.johnnyzen.util.reuslt.ResultCode;
import cn.johnnyzen.util.reuslt.ResultUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpRequest;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/10/7  02:04:54
 * @Description:
    在springboot添加过滤器有两种方式：
　　  1、通过创建FilterRegistrationBean的方式
        （建议使用此种方式，统一管理，且通过注解的方式若不是本地调试，如果在filter中需要增加cookie可能会存在写不进前端情况）
　　  2、通过注解@WebFilter + @ServletComponentScan的方式
      3、参考文档 https://blog.csdn.net/weixin_37891479/article/details/79527641
                 https://blog.csdn.net/rt940910a/article/details/79351510
*/
@WebFilter(filterName = "loginFilter",urlPatterns = {"/*"})
@Configuration //@Configuration中所有带@Bean注解的方法都会被动态代理，调用该方法返回的都是同一个实例。
               //@Configuration注解本质上还是@Component
@Order(value = 2)
public class LoginFilter implements Filter {
    private static final Logger logger = Logger.getLogger(LoginFilter.class.getName());

    private UserService userService; //如无特别配置，Spring IOC无法主动注入Bean到Filter或者拦截器

    //不需要登录就可以访问的路径(比如:注册登录等)
    // 注:仅仅支持单"/"级路径
    String[] includeUrls = new String[]{"/login","/register","/register-activate"};

    public LoginFilter(){
        userService = new UserService();
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, IOException {
        String logPrefix = "[LoginFilter.doFilter()] ";
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String uri = request.getRequestURI();

        System.out.println(logPrefix + "FILTER URL:" + request.getRequestURL());
        //是否需要过滤
        boolean needFilter = this.isNeedFilter(uri);


        if (!needFilter) { //不需要过滤直接传给下一个过滤器
            logger.info("[LoginFilter.doFilter()] uri:" + uri + " no need filter");
            filterChain.doFilter(servletRequest, servletResponse);
        } else { //需要过滤器
            // session中包含user对象,则是登录状态

            int loginState = userService.loginCheck(request);
            String message = logPrefix;
            if(loginState == 5){
                message = "登陆成功，且时间有效~";
                logger.info("[LoginFilter.doFilter()] uri:" + uri + " pass filter");
                filterChain.doFilter(request, response);//通过filter
                return;
            } else if(loginState == 1){
                message = "操作失败，未曾登陆!";
            } else if(loginState == 2){
                message = "操作失败，请求参数[token]不全！";
            } else if(loginState == 3){
                message = "操作失败，token无效。";
            } else if(loginState == 4){
                message = "操作失败，因登陆已超有效时间而失效。]";
            } else { //unknown
                message = "操作失败，原因未知。]";
            }

            logger.info("[LoginFilter.doFilter()] uri:" + uri + " pass filter failed!");

            //判断URI类型，如果为API请求，返回JSON，反之重定向到login模板页
            if(RequestUtil.isApiURI(uri)){
                ObjectMapper mapper = new ObjectMapper(); //jackson:object to json-string

                response.setHeader("content-type", "application/json;charset=UTF-8");
                response.setCharacterEncoding("UTF-8");

                response.getWriter().write(
                        mapper.writeValueAsString(
                                ResultUtil.error(
                                        ResultCode.NOT_LOGIN_NO_ACCESS, message)));
                return;
            } else {//重定向到登录页(需要在static文件夹下建立此html文件)
                response.sendRedirect(request.getContextPath()+"/user/login.html");
                return;
            }
        }
    }

    /**
     * @Author: xxxxx
     * @Description: 是否需要过滤
     * @Date: 2018-03-12 13:20:54
     * @param uri
     */
    public boolean isNeedFilter(String uri) {

        for (String includeUrl : includeUrls) {
            String [] uriDirs = uri.split("/");
            for(String uriItem:uriDirs){
                if(includeUrl.equals("/" + uriItem)) {
                    return false;//不需要filter的
                }
            }
        }
        return true;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("[LoginFilter.init()] ...");
    }

    @Override
    public void destroy() {

    }
}