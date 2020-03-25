package com.miniurl.entity;

import java.util.Date;
import javax.persistence.*;

public class User {
    /**
     * 用户id
     */
    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    /**
     * 邮箱
     */
    @Column(name = "USER_EMAIL")
    private String userEmail;

    /**
     * 是否验证邮箱
     */
    @Column(name = "USER_EMAIL_VERIFY")
    private String userEmailVerify;

    /**
     * 用户名
     */
    @Column(name = "USER_NAME")
    private String userName;

    /**
     * 密码
     */
    @Column(name = "HASH_PASS")
    private String hashPass;

    /**
     * 用户等级
     */
    @Column(name = "USER_CLASS")
    private String userClass;

    /**
     * 创建时间
     */
    @Column(name = "CREATED_TIME")
    private Date createdTime;

    /**
     * 上次登录时间
     */
    @Column(name = "LAST_LOGIN_TIME")
    private Date lastLoginTime;

    /**
     * 变短次数
     */
    @Column(name = "URL_NUM")
    private Integer urlNum;

    /**
     * 获取用户id
     *
     * @return USER_ID - 用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取邮箱
     *
     * @return USER_EMAIL - 邮箱
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * 设置邮箱
     *
     * @param userEmail 邮箱
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * 获取是否验证邮箱
     *
     * @return USER_EMAIL_VERIFY - 是否验证邮箱
     */
    public String getUserEmailVerify() {
        return userEmailVerify;
    }

    /**
     * 设置是否验证邮箱
     *
     * @param userEmailVerify 是否验证邮箱
     */
    public void setUserEmailVerify(String userEmailVerify) {
        this.userEmailVerify = userEmailVerify;
    }

    /**
     * 获取用户名
     *
     * @return USER_NAME - 用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户名
     *
     * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 获取密码
     *
     * @return HASH_PASS - 密码
     */
    public String getHashPass() {
        return hashPass;
    }

    /**
     * 设置密码
     *
     * @param hashPass 密码
     */
    public void setHashPass(String hashPass) {
        this.hashPass = hashPass;
    }

    /**
     * 获取用户等级
     *
     * @return USER_CLASS - 用户等级
     */
    public String getUserClass() {
        return userClass;
    }

    /**
     * 设置用户等级
     *
     * @param userClass 用户等级
     */
    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    /**
     * 获取创建时间
     *
     * @return CREATED_TIME - 创建时间
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * 设置创建时间
     *
     * @param createdTime 创建时间
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * 获取上次登录时间
     *
     * @return LAST_LOGIN_TIME - 上次登录时间
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * 设置上次登录时间
     *
     * @param lastLoginTime 上次登录时间
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    /**
     * 获取变短次数
     *
     * @return URL_NUM - 变短次数
     */
    public Integer getUrlNum() {
        return urlNum;
    }

    /**
     * 设置变短次数
     *
     * @param urlNum 变短次数
     */
    public void setUrlNum(Integer urlNum) {
        this.urlNum = urlNum;
    }
}