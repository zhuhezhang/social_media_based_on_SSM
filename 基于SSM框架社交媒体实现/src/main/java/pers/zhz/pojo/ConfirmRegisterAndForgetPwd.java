package pers.zhz.pojo;

import java.io.Serializable;

/**
 * 用户注册激活码和修改密码表
 */
public class ConfirmRegisterAndForgetPwd implements Serializable {
    private Integer id;//表id
    private Integer userId;//用户账号
    private Boolean state;//是否有效，0无效，1有效
    private String code;//注册激活码/修改密码确认码
    private Boolean flag;//注册还是修改密码，0注册，1修改密码
    private String newPassword;//新密码

    public ConfirmRegisterAndForgetPwd(Integer id, Integer userId, Boolean state, String code, Boolean flag, String newPassword) {
        this.id = id;
        this.userId = userId;
        this.state = state;
        this.code = code;
        this.flag = flag;
        this.newPassword = newPassword;
    }

    public ConfirmRegisterAndForgetPwd() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean getSate() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean isFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "ConfirmRegisterAndForgetPwd{" +
                "id=" + id +
                ", userId=" + userId +
                ", state=" + state +
                ", code='" + code + '\'' +
                ", flag=" + flag +
                ", newPassword='" + newPassword + '\'' +
                '}';
    }
}
