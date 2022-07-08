package pers.zhz.mapper;

import org.apache.ibatis.annotations.Param;
import pers.zhz.pojo.ConfirmRegisterAndForgetPwd;
import pers.zhz.pojo.User;

import java.util.HashMap;

/**
 * 未登录便可进行访问的相关mapper
 */
public interface NoLoginMapper {

    /**
     * 判断用户名是否存在
     *
     * @param username 用户名
     * @return true存在false不存在
     */
    boolean isUsernameExist(@Param("username") String username);

    /**
     * 判断邮箱是否存在
     *
     * @param email 邮箱
     * @return true存在false不存在
     */
    boolean isEmailExist(@Param("email") String email);

    /**
     * 插入用户注册信息
     *
     * @param user User实体类
     * @return 数据库受影响的行数
     */
    int userRegister(User user);

    /**
     * 通过用户名获取用户id
     *
     * @param username 用户名
     * @return 用户id（账号）
     */
    Integer getUserIdByName(String username);

    /**
     * 插入用户注册激活或修改密码信息
     *
     * @param ramp 实体类
     */
    void insertRegisterOrModifyPwdInfo(ConfirmRegisterAndForgetPwd ramp);

    /**
     * 根据code设置用户确认注册激活码或修改密码码表的code，使该记录生效1/失效0
     *
     * @param code  注册激活码/修改密码确认码
     * @param state 生效1or失效0
     * @param flag  注册0or修改密码1
     * @return 数据库受影响的行数
     */
    int setStateByCode(@Param("code") String code, @Param("state") boolean state, @Param("flag") boolean flag);

    /**
     * 根据用户id设置用户表激活状态
     *
     * @param userId 账号
     * @param state  账号生效1/失效0
     * @return 数据库受影响的行数
     */
    int setStateById(@Param("userId") int userId, @Param("state") boolean state);

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 状态码state：true表示账号已生效，否则未生效；账号id
     */
    HashMap<String, Object> login(@Param("username") String username, @Param("password") String password);

    /**
     * 设置用户在线状态
     *
     * @param id       用户账号
     * @param isActive true在线/false下线
     */
    void setIsActiveById(@Param("id") int id, @Param("isActive") boolean isActive);

    /**
     * 根据邮箱查询账号
     *
     * @param email 邮箱
     * @return 用户账号
     */
    Integer getIdByEmail(@Param("email") String email);

    /**
     * 根据code码从修改密码表获取用户新密码
     *
     * @param code 码code
     * @return 密码
     */
    String getPwdByCode(@Param("code") String code);

    /**
     * 根据用户id修改密码
     *
     * @param id       用户账号
     * @param password 密码
     * @return 数据库受影响的行数
     */
    int setPwdById(@Param("id") int id, @Param("password") String password);
}