package pers.zhz.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import pers.zhz.mapper.ChatMapper;
import pers.zhz.mapper.FriendMapper;
import pers.zhz.pojo.UserChatList;
import pers.zhz.pojo.UserChatMessage;
import pers.zhz.service.ChatService;
import pers.zhz.service.PersonalCenterService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service("chatService")//起别名，使spring按名字注入时注入的是实现类而不是接口
public class ChatServiceImpl implements ChatService {

    @Resource
    private ChatMapper chatMapper;
    @Resource
    private HttpSession httpSession;
    @Resource
    private PersonalCenterService personalCenterService;
    @Resource
    private FriendMapper friendMapper;
    private static final Logger logger = Logger.getLogger(ChatServiceImpl.class);

    @Override
    public JSONObject getChatMessage() {
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        List<UserChatList> chatUserList = chatMapper.getChatUserListById(myId);//查找用户聊天列表
        if (chatUserList.isEmpty()) {
            JSONObject tmpJson = new JSONObject();
            tmpJson.put("status", 200);
            return tmpJson;
        }
        chatMapper.setChatMsgReceivedByUserAndFriendId(chatUserList, myId);//使聊天用户列表中的用户和本人的聊天消息为已接收状态
        Map<Integer, JSONObject> returnMap = new HashMap<>();//返回的数据

        for (UserChatList userChatList : chatUserList) {//获取并处理返回各个用户的聊天信息
            int friendId = userChatList.getFriendId();
            JSONObject userInfoJson = new JSONObject();
            String name = friendMapper.getFriendNicknameById(friendId, myId);
            if (name != null) {
                userInfoJson.put("name", name);
            } else {
                userInfoJson.put("name", personalCenterService.getUsernameById(friendId));
            }
            userInfoJson.put("hpName", personalCenterService.getHeadPortraitById(friendId));
            List<UserChatMessage> userChatMessageList = chatMapper.getChatMessageById(friendId, myId);//获取聊天消息
            logger.debug("userChatMessageList: " + userChatMessageList);
            if (userChatMessageList == null || userChatMessageList.isEmpty()) {//消息为空
                logger.debug("空消息为" + userChatMessageList);
                userInfoJson.put("messages", userChatMessageList);
                userInfoJson.put("firstTime", userChatList.getCreateTime());
                userInfoJson.put("lastTime", userChatList.getCreateTime());
            } else {//消息不为空
                List<JSONObject> jsonMsgList = new ArrayList<>();
                userInfoJson.put("firstTime", userChatMessageList.get(0).getSendTime());
                Date lastMsgTime = userChatMessageList.get(userChatMessageList.size() - 1).getSendTime();
                if (lastMsgTime.getTime() > userChatList.getCreateTime().getTime()) {
                    userInfoJson.put("lastTime", lastMsgTime);
                } else {
                    userInfoJson.put("lastTime", userChatList.getCreateTime());
                }

                for (UserChatMessage userChatMessage : userChatMessageList) {
                    JSONObject msgJson = new JSONObject();
                    if (userChatMessage.getSenderId() == friendId) {
                        msgJson.put("flag", false);
                    } else {
                        msgJson.put("flag", true);
                    }
                    msgJson.put("content", userChatMessage.getContent());
                    jsonMsgList.add(msgJson);
                }
                userInfoJson.put("messages", jsonMsgList);
            }

            returnMap.put(friendId, userInfoJson);
        }

        JSONObject json = (JSONObject) JSON.toJSON(returnMap);
        json.put("status", 200);
        return json;
    }

