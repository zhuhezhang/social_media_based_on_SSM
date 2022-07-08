package pers.zhz.service;

import com.alibaba.fastjson.JSONObject;

/**
 * 未登录便可访问的资源路径（拦截器不拦截）的服务层，登录、注册、忘记密码
 */
public interface NoLoginService {
    /**
     * 插入用户注册信息
     *
     * @param username 用户名
     * @param email    邮箱
     * @param password 密码
     * @return 4-成功注册；1-用户名被占用；2-邮箱已被占用；3-邮箱用户名均被占用
     * @throws Exception 发送邮件相关异常
     */
    int userRegister(String username, String email, String password) throws Exception;

    /**
     * 根据用户id设置用户表状态，使该账号生效，并修改注册/修改密码表中的状态，使之失效
     *
     * @param userId 用户账号
     * @param code   32位标识码
     * @return 数据库所受影响的行数
     */
    int confirmRegister(int userId, String code);

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 0-登录成功；1-账号未激活；2-账号或密码出错
     */
    int login(String username, String password);

    /**
     * 设置用户在线状态
     *
     * @param id       用户账号
     * @param isActive true在线；false下线
     */
    void setIsActiveById(int id, boolean isActive);

    /**
     * 处理忘记密码
     *
     * @param data 邮箱和密码
     * @return true-成功；false-失败
     * @throws Exception 邮件相关异常
     */
    boolean forgetPwd(JSONObject data) throws Exception;

    /**
     * 处理确认忘记密码
     *
     * @param userId 用户id
     * @param code   忘记密码码
     */
    int confirmForgetPwd(int userId, String code);
}