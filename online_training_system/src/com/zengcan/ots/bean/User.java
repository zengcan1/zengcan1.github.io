package com.zengcan.ots.bean;

import java.util.Objects;

public class User {
    // 用户编号
    private String id;
    // 名字
    private String name;
    // 用户名
    private String username;
    // 密码
    private String password;
    // 性别
    private String sex;
    // 用户类型
    private String user_type;

    public User() {
    }

    public User(String username, String password, String user_type) {
        this.username = username;
        this.password = password;
        this.user_type = user_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sex='" + sex + '\'' +
                ", user_type='" + user_type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(sex, user.sex) && Objects.equals(user_type, user.user_type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, username, password, sex, user_type);
    }
}
