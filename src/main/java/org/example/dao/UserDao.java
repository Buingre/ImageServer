package org.example.dao;

import org.example.model.User;
import org.example.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * userinfo的增删改查方法
 */
public class UserDao {
    /**
     * 【增】--添加用户
     * @param user 前端传过来一个user对象
     * @return 如果返回的为true说明  操作成功
     */
    public boolean add(User user) throws SQLException {
        boolean result = false;
        //1.非空校验
        if(user.getUsername()!=null && user.getPassword()!=null){
            //说明是正确的参数
            //2.数据库的经典操作
            Connection connection = Util.getConnection();
            String sql = "insert into user_table(username,password) values(?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,user.getUsername());
            statement.setString(2,user.getPassword());
            result = statement.executeUpdate()>=1?true:false;
            Util.close(connection,statement,null);
        }

        return result;

    }

    /**
     * 【查】-->对应登录功能
     * @param
     * @return
     */
    public boolean isLogin(User user) throws SQLException {
        boolean result = false;
        //1.校验参数
        if(user.getUsername()!=null && user.getPassword()!=null &&
                !user.getUsername().equals("") && !user.getPassword().equals("")){
            //说明是正确的参数
            //2.数据库的经典操作
            Connection connection = Util.getConnection();
            String sql = "select * from user_table where username=? and password=? and state=1 ";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,user.getUsername());
            statement.setString(2,user.getPassword());
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next()){
                result = true;
            }
            Util.close(connection,statement,resultSet);
        }
        return result;
    }
}

