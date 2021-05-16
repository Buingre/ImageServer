package org.example.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 公共方法：json字符串、数据库JDBC操作
 */
public class Util {
    //因为创建比较费时，所以就创建一个 够用
    private static final ObjectMapper M = new ObjectMapper();

    //数据库JDBC
    //数据库连接池：一个程序用一个数据库连接池，
    //            而这一个数据库连接池  创建的时候 就生成一定数量的连接
    //todo:用单例模式
    private static final MysqlDataSource DS = new MysqlDataSource();

    //TODO:学完单线程双重校验锁的单例模式，自己改造
    static {
        DS.setURL("jdbc:mysql://localhost:3306/image_system");
        DS.setUser("root");
        DS.setPassword("233233");
        DS.setUseSSL(false);
        DS.setCharacterEncoding("UTF-8");
    }

    /**
     * json序列化：把一个对象--->json字符串 （Servlet响应输出的body需要json字符串）
     * */
    public static String serialize(Object o){//要既满足list又要满足单个对象
        try {
            return M.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            //e.printStackTrace();
           //序列化和反序列化
            throw new RuntimeException("序列化Java对象失败："+o, e);
        }
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection(){
        try {
            return DS.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("数据库连接获取失败 ",e);
        }
    }

    /**
     * 统一数据库关闭方法：查询操作的
     */
    public static void close(Connection c, Statement s, ResultSet rs){
        try {
            if(rs != null) rs.close();
            if(s != null) s.close();
            if(c !=null) c.close();
        } catch (SQLException e) {
            throw new RuntimeException("释放数据库资源失败 ",e);
        }
    }

    /**【重载方法】
     * 统一数据库关闭方法：插入、删除、修改 等没有结果集对象
     */
    public static void close(Connection c, Statement s){
        close(c,s,null);
    }


    public static void main(String[] args) throws SQLException {
        //【测试数据库连接】
        System.out.println(DS.getConnection());//看是不是有值
    }
}
