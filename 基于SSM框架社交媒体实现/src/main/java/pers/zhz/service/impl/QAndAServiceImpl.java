package pers.zhz.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import pers.zhz.mapper.*;
import pers.zhz.pojo.*;
import pers.zhz.service.PersonalCenterService;
import pers.zhz.service.QAndAService;

import javax.annotation.Resource;
import java.util.*;

@Service("qAndAService")//起别名，使spring按名字注入时注入的是实现类而不是接口
public class QAndAServiceImpl implements QAndAService {

    private static final Logger logger = Logger.getLogger(QAndAServiceImpl.class);
    @Resource
    private QAndAMapper qAndAMapper;
    @Resource
    private PersonalCenterService personalCenterService;
    @Resource
    private FriendMapper friendMapper;
    @Resource
    private PersonalCenterMapper personalCenterMapper;
    @Resource
    private SystemMessageMapper systemMessageMapper;

    @Override
    public JSONObject publishQAndAQuestion(String content, int myId) {
        QAndAQuestion qAndAQuestion = new QAndAQuestion();
        qAndAQuestion.setContent(content);
        Date currentDate = new Date();
        qAndAQuestion.setQuestionTime(currentDate);
        qAndAQuestion.setQuestionerId((myId));
        qAndAMapper.addQAndAQuestion(qAndAQuestion);//数据库插入新的问题

        JSONObject json = new JSONObject();
        json.put("questionId", qAndAQuestion.getId());
        json.put("name", personalCenterMapper.getUsernameById(myId));
        json.put("time", currentDate.getTime());
        json.put("status", 200);
        return json;
    }

    @Override
    public Map<String, List<JSONObject>> getQAndAQuestion(int myId, int currentPage) {
        return commonGetQuestion(myId, myId, currentPage, true);
    }

    @Override
    public Map<String, List<JSONObject>> getUserQuestionByUserId(int myId, int friendId, int currentPage) {
        return commonGetQuestion(myId, friendId, currentPage, false);
    }

    @Override
    public JSONObject getQuestionsByContent(String content, int myId) {
        List<QAndAQuestion> questions = qAndAMapper.getQuestionByContent(content);
        JSONObject json = new JSONObject();
        List<JSONObject> qAndAs = new ArrayList<>();
        for (QAndAQuestion question : questions) {
            JSONObject object = new JSONObject();
            int questionerId = question.getQuestionerId();
            object.put("questionerHeadPortrait", personalCenterService.getHeadPortraitById(questionerId));
            object.put("questionerId", questionerId);
            if (friendMapper.getFriendNicknameById(myId, questionerId) == null) {//非好友、本人，获取用户名
                object.put("name", personalCenterMapper.getUsernameById(questionerId));
            } else {//否则获取该好友的昵称
                object.put("name", friendMapper.getFriendNicknameById(questionerId, myId));
            }
            object.put("time", question.getQuestionTime().getTime());
            object.put("content", question.getContent());
            object.put("questionId", question.getId());
            qAndAs.add(object);
        }
        json.put("questions", qAndAs);
        json.put("status", 200);
        logger.debug(json);
        return json;
    }

