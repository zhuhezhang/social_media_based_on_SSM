package pers.zhz.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import pers.zhz.pojo.User;
import pers.zhz.service.NoLoginService;
import pers.zhz.service.PersonalCenterService;
import pers.zhz.service.SetupService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.regex.Pattern;

/**
 * 用户信息修改、密码修改、退出相关的（对应前端设置的）控制器
 */
@Controller
public class SetupController {

    @Resource
    private NoLoginService noLoginService;
    @Resource
    private SetupService setupService;
    @Resource
    private PersonalCenterService personalCenterService;
    private static final Logger logger = Logger.getLogger(SetupController.class);

    /**
     * 退出并使其session失效，再返回登录界面
     *
     * @param httpSession httpSession实体类
     * @return 重定向到首页的string
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    @Transactional
    public String logout(HttpSession httpSession) {
        noLoginService.setIsActiveById(Integer.parseInt(httpSession.getAttribute("loginUserId").toString()), false);
        httpSession.invalidate();
        return "redirect:/";
    }

    /**
     * 返回修改资料界面
     *
     * @param httpSession httpSession实体类
     * @return user-包含用户信息的实体类；视图名称-myProfile；headPortrait-头像路径
     */
    @RequestMapping(value = "/modifyProfile", method = RequestMethod.GET)
    public ModelAndView getModifyProfile(HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("modifyProfile");
        User user = personalCenterService.getUserById(((Integer) httpSession.getAttribute("loginUserId")));
        modelAndView.addObject("user", user);
        modelAndView.addObject("headPortrait", user.getHeadPortrait());
        return modelAndView;
    }

    /**
     * 处理修改资料
     *
     * @param headPortrait 头像文件夹对象
     * @param profile      新资料的json字符串
     * @return state-修改资料状态（1-成功；2-用户名已被占用；3-邮箱已被占用；4-手机号已被占用；5-用户名和邮箱已被占用；
     * 6-用户名和手机号已被占用；7-邮箱和手机号已被占用；9-用户名、手机号、邮箱均已被占用）；hpPath-新头像路径；status-状态码，200成功
     * @throws Exception 保存文件相关的异常
     */
    @RequestMapping(value = "/modifyProfile", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public JSONObject handelModifyProfile(@RequestParam(value = "headPortrait", required = false) MultipartFile headPortrait,
                                          @RequestParam(value = "profile", required = false) String profile) throws Exception {
        if (headPortrait == null && profile == null) {
            logger.warn("提交的数据均为空，非法修改资料的请求");
            return null;
        }
        User user = JSONObject.parseObject(profile, User.class);
        if (user.getUsername() != null && user.getUsername().length() > 11 && user.getUsername().length() < 1) {
            logger.warn("提交的用户名不符合规范，非法修改资料的请求");
            return null;
        }
        if (user.getPhoneNumber() != null && !Pattern.matches("(^\\d{11}$)|(^$)", user.getPhoneNumber())) {
            logger.warn("提交的手机号不符合规范，非法修改资料的请求");
            return null;
        }
        if (user.getEmail() != null && !Pattern.matches("^([a-zA-Z]|[0-9])(\\w|-)+@[a-zA-Z0-9]+\\.([a-zA-Z]{2,4})$", user.getEmail())) {
            logger.warn("邮箱格式出错：" + user.getEmail());
            return null;
        }
        if (user.getHometown() != null && user.getHometown().length() > 20) {
            logger.warn("提交的家乡信息不符合规范，非法修改资料的请求");
            return null;
        }
        if (user.getIntroduce() != null && user.getIntroduce().length() > 150) {
            logger.warn("提交的个人简介信息不符合规范，非法修改资料的请求");
            return null;
        }

        return setupService.updateUserById(headPortrait, user);
    }

    /**
     * 返回修改密码界面（不同于忘记密码请求，修改密码是已登录的，不需要利用邮箱验证，而忘记密码是需要的）
     *
     * @param httpSession httpSession实体类
     * @return 返回用户头像、修改密码的界面文件名
     */
    @RequestMapping(value = "/modifyPwd", method = RequestMethod.GET)
    public ModelAndView getModifyPwd(HttpSession httpSession) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("modifyPwd");
        modelAndView.addObject("headPortrait", personalCenterService.getHeadPortraitById
                ((Integer) httpSession.getAttribute("loginUserId")));
        return modelAndView;
    }

    /**
     * 处理修改密码
     *
     * @param data        password-密码
     * @param httpSession httpSession实体类
     * @return state-1,成功,0,失败；status-状态码，200成功
     */
    @RequestMapping(value = "/modifyPwd", method = RequestMethod.PATCH)
    @ResponseBody
    @Transactional
    public JSONObject modifyPwd(@RequestBody JSONObject data, HttpSession httpSession) {
        String password = data.getString("password");
        if (password.length() < 10 || password.length() > 20) {
            logger.warn("密码格式出错：" + password);
            return null;
        }

        logger.debug(data);
        int id = (Integer) httpSession.getAttribute("loginUserId");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("state", setupService.modifyPwd(id, DigestUtils.md5DigestAsHex(password.getBytes())));
        jsonObject.put("userId", id);
        jsonObject.put("status", 200);
        return jsonObject;
    }
}