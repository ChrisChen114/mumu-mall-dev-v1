package com.imooc.malldevv1.filter;

import com.imooc.malldevv1.common.ApiRestResponse;
import com.imooc.malldevv1.common.Constant;
import com.imooc.malldevv1.exception.ImoocMallExceptionEnum;
import com.imooc.malldevv1.model.pojo.Category;
import com.imooc.malldevv1.model.pojo.User;
import com.imooc.malldevv1.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Admin过滤器
 * 2022-08-23 创建
 * 来自视频5-8 统一校验管理员身份
 *
 * implements Filter别导入错了，一定是import javax.servlet.*;
 * Admin过滤器写好后，还需要配置才能使用。配置在config包中，增加AdminFilterConfig
 */

public class AdminFilter implements Filter {

    @Autowired
    UserService userService;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }


    @Override
    public void destroy() {

    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //入参已经固定，如何拿到session呢？ 从request可以拿到
        HttpServletRequest currentRequest = (HttpServletRequest) request;
        HttpSession session = currentRequest.getSession();

        User currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_DEV_V1_USER);
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
        //校验是不是管理员
        if (userService.checkAdminRole(currentUser)){
            //放行
            chain.doFilter(request,response);//将入参继续往下传递
        }else{
            //return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN);
            PrintWriter out = new HttpServletResponseWrapper((HttpServletResponse) response).getWriter();
            out.write("{\n" +
                    "    \"status\": 10009,\n" +
                    "    \"msg\": \"NEED_ADMIN\",\n" +
                    "    \"data\": null\n" +
                    "}");
            out.flush();//把缓冲区的内容强制的写出
            out.close();//关闭
            //不需要加return，因为方法到这里就结束了
        }
    }
}
