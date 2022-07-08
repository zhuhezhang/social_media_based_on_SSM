package pers.zhz.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pers.zhz.mapper.NoLoginMapper;
import pers.zhz.mapper.PersonalCenterMapper;
import pers.zhz.mapper.SetupMapper;
import pers.zhz.pojo.User;
import pers.zhz.service.SetupService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;

@Service("setupService")//起别名，使spring按名字注入时注入的是实现类而不是接口
public class SetupServiceImpl implements SetupService {

    @Resource
    private SetupMapper setupMapper;
    @Resource
    private NoLoginMapper noLoginMapper;
    @Resource
    private PersonalCenterMapper personalCenterMapper;
    @Resource
    private HttpSession httpSession;
    private static final Logger logger = Logger.getLogger(SetupServiceImpl.class);

    @Override
    public JSONObject updateUserById(MultipartFile headPortrait, User user) throws Exception {
        int id = (Integer) httpSession.getAttribute("loginUserId");
        JSONObject jsonObject = new JSONObject();
        int result = 0;
        if (user.getUsername() != null) {
            if (noLoginMapper.isUsernameExist(user.getUsername())) {//判断用户名、邮箱、手机号是否存在
                result = 2;
            }
        }
        if (user.getEmail() != null) {
            if (noLoginMapper.isEmailExist(user.getEmail())) {
                result += 3;
            }
        }
        if (user.getPhoneNumber() != null) {
            if (setupMapper.isPhoneNumberExist(user.getPhoneNumber())) {
                result += 4;
            }
        }
        if (result != 0) {//有重复数据则返回
            jsonObject.put("state", result);
            return jsonObject;
        }

        if (headPortrait != null) {//保存头像
            logger.debug(httpSession.getServletContext().getRealPath("userData") + "/headPortrait/");
            String hpParentPath = httpSession.getServletContext().getRealPath("userData") + "/headPortrait/";//头像父路径
            String originHpName = personalCenterMapper.getHeadPortraitById(id);//先删除原来的头像，如果有的话
            if (originHpName != null) {
                if (new File(hpParentPath + originHpName).delete()) {
                    logger.debug("用户" + id + "原头像" + originHpName + "删除成功");
                }
            }

            File file = new File(hpParentPath);
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    jsonObject.put("state", 0);
                    return jsonObject;
                }
            }
            String filename = headPortrait.getOriginalFilename();
            if (filename != null) {
                filename = id + filename.substring(filename.lastIndexOf("."));
            }
            file = new File(hpParentPath + filename);
            headPortrait.transferTo(file);
            jsonObject.put("hpPath", "/userData/headPortrait/" + filename);
            user.setHeadPortrait(filename);
        }
        user.setId(id);
        result = setupMapper.updateUserById(user);
        jsonObject.put("state", result);
        jsonObject.put("status", 200);
        return jsonObject;
    }

    @Override
    public int modifyPwd(int id, String password) {
        return noLoginMapper.setPwdById(id, password);
    }
}