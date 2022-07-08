package pers.zhz.pojo;

import java.util.Date;

/**
 * 用户聊天列表表，记录用户与好友的聊天列表
 */
public class UserChatList {
    private Integer id;//表id
    private Date createTime;//创建时间
    private Integer userId;//所属用户账号
    private Integer friendId;//用户好友的账号

    public UserChatList() {
    }

    public UserChatList(Integer id, Date createTime, Integer userId, Integer friendId) {
        this.id = id;
        this.createTime = createTime;
        this.userId = userId;
        this.friendId = friendId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFriendId() {
        return friendId;
    }

    public void setFriendId(Integer friendId) {
        this.friendId = friendId;
    }

    @Override
    public String toString() {
        return "UserChat{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", userId=" + userId +
                ", friendId=" + friendId +
                '}';
    }
}