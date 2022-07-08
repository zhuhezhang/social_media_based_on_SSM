package pers.zhz.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import pers.zhz.mapper.*;
import pers.zhz.pojo.UserChatList;
import pers.zhz.pojo.SystemMessage;
import pers.zhz.service.FriendService;
import pers.zhz.service.PersonalCenterService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service("friendService")//起别名，使spring按名字注入时注入的是实现类而不是接口
public class FriendServiceImpl implements FriendService {

    @Resource
    private FriendMapper friendMapper;
    @Resource
    private NoLoginMapper noLoginMapper;
    @Resource
    private SystemMessageMapper systemMessageMapper;
    @Resource
    private PersonalCenterService personalCenterService;
    @Resource
    private HttpSession httpSession;
    @Resource
    private ChatMapper chatMapper;
    @Resource
    private CasualWordMapper casualWordMapper;
    private static final Logger logger = Logger.getLogger(FriendServiceImpl.class);

    @Override
    public HashMap<String, String> getUserFriendByUserId(int userId) {
        List<JSONObject> friendList = friendMapper.getUserFriendByUserId(userId);
        logger.debug(friendList);
        HashMap<String, String> returnMap = new HashMap<>();
        for (JSONObject friend : friendList) {
            int id = friend.getIntValue("friend_id");
            returnMap.put(id + "_" + personalCenterService.getHeadPortraitById(id), friend.getString("friend_nickname"));
        }
        return returnMap;
    }

    @Override
    public JSONObject searchUserByName(String username) {
        Integer friendId = noLoginMapper.getUserIdByName(username);//根据用户名获取对应id
        JSONObject jsonObject = new JSONObject();
        if (friendId == null) {//用户不存在
            jsonObject.put("state", 0);
            return jsonObject;
        }
        Integer userSelfId = (Integer) httpSession.getAttribute("loginUserId");
        if (friendId.equals(userSelfId)) {//用户本人
            jsonObject.put("state", 1);
            return jsonObject;
        }

        if (friendMapper.getFriendNicknameById(friendId, userSelfId) != null) {//用户存在该好友
            jsonObject.put("state", 2);
        } else {//用户不存在该好友
            jsonObject.put("state", 3);
        }
        jsonObject.put("id", friendId);
        jsonObject.put("status", 200);
        return jsonObject;
    }

    @Override
    public JSONObject handelAddFriend(int friendId) {
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        JSONObject json = new JSONObject();
        if (systemMessageMapper.checkAddFriendSystemMessageExistById(myId, friendId)) {
            json.put("state", 2);
            return json;//已经添加过该好友，提示等待消息回复
        }
        if (systemMessageMapper.checkAddFriendSystemMessageExistById(friendId, myId)) {
            json.put("state", 3);
            return json;//对方已经发送给好友请求，请前往系统消息中查看
        }
        if (friendMapper.getFriendNicknameById(friendId, myId) != null) {
            json.put("state", 4);
            return json;//已经是好友
        }

        SystemMessage message = new SystemMessage();
        message.setSendTime(new Date());
        message.setSenderId(myId);
        message.setReceiverId(friendId);
        message.setMessageType(0);
        json.put("state", systemMessageMapper.insertNewSystemMessage(message));
        json.put("status", 200);
        return json;
    }

    @Override
    public String getFriendNicknameById(int friendId) {
        int userId = (Integer) httpSession.getAttribute("loginUserId");
        return friendMapper.getFriendNicknameById(friendId, userId);
    }

    @Override
    public JSONObject modifyFriendNickname(String nickname, int friendId) {
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        JSONObject json = new JSONObject();
        json.put("state", friendMapper.modifyFriendNickname(myId, friendId, nickname));
        json.put("status", 200);
        return json;
    }

    @Override
    public JSONObject deleteFriend(int friendId) {
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        SystemMessage message = new SystemMessage();
        message.setMessageType(3);
        message.setSenderId(myId);
        message.setReceiverId(friendId);
        message.setSendTime(new Date());
        int result = systemMessageMapper.insertNewSystemMessage(message);//插入删除的系统消息
        result += friendMapper.deleteFriend(myId, friendId);//执行删除
        chatMapper.deleteUserFromChatUserListById(friendId, myId);//将该好友从好友聊天列表中移除
        chatMapper.deleteChatMessageById(friendId, myId);//删除相关聊天记录
        List<Integer> friendCasualWordIdList = casualWordMapper.getCasualWordIdsByUserId(friendId);
        friendCasualWordIdList.add(0);//防止为空出现异常
        List<Integer> myCasualWordIdList = casualWordMapper.getCasualWordIdsByUserId(myId);
        myCasualWordIdList.add(0);
        casualWordMapper.deleteCasualWordLikeById(friendCasualWordIdList, friendId, myCasualWordIdList, myId);//删除点赞随说的记录
        JSONObject json = new JSONObject();
        json.put("state", result);
        json.put("status", 200);
        return json;
    }

    @Override
    public void addUserChat(int friendId) {
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        if (friendMapper.getFriendNicknameById(friendId, myId) == null) {
            logger.warn(myId + "非法添加好友" + friendId + "到聊天列表的请求，因为相互不是好友");
            return;
        }
        Integer chatListId = chatMapper.getUserChatListIdByUserAndFriendId(friendId, myId);
        if (chatListId == null) {//聊天列表不存在则新增
            UserChatList userChatList = new UserChatList();
            userChatList.setUserId(myId);
            userChatList.setFriendId(friendId);
            userChatList.setCreateTime(new Date());
            chatMapper.addUserChat(userChatList);
        } else {//否则更新时间
            chatMapper.updateUserChatListCreateTimeByUserChatListId(chatListId, new Date());
        }
    }
}
