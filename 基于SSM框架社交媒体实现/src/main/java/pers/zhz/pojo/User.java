package pers.zhz.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 */
public class User implements Serializable {
    private Integer id;//用户账号/表id
    private String username;//用户名
    private String email;//邮箱
    private String password;//密码
    private String phoneNumber;//手机号码
    private Boolean sex;//性别，0女，1男,
    private String headPortrait;//头像，默认系统头像
    private Date registerTime;//注册时间
    private Boolean isActive;//default 0，是否在线，0下线，1在线
    private Boolean state;//default 0，是否已激活，0未激活，1已激活
    private String birthday;//生日
    private String hometown;//家乡
    private String introduce;//个人简介

    public User() {
    }

    public User(Integer id, String username, String email, String password, String phoneNumber, Boolean sex, String headPortrait, Date registerTime, Boolean isActive, Boolean state, String birthday, String hometown, String introduce) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.sex = sex;
        this.headPortrait = headPortrait;
        this.registerTime = registerTime;
        this.isActive = isActive;
        this.state = state;
        this.birthday = birthday;
        this.hometown = hometown;
        this.introduce = introduce;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getHometown() {
        return hometown;
    }

    public void setHometown(String hometown) {
        this.hometown = hometown;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", sex=" + sex +
                ", headPortrait='" + headPortrait + '\'' +
                ", registerTime=" + registerTime +
                ", isActive=" + isActive +
                ", state=" + state +
                ", birthday='" + birthday + '\'' +
                ", hometown='" + hometown + '\'' +
                ", introduce='" + introduce + '\'' +
                '}';
    }
}
