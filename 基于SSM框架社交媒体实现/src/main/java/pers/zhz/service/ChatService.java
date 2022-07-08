package pers.zhz.service;

import com.alibaba.fastjson.JSONObject;

/**
 * 聊天消息相关的服务层
 */
public interface ChatService {
    /**
     * 获取用户聊天信息，先设置用户列表中的消息为已接收，再各获取前10条消息，然后再获取当前不在用户列表中的未接收的好友信息
     *
     * @return 返回{"1":{"name":"zhz","hpName": "1.jpg","firstTime":1648989733570,"lastTime":1648989733576,
     * messages":[{"flag":false,"content":"消息1"},{"flag":true,"content":"消息2"}]},"status":200}, 1表示好友的账号；name名字；
     * hpName为头像名称；firstTime第一条消息的时间戳；lastTime最新消息的时间戳；messages为消息列表，升序排列，其中flag为false时表示为好友的消息，
     * 否则为本人的消息，content为消息内容；status-状态码，200则操作成功
     */
    JSONObject getChatMessage();

    /**
     * 获取未接收的消息
     *
     * @return 消息相关信息
     */
    JSONObject getNotReceiveChatMessage();

    /**
     * 将指定用户移出聊天列表
     *
     * @param currentChatUserId currentChatUserId-移除的用户id
     * @param myId              我的账号
     * @return status-200则表示移除成功
     */
    JSONObject deleteUserFromChatUserList(int currentChatUserId, int myId);

    /**
     * 根据好友id、最后一条消息的时间获取该时间以前的好友和本人的聊天消息
     *
     * @param friendId 好友账号
     * @param time     时间戳
     * @param myId     我的账号
     * @return 消息相关数据，hasMsg是否有消息；firstTime第一消息时间；messages字段，消息列表（按照消息发送时间升序排列）; status-状态码，200表示成功
     */
    JSONObject getMoreChatContent(int friendId, long time, int myId);

    /**
     * 处理发送消息请求
     *
     * @param myId              我的账号
     * @param currentChatUserId 好友id
     * @param msg               消息内容
     * @return status-200成功，否则失败；time-消息时间戳
     */
    JSONObject addUserChatMessage(int myId, int currentChatUserId, String msg);
}
