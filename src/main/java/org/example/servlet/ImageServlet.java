package org.example.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.dao.ImageDAO;
import org.example.exception.AppException;
import org.example.model.Image;
import org.example.util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * servlet开发的步骤：
 *   1.注解
 *   2.继承HttpServlet
 *   3.重写doXXX
 */
//todo:尝试这里的路径修改
@WebServlet("/image")
@MultipartConfig
public class ImageServlet extends HttpServlet {

//    public static final String IMAGE_DIR = "D://TMP";
public static final String IMAGE_DIR = "/root/tmp";
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");//响应体数据类型
        //构造要返回的响应体信息---也可以创建一个类
        Map<String,Object> map = new HashMap<>();
        try {

            //1.解析请求数据：解析formdata类型---getPart
            //解析formdata类型---getPart
            Part p = req.getPart("uploadImage");
            //todo:API文档
            long size = p.getSize();//获取上传的文件大小
            String contentType = p.getContentType();//不是HTTP头信息，而是指获取每个part的数据格式
            String name = p.getSubmittedFileName();//获取上传的文件名字


            //图片上传时间，数据库保存的是字符串，所以用日期格式化为字符串
            Date date = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//date格式化
            String uploadTime = df.format(date);//转成字符串

            //获取part（上传图片文件）的输入流
            InputStream is = p.getInputStream();//获取上传文件的输入流(数据)
            String md5 = DigestUtils.md5Hex(is);//根据输入流转md5校验码

            //【经分析：如果已上传该图片（数据库中存在相同的md5），就不能插入数据和保存本地】
            int num = ImageDAO.queryCount(md5);
            if(num >= 1){
                throw new AppException("上传图片重复");
            }

            //2.根据请求数据完成业务处理

            // 【2-1】保存上传图片为服务端本地文件
            //问题1.这个路径下有相同的图片(所以先在数据库查)
            //问题2.文件名一样，但md5不一样 (下面的代码可以解决)
            p.write(IMAGE_DIR+"/"+md5);



            // 【2-2】图片信息保存在数据库（后续查询图片列表接口要用）
            //插入数据操作，字段太多，最好是把字段转为对象的属性
            Image image = new Image();
            image.setImageName(name);
            image.setContentType(contentType);
            image.setSize(size);
            image.setUploadTime(uploadTime);
            image.setMd5(md5);
            image.setPath("/"+md5);
            int n = ImageDAO.insert(image);
            map.put("ok",true);
        }catch (Exception e){//只要发生异常，就返回500
            e.printStackTrace();
            map.put("ok",false);
            if(e instanceof AppException){
                map.put("msg",e.getMessage());//错误信息
            }else {
                map.put("msg","未知错误，请联系管理员");
            }
//            resp.setStatus(500);
            //报错可以往body中写错误信息，如果没有，就只能检查后台日志信息
        }

        //3.返回数据
        //因为前端只是接收一个状态码，然后执行app.getImages()/error代码，所有这里不用谢这些
//        ObjectMapper m = new ObjectMapper();
//        Map<String,Object> data = new HashMap<>();
//        data.put("size",size/1024);//除1024
//        data.put("contentType",contentType);
//        data.put("name",name);
//        String json = m.writeValueAsString(data);
//        resp.getWriter().println(json);
//        ok(resp);
        String s = Util.serialize(map);
        resp.getWriter().println(s);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        //【1】.解析请求数据
        //【2】.根据请求数据完成业务处理
        //【3】.返回响应数据

        String id = req.getParameter("imageId");

        int state = -1;
        String msg = "";


            Object o = null;//定义一个往输出流要打印的东西
            if (id == null) { //查询所有图片 o = List<Image>
                //这里是一个list
                o = ImageDAO.queryAll();
            } else { //查询指定id的一个图片 o = image对象
                //这里是一个image对象
                o = ImageDAO.queryOne(Integer.parseInt(id));
            }

            //写入body
            String json = Util.serialize(o);
            resp.getWriter().println(json);//json放进输出流

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        //【1】.解析请求数据
        //【2】.根据请求数据完成业务处理
        //【3】.返回响应数据

        String id = req.getParameter("imageId");
        //TODO:数据库删除和本地硬盘删除 使用事务保证ACID
        //数据库根据id删除图片
        Image image = ImageDAO.queryOne(Integer.parseInt(id));
        int n = ImageDAO.delete(Integer.parseInt(id));
        //本地硬盘删除图片文件
        String path = IMAGE_DIR+image.getPath();
        File f = new File(path);//本地文件变成Java对象
        f.delete();
        ok(resp);


    }

    public static void ok(HttpServletResponse resp) throws IOException {
        resp.getWriter().println("{\"ok\":true}");
    }
}
