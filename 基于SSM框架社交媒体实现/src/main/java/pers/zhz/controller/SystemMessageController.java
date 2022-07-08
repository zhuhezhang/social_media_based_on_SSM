package pers.zhz.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pers.zhz.service.PersonalCenterService;
import pers.zhz.service.SystemMessageService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 系统消息相关的控制器
 */
@Controller
public class SystemMessageController {

    @Resource
    private SystemMessageService systemMessageService;
    @Resource
    private PersonalCenterService personalCenterService;
    @Resource
    private HttpSession httpSession;
    private static final Logger logger = Logger.getLogger(SystemMessageController.class);

    /**
     * 返回系统消息界面
     *
     * @return 转发到系统消息界面第一页的string
     */
    @RequestMapping(value = "/systemMessage", method = RequestMethod.GET)
    public String getSystemMessage() {
        return "forward:/systemMessage/1";
    }

    /**
     * 通过请求的页码获取用户的系统消息
     *
     * @param currentPage 当前页码
     * @return 返回系统消息相关的数据
     */
    @RequestMapping(value = "/systemMessage/{currentPage}", method = RequestMethod.GET)
    public ModelAndView getSystemMessage(@PathVariable int currentPage) {
        ModelAndView modelAndView = new ModelAndView();
        if (currentPage < 1) {
            modelAndView.setViewName("404");
            return modelAndView;
        }
        Map<String, List<JSONObject>> messages = systemMessageService.getSystemMessage(currentPage);
        logger.debug("获取系统消息返回的数据" + messages);
        modelAndView.addObject("headPortrait", personalCenterService.getHeadPortraitById((Integer) httpSession.getAttribute("loginUserId")));
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("totalPages", messages.get("totalPages").get(0).getIntValue("totalPages"));
        modelAndView.addObject("messages", messages.get("messages"));
        modelAndView.setViewName("systemMessage");
        return modelAndView;
    }

    /**
     * 回应添加好友
     *
     * @param jsonObject messageId-系统消息id；flag-true同意添加好友，false则拒绝
     * @return state-状态变量（4-成功添加；3-消息已经失效；2-拒绝添加好友）；status-状态码，200成功
     */
    @RequestMapping(value = "/responseAddFriend", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public JSONObject responseAddFriend(@RequestBody JSONObject jsonObject) {
        int messageId = jsonObject.getIntValue("messageId");
        if (messageId < 1) {
            logger.warn("非法回应添加好友，消息id小于1");
            return null;
        }
        Boolean flag = jsonObject.getBoolean("flag");
        if (flag == null) {
            logger.warn("非法回应添加好友的请求，标识位为空");
            return null;
        }
        return systemMessageService.responseAddFriend(messageId, flag);
    }

    /**
     * 根据系统消息账号删除系统消息
     *
     * @param data messageId-消息id
     * @return state-2删除添加好友的消息成功；1删除其他消息成功；其他失败；status-状态码，200成功
     */
    @RequestMapping(value = "/systemMessage", method = RequestMethod.DELETE)
    @ResponseBody
    @Transactional
    public JSONObject deleteSystemMessage(@RequestBody JSONObject data) {
        int messageId = data.getIntValue("messageId");
        if (messageId < 1) {
            logger.warn("非法删除系统消息，消息id小于1");
            return null;
        }
        return systemMessageService.deleteSystemMessage(messageId);
    }
}