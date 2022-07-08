package pers.zhz.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * 问答相关的服务层
 */
public interface QAndAService {

    /**
     * 处理发布问题的
     *
     * @param content 问题内容
     * @param myId    我的账号
     * @return questionId-问题账号；name-用户名；time-发布时间；status-状态码，200成功
     */
    JSONObject publishQAndAQuestion(String content, int myId);

    /**
     * 获取问答相关信息
     *
     * @param myId        本人id
     * @param currentPage 页码
     * @return qAndAs-问题列表；totalPages-问题页数
     */
    Map<String, List<JSONObject>> getQAndAQuestion(int myId, int currentPage);

    /**
     * 获取指定用户的问题相关信息
     *
     * @param myId        本人账号
     * @param userId      用户id
     * @param currentPage 页码
     * @return qAndAs-问题列表；totalPages-问题页数
     */
    Map<String, List<JSONObject>> getUserQuestionByUserId(int myId, int userId, int currentPage);

    /**
     * 根据问题内容模糊查找问题
     *
     * @param content 问题内容
     * @param myId    我的账号
     * @return 结果数据
     */
    JSONObject getQuestionsByContent(String content, int myId);

    /**
     * 处理回答问题
     *
     * @param myId    我的id
     * @param content 回答内容
     * @param id      问题/回答id
     * @param flag    0(非问题详细界面回答问题)，1（问题详细界面回答问题），2（问题详细界面回答回答）
     * @return json相关数据，time-时间；answerId-回答id；myName-我的名字；respondentId-被回复者id；respondentName-被回复者姓名；status-操作成功200
     */
    JSONObject answerQuestion(int myId, int id, String content, int flag);

    /**
     * 根据问题id获取问题详细信息
     *
     * @param myId        本人账号
     * @param questionId  问题id
     * @param currentPage 当前回答页码
     * @return answers-问题回答列表；questionInfo-问题相关信息
     */
    Map<String, List<JSONObject>> getQAndADetail(int myId, int questionId, int currentPage);

    /**
     * 根据问题id删除问题，及其相关的回答、系统通知信息
     *
     * @param questionId 随说id
     * @param myId         我的账号
     */
    void deleteQuestionById(int questionId, int myId);
}