    @Override
    public JSONObject answerQuestion(int myId, int id, String content, int flag) {
        JSONObject returnObject = new JSONObject();
        QAndAAnswer qAndAAnswer = new QAndAAnswer();
        Date currentDate = new Date();
        qAndAAnswer.setResponseTime(currentDate);
        qAndAAnswer.setContent(content);
        qAndAAnswer.setResponderId(myId);

        if (flag == 0) {//非问题详细界面回答问题
            qAndAAnswer.setQuestionId(id);
            qAndAMapper.addQAndAAnswer(qAndAAnswer);//数据库表插入回答信息
            int userId = qAndAMapper.getQuestionerIdByQuestionId(id);//获取问题所属的用户id
            if (userId != myId) {//非本人回答自己的问题则插入系统消息
                SystemMessage message = new SystemMessage();
                message.setQuestionId(id);
                message.setMessageType(8);
                message.setSenderId(myId);
                message.setReceiverId(userId);
                message.setSendTime(new Date());
                systemMessageMapper.insertNewSystemMessage(message);
            }
        } else if (flag == 1) {//问题详细界面回答问题
            qAndAAnswer.setQuestionId(id);
            qAndAMapper.addQAndAAnswer(qAndAAnswer);//数据库表插入回答信息
            int userId = qAndAMapper.getQuestionerIdByQuestionId(id);
            if (userId != myId) {//非本人回答自己的问题则插入系统消息
                SystemMessage message = new SystemMessage();
                message.setMessageType(8);
                message.setQuestionId(id);
                message.setSenderId(myId);
                message.setReceiverId(userId);
                message.setSendTime(new Date());
                systemMessageMapper.insertNewSystemMessage(message);
            }

            returnObject.put("time", currentDate.getTime());
            returnObject.put("answerId", qAndAAnswer.getId());
            returnObject.put("myName", personalCenterMapper.getUsernameById(myId));
        } else {//问题详细界面回答回答
            int questionId = qAndAMapper.getQuestionIdByAnswerId(id);//根据回答id获取问题id
            qAndAAnswer.setReplyAnswerId(id);
            qAndAAnswer.setQuestionId(questionId);
            qAndAMapper.addQAndAAnswer(qAndAAnswer);//插入答案
            int questionerId = qAndAMapper.getQuestionerIdByQuestionId(questionId);//问题所属用户账号
            int respondentId = qAndAMapper.getUserIdByAnswerId(id);//被回复回答的回答发布者的账号id
            SystemMessage message = new SystemMessage();
            message.setSendTime(new Date());
            message.setQuestionId(questionId);
            message.setSenderId(myId);
            //根据需要通知的对象进行分类
            if (questionerId != myId && questionId != respondentId && myId != respondentId) {//情况1：三者都不是同一人，需要通知被回答者、问题所属者
                message.setMessageType(8);
                message.setReceiverId(questionerId);
                systemMessageMapper.insertNewSystemMessage(message);//通知问题拥有者
                message.setMessageType(9);
                message.setReceiverId(respondentId);
                systemMessageMapper.insertNewSystemMessage(message);//通知被回答的人
            } else if (questionerId != respondentId && myId == respondentId) {//情况2：回答者和被回答者是同一人，需要通知问题拥有者
                message.setMessageType(8);
                message.setReceiverId(questionerId);
                systemMessageMapper.insertNewSystemMessage(message);
            } else if (questionerId != respondentId) {//情况3：回答者和随问题所有者是同一人，需要通知被回答者
                message.setMessageType(9);
                message.setReceiverId(respondentId);
                systemMessageMapper.insertNewSystemMessage(message);
            } else if (questionerId != myId) {//情况4：被回答者和问题所有者是同一人，需要通知被回答者被回答回答
                message.setMessageType(9);
                message.setReceiverId(respondentId);
                systemMessageMapper.insertNewSystemMessage(message);
            }

            returnObject.put("time", currentDate.getTime());
            returnObject.put("answerId", qAndAAnswer.getId());
            returnObject.put("myName", personalCenterMapper.getUsernameById(myId));
            returnObject.put("respondentId", respondentId);
            returnObject.put("respondentName", personalCenterMapper.getUsernameById(respondentId));
        }
        returnObject.put("status", 200);
        return returnObject;
    }

    @Override
    public Map<String, List<JSONObject>> getQAndADetail(int myId, int questionId, int currentPage) {
        Map<String, List<JSONObject>> qAndADetailInfoMap = new HashMap<>();//返回给控制层的全部数据，本条问题信息、评论信息等
        List<JSONObject> questionInfoList = new ArrayList<>();//问题信息
        JSONObject questionInfoJsObj = new JSONObject();
        int userId = qAndAMapper.getQuestionerIdByQuestionId(questionId);//获取问题发布者id
        questionInfoJsObj.put("userHeadPortrait", personalCenterService.getHeadPortraitById(userId));
        questionInfoJsObj.put("questionerId", userId);
        if (userId == myId) {//本人的问题
            questionInfoJsObj.put("username", personalCenterMapper.getUsernameById(myId));
            questionInfoJsObj.put("isMyQuestion", true);
        } else {
            if (friendMapper.getFriendNicknameById(myId, userId) != null) {//好友，获取昵称
                questionInfoJsObj.put("username", friendMapper.getFriendNicknameById(userId, myId));
            } else {//否获取名字
                questionInfoJsObj.put("username", personalCenterMapper.getUsernameById(userId));
            }
            questionInfoJsObj.put("isMyQuestion", false);
        }
        QAndAQuestion qAndAQuestion = qAndAMapper.getQuestionByQuestionId(questionId);
        questionInfoJsObj.put("time", qAndAQuestion.getQuestionTime().getTime());
        questionInfoJsObj.put("content", qAndAQuestion.getContent());

        int answerNum = qAndAMapper.getAnswersNumByQuestionId(questionId);
        int totalPages = answerNum % 10 == 0 ? answerNum / 10 : answerNum / 10 + 1;//页数，10表示每页最多有10条消息
        if (totalPages == 0) {//防止页数为0影响前端分页插件的初始化
            totalPages++;
        }
        boolean getAnswerFlag = true;
        if (currentPage > totalPages) {//防止当前页码大于总页码引发前端分页插件的问题
            getAnswerFlag = false;//不再需要获取回答
            totalPages = currentPage;
        }
        questionInfoJsObj.put("totalPages", totalPages);
        questionInfoList.add(questionInfoJsObj);
        qAndADetailInfoMap.put("questionInfo", questionInfoList);
        if (answerNum == 0 || !getAnswerFlag) {//没有回答消息，直接返回
            return qAndADetailInfoMap;
        }

        //处理返回的问答回答信息
        List<QAndAAnswer> qAndAAnswerList = qAndAMapper.getAnswersByQuestionId(questionId, (currentPage - 1) * 10);
        List<JSONObject> answers = new ArrayList<>();
        for (QAndAAnswer answer : qAndAAnswerList) {
            JSONObject object = new JSONObject();
            object.put("answerId", answer.getId());
            object.put("time", answer.getResponseTime().getTime());
            int responderId = answer.getResponderId();
            object.put("headPortrait", personalCenterService.getHeadPortraitById(responderId));
            object.put("responderId", responderId);
            if (myId == responderId) {//用户本人的问题，获取本人的用户名
                object.put("responderName", personalCenterMapper.getUsernameById(responderId));
            } else {//非本人
                String nickname = friendMapper.getFriendNicknameById(responderId, myId);
                if (nickname == null) {//非好友
                    object.put("responderName", personalCenterMapper.getUsernameById(responderId));
                } else {//好友
                    object.put("responderName", nickname);
                }
            }

            if (answer.getReplyAnswerId() != null) {//有回复的回答
                object.put("hasRespondent", true);
                int respondentId = qAndAMapper.getUserIdByAnswerId(answer.getReplyAnswerId());//获取被回答人的id
                object.put("respondentId", respondentId);
                if (myId == respondentId) {//本人回复本人
                    object.put("respondentName", personalCenterMapper.getUsernameById(myId));
                } else {
                    String nickname = friendMapper.getFriendNicknameById(respondentId, myId);
                    if (nickname == null) {//非好友
                        object.put("respondentName", personalCenterMapper.getUsernameById(respondentId));
                    } else {//好友
                        object.put("respondentName", nickname);
                    }
                }
            } else {
                object.put("hasRespondent", false);
            }
            object.put("content", answer.getContent());
            answers.add(object);
        }
        qAndADetailInfoMap.put("answers", answers);
        return qAndADetailInfoMap;
    }

