package pers.zhz.service.impl;

import org.springframework.stereotype.Service;
import pers.zhz.mapper.PersonalCenterMapper;
import pers.zhz.pojo.User;
import pers.zhz.service.PersonalCenterService;

import javax.annotation.Resource;

@Service("personalCenterService")//起别名，使spring按名字注入时注入的是实现类而不是接口
public class PersonalCenterServiceImpl implements PersonalCenterService {

    @Resource
    private PersonalCenterMapper personalCenterMapper;

    @Override
    public String getHeadPortraitById(int id) {
        String fileName = personalCenterMapper.getHeadPortraitById(id);
        if (fileName == null) {
            return "/static/img/login.svg";
        } else {
            return "/userData/headPortrait/" + fileName;
        }
    }

    @Override
    public User getUserById(int id) {
        User user = personalCenterMapper.getUserById(id);
        String headPortrait = user.getHeadPortrait();
        if (headPortrait == null) {
            user.setHeadPortrait("/static/img/login.svg");
        } else {
            user.setHeadPortrait("/userData/headPortrait/" + headPortrait);
        }
        return user;
    }

    @Override
    public String getUsernameById(int userId) {
        return personalCenterMapper.getUsernameById(userId);
    }
}
