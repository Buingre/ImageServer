package org.example.dao;

import org.example.model.Image;
import org.example.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImageDAO {

    /**
     * 查询操作
     * @param md5
     * @return
     */
    public static int queryCount(String md5) {
//        try {
//        1.获取数据库链接 Connection
//        2.获取操作命令对象 Statement (connection + sql)
//        3.执行SQL：执行前替换占位符
//        4.如果是查询语句，需要处理结果集 ResultSet
//         }catch(..) {..}
//        finally {
//        5.释放资源
//         }
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
           connection = Util.getConnection();
           String sql =
                   "select count(0) c from image_table where md5=? ";
           ps = connection.prepareStatement(sql);
           ps.setString(1,md5);
           rs = ps.executeQuery();
           rs.next();//指向下一行（rs初始指向的为null）
           return rs.getInt("c");

        }catch (SQLException e) {
            //变运行时异常
            throw new RuntimeException("查询上传图片MD5出错："+md5,e);
        } finally {
            Util.close(connection,ps,rs);
        }



    }

    /**
     * 插入操作
     * @param image
     * @return
     */
    public static int insert(Image image) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = Util.getConnection();

            String sql = "insert into image_table values(null,?,?,?,?,?,?)";

            ps = connection.prepareStatement(sql);

            ps.setString(1,image.getImageName());
            ps.setLong(2,image.getSize());
            ps.setString(3,image.getUploadTime());
            ps.setString(4,image.getMd5());
            ps.setString(5,image.getContentType());
            ps.setString(6,image.getPath());

            return ps.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException("新增上传图片出错 ",e);
        } finally {
            Util.close(connection,ps);
        }




    }

    /**
     * 查询所有的图片--->展示
     * @return
     */
    public static List<Image> queryAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = Util.getConnection();
            String sql = "select * from image_table";//一般不写*  要罗列出来呢
            statement = connection.prepareStatement(sql);
            resultSet = statement.executeQuery();

            List<Image> list = new ArrayList<>();
            while (resultSet.next()){
                Image image = new Image();
                image.setImageId(resultSet.getInt("image_id"));
                image.setImageName(resultSet.getString("image_name"));
                image.setContentType(resultSet.getString("content_type"));
                image.setMd5(resultSet.getString("md5"));
                list.add(image);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("查询所有图片出错 ",e);
        } finally {
            Util.close(connection,statement,resultSet);
        }

    }

    /**
     * 展示指定图片
     * @param id
     * @return
     */
    public static Image queryOne(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = Util.getConnection();
            String sql = "select * from image_table where image_id=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1,id);
            resultSet = statement.executeQuery();

            Image image = null;
            while (resultSet.next()){
                image = new Image();
                image.setImageId(resultSet.getInt("image_id"));
                image.setImageName(resultSet.getString("image_name"));
                image.setContentType(resultSet.getString("content_type"));
                image.setMd5(resultSet.getString("md5"));
                image.setPath(resultSet.getString("path"));
            }
            return image;
        } catch (SQLException e) {
            throw new RuntimeException("查询所有图片出错 ",e);
        } finally {
            Util.close(connection,statement,resultSet);

        }
    }

    public static int delete(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
          connection = Util.getConnection();
          connection.setAutoCommit(false);//不自动提交（开启事务）
          String sql = "delete from image_table where image_id=?";
          statement = connection.prepareStatement(sql);
          statement.setInt(1,id);
          int n = statement.executeUpdate();
          //TODO:删除本地图片文件
            connection.commit();
          return n;
        }catch (Exception e){//所有的异常  都要回滚，不止SQLException
            try {
                connection.rollback();
            } catch (SQLException se) {
                throw new RuntimeException("删除图片回滚出错："+id,e);
            }
            throw new RuntimeException("删除图片出错："+id,e);
        }finally {
            Util.close(connection,statement);
        }
    }
}
