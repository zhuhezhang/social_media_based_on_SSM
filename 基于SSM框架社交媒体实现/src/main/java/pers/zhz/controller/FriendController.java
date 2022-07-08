package pers.zhz.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pers.zhz.pojo.User;
import pers.zhz.service.CasualWordService;
import pers.zhz.service.FriendService;
import pers.zhz.service.PersonalCenterService;
import pers.zhz.service.QAndAService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 好友相关的控制器
 */
@Controller
public class FriendController {
    @Resource
    private FriendService friendService;
    private static final Logger logger = Logger.getLogger(FriendController.class);
    @Resource
    private HttpSession httpSession;
    @Resource
    private PersonalCenterService personalCenterService;
    @Resource
    private CasualWordController casualWordController;
    @Resource
    private CasualWordService casualWordService;
    @Resource
    private QAndAController qAndAController;
    @Resource
    private QAndAService qAndAService;

    /**
     * 返回好友界面
     *
     * @return 好友界面相关信息，包含视图、头像、好友列表
     */
    @RequestMapping(value = "/friend", method = RequestMethod.GET)
    public ModelAndView friend() {
        ModelAndView modelAndView = new ModelAndView();
        int userId = (Integer) httpSession.getAttribute("loginUserId");
        modelAndView.addObject("friends", friendService.getUserFriendByUserId(userId));
        modelAndView.addObject("headPortrait", personalCenterService.getHeadPortraitById(userId));
        modelAndView.setViewName("friend");
        return modelAndView;
    }

