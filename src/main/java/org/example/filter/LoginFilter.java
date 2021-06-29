package org.example.filter;

import org.example.util.Util;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户会话的统一管理：《基于过滤器实现》
 * 可以统一的进行权限管理、过滤敏感信息等等
 */
// /*表示所有的路径都要校验
@WebFilter("/*")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public static boolean isLogin(HttpServletRequest req){
        HttpSession session = req.getSession(false);

        if(session != null){
            //获取的键为登陆时保存在session中的键
            Object username = session.getAttribute("userinfo");
            if(username !=null){
                return true;//已登录的用户访问
            }
        }
        return false;//未登录的用户访问
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        //获取请求路径，判断是否为敏感资源，如果是，校验session
        String uri = req.getServletPath();//应用上下文路径后边的服务路径
        //前端敏感资源：/index.html
        //后端敏感资源：/image, /imageShow
        if(uri.equals("/index.html") && !isLogin(req)){
            //前端敏感资源重定向到登录页面
            //todo:真实的代码：要写绝对路径【通过request对象，获取绝对路径每个部分】
//            req.getScheme();//http
//            req.getServerName();//ip或域名
//            req.getServerPort();//port
//            req.getContextPath();//应用上下文路径
            resp.sendRedirect("reglogin.html");
            return;
        }else if((uri.equals("/image")||uri.equals("/imageShow"))&& !isLogin(req)){
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            //后端敏感资源 设置401，返回json数据
            resp.setStatus(401);
            Map<String,Object> map = new HashMap<>();
            map.put("ok",false);
            map.put("msg","用户未登录，不允许访问");
            String json = Util.serialize(map);
            resp.getWriter().println(map);
            return;
        }

        //前后端敏感资源但已登录 或开放的资源，允许访问
        chain.doFilter(request, response);


    }

    @Override
    public void destroy() {

    }
}
