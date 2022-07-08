package pers.zhz.service;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

/**
 * 好友相关的服务层
 */
public interface FriendService {
    /**
     * 通过用户账号获取用户的好友列表
     *
     * @param userId 用户id
     * @return 返回用户好友map，以用户好友的id+头像完整路径为key（例如1_1.jpg），用户给ta的好友的昵称为value
     */
    HashMap<String, String> getUserFriendByUserId(int userId);

    /**
     * 通过用户名查找用户
     *
     * @param username 用户名
     * @return 返回state=0没有该用户；1-用户本人；2-存在该好友；3-不存在该好友，并返回跳转到该用户的id并由前端控制跳转到指定页面；status-状态码，200成功
     */
    JSONObject searchUserByName(String username);

    /**
     * 处理添加好友
     *
     * @param friendId 好友id
     * @return 1成功发送方添加请求；2已经发送过添加请求；3是对方已经发送给好友请求，请前往系统消息中查看；0请求添加失败；status-状态码，200成功
     */
    JSONObject handelAddFriend(int friendId);

    /**
     * 根据账号查找用户的好友昵称
     *
     * @param friendId 好友账号
     * @return 好友昵称
     */
    String getFriendNicknameById(int friendId);

    /**
     * 根据账号修改好友的昵称
     *
     * @param nickname 新昵称
     * @param friendId 好友id
     * @return state-状态变量（1-成功，其他失败）；status-状态码，200成功
     */
    JSONObject modifyFriendNickname(String nickname, int friendId);

    /**
     * 根据账号删除好友
     *
     * @param friendId 要删除的好友账号
     * @return state（3-成功，其他失败）；status-状态码，200成功
     */
    JSONObject deleteFriend(int friendId);

    /**
     * 添加聊天用户
     *
     * @param friendId 好友账号
     */
    void addUserChat(int friendId);
}
