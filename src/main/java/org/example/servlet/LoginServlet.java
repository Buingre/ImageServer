package org.example.servlet;

import org.example.exception.AppException;
import org.example.util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        //返回的数据
        Map<String,Object> map = new HashMap<>();
        String username = req.getParameter("username");//解析请求数据
        String password = req.getParameter("password");
        //【没写真正的数据库操作，仅演示】
        List<String> usernames = Arrays.asList("a","b","c");//模拟数据库中的帐号
        try {
            if(!usernames.contains(username)){
                throw new AppException("用户不存在");
            } else if(!password.equals("123")){
                throw new AppException("账号或密码错误");
            }
            //用户名和密码校验成功：帐号为a、b、c,密码123
            //【Session】
            HttpSession session = req.getSession();
            session.setAttribute("username",username);//真正情况：要保存用户所有信息

            map.put("ok",true);
        } catch (Exception e){
            e.printStackTrace();
            map.put("ok",false);
            if(e instanceof AppException){
                map.put("msg",e.getMessage());
            }else {
                map.put("msg","未知错误，请联系管理员");
            }
        }
        String s = Util.serialize(map);
        resp.getWriter().println(s);
    }
}