    /**
     * 根据用户名搜索用户，并跳转到其资料页面
     *
     * @param username 用户名
     * @return id-用户id; state-0没有该用户；1-用户本人；2-存在该好友；3-不存在该好友；status-状态码，200成功
     */
    @RequestMapping(value = "/user/name/{username}", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject searchUserByUsername(@PathVariable String username) {
        if (username.length() < 1 || username.length() > 11) {
            logger.warn("非法搜索不符合规范的用户名：" + username);
            return null;
        }
        logger.debug(username);
        return friendService.searchUserByName(username);
    }

    /**
     * 根据用户提供的其他用户账号返回后者资料界面（不属于用户好友的）
     *
     * @param id 用户账号
     * @return 非好友相关界面、资料
     */
    @RequestMapping(value = "/notUserFriendProfile/{id}", method = RequestMethod.GET)
    public ModelAndView getNotUserFriendProfile(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView();
        if (id < 1) {
            modelAndView.setViewName("404");
            return modelAndView;
        }
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        if (myId == id) {//用户本人，跳转到用户自己的界面
            modelAndView.setViewName("redirect:/myProfile");
            return modelAndView;
        }
        if (friendService.getFriendNicknameById(id) != null) {//用户存在该好友，转发到存在该好友的界面
            modelAndView.setViewName("redirect:/userFriendProfile/" + id);
            return modelAndView;
        }
        User user = personalCenterService.getUserById(id);
        user.setId(id);
        modelAndView.addObject("user", user);
        modelAndView.addObject("headPortrait", personalCenterService.getHeadPortraitById(myId));
        modelAndView.setViewName("notUserFriendProfile");
        return modelAndView;
    }

    /**
     * 处理添加好友请求
     *
     * @param data friendId-好友账号
     * @return state-1成功发送方添加请求；2已经发送过添加请求；3是对方已经发送给好友请求，请前往系统消息中查看；0请求添加失败；status-状态码，200成功
     */
    @RequestMapping(value = "/addFriend", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject handelAddFriend(@RequestBody JSONObject data) {
        int friendId = data.getIntValue("friendId");
        if (friendId < 1) {
            logger.warn("非法添加好友的请求，账号不符合规范");
            return null;
        }
        logger.debug("要添加的好友账号：" + data);
        return friendService.handelAddFriend(friendId);
    }

    /**
     * 根据用户提供的其他用户账号返回后者资料界面（属于用户好友的）
     *
     * @param id 好友账号
     * @return 好友相关界面、资料
     */
    @RequestMapping(value = "/userFriendProfile/{id}", method = RequestMethod.GET)
    public ModelAndView getUserFriendProfile(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView();
        if (id < 1) {
            modelAndView.setViewName("404");
            return modelAndView;
        }
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        if (myId == id) {//用户本人，跳转到用户自己的界面
            modelAndView.setViewName("redirect:/myProfile");
            return modelAndView;
        }
        if (friendService.getFriendNicknameById(id) == null) {//用户不存在该好友，转发到不存在该好友的界面
            modelAndView.setViewName("redirect:/notUserFriendProfile/" + id);
            return modelAndView;
        }
        User user = personalCenterService.getUserById(id);
        user.setId(id);
        modelAndView.addObject("user", user);
        modelAndView.addObject("nickname", friendService.getFriendNicknameById(id));
        modelAndView.addObject("headPortrait", personalCenterService.getHeadPortraitById(myId));
        modelAndView.setViewName("userFriendProfile");
        return modelAndView;
    }

    /**
     * 修改好友昵称
     *
     * @param data nickname-好友新昵称；friendId-好友账号
     * @return state-状态变量（1-成功，其他失败）；status-状态码，200成功
     */
    @RequestMapping(value = "/friendNickname", method = RequestMethod.PATCH)
    @ResponseBody
    public JSONObject modifyFriendNickname(@RequestBody JSONObject data) {
        String nickname = data.getString("nickname");
        if (nickname.length() < 1 || nickname.length() > 11) {
            logger.warn("非法修改好友昵称，长度不符合规范");
            return null;
        }
        int friendId = data.getIntValue("friendId");
        if (friendId < 1) {
            logger.warn("非法修改好友昵称，好友账号小于0");
            return null;
        }
        return friendService.modifyFriendNickname(nickname, friendId);
    }

    /**
     * 删除好友
     *
     * @param data friendId-好友账号
     * @return state（3-成功，其他失败）；status-状态码，200成功
     */
    @RequestMapping(value = "/friend", method = RequestMethod.DELETE)
    @ResponseBody
    @Transactional
    public JSONObject deleteFriend(@RequestBody JSONObject data) {
        int friendId = data.getIntValue("friendId");
        if (friendId < 0) {
            logger.warn("非法删除好友请求，好友账号小于0");
            return null;
        }
        return friendService.deleteFriend(friendId);
    }

    /**
     * 转发到返回用户非好友随说界面
     *
     * @param userId 用户id
     * @return 转发路径string值
     */
    @RequestMapping(value = "/notUserFriendCasualWord/{userId}", method = RequestMethod.GET)
    public String getNotUserFriendCasualWord(@PathVariable int userId) {
        if (userId < 1) {
            logger.warn("持有小于1的用户id非法访问非好友随说界面");
            return null;
        }
        return "forward:/notUserFriendCasualWord/" + userId + "/1";
    }

    /**
     * 转发到返回用户非好友随说界面
     *
     * @param userId      用户id
     * @param currentPage 当前页码
     * @return 转发到返回用户非好友随说界面的相关信息
     */
    @RequestMapping(value = "/notUserFriendCasualWord/{userId}/{currentPage}", method = RequestMethod.GET)
    public ModelAndView getNotUserFriendCasualWord(@PathVariable int userId, @PathVariable int currentPage) {
        ModelAndView modelAndView = new ModelAndView();
        if (currentPage < 1 || userId < 1) {
            modelAndView.setViewName("404");
            return modelAndView;
        }
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        if (myId == userId) {//是本人，重定向返回本人的随说界面
            modelAndView.setViewName("redirect:/myCasualWord/" + currentPage);
            return modelAndView;
        }
        if (friendService.getFriendNicknameById(userId) != null) {//本人好友，重定向到非好友随说界面
            modelAndView.setViewName("redirect:/userFriendCasualWord/" + userId + "/" + currentPage);
            return modelAndView;
        }
        Map<String, List<JSONObject>> casualWordInfo = casualWordService.getNotUserFriendCasualWord(userId, currentPage);
        casualWordController.commonSetModelAndView(casualWordInfo, currentPage, modelAndView, userId, false, "notUserFriendCasualWord", myId);
        modelAndView.addObject("userId", userId);
        logger.debug("非好友随说返回的modalAndView" + modelAndView);
        return modelAndView;
    }

    /**
     * 转发到返回用户好友随说界面
     *
     * @param friendId 好友账号
     * @return 转发路径string值
     */
    @RequestMapping(value = "/userFriendCasualWord/{friendId}", method = RequestMethod.GET)
    public String getUserFriendCasualWord(@PathVariable int friendId) {
        if (friendId < 1) {
            logger.warn("持有小于1的好友id非法访问好友随说界面");
            return null;
        }
        return "forward:/userFriendCasualWord/" + friendId + "/1";
    }

    /**
     * 返回用户好友随说界面
     *
     * @param friendId    好友id
     * @param currentPage 当前页码
     * @return 好友随说界面相关信息
     */
    @RequestMapping(value = "/userFriendCasualWord/{friendId}/{currentPage}", method = RequestMethod.GET)
    public ModelAndView getUserFriendCasualWord(@PathVariable int friendId, @PathVariable int currentPage) {
        ModelAndView model = new ModelAndView();
        if (currentPage < 1 || friendId < 1) {
            model.setViewName("404");
            return model;
        }
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        if (myId == friendId) {//是本人，重定向返回本人的随说界面
            model.setViewName("redirect:/myCasualWord/" + currentPage);
            return model;
        }
        if (friendService.getFriendNicknameById(friendId) == null) {//非本人好友，重定向到非好友随说界面
            model.setViewName("redirect:/notUserFriendCasualWord/" + friendId + "/" + currentPage);
            return model;
        }

        Map<String, List<JSONObject>> casualWordInfo = casualWordService.getUserFriendCasualWords(myId, friendId, currentPage);
        casualWordController.commonSetModelAndView(casualWordInfo, currentPage, model, friendId, true, "userFriendCasualWord", myId);
        model.addObject("userId", friendId);
        logger.debug("好友随说返回的modalAndView" + model);
        return model;
    }

    /**
     * 处理添加好友到聊天列表的请求，成功后让其跳转到聊天界面
     *
     * @param data friendId-好友账号
     */
    @RequestMapping(value = "/chatUserList", method = RequestMethod.POST)
    @ResponseBody
    public void addUserChat(@RequestBody JSONObject data) {
        int friendId = data.getInteger("friendId");
        if (friendId < 1) {
            return;
        }
        friendService.addUserChat(friendId);
    }

    /**
     * 转发到返回用户非好友问答界面
     *
     * @param userId 用户id
     * @return 转发路径string值
     */
    @RequestMapping(value = "/notUserFriendQAndA/{userId}", method = RequestMethod.GET)
    public String getNotUserFriendQAndA(@PathVariable int userId) {
        if (userId < 1) {
            logger.warn("持有小于1的用户id非法访问非好友问答界面");
            return null;
        }
        return "forward:/notUserFriendQAndA/" + userId + "/1";
    }

    /**
     * 转发到返回用户非好友问答界面
     *
     * @param userId      用户id
     * @param currentPage 当前页码
     * @return 转发到返回用户非好友问答界面的相关信息
     */
    @RequestMapping(value = "/notUserFriendQAndA/{userId}/{currentPage}", method = RequestMethod.GET)
    public ModelAndView getNotUserFriendQAndA(@PathVariable int userId, @PathVariable int currentPage) {
        ModelAndView modelAndView = new ModelAndView();
        if (currentPage < 1 || userId < 1) {
            modelAndView.setViewName("404");
            return modelAndView;
        }
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        if (myId == userId) {//是本人，重定向返回本人的问答界面
            modelAndView.setViewName("redirect:/myQAndA/" + currentPage);
            return modelAndView;
        }
        if (friendService.getFriendNicknameById(userId) != null) {//本人好友，重定向到非好友问答界面
            modelAndView.setViewName("redirect:/userFriendQAndA/" + userId + "/" + currentPage);
            return modelAndView;
        }
        Map<String, List<JSONObject>> questionInfo = qAndAService.getUserQuestionByUserId(myId, userId, currentPage);
        qAndAController.commonSetModelAndView(questionInfo, currentPage, modelAndView, userId, "notUserFriendQAndA", myId, 2);
        logger.debug("非好友问答返回的modalAndView" + modelAndView);
        return modelAndView;
    }

    /**
     * 转发到返回用户好友问答界面
     *
     * @param friendId 好友账号
     * @return 转发路径string值
     */
    @RequestMapping(value = "/userFriendQAndA/{friendId}", method = RequestMethod.GET)
    public String getUserFriendQAndA(@PathVariable int friendId) {
        if (friendId < 1) {
            logger.warn("持有小于1的好友id非法访问好友问答界面");
            return null;
        }
        return "forward:/userFriendQAndA/" + friendId + "/1";
    }

    /**
     * 返回用户好友随说界面
     *
     * @param friendId    好友id
     * @param currentPage 当前页码
     * @return 好友随说界面相关信息
     */
    @RequestMapping(value = "/userFriendQAndA/{friendId}/{currentPage}", method = RequestMethod.GET)
    public ModelAndView getUserFriendQAndA(@PathVariable int friendId, @PathVariable int currentPage) {
        ModelAndView model = new ModelAndView();
        if (currentPage < 1 || friendId < 1) {
            model.setViewName("404");
            return model;
        }
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        if (myId == friendId) {//是本人，重定向返回本人的问答界面
            model.setViewName("redirect:/myQAndA/" + currentPage);
            return model;
        }
        if (friendService.getFriendNicknameById(friendId) == null) {//非本人好友，重定向到非好友问答界面
            model.setViewName("redirect:/notUserFriendQAndA/" + friendId + "/" + currentPage);
            return model;
        }

        Map<String, List<JSONObject>> questionInfo = qAndAService.getUserQuestionByUserId(myId, friendId, currentPage);
        qAndAController.commonSetModelAndView(questionInfo, currentPage, model, friendId, "userFriendQAndA", myId, 3);
        logger.debug("好友问答返回的modalAndView" + model);
        return model;
    }
}