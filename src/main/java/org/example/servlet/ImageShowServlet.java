package org.example.servlet;

import org.example.dao.ImageDAO;
import org.example.model.Image;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@WebServlet("/imageShow")
public class ImageShowServlet extends HttpServlet {

    //【白名单--防盗链】
    private static final Set<String> whiteList = new HashSet<>();
    static {//白名单允许获取图片内容：还可以设计为白名单➕黑名单的方式
//        whiteList.add("http://localhost:8080/image_server/");
//        whiteList.add("http://localhost:8080/image_server/index.html");

        whiteList.add("http://82.156.227.58:8080/image_server/");
        whiteList.add("http://82.156.227.58:8080/image_server/index.html");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String referer = req.getHeader("Referer");
        if(!whiteList.contains(referer)){//白名单不包含当前请求的Referer
            resp.setStatus(403);//还可以设置响应体数据

            return;
        }
//        【1】.解析请求数据：imageId
        String id = req.getParameter("imageId");
//        【2】.根据请求数据完成业务处理：（1）根据id查图片path字段 （2）通过path找本地图片文件
        Image image = ImageDAO.queryOne(Integer.parseInt(id));
        //图片是以二进制数据放在body，所以要指定Content-Type
        resp.setContentType(image.getContentType());
        String path = ImageServlet.IMAGE_DIR + image.getPath();//本地图片的绝对路径
        //io输入流读文件
        FileInputStream fis = new FileInputStream(path);
//        【3】.返回响应数据：服务器本地图片的二进制路径
        OutputStream os= resp.getOutputStream();//得到输出流，输出流都是输出到body
        byte[] bytes = new byte[1024*8];
        int len;
        while ((len = fis.read(bytes))!=-1){//输入流读，读到字节数组里.没有读到-1说明读的都是有内容的
            os.write(bytes,0,len);//有可能没读满
        }
        os.flush();//刷新缓冲区
        //释放资源
        fis.close();
        os.close();


    }
}
