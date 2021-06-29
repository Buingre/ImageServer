package org.example.servlet;

import org.example.exception.AppException;
import org.example.util.Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 响应类型是“application/json”才继承这个类
 */
public abstract class AbstractBaseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        //返回的数据
        Map<String,Object> map = new HashMap<>();


        try {
            Object o = process(req);
            if(o == null){
                map.put("ok",true);
            }else {
                resp.getWriter().println(Util.serialize(o));

            }

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

    protected abstract Object process(HttpServletRequest req) throws Exception;

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
