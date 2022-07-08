package pers.zhz.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pers.zhz.service.NoLoginService;

import javax.annotation.Resource;
import java.util.regex.Pattern;

/**
 * 未登录便可访问的资源路径（拦截器不拦截）的控制器，包括登录、注册、忘记密码、错误界面
 */
@Controller
public class NoLoginController {

    @Resource
    private NoLoginService noLoginService;
    private static final Logger logger = Logger.getLogger(NoLoginController.class);

    /**
     * 返回主页（登录）界面
     *
     * @return 主页文件名
     */
    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public String login() {
        return "index";
    }

    /*
     * 返回公共modal界面
     *
     * @return modal文件名
    @RequestMapping(value = "/common/modal.html", method = RequestMethod.GET)
    public String modal() {
        return "common/modal";
    }
    */

    /**
     * 返回注册界面
     *
     * @return 注解界面文件名
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String getRegister() {
        return "register";
    }

    /**
     * 处理提交的注册信息并返回首页（登录页）
     *
     * @param data json数据，username-用户名；email-邮箱；password-密码
     * @return json数据，url跳转路径；state注册状态（4-成功注册；1-用户名被占用；2-邮箱已被占用；3-邮箱用户名均被占用）；status-状态码，200成功
     * @throws Exception 发送邮件相关异常
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public JSONObject handelRegister(@RequestBody JSONObject data) throws Exception {
        String username = data.getString("username");
        String email = data.getString("email");
        String password = data.getString("password");
        if (username.length() < 1 || username.length() > 11) {
            logger.warn("用户名格式出错：" + username);
            return null;
        }
        if (!Pattern.matches("^([a-zA-Z]|[0-9])(\\w|-)+@[a-zA-Z0-9]+\\.([a-zA-Z]{2,4})$", email)) {
            logger.warn("邮箱格式出错：" + email);
            return null;
        }
        if (password.length() < 10 || password.length() > 20) {
            logger.warn("密码格式出错：" + password);
            return null;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("state", noLoginService.userRegister(username, email, password));
        jsonObject.put("url", "index");
        jsonObject.put("status", 200);
        return jsonObject;
    }

    /**
     * 处理提交的确认注册信息
     *
     * @param userId 用户账户
     * @param code   32位标识码
     * @return 注册提示信息
     */
    @RequestMapping(value = "/register/{userId}/{code}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    @Transactional
    public String confirmRegister(@PathVariable int userId, @PathVariable String code) {
        if (userId < 1 || code.length() != 32) {
            logger.warn("非法持有信息确认注册（userId：" + userId + "；code：" + code + ")");
            return null;
        }
        int result = noLoginService.confirmRegister(userId, code);
        if (result == 2) {
            return "账号已成功注册！";
        } else {
            return "链接已失效或不存在！";
        }
    }

    /**
     * 处理用户登录
     *
     * @param data username-用户名；password-密码
     * @return url-跳转地址；state-登录状态（0-成功；1-账号未激活；2-账号或密码出错）;；status-状态码，200成功
     */
    @RequestMapping(value = "/login", method = RequestMethod.PATCH)
    @ResponseBody
    @Transactional
    public JSONObject login(@RequestBody JSONObject data) {
        String username = data.getString("username");
        String password = data.getString("password");
        if (username.length() < 1 || username.length() > 11) {
            logger.warn("用户名格式出错：" + username);
            return null;
        }
        if (password.length() < 10 || password.length() > 20) {
            logger.warn("密码格式出错：" + password);
            return null;
        }

        int result = noLoginService.login(username, password);//返回登录者的账号和激活状态
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("state", result);
        if (result == 0) {
            jsonObject.put("url", "/chat");
        }
        jsonObject.put("status", 200);
        return jsonObject;
    }

    /**
     * 返回忘记密码界面
     *
     * @return 密码界面界面的文件名
     */
    @RequestMapping(value = "/forgetPwd", method = RequestMethod.GET)
    public String forgetPwd() {
        return "forgetPwd";
    }

    /**
     * 处理提交的修改密码信息
     *
     * @param data email-邮箱；password-新密码
     * @return state-修改状态（true-成功；false-失败）；status-状态码，200成功
     * @throws Exception 邮件相关异常
     */
    @RequestMapping(value = "/forgetPwd", method = RequestMethod.PATCH)
    @ResponseBody
    @Transactional
    public JSONObject handelForgetPwd(@RequestBody JSONObject data) throws Exception {
        String email = data.getString("email"), password = data.getString("password");
        if (!Pattern.matches("^([a-zA-Z]|[0-9])(\\w|-)+@[a-zA-Z0-9]+\\.([a-zA-Z]{2,4})$", email)) {
            logger.warn("邮箱格式出错：" + email);
            return null;
        }
        if (password.length() < 10 || password.length() > 20) {
            logger.warn("密码格式出错：" + password);
            return null;
        }

        JSONObject reData = new JSONObject();
        reData.put("state", noLoginService.forgetPwd(data));
        reData.put("status", 200);
        return reData;
    }

    /**
     * 处理确认修改密码信息
     *
     * @param userId 用户账号
     * @param code   标识码
     * @return 修改密码提示信息
     */
    @RequestMapping(value = "/forgetPwd/{userId}/{code}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    @Transactional
    public String confirmForgetPwd(@PathVariable int userId, @PathVariable String code) {
        if (userId < 1 || code.length() != 32) {
            logger.warn("非法持有信息确认修改密码（userId：" + userId + "；code：" + code + ")");
            return null;
        }

        if (noLoginService.confirmForgetPwd(userId, code) == 2) {
            return "密码修改成功！";
        } else {
            return "该链接已失效或不存在！";
        }
    }

    /*
     * 返回公共头部界面
     *
     * @return 公共头部界面文件名
    @RequestMapping(value = "/common/header.html", method = RequestMethod.GET)
    public String header() {
        return "common/header";
    }
     */

    /**
     * 返回403的界面
     *
     * @return 返回403的界面文件名
     */
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied() {
        return "403";
    }

    /**
     * 返回404的界面
     *
     * @return 返回404的界面文件名
     */
    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public String notFound() {
        return "404";
    }
}