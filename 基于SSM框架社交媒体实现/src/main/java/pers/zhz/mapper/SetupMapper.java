package pers.zhz.mapper;

import org.apache.ibatis.annotations.Param;
import pers.zhz.pojo.User;

/**
 * 设置相关的mapper
 */
public interface SetupMapper {
    /**
     * 判断手机号是否存在
     *
     * @param phoneNumber 手机号
     * @return true存在false不存在
     */
    boolean isPhoneNumberExist(@Param("phoneNumber") String phoneNumber);

    /**
     * 通过id更新用户信息
     *
     * @param user 用户对象
     * @return 数据库影响的行数
     */
    int updateUserById(User user);
}
