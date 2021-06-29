drop database if exists image_system;
create database image_system character set utf8mb4;
use image_system;

drop table if exists `image_table`;
create table `image_table`(image_id int not null primary key auto_increment,
                          image_name varchar(50),
                          size bigint,
                          upload_time varchar(50),
                          md5 varchar(128),
                          content_type varchar(50) comment '图片类型',
                          path varchar(1024) comment '图片所在路径')

-- 创建表[用户表]
drop table if exists  user_table;
create table user_table(
    user_id int primary key auto_increment,
    username varchar(100) not null,
    password varchar(32) not null,
    createtime timestamp NULL default CURRENT_TIMESTAMP,
    `state` int default 1
);