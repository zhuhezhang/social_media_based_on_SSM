package pers.zhz.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import pers.zhz.service.CasualWordService;
import pers.zhz.service.FriendService;
import pers.zhz.service.PersonalCenterService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 随说相关的控制器
 */
@Controller
public class CasualWordController {

    private static final Logger logger = Logger.getLogger(CasualWordController.class);
    @Resource
    private CasualWordService casualWordService;
    @Resource
    private PersonalCenterService personalCenterService;
    @Resource
    private HttpSession httpSession;
    @Resource
    private FriendService friendService;

    /**
     * 处理发布随说
     *
     * @param imgOrVideo  图片或视频文件对象
     * @param textContent 随说文本内容
     * @return state-true保存成功，否则失败；casualWordId-随说账号；name-用户名；time-随说发布时间；status-200，操作成功
     * @throws Exception 保存文件相关异常
     */
    @RequestMapping(value = "/casualWord", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject publishCasualWord(@RequestParam(value = "imgOrVideo", required = false) MultipartFile imgOrVideo,
                                        @RequestParam(value = "textContent", required = false) String textContent) throws Exception {
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        if (imgOrVideo == null && textContent == null) {
            logger.warn("用户（id=" + myId + "）提交的数据均为空，非法发布随说的请求");
            return null;
        }
        if (imgOrVideo == null && (textContent.length() < 1 || textContent.length() > 5000)) {
            logger.warn("用户（id=" + myId + "）提交的随说文本内容长度不符合规范，非法发布随说的请求");
            return null;
        }
        return casualWordService.publishCasualWord(imgOrVideo, textContent, myId);
    }

    /**
     * 返回好友及本人随说界面
     *
     * @return 转发到好友及本人随说界面的第一页
     */
    @RequestMapping(value = "/casualWord", method = RequestMethod.GET)
    public String getCasualWord() {
        return "forward:/casualWord/1";
    }

    /**
     * 返回好友及本人随说界面
     *
     * @param currentPage 当前页码
     * @return 好友及本人随说的相关信息
     */
    @RequestMapping(value = "/casualWord/{currentPage}", method = RequestMethod.GET)
    public ModelAndView casualWord(@PathVariable int currentPage) {
        ModelAndView modelAndView = new ModelAndView();
        if (currentPage < 1) {
            modelAndView.setViewName("404");
            return modelAndView;
        }
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        Map<String, List<JSONObject>> casualWordInfo = casualWordService.getUserAndFriendCasualWords(myId, currentPage);
        logger.debug("获取到的随说信息" + casualWordInfo);
        commonSetModelAndView(casualWordInfo, currentPage, modelAndView, myId, null, null, myId);
        modelAndView.addObject("currentPath", "/casualWord/");
        modelAndView.setViewName("casualWord");
        return modelAndView;
    }

    /**
     * 随说点赞或取消点赞
     *
     * @param object casualWordId-随说id；isLike-true点赞，false取消点赞
     * @return state-2成功，其他失败；status-200
     */
    @RequestMapping(value = "/casualWord/like", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public JSONObject casualWordLike(@RequestBody JSONObject object) {
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        int casualWordId = object.getIntValue("casualWordId");
        Boolean isLike = object.getBoolean("isLike");
        if (casualWordId < 1 || isLike == null) {
            logger.warn("用户（id=" + myId + "）非法点赞/取消点赞随说的请求（" + object + "）");
            return null;
        }
        return casualWordService.handelCasualWordLike(casualWordId, myId, isLike);
    }

    /**
     * 评论随说
     *
     * @param object commentContent-评论内容；id-评论/随说id；flag-0(非随说界面评论)，1（随说详细界面评论随说），2（随说详细界面评论评论）
     * @return 评论相关数据
     */
    @RequestMapping(value = "/casualWord/comment", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public JSONObject comment(@RequestBody JSONObject object) {
        int myId = (Integer) httpSession.getAttribute("loginUserId"),
                id = object.getIntValue("id"), flag = object.getIntValue("flag");
        String content = object.getString("commentContent");
        if (id < 1 || (flag != 0 && flag != 1 && flag != 2) || content.length() > 100 || content.length() < 1) {
            logger.warn("用户（id=" + myId + "）非法评论随说的请求（" + object + "）");
            return null;
        }
        return casualWordService.comment(myId, id, content, flag);
    }

    /**
     * 重定向到返回随说详细页面
     *
     * @param casualWordId 随说id
     * @return 重定向到返回随说详细页面的string
     */
    @RequestMapping(value = "/casualWord/detail/{casualWordId}", method = RequestMethod.GET)
    public String getCasualWordDetail(@PathVariable int casualWordId) {
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        if (casualWordId < 1) {
            logger.warn("用户（id=" + myId + "）非法访问随说详细界面的请求（" + casualWordId + "）");
            return "redirect:/404";
        }
        return "forward:/casualWord/detail/" + casualWordId + "/1";
    }

    /**
     * 返回随说详细页面
     *
     * @param casualWordId 随说id
     * @param currentPage  当前页码
     * @return 随说相关数据
     */
    @RequestMapping(value = "/casualWord/detail/{casualWordId}/{currentPage}", method = RequestMethod.GET)
    public ModelAndView getCasualWordDetail(@PathVariable int casualWordId, @PathVariable int currentPage) {
        ModelAndView modelAndView = new ModelAndView();
        if (currentPage < 1 || casualWordId < 1) {
            modelAndView.setViewName("404");
            return modelAndView;
        }
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        Map<String, List<JSONObject>> casualWordDetailInfo = casualWordService.getCasualWordDetail(myId, casualWordId, currentPage);//获取随说
        if (casualWordDetailInfo == null) {
            logger.warn("用户（id=" + myId + "）非法访问非好友/非本人的随说详细界面的请求（" + casualWordId + "）");
            return null;
        }

        modelAndView.addObject("headPortrait", personalCenterService.getHeadPortraitById(myId));//用户本人的头像路径
        modelAndView.addObject("userHeadPortrait", casualWordDetailInfo.get("casualWordInfo").get(0).getString("userHeadPortrait"));//该随说发布者的头像路径
        modelAndView.addObject("publisherId", casualWordDetailInfo.get("casualWordInfo").get(0).getIntValue("publisherId"));//随说发布者账号
        modelAndView.addObject("username", casualWordDetailInfo.get("casualWordInfo").get(0).getString("username"));//本人对应随说发布者的用户昵称
        modelAndView.addObject("time", casualWordDetailInfo.get("casualWordInfo").get(0).getLongValue("time"));//随说发布时间戳
        modelAndView.addObject("isMyCasualWord", casualWordDetailInfo.get("casualWordInfo").get(0).getBooleanValue("isMyCasualWord"));//属于本人的随说则赋予其删除随说的权利
        modelAndView.addObject("casualWordId", casualWordId);
        modelAndView.addObject("textContent", casualWordDetailInfo.get("casualWordInfo").get(0).getString("textContent"));//随说文字内容
        modelAndView.addObject("image", casualWordDetailInfo.get("casualWordInfo").get(0).getString("image"));//随说配图路径
        modelAndView.addObject("video", casualWordDetailInfo.get("casualWordInfo").get(0).getString("video"));//随说的视频路径
        modelAndView.addObject("isLike", casualWordDetailInfo.get("casualWordInfo").get(0).getBooleanValue("isLike"));//是否点赞
        modelAndView.addObject("comments", casualWordDetailInfo.get("comments"));
        modelAndView.addObject("currentPath", "/casualWord/detail/" + casualWordId + "/");
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("totalPages", casualWordDetailInfo.get("casualWordInfo").get(0).getIntValue("totalPages"));
        modelAndView.setViewName("casualWordDetail");
        return modelAndView;
    }

    /**
     * 删除随说
     *
     * @param data casualWordId-随说id
     * @return status-200，操作成功
     */
    @RequestMapping(value = "/casualWord", method = RequestMethod.DELETE)
    @ResponseBody
    @Transactional
    public JSONObject deleteCasualWord(@RequestBody JSONObject data) {
        int myId = (Integer) httpSession.getAttribute("loginUserId");
        int casualWordId = data.getIntValue("casualWordId");
        if (casualWordId < 1) {
            logger.warn("用户（id=" + myId + "）非法删除随说的请求（" + data + "）");
            return null;
        }
        casualWordService.deleteCasualWordById(casualWordId, myId);//删除随说
        data = new JSONObject();
        data.put("status", 200);
        return data;
    }

    /**
     * 返回随说界面提取公共的设置ModelAndView
     *
     * @param casualWordInfo 返回的随说信息
     * @param currentPage    当前页码
     * @param modelAndView   设置的对象
     * @param userId         用户账号
     * @param nameFlag       区别name字段是获取昵称还是用户名，null-不需要设置；false-用户名；true-好友昵称
     * @param currentPath    当前请求路径
     * @param myId           我的账号，userId!=myId则需要设置userHeadPortrait字段
     */
    protected void commonSetModelAndView(Map<String, List<JSONObject>> casualWordInfo, int currentPage,
                                         ModelAndView modelAndView, int userId, Boolean nameFlag, String currentPath, int myId) {
        modelAndView.addObject("headPortrait", personalCenterService.getHeadPortraitById(myId));
        if (userId != myId) {
            modelAndView.addObject("userHeadPortrait", personalCenterService.getHeadPortraitById(userId));
        }
        if (nameFlag != null) {
            if (nameFlag) {
                modelAndView.addObject("name", friendService.getFriendNicknameById(userId));
            } else {
                modelAndView.addObject("name", personalCenterService.getUsernameById(userId));
            }
        }
        modelAndView.addObject("casualWords", casualWordInfo.get("casualWords"));
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("totalPages", casualWordInfo.get("totalPages").get(0).getIntValue("totalPages"));
        if (currentPath != null) {
            modelAndView.addObject("currentPath", "/" + currentPath + "/" + userId + "/");
            modelAndView.setViewName(currentPath);
        }
    }
}