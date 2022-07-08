package pers.zhz.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.DigestUtils;
import pers.zhz.mapper.NoLoginMapper;
import pers.zhz.pojo.ConfirmRegisterAndForgetPwd;
import pers.zhz.pojo.User;
import pers.zhz.service.NoLoginService;
import pers.zhz.utils.Utils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Service("noLoginService")//起别名，使spring按名字注入时注入的是实现类而不是接口
public class NoLoginServiceImpl implements NoLoginService {

    @Resource
    private NoLoginMapper noLoginMapper;
    @Resource
    private HttpSession httpSession;
    private static final Logger logger = Logger.getLogger(NoLoginServiceImpl.class);

    @Override
    public int userRegister(String username, String email, String password) throws Exception {
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        int result = 0;
        if (noLoginMapper.isUsernameExist(username)) {//验证用户名邮箱是否被占用
            result++;//result+或+2是为了区分哪个被占用或者说两个都被占用
        }
        if (noLoginMapper.isEmailExist(email)) {
            result += 2;
        }
        if (result != 0) {
            return result;
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setRegisterTime(new Date());
        result = 3 + noLoginMapper.userRegister(user);//用户注册信息插入数据库
        logger.debug("数据库表user插入数据" + user);

        int userId = noLoginMapper.getUserIdByName(username);//根据用户名获取用户id
        String code = UUID.randomUUID().toString().replaceAll("-", "");
        ConfirmRegisterAndForgetPwd ramp = new ConfirmRegisterAndForgetPwd();
        ramp.setUserId(userId);
        ramp.setState(true);
        ramp.setFlag(false);//0注册，1修改密码
        ramp.setCode(code);
        noLoginMapper.insertRegisterOrModifyPwdInfo(ramp);//确认注册信息插入数据库
        logger.debug("数据库表confirm_register_and_forget_pwd插入数据" + ramp);
        String mailContent = "<!DOCTYPE html><html lang='zh-Hans-CN'><head><meta charset='utf-8'></head>"
                + "<body><p>您正在注册lescha账号，请点击 <a href='http://localhost:8080/register/"
                + userId + "/" + code + "'>http://localhost:8080/register/" + userId + "/" + code + "</a>"
                + "进行注册确认，如果不是本人操作请忽略。</p></body></html>";
        Utils.send163Mail(email, "lescha账号注册", mailContent);//发送邮件提示确认注册
        return result;
    }

    @Override
    public int confirmRegister(int userId, String code) {
        int result = noLoginMapper.setStateByCode(code, false, false);//注册成功使code失效
        result += noLoginMapper.setStateById(userId, true);//设置用户账号状态使之生效
        if (result != 2) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动事务回滚
        }
        return result;
    }

    @Override
    public int login(String username, String password) {
        HashMap<String, Object> idAndState = noLoginMapper.login(username, DigestUtils.md5DigestAsHex(password.getBytes()));
        if (idAndState != null) {
            if (Boolean.parseBoolean(idAndState.get("state").toString())) {//用户名、密码验证成功
                int userId = Integer.parseInt(idAndState.get("id").toString());
                noLoginMapper.setIsActiveById(userId, true);//设置用户在线
                httpSession.setAttribute("loginUserId", userId);//保存session，免密登录
                return 0;
            } else {//账号未激活
                return 1;
            }
        } else {//账号密码出错
            return 2;
        }
    }

    @Override
    public void setIsActiveById(int id, boolean isActive) {
        noLoginMapper.setIsActiveById(id, isActive);
    }

    @Override
    public boolean forgetPwd(JSONObject data) throws Exception {
        String email = data.getString("email"), password = data.getString("password");
        Integer userId = noLoginMapper.getIdByEmail(email);
        logger.debug("email:" + email + "对应的用户id:" + userId);
        if (userId == null) {//不存在该用户（邮箱）
            return false;
        }

        String code = UUID.randomUUID().toString().replaceAll("-", "");
        ConfirmRegisterAndForgetPwd ramp = new ConfirmRegisterAndForgetPwd();
        ramp.setUserId(userId);
        ramp.setState(true);
        ramp.setFlag(true);//0注册，1修改密码
        ramp.setCode(code);
        ramp.setNewPassword(DigestUtils.md5DigestAsHex(password.getBytes()));
        noLoginMapper.insertRegisterOrModifyPwdInfo(ramp);//确认注册信息插入数据库

        String mailContent = "<!DOCTYPE html><html lang='zh-Hans-CN'><head><meta charset='utf-8'></head>"
                + "<body><p>您正在修改lescha账号的密码，请点击 <a href='http://localhost:8080/forgetPwd/"
                + userId + "/" + code + "'>http://localhost:8080/forgetPwd/" + userId + "/" + code + "</a>"
                + "进行确认，如果不是本人操作请忽略。</p></body></html>";
        Utils.send163Mail(email, "lescha账号的密码修改", mailContent);//发送邮件提示确认修改密码
        return true;
    }

    @Override
    public int confirmForgetPwd(int userId, String code) {
        String newPwd = noLoginMapper.getPwdByCode(code);//从修改密码表获取新密码
        if (newPwd == null) {
            return 0;
        }
        int result = noLoginMapper.setPwdById(userId, newPwd);//给用户表设置新的密码
        result += noLoginMapper.setStateByCode(code, false, true);//使code失效
        if (result != 2) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//手动事务回滚
        }
        return result;
    }
}
