package pers.zhz.mapper;

import org.apache.ibatis.annotations.Param;
import pers.zhz.pojo.User;

/**
 * 个人中心mapper
 */
public interface PersonalCenterMapper {
    /**
     * 根据获取用户id获取用户头像名称（名称，不是路径）
     *
     * @param id 账号
     * @return 头像名称
     */
    String getHeadPortraitById(@Param("id") int id);

    /**
     * 通过id查找用户
     *
     * @param id 账号
     * @return User对象
     */
    User getUserById(@Param("id") int id);

    /**
     * 通过账号获取用户名
     *
     * @param id 账号
     * @return 用户名
     */
    String getUsernameById(int id);
}
