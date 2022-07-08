package pers.zhz.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import pers.zhz.mapper.FriendMapper;
import pers.zhz.mapper.PersonalCenterMapper;
import pers.zhz.mapper.SystemMessageMapper;
import pers.zhz.pojo.SystemMessage;
import pers.zhz.pojo.UserFriend;
import pers.zhz.service.PersonalCenterService;
import pers.zhz.service.SystemMessageService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service("systemMessageService")//起别名，使spring按名字注入时注入的是实现类而不是接口
public class SystemMessageServiceImpl implements SystemMessageService {

    @Resource
    private HttpSession httpSession;
    @Resource
    private SystemMessageMapper systemMessageMapper;
    @Resource
    private PersonalCenterService personalCenterService;
    @Resource
    private PersonalCenterMapper personalCenterMapper;
    @Resource
    private FriendMapper friendMapper;
    private static final Logger logger = Logger.getLogger(SystemMessageServiceImpl.class);

    @Override
    public Map<String, List<JSONObject>> getSystemMessage(int currentPage) {
        int id = (Integer) httpSession.getAttribute("loginUserId");
        Map<String, List<JSONObject>> messages = new HashMap<>();//返回给控制层的数据
        int msgNum = systemMessageMapper.getSystemMessageNumById(id);
        List<JSONObject> totalPages = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        int pageNum = msgNum % 10 == 0 ? msgNum / 10 : msgNum / 10 + 1;//页数，10表示每页最多有10条消息
        if (pageNum == 0) {//防止页数为0影响前端分页插件的初始化
            pageNum++;
        }
        boolean flag = false;
        if (currentPage > pageNum) {//防止当前页码大于总页码引发前端分页插件的问题
            pageNum = currentPage;
            flag = true;//不再需要获取系统消息
        }
        jsonObject.put("totalPages", pageNum);
        totalPages.add(jsonObject);
        messages.put("totalPages", totalPages);
        if (msgNum == 0 || flag) {//没有消息，直接返回
            return messages;
        }

        //添加用户的所有消息列表，每次返回10条系统消息
        List<SystemMessage> tmpMessage = systemMessageMapper.getSystemMessageByReceiverId(id, (currentPage - 1) * 10);
        logger.debug("系统消息" + tmpMessage);
        List<JSONObject> systemMsg = new ArrayList<>();
        for (SystemMessage message : tmpMessage) {
            JSONObject object = new JSONObject();
            object.put("messageId", message.getId());
            object.put("headPortrait", personalCenterService.getHeadPortraitById(message.getSenderId()));
            int msgType = message.getMessageType();
            object.put("messageType", msgType);
            object.put("senderId", message.getSenderId());
            object.put("time", message.getSendTime().getTime());
            //互为好友的消息，则获取昵称
            if (friendMapper.getFriendNicknameById(id, message.getSenderId()) != null) {
                object.put("name", friendMapper.getFriendNicknameById(message.getSenderId(), id));
            } else {//否则获取对方用户名
                object.put("name", personalCenterMapper.getUsernameById(message.getSenderId()));
            }
            if (msgType == 4 || msgType == 5 || msgType == 6 || msgType == 7) {//点赞随说、取消点赞、评论随说、回复随说相关的消息，添加casualWordId字段
                object.put("casualWordId", message.getCasualWordId());
            }
            if (msgType == 8 || msgType == 9) {//回答问题、回复问题的回答相关的消息，添加qAndAId字段
                object.put("qAndAId", message.getQuestionId());
            }
            systemMsg.add(object);
        }
        messages.put("messages", systemMsg);//返回系统消息
        return messages;
    }

    @Override
    public JSONObject responseAddFriend(int messageId, boolean flag) {
        SystemMessage tmp = systemMessageMapper.getSystemMsgByMsgId(messageId);
        int senderId = tmp.getSenderId();
        JSONObject returnObj = new JSONObject();
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        if (tmp.getReceiverId() != myId) {//非本人回应
            logger.warn("用户" + myId + "非法回应非本人的添加好友的请求，回应的消息id：" + messageId);
            return null;
        }
        if (tmp.getMessageType() != 0) {//消息类型不符合，非法回应添加好友的请求
            logger.warn("用户" + myId + "非法回应添加好友的请求，消息类型不符合，回应的消息id：" + messageId);
            return null;
        }
        if (!tmp.getIsEffective()) {//该消息已经失效
            returnObj.put("state", 3);
            return returnObj;
        }

        int result = 0;
        SystemMessage systemMessage = new SystemMessage();//同意添加/拒绝好友的系统消息
        systemMessage.setSendTime(new Date());
        systemMessage.setSenderId(myId);
        systemMessage.setReceiverId(senderId);

        if (flag) {//同意添加好友
            UserFriend userFriend = new UserFriend();//插入两条用户好友信息，用户自己的以及好友的，好友昵称默认对方的用户名
            userFriend.setFriendId(senderId);
            userFriend.setUserId(myId);
            userFriend.setFriendNickname(personalCenterMapper.getUsernameById(senderId));
            result += friendMapper.addUserFriend(userFriend);//插入第一条信息
            userFriend.setFriendId(myId);
            userFriend.setUserId(senderId);
            userFriend.setFriendNickname(personalCenterMapper.getUsernameById(myId));
            result += friendMapper.addUserFriend(userFriend);//插入第二条
            systemMessage.setMessageType(1);
        } else {//拒绝添加
            systemMessage.setMessageType(2);
        }
        result += systemMessageMapper.insertNewSystemMessage(systemMessage);//插入同意/拒绝添加好友的信息
        result += systemMessageMapper.makeSystemMessageInvalid(messageId);//使该消息失效
        returnObj.put("state", result);
        returnObj.put("status", 200);
        return returnObj;
    }

    @Override
    public JSONObject deleteSystemMessage(int messageId) {
        SystemMessage tmp = systemMessageMapper.getSystemMsgByMsgId(messageId);
        int result = 0;
        if (tmp.getMessageType() == 0 && tmp.getIsEffective()) {//添加好友的消息、且生效
            SystemMessage systemMessage = new SystemMessage();
            systemMessage.setMessageType(2);
            systemMessage.setReceiverId(tmp.getSenderId());
            systemMessage.setSendTime(new Date());
            systemMessage.setSenderId((Integer) httpSession.getAttribute("loginUserId"));
            result += systemMessageMapper.insertNewSystemMessage(systemMessage);//插入拒绝添加好友的消息，告知对方
        }
        result += systemMessageMapper.delSystemMsgByMsgId(messageId);//删除该消息
        JSONObject json = new JSONObject();
        json.put("state", result);
        json.put("status", 200);
        return json;
    }
}