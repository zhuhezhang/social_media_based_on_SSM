package pers.zhz.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pers.zhz.service.ChatService;
import pers.zhz.service.PersonalCenterService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * 聊天消息相关的控制器
 */
@Controller
public class ChatController {

    @Resource
    private HttpSession httpSession;
    @Resource
    private PersonalCenterService personalCenterService;
    @Resource
    private ChatService chatService;
    private static final Logger logger = Logger.getLogger(ChatController.class);

    /**
     * 返回聊天界面
     *
     * @return 聊天界面相关信息，其中chatData聊天数据数据为{"1":{"name":"zhz","hpName": "1.jpg","firstTime":1648989733570,
     * "lastTime":1648989733576,messages":[{"flag":false,"content":"消息1"},{"flag":true,"content":"消息2"}]},
     * "status":200}, 1表示好友的账号；name名字；hpName为头像名称；firstTime第一条消息的时间戳；lastTime最新消息的时间戳；
     * messages为消息列表，升序排列，其中flag为false时表示为好友的消息，否则为本人的消息，content为消息内容; status-状态码，200则操作成功
     */
    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    @Transactional
    public ModelAndView getChat() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("headPortrait", personalCenterService.getHeadPortraitById(
                (Integer) httpSession.getAttribute("loginUserId")));
        modelAndView.addObject("chatData", chatService.getChatMessage());
        modelAndView.setViewName("chat");
        return modelAndView;
    }

    /**
     * 获取用户未接收的聊天消息
     *
     * @return 格式基本同上面getChatMessage()返回的
     */
    @RequestMapping(value = "/notReceiveChatMessage", method = RequestMethod.GET)
    @ResponseBody
    @Transactional
    public JSONObject getNotReceiveChatMessage() {
        return chatService.getNotReceiveChatMessage();
    }

    /**
     * 将指定聊天用户移除出聊天列表
     *
     * @param data currentChatUserId-用户id
     * @return status-200则表示移除成功，否则失败
     */
    @RequestMapping(value = "/chatUserList", method = RequestMethod.DELETE)
    @ResponseBody
    public JSONObject deleteUserFromChatUserList(@RequestBody JSONObject data) {
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        int currentChatUserId = data.getIntValue("currentChatUserId");
        if (currentChatUserId < 1) {
            logger.warn(myId + "非法将指定聊天用户移除出聊天列表");
            return null;
        }
        logger.debug(data);
        return chatService.deleteUserFromChatUserList(currentChatUserId, myId);
    }

    /**
     * 根据用户账号和第一条消息的时间戳获取该时间前面的10条消息
     *
     * @param friendId 好友账号
     * @param time     时间戳
     * @return 消息相关数据
     */
    @RequestMapping(value = "/moreChatContent/{friendId}/{time}", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getMoreChatContent(@PathVariable int friendId, @PathVariable long time) {
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        if (friendId < 1) {
            logger.warn(myId + "非法请求更多消息（friendId=" + friendId + "）");
            return null;
        }
        if (Long.toString(time).length() != 13) {
            logger.warn(myId + "非法请求更多消息（time=" + time + "）");
            return null;
        }
        return chatService.getMoreChatContent(friendId, time, myId);
    }

    /**
     * 处理发送聊天信息
     *
     * @param data data–currentChatUserId-好友id；msg-消息内容
     * @return status-200成功，否则失败；time-消息时间戳
     */
    @RequestMapping(value = "/chat", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public JSONObject postChat(@RequestBody JSONObject data) {
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        int currentChatUserId = data.getIntValue("currentChatUserId");
        if (currentChatUserId < 1) {
            logger.warn(myId + "非法发送聊天消息，好友账号不符合规范");
            return null;
        }
        String msg = data.getString("msg");
        if (msg.length() < 1 || msg.length() > 1000) {
            logger.warn(myId + "非法发送聊天消息，消息内容过长");
            return null;
        }
        return chatService.addUserChatMessage(myId, currentChatUserId, msg);
    }
}