    @Override
    public JSONObject getNotReceiveChatMessage() {
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        Map<Integer, List<UserChatMessage>> userChatMessageListMap = new HashMap<>();//保存根据发送者的账号对获取的未接收消息进行分类的数据
        for (UserChatMessage userChatMessage : chatMapper.getNotReceiveChatMessageByUserId(myId)) {//获取未接收的消息并遍历
            int senderId = userChatMessage.getSenderId();
            chatMapper.setChatMsgReceivedByMsgId(userChatMessage.getId());
            if (userChatMessageListMap.containsKey(senderId)) {
                userChatMessageListMap.get(senderId).add(userChatMessage);
            } else {
                userChatMessageListMap.put(senderId, new ArrayList<>(Collections.singletonList(userChatMessage)));
            }
        }

        Map<Integer, JSONObject> returnMap = new HashMap<>();
        for (Map.Entry<Integer, List<UserChatMessage>> entry : userChatMessageListMap.entrySet()) {//处理返回的数据
            int senderId = entry.getKey();
            List<UserChatMessage> userChatMessageList = entry.getValue();
            JSONObject userInfoJson = new JSONObject();
            userInfoJson.put("name", friendMapper.getFriendNicknameById(senderId, myId));
            userInfoJson.put("hpName", personalCenterService.getHeadPortraitById(senderId));
            userInfoJson.put("firstTime", userChatMessageList.get(0).getSendTime());
            userInfoJson.put("lastTime", userChatMessageList.get(userChatMessageList.size() - 1).getSendTime());
            List<JSONObject> jsonMsgList = new ArrayList<>();
            for (UserChatMessage userChatMessage : userChatMessageList) {
                JSONObject msgJson = new JSONObject();
                msgJson.put("flag", false);
                msgJson.put("content", userChatMessage.getContent());
                jsonMsgList.add(msgJson);
            }
            userInfoJson.put("messages", jsonMsgList);
            returnMap.put(senderId, userInfoJson);
        }

        JSONObject json = (JSONObject) JSON.toJSON(returnMap);
        json.put("status", 200);
        return json;
    }

    @Override
    public JSONObject deleteUserFromChatUserList(int currentChatUserId, int myId) {
        chatMapper.deleteUserFromChatUserListById(currentChatUserId, myId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", 200);
        return jsonObject;
    }

    @Override
    public JSONObject getMoreChatContent(int friendId, long time, int myId) {
        List<UserChatMessage> userChatMessageList = chatMapper.getMoreChatContentByIdAndTime(friendId, myId, new Date(time));//获取更多消息
        JSONObject returnJson = new JSONObject();
        if (userChatMessageList == null || userChatMessageList.isEmpty()) {//没有消息
            returnJson.put("hasMsg", false);
            return returnJson;
        }
        returnJson.put("hasMsg", true);
        returnJson.put("firstTime", userChatMessageList.get(userChatMessageList.size() - 1).getSendTime());
        List<JSONObject> objects = new ArrayList<>();
        for (UserChatMessage userChatMessage : userChatMessageList) {//遍历处理获得的消息
            JSONObject object = new JSONObject();
            object.put("content", userChatMessage.getContent());
            if (userChatMessage.getSenderId() == myId) {//本人的消息
                object.put("flag", true);
            } else {
                object.put("flag", false);
            }
            objects.add(object);
        }
        returnJson.put("messages", objects);
        returnJson.put("status", 200);
        return returnJson;
    }

    @Override
    public JSONObject addUserChatMessage(int myId, int currentChatUserId, String msg) {
        JSONObject jsonObject = new JSONObject();
        if (friendMapper.getFriendNicknameById(currentChatUserId, myId) == null) {//非好友
            jsonObject.put("status", 500);
            return jsonObject;
        }

        UserChatMessage userChatMessage = new UserChatMessage();
        userChatMessage.setContent(msg);
        userChatMessage.setSenderId(myId);
        userChatMessage.setReceiverId(currentChatUserId);
        userChatMessage.setSendTime(new Date());
        chatMapper.addUserChatMessage(userChatMessage);//插入消息

        Integer userChatId = chatMapper.getUserChatListIdByUserAndFriendId(myId, currentChatUserId);//检查好友聊天列表中是否存在和本人的聊天
        Date currentDate = new Date();
        if (userChatId == null) {//不存在则新建聊天
            UserChatList userChatList = new UserChatList();
            userChatList.setUserId(currentChatUserId);
            userChatList.setCreateTime(currentDate);
            userChatList.setFriendId(myId);
            chatMapper.addUserChat(userChatList);
        }
        jsonObject.put("status", 200);
        jsonObject.put("lastTime", currentDate);
        return jsonObject;
    }
}
