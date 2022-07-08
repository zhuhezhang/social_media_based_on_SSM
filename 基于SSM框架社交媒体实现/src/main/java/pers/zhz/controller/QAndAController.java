package pers.zhz.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pers.zhz.service.FriendService;
import pers.zhz.service.PersonalCenterService;
import pers.zhz.service.QAndAService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 问答相关的控制器
 */
@Controller
public class QAndAController {

    private static final Logger logger = Logger.getLogger(QAndAController.class);
    @Resource
    private QAndAService qAndAService;
    @Resource
    private PersonalCenterService personalCenterService;
    @Resource
    private HttpSession httpSession;
    @Resource
    private FriendService friendService;

    /**
     * 处理发布问题
     *
     * @param data content-问题内容
     * @return questionId-问题id；name-用户名；time-发布时间；status-状态码，200成功
     */
    @RequestMapping(value = "/qAndA/question", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject publishQAndAQuestion(@RequestBody JSONObject data) {
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        String content = data.getString("content");
        if (content.length() < 1 || content.length() > 5000) {
            logger.warn("用户（id=" + myId + "）提交的问题文本内容长度不符合规范，非法发布问题的请求");
            return null;
        }
        return qAndAService.publishQAndAQuestion(content, myId);
    }

    /**
     * 返回问答界面
     *
     * @return 转发到问答界面的第一页
     */
    @RequestMapping(value = "/qAndA", method = RequestMethod.GET)
    public String getQAndA() {
        return "forward:/qAndA/1";
    }

    /**
     * 返回问答界面
     *
     * @param currentPage 当前页码
     * @return 问答页面的相关信息
     */
    @RequestMapping(value = "/qAndA/{currentPage}", method = RequestMethod.GET)
    public ModelAndView getQAndA(@PathVariable int currentPage) {
        ModelAndView modelAndView = new ModelAndView();
        if (currentPage < 1) {
            modelAndView.setViewName("404");
            return modelAndView;
        }
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        Map<String, List<JSONObject>> questionInfo = qAndAService.getQAndAQuestion(myId, currentPage);
        logger.debug("获取到的问题信息" + questionInfo);
        commonSetModelAndView(questionInfo, currentPage, modelAndView, myId, null, myId, 0);
        modelAndView.addObject("currentPath", "/qAndA/");
        modelAndView.setViewName("qAndA");
        return modelAndView;
    }

    /**
     * 搜索问题
     *
     * @param content 问题内容
     * @return 问题的相关信息
     */
    @RequestMapping(value = "/qAndA/search/{content}", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getQuestionsByContent(@PathVariable String content) {
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        if (content.length() > 5000) {
            logger.warn("用户（id=" + myId + "）提交的问题文本内容长度不符合规范，非法搜素问题的请求");
            return null;
        }
        return qAndAService.getQuestionsByContent(content, myId);
    }

    /**
     * 回答问题
     *
     * @param object content-回答内容；id-问题/回答id；flag-0(非问答详细界面回答问题)，1（问答详细界面回答问题），2（问答详细界面回答回答）
     * @return 回答相关数据
     */
    @RequestMapping(value = "/qAndA/answer", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public JSONObject comment(@RequestBody JSONObject object) {
        int myId = (Integer) httpSession.getAttribute("loginUserId"),
                id = object.getIntValue("id"), flag = object.getIntValue("flag");
        String content = object.getString("content");
        if (id < 1 || (flag != 0 && flag != 1 && flag != 2) || content.length() > 100 || content.length() < 1) {
            logger.warn("用户（id=" + myId + "）非法回答问题的请求（" + object + "）");
            return null;
        }
        return qAndAService.answerQuestion(myId, id, content, flag);
    }

    /**
     * 重定向到返回问题详细页面
     *
     * @param questionId 问题id
     * @return 重定向到返回问题详细页面的string
     */
    @RequestMapping(value = "/qAndA/detail/{questionId}", method = RequestMethod.GET)
    public String getQAndADetail(@PathVariable int questionId) {
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        if (questionId < 1) {
            logger.warn("用户（id=" + myId + "）非法访问问题详细界面的请求（" + questionId + "）");
            return "redirect:/404";
        }
        return "forward:/qAndA/detail/" + questionId + "/1";
    }

    /**
     * 返回问题详细页面
     *
     * @param questionId  问题id
     * @param currentPage 当前页码
     * @return 问题相关数据
     */
    @RequestMapping(value = "/qAndA/detail/{questionId}/{currentPage}", method = RequestMethod.GET)
    public ModelAndView getQAndADetail(@PathVariable int questionId, @PathVariable int currentPage) {
        ModelAndView modelAndView = new ModelAndView();
        if (currentPage < 1 || questionId < 1) {
            modelAndView.setViewName("404");
            return modelAndView;
        }
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        Map<String, List<JSONObject>> qAndADetailInfo = qAndAService.getQAndADetail(myId, questionId, currentPage);//获取问题相关信息

        modelAndView.addObject("headPortrait", personalCenterService.getHeadPortraitById(myId));//用户本人的头像路径
        modelAndView.addObject("userHeadPortrait", qAndADetailInfo.get("questionInfo").get(0).getString("userHeadPortrait"));//该问题发布者的头像路径
        modelAndView.addObject("questionerId", qAndADetailInfo.get("questionInfo").get(0).getIntValue("questionerId"));//问题发布者账号
        modelAndView.addObject("username", qAndADetailInfo.get("questionInfo").get(0).getString("username"));//本人对应问题发布者的用户名/昵称
        modelAndView.addObject("time", qAndADetailInfo.get("questionInfo").get(0).getLongValue("time"));//问题发布时间戳
        modelAndView.addObject("isMyQuestion", qAndADetailInfo.get("questionInfo").get(0).getBooleanValue("isMyQuestion"));//属于本人的问题则赋予其删除问题的权利
        modelAndView.addObject("questionId", questionId);
        modelAndView.addObject("content", qAndADetailInfo.get("questionInfo").get(0).getString("content"));//问题内容
        modelAndView.addObject("answers", qAndADetailInfo.get("answers"));
        modelAndView.addObject("currentPath", "/qAndA/detail/" + questionId + "/");
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("totalPages", qAndADetailInfo.get("questionInfo").get(0).getIntValue("totalPages"));
        modelAndView.setViewName("qAndADetail");
        return modelAndView;
    }

    /**
     * 删除问题
     *
     * @param data questionId-问题id
     */
    @RequestMapping(value = "/qAndA/question", method = RequestMethod.DELETE)
    @ResponseBody
    @Transactional
    public void deleteCasualWord(@RequestBody JSONObject data) {
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        int questionId = data.getIntValue("questionId");
        if (questionId < 1) {
            logger.warn("用户（id=" + myId + "）非法删除问题的请求（" + data + "）");
        } else {
            qAndAService.deleteQuestionById(questionId, myId);
        }
    }

    /**
     * 返回问答界面提取公共的设置ModelAndView
     *
     * @param questionInfo 返回的问题信息
     * @param currentPage  当前页码
     * @param modelAndView 设置的对象
     * @param userId       用户账号
     * @param currentPath  当前请求路径
     * @param myId         我的账号
     * @param flag         0-全部用户问答页面；1-我的问答；2-非好友问答；3-好友问答
     */
    protected void commonSetModelAndView(Map<String, List<JSONObject>> questionInfo, int currentPage,
                                         ModelAndView modelAndView, int userId, String currentPath, int myId, int flag) {
        modelAndView.addObject("headPortrait", personalCenterService.getHeadPortraitById(myId));
        if (flag != 0) {
            modelAndView.addObject("userHeadPortrait", personalCenterService.getHeadPortraitById(userId));
            if (flag == 1) {
                modelAndView.addObject("profileSrc", "/myProfile");
                modelAndView.addObject("profileText", "我的资料");
                modelAndView.addObject("casualWordSrc", "/myCasualWord");
                modelAndView.addObject("casualWordText", "我的随说");
                modelAndView.addObject("qAndAText", "我的问答");
                modelAndView.addObject("name", personalCenterService.getUsernameById(userId));
            } else if (flag == 2) {
                modelAndView.addObject("profileSrc", "/notUserFriendProfile/" + userId);
                modelAndView.addObject("profileText", "TA的资料");
                modelAndView.addObject("casualWordSrc", "/notUserFriendCasualWord/" + userId);
                modelAndView.addObject("casualWordText", "TA的随说");
                modelAndView.addObject("qAndAText", "TA的问答");
                modelAndView.addObject("name", personalCenterService.getUsernameById(userId));
            } else if (flag == 3) {
                modelAndView.addObject("profileSrc", "/userFriendProfile/" + userId);
                modelAndView.addObject("profileText", "好友资料");
                modelAndView.addObject("casualWordSrc", "/userFriendCasualWord/" + userId);
                modelAndView.addObject("casualWordText", "好友随说");
                modelAndView.addObject("qAndAText", "好友问答");
                modelAndView.addObject("name", friendService.getFriendNicknameById(userId));
            }
            modelAndView.setViewName("userQAndA");
        }
        modelAndView.addObject("qAndAs", questionInfo.get("qAndAs"));
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("totalPages", questionInfo.get("totalPages").get(0).getIntValue("totalPages"));
        if (currentPath != null) {
            modelAndView.addObject("currentPath", "/" + currentPath + "/" + userId + "/");
        }
    }
}