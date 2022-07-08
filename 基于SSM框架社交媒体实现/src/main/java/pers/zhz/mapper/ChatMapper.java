package pers.zhz.mapper;

import org.apache.ibatis.annotations.Param;
import pers.zhz.pojo.UserChatList;
import pers.zhz.pojo.UserChatMessage;

import java.util.Date;
import java.util.List;

/**
 * 聊天消息mapper
 */
public interface ChatMapper {
    /**
     * 添加聊天用户
     *
     * @param userChatList 聊天用户表实体类
     */
    void addUserChat(UserChatList userChatList);

    /**
     * 根据用户账号获取聊天用户列表
     *
     * @param id 用户账号
     * @return ChatUser实体类列表，createTime-创建时间；friendId-好友账号
     */
    List<UserChatList> getChatUserListById(@Param("id") int id);

    /**
     * 根据好友id列表和本人id使对方发送给本人的聊天消息为已接收状态
     *
     * @param userChatLists pojo实体类列表
     * @param myId          我的账号
     */
    void setChatMsgReceivedByUserAndFriendId(@Param("userChatLists") List<UserChatList> userChatLists, @Param("myId") int myId);

    /**
     * 根据好友id和本人id获取最新10条聊天消息
     *
     * @param friendId 好友账号
     * @param myId     我的账号
     * @return UserChatMessage用户聊天消息实体类列表，按照时间升序排列
     */
    List<UserChatMessage> getChatMessageById(@Param("friendId") int friendId, @Param("myId") int myId);

    /**
     * 根据用户账号id获取未接收的聊天消息
     *
     * @param myId 我的账号
     * @return UserChatMessage用户聊天消息实体类列表，按照时间升序排列
     */
    List<UserChatMessage> getNotReceiveChatMessageByUserId(@Param("myId") int myId);

    /**
     * 根据聊天消息id设置其为已接收
     *
     * @param id 消息id
     */
    void setChatMsgReceivedByMsgId(@Param("id") int id);

    /**
     * 根据用户和好友id将好友移除当前用户的聊天列表
     *
     * @param friendId 好友id
     * @param myId     我的id
     */
    void deleteUserFromChatUserListById(@Param("friendId") int friendId, @Param("myId") int myId);

    /**
     * 根据本人和好友的id获取两者的指定时间以前的10条聊天消息
     *
     * @param friendId 好友id
     * @param myId     我的id
     * @param time     时间
     * @return userChatMessage实体类列表，按照时间降序排列
     */
    List<UserChatMessage> getMoreChatContentByIdAndTime(@Param("friendId") int friendId, @Param("myId") int myId, @Param("time") Date time);

    /**
     * 插入新的系统消息
     *
     * @param userChatMessage 实体类
     */
    void addUserChatMessage(UserChatMessage userChatMessage);

    /**
     * 根据本人id和好友id获取用户聊天列表的id
     *
     * @param friendId 好友账号
     * @param myId     我的账号
     * @return 数据行id
     */
    Integer getUserChatListIdByUserAndFriendId(@Param("friendId") int friendId, @Param("myId") int myId);

    /**
     * 根据用户聊天列表的id更新其创建时间
     *
     * @param id          用户聊天列表的账号
     * @param currentTime 当前时间
     */
    void updateUserChatListCreateTimeByUserChatListId(@Param("id") int id, @Param("currentTime") Date currentTime);

    /**
     * 根据本人id和好友id删除两者之间的聊天记录
     *
     * @param friendId 好友账号
     * @param myId     我的账号
     */
    void deleteChatMessageById(@Param("friendId") int friendId, @Param("myId") int myId);
}
