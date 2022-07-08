package pers.zhz.mapper;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import pers.zhz.pojo.UserFriend;

import java.util.List;

/**
 * 好友相关的mapper
 */
public interface FriendMapper {

    /**
     * 通过用户账号获取用户的好友列表
     *
     * @param userId 用户id
     * @return 返回用户好友的账号、昵称，以用户好友的账号为key，用户给ta的好友的昵称为value
     */
    List<JSONObject> getUserFriendByUserId(@Param("userId") int userId);

    /**
     * 根据账号查找用户的好友昵称
     *
     * @param friendId 好友账号
     * @param userId   用户账号
     * @return 好友昵称
     */
    String getFriendNicknameById(@Param("friendId") int friendId, @Param("userId") int userId);

    /**
     * 添加好友
     *
     * @param userFriend 用户好友实体类
     * @return 数据库受影响行数
     */
    int addUserFriend(UserFriend userFriend);

    /**
     * 根据账号修改好友的昵称
     *
     * @param userId   用户id
     * @param friendId 好友的id
     * @param nickname 新的昵称
     * @return 数据库影响的行数
     */
    int modifyFriendNickname(@Param("userId") int userId, @Param("friendId") int friendId, @Param("nickname") String nickname);

    /**
     * 根据账号删除好友
     *
     * @param userId   用户账号
     * @param friendId 好友账号
     * @return 数据库影响的行数
     */
    int deleteFriend(@Param("userId") int userId, @Param("friendId") int friendId);
}
