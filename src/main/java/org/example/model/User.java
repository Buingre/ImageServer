package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
@Getter
@Setter
@ToString
public class User {
    private int userid;
    private String username;
    private String password;
    private Date createtime;
    private int state;
}
