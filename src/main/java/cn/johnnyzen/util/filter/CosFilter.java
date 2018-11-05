package cn.johnnyzen.util.filter;

import cn.johnnyzen.user.filter.LoginFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/10/7  17:00:46
 * @Description: 跨域过滤器
 */

//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)//控制过滤器的级别
public class CosFilter implements Filter {
    private static final Logger logger = Logger.getLogger(LoginFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("[CosFilter.init()] ...");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws
            IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        HttpServletRequest reqs = (HttpServletRequest) req;
        response.setHeader("Access-Control-Allow-Origin", reqs.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true"); //允许跨域
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type,X-Requested-With");
        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("JSESSIONID", ((HttpServletRequest) req).getSession().getId());
        if ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) req).getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            logger.info("[CosFilter.doFilter()] ...");
            filterChain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
    }
}