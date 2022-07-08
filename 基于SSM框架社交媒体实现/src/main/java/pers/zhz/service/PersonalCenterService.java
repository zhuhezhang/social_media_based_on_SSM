package pers.zhz.service;

import pers.zhz.pojo.User;

/**
 * 个人中心相关的服务层
 */
public interface PersonalCenterService {
    /**
     * 根据获取用户id获取用户头像路径
     *
     * @param id 账号
     * @return 头像路径
     */
    String getHeadPortraitById(int id);

    /**
     * 根据获取用户id获取用户
     *
     * @param id 账号
     * @return User对象
     */
    User getUserById(int id);

    /**
     * 根据用户的id获取用户名
     *
     * @param userId 用户账号
     * @return 用户名
     */
    String getUsernameById(int userId);
}
