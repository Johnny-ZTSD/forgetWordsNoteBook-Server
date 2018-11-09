package cn.johnnyzen.user.filter;

import cn.johnnyzen.user.UserService;
import cn.johnnyzen.util.request.RequestUtil;
import cn.johnnyzen.util.reuslt.Result;
import cn.johnnyzen.util.reuslt.ResultCode;
import cn.johnnyzen.util.reuslt.ResultUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/10/7  02:04:54
 * @Description:
 *  在springboot添加过滤器有两种方式：
 *　  1、通过创建FilterRegistrationBean的方式
 *      （建议使用此种方式，统一管理，且通过注解的方式若不是本地调试，如果在filter中需要增加cookie可能会存在写不进前端情况）
 *　  2、通过注解@WebFilter + @ServletComponentScan的方式
 *    3、参考文档 https://blog.csdn.net/weixin_37891479/article/details/79527641
 *               https://blog.csdn.net/rt940910a/article/details/79351510
 */
@WebFilter(filterName = "loginFilter",urlPatterns = {"/*"})
@Configuration //@Configuration中所有带@Bean注解的方法都会被动态代理，调用该方法返回的都是同一个实例。
               //@Configuration注解本质上还是@Component
@Order(value = 2)
public class LoginFilter implements Filter {
    private static final Logger logger = Logger.getLogger(LoginFilter.class.getName());

    //日志前缀字符串,方便通过日志定位程序
    private static String logPrefix = null;

    private UserService userService; //如无特别配置，Spring IOC无法主动注入Bean到Filter或者拦截器

    /**
     * 被过滤掉<不阻拦处理>URL虚拟路径
     *  不需要登录就可以访问的路径(比如:注册登录等)
     *      注:仅仅支持单"/"级路径
     */
    String[] includeUrls = new String[]{"/static","/public","/login","/register","/register-activate"};

    public LoginFilter(){
        userService = new UserService();
    }

    /**
     * 过滤器执行方法
     *   算法：
     *      0.判断请求[header/parameter]中是否带token口令（验证串）:
     *          如果header与parameter中均含有token：token存在二义性故只能上传一个token，通过过滤器失败，本方法执行结束。
     *      1.判断请求URL是否在过滤路径内
     *          如果是：直接通过过滤器，本方法执行结束
     *      2.判断是否已经登陆loginCheck():(执行本阶段时，当前路径一定属于被阻拦路径)
     *          未登录：响应客户端未登录，通过过滤器失败，本方法执行结束
     *          已登录：允许通过过滤器，本方法执行结束
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logPrefix = "[LoginFilter.doFilter()] ";
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String uri = request.getRequestURI();
        //output token in request.header to log for find defect.
        String headerToken = null;
        //output token in request.paramter to log for find defect.
        String parameterToken = null;
        Result result = null;
        String message = logPrefix; // just init

        //return JsessionID:for weixin mini program
        response.addHeader("JSESSIONID", ((HttpServletRequest) servletRequest).getSession().getId());
        logger.info(logPrefix + "FILTER URL:" + request.getRequestURL());
        //output JSESSIONID in log for find defect.
        logger.info(logPrefix + "JSESSIONID in request.session:" + request.getSession().getId());
        //get values of token in header and parameter
        headerToken = request.getHeader("token");
        if(headerToken != null)
            logger.info(logPrefix + "token in header:" + headerToken);
        parameterToken = request.getParameter("token");
        if(parameterToken != null){
            logger.info(logPrefix + "token in request:" + parameterToken);
        }

        //0.only pick/upload one in header or paramter
        if(headerToken != null && parameterToken != null){
            logger.info(logPrefix + "fail to pass login filter,because token is uploaded one in header or paramter");
            message = "操作失败，token多处冗余上传，存在二义性！";
            result = ResultUtil.error(ResultCode.FAIL, message);
            handlePassFilterFailedRequest(request, response, uri, result);
            return; //否则会继续执行后续代码
        }
        /**
         * 1.check whether this url need filter to block.
         *  [是否需要过滤器阻拦<后续校验>]
         */
        boolean needFilter = this.isNeedFilter(uri);
        if (!needFilter) { //不需要过滤直接传给下一个过滤器
            logger.info("[LoginFilter.doFilter()] uri:" + uri + " no need filter");
            filterChain.doFilter(servletRequest, servletResponse);
            return;//否则会继续执行后续代码
        }
        /**
         * 2.[need filter]检查登陆check login
         *  注：session中包含user对象,则是登录状态
         */
        int loginState = userService.loginCheck(request);
        if(loginState == 6){
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
            message = "操作失败，该账户被锁定或者未激活。]";
        } else if(loginState == 5){
            message = "操作失败，因登陆已超有效时间而失效。]";
        } else { //unknown
            message = "操作失败，原因未知。]";
        }
        logger.info("[LoginFilter.doFilter()] uri:" + uri + " pass filter failed!");
        result = ResultUtil.error(ResultCode.NOT_LOGIN_NO_ACCESS, message);
        handlePassFilterFailedRequest(request,response,uri,result);
        return;
    }

    /**
     * 处理通过过滤器失败的请求
     *  判断URI属于api请求his网页请求
     *      如果是前者：
     *          需要传递可被json化的result实体对象
     *          返回json格式
     *      如果是后者：
     *          responseContent可设置为null
     *          返回html格式
     * @param request
     * @param response
     * @param uri
     * @param result
     */
    public void handlePassFilterFailedRequest(HttpServletRequest request, HttpServletResponse response, String uri, Result result) throws IOException {
        //判断URI类型，如果为API请求，返回JSON，反之重定向到login模板页
        if(RequestUtil.isApiURI(uri)){
            ObjectMapper mapper = new ObjectMapper(); //jackson:object to json-string
            response.setHeader("content-type", "application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(
                    mapper.writeValueAsString(
                            result));
            return;
        } else {//重定向到登录页(需要在static文件夹下建立此html文件)
            response.sendRedirect(request.getContextPath()+ "/static/user/login.html");
            return;
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