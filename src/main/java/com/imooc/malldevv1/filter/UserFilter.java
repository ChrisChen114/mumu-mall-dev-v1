package com.imooc.malldevv1.filter;

import com.imooc.malldevv1.common.Constant;
import com.imooc.malldevv1.model.pojo.User;
import com.imooc.malldevv1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * User用户过滤器
 * 2022-08-26 创建
 * 来自视频7-2 用户过滤器
 *
 * implements Filter别导入错了，一定是import javax.servlet.*;
 * User过滤器写好后，还需要配置才能使用。配置在config包中，增加UserFilterConfig
 */

public class UserFilter implements Filter {

    public static User currentUser;

    @Autowired
    UserService userService;

    /**
     *
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    /**
     *
     */
    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    /**
     * 判断有没有登录
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request1 = (HttpServletRequest) request;
        HttpSession session = ((HttpServletRequest) request).getSession();
        currentUser = (User)session.getAttribute(Constant.IMOOC_MALL_DEV_V1_USER);
        if (currentUser == null){
            //但是返回值是void，如何抛出异常呢？使用writer可以达到这个效果
            //return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) response).getWriter();
            out.write("{\n" +
                    "    \"status\": 10007,\n" +
                    "    \"msg\": \"NEED_LOGIN\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();//把缓冲区的内容强制的写出
            out.close();//关闭
            return;//结束；不会进入后续的filter过滤器和controller层
        }
        chain.doFilter(request,response);

    }

}
