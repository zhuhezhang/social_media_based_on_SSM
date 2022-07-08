package pers.zhz.mapper;

import org.apache.ibatis.annotations.Param;
import pers.zhz.pojo.SystemMessage;

import java.util.List;

/**
 * 系统消息mapper
 */
public interface SystemMessageMapper {

    /**
     * 创建新的系统消息
     *
     * @param systemMessage 系统消息类
     * @return 数据库影响的行数
     */
    int insertNewSystemMessage(SystemMessage systemMessage);

    /**
     * 根据id获取本用户的系统消息数量
     *
     * @param receiverId 用户id
     * @return 消息数量
     */
    int getSystemMessageNumById(@Param("receiverId") int receiverId);

    /**
     * 通过发送接收者的id查看是否有存在的生效的添加好友系统消息
     *
     * @param senderId   发送者id
     * @param receiverId 接收者id
     * @return 是否存在
     */
    Boolean checkAddFriendSystemMessageExistById(@Param("senderId") int senderId, @Param("receiverId") int receiverId);

    /**
     * 通过接收者账号、页码获取系统消息列表（每次10条）
     *
     * @param receiverId 接收者账号
     * @param pageIndex  起始页
     * @return 系统消息实体类列表
     */
    List<SystemMessage> getSystemMessageByReceiverId(@Param("receiverId") int receiverId, @Param("pageIndex") int pageIndex);

    /**
     * 根据系统消息的id获取该消息发送者id
     *
     * @param msgId 消息账号
     * @return 消息类
     */
    SystemMessage getSystemMsgByMsgId(@Param("msgId") int msgId);

    /**
     * 根据系统消息id使该消息失效
     *
     * @param id 消息id
     * @return 数据库影响的行数
     */
    int makeSystemMessageInvalid(int id);

    /**
     * 根据系统消息id删除消息
     *
     * @param messageId 消息id
     * @return 数据库受影响的行数
     */
    int delSystemMsgByMsgId(@Param("messageId") int messageId);

    /**
     * 根据随说id删除系统消息
     *
     * @param casualWordId 随说id
     */
    void delSystemMsgByCasualWordId(@Param("casualWordId") int casualWordId);

    /**
     * 根据问答-问题id删除系统消息
     *
     * @param questionId 问题id
     */
    void delSystemMsgByQuestionId(@Param("questionId") int questionId);
}
