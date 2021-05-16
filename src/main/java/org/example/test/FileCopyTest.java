package org.example.test;

import java.io.*;

/**
 * IO操作
 * 实现复制文件
 */
public class FileCopyTest {

    public static void main(String[] args) throws IOException {
        InputStream is =  new FileInputStream("D:\\作业们\\somephoto\\h.png");//输入流
        FileOutputStream fos = new FileOutputStream("D://作业们//somephoto//h1.png");//输出流
        byte[] bytes = new byte[1024];//1024个字节=1024kb
        int len;
        while ((len = is.read(bytes))!=-1){
            fos.write(bytes,0,len);
        }
        //关闭流
        is.close();
        fos.close();
    }

}
