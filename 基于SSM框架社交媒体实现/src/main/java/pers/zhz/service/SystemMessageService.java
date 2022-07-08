package pers.zhz.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 系统消息相关的服务层
 */
public interface SystemMessageService {

    /**
     * 获取该用户的系统消息
     *
     * @param currentPage 页码
     * @return 消息的json列表、消息总数
     */
    Map<String, List<JSONObject>> getSystemMessage(int currentPage);

    /**
     * 处理添加好友的
     *
     * @param messageId 消息id
     * @param flag      添加或拒绝标识(true同意添加；false拒绝)
     * @return state-状态变量（4-成功添加；3-消息已经失效；2-拒绝添加好友；null则为非法回应）；status-状态码，200成功
     */
    JSONObject responseAddFriend(int messageId, boolean flag);

    /**
     * 根据消息id删除系统消息
     *
     * @param messageId 消息id
     * @return state-2删除添加好友的消息成功；1删除其他消息成功；其他失败；status-状态码，200成功
     */
    JSONObject deleteSystemMessage(int messageId);
}
