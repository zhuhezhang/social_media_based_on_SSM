package pers.zhz.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import pers.zhz.pojo.User;
import pers.zhz.service.CasualWordService;
import pers.zhz.service.PersonalCenterService;
import pers.zhz.service.QAndAService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * 个人中心控制器
 */
@Controller
public class PersonalCenterController {

    private static final Logger logger = Logger.getLogger(PersonalCenterController.class);
    @Resource
    private PersonalCenterService personalCenterService;
    @Resource
    private CasualWordService casualWordService;
    @Resource
    private CasualWordController casualWordController;
    @Resource
    private QAndAService qAndAService;
    @Resource
    private QAndAController qAndAController;
    @Resource
    private HttpSession httpSession;

    /**
     * 返回获取用户自己的资料页面
     *
     * @return user-包含用户信息的实体类；视图名称-myProfile；headPortrait-头像路径
     */
    @RequestMapping(value = "/myProfile", method = RequestMethod.GET)
    public ModelAndView myProfile() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("myProfile");
        User user = personalCenterService.getUserById(((Integer) httpSession.getAttribute("loginUserId")));
        modelAndView.addObject("user", user);
        modelAndView.addObject("headPortrait", user.getHeadPortrait());
        return modelAndView;
    }

    /**
     * 转发到本人随说界面
     *
     * @return 本人随说界面请求
     */
    @RequestMapping(value = "/myCasualWord", method = RequestMethod.GET)
    public String getMyCasualWord() {
        return "forward:/myCasualWord/1";
    }

    /**
     * 返回本人随说界面
     *
     * @param currentPage 当前页码
     * @return 包含随说相关的各种信息
     */
    @RequestMapping(value = "/myCasualWord/{currentPage}", method = RequestMethod.GET)
    public ModelAndView getMyCasualWord(@PathVariable int currentPage) {
        if (currentPage < 1) {
            logger.warn("非法请求本人的随说界面页码，已经将其请求的当前页设置为1");
            currentPage = 1;
        }

        int myId = (Integer) httpSession.getAttribute("loginUserId");
        Map<String, List<JSONObject>> casualWordInfo = casualWordService.getMyCasualWords(myId, currentPage);
        logger.debug("获取到的本人随说信息" + casualWordInfo);
        ModelAndView modelAndView = new ModelAndView();
        casualWordController.commonSetModelAndView(casualWordInfo, currentPage, modelAndView, myId, false, null, myId);
        modelAndView.addObject("currentPath", "/myCasualWord/");
        modelAndView.setViewName("myCasualWord");
        return modelAndView;
    }

    /**
     * 转发到本人问题界面
     *
     * @return 本人问题界面请求
     */
    @RequestMapping(value = "/myQAndA", method = RequestMethod.GET)
    public String getMyQAndA() {
        return "forward:/myQAndA/1";
    }

    /**
     * 返回本人问题界面
     *
     * @param currentPage 当前页码
     * @return 包含问题页面相关的各种信息
     */
    @RequestMapping(value = "/myQAndA/{currentPage}", method = RequestMethod.GET)
    public ModelAndView getMyQAndA(@PathVariable int currentPage) {
        if (currentPage < 1) {
            logger.warn("非法请求本人的问题界面页码，已经将其请求的当前页设置为1");
            currentPage = 1;
        }

        int myId = (Integer) httpSession.getAttribute("loginUserId");
        Map<String, List<JSONObject>> questionInfo = qAndAService.getUserQuestionByUserId(myId, myId, currentPage);
        logger.debug("获取到的本人问题信息" + questionInfo);
        ModelAndView modelAndView = new ModelAndView();
        qAndAController.commonSetModelAndView(questionInfo, currentPage, modelAndView, myId, null, myId, 1);
        modelAndView.addObject("currentPath", "/myQAndA/");
        modelAndView.setViewName("userQAndA");
        return modelAndView;
    }
}










