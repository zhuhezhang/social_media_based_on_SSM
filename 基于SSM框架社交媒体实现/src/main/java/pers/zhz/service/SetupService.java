package pers.zhz.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import pers.zhz.pojo.User;

/**
 * 设置相关的服务层
 */
public interface SetupService {
    /**
     * 通过id更新用户信息
     *
     * @param headPortrait 用户头像文件对象
     * @param user      user用户资料实体类
     * @return state-修改资料状态（1-成功；2-用户名已被占用；3-邮箱已被占用；4-手机号已被占用；5-用户名和邮箱已被占用；
     * 6-用户名和手机号已被占用；7-邮箱和手机号已被占用；9-用户名、手机号、邮箱均已被占用）；hpPath-新头像路径；status-状态码，200成功
     * @throws Exception 保存文件相关异常
     */
    JSONObject updateUserById(MultipartFile headPortrait, User user) throws Exception;

    /**
     * 修改密码
     *
     * @param id       用户账号
     * @param password 新密码
     * @return 数据库受影响的行数
     */
    int modifyPwd(int id, String password);
}
