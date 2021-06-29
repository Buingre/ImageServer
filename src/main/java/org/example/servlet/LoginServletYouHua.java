package org.example.servlet;

import org.example.exception.AppException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

public class LoginServletYouHua extends AbstractBaseServlet{

    @Override
    protected Object process(HttpServletRequest req) throws Exception {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        //【没写真正的数据库操作，仅演示】
        List<String> usernames = Arrays.asList("a","b","c");//模拟数据库中的帐号

            if(!usernames.contains(username)){
                throw new AppException("用户不存在");
            } else if(!password.equals("123")){
                throw new AppException("账号或密码错误");
            }
            //用户名和密码校验成功：帐号为a、b、c,密码123
            //【Session】
            HttpSession session = req.getSession();
            session.setAttribute("username",username);//真正情况：要保存用户所有信息
        return null;
    }
}