    @Override
    public void deleteQuestionById(int questionId, int myId) {
        if (myId != qAndAMapper.getQuestionerIdByQuestionId(questionId)) {
            logger.warn("用户（id=" + myId + "）非法删除非本人的问题的请求（" + questionId + "）");
            return;
        }
        qAndAMapper.delQuestionByQuestionId(questionId);//删除问题
        qAndAMapper.delAnswerByQuestionId(questionId);//删除问题相关的回答
        systemMessageMapper.delSystemMsgByQuestionId(questionId);//删除问题相关的系统消息
    }

    /**
     * 获取问题通用函数
     *
     * @param myId        本人id
     * @param userId      用户账号
     * @param currentPage 当前页
     * @param flag        true-获取全部用户问题的；false-获取指定用户问题的
     * @return 包含问答信息map，qAndAs-问题列表；totalPages-问题页数（每页最多20）
     */
    private Map<String, List<JSONObject>> commonGetQuestion(Integer myId, int userId, int currentPage, boolean flag) {
        Map<String, List<JSONObject>> questionInfo = new HashMap<>();//返回给控制层的数据
        int questionNum;
        if (flag) {//获取全部用户问题数量
            questionNum = qAndAMapper.getAllUserQuestionNum();
        } else {//只获取userId的
            questionNum = qAndAMapper.getUserQuestionNumByUserId(userId);
        }
        List<JSONObject> totalPagesList = new ArrayList<>();
        boolean getQFlag = true;
        JSONObject json = new JSONObject();
        int totalPages = questionNum % 20 == 0 ? questionNum / 20 : questionNum / 20 + 1;//页数，20表示每页最多有20个问题
        if (totalPages == 0) {//防止页数为0影响前端分页插件的初始化
            totalPages = 1;
        }
        if (currentPage > totalPages) {//防止当前页码大于总页码引发前端分页插件的问题
            totalPages = currentPage;
            getQFlag = false;//不再需要获取问题
        }
        json.put("totalPages", totalPages);
        totalPagesList.add(json);
        questionInfo.put("totalPages", totalPagesList);
        if (questionNum == 0 || !getQFlag) {//没有问题，直接返回
            return questionInfo;
        }

        //处理返回的问题信息
        List<QAndAQuestion> qAndAQuestionList;
        if (flag) {//获取全部用户的问答
            qAndAQuestionList = qAndAMapper.getAllUserQuestion((currentPage - 1) * 20);
        } else {
            qAndAQuestionList = qAndAMapper.getUserQuestionsByUserId(userId, (currentPage - 1) * 20);
        }
        List<JSONObject> qAndAs = new ArrayList<>();
        for (QAndAQuestion question : qAndAQuestionList) {
            JSONObject object = new JSONObject();
            if (flag) {//只有获取全部用户的问题才获取以下字段
                int questionerId = question.getQuestionerId();
                object.put("questionerHeadPortrait", personalCenterService.getHeadPortraitById(questionerId));
                object.put("questionerId", questionerId);
                if (friendMapper.getFriendNicknameById(myId, questionerId) == null) {//非好友、本人，获取用户名
                    object.put("questionerName", personalCenterMapper.getUsernameById(userId));
                } else {//否则获取该好友的昵称
                    object.put("questionerName", friendMapper.getFriendNicknameById(questionerId, myId));
                }
            }
            object.put("questionTime", question.getQuestionTime().getTime());
            object.put("question", question.getContent());
            object.put("questionId", question.getId());
            qAndAs.add(object);
        }
        questionInfo.put("qAndAs", qAndAs);
        return questionInfo;
    }
}