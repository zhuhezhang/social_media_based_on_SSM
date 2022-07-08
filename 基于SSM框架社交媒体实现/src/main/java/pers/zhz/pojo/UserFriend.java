package pers.zhz.pojo;

public class UserFriend {
    private Integer id;//表id
    private Integer friendId;//好友id
    private Integer userId;//该表所属的用户id
    private String friendNickname;//备注好友昵称，默认为好友的昵称

    public UserFriend() {
    }

    public UserFriend(Integer id, Integer friendId, Integer userId, String friendNickname) {
        this.id = id;
        this.friendId = friendId;
        this.userId = userId;
        this.friendNickname = friendNickname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFriendId() {
        return friendId;
    }

    public void setFriendId(Integer friendId) {
        this.friendId = friendId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFriendNickname() {
        return friendNickname;
    }

    public void setFriendNickname(String friendNickname) {
        this.friendNickname = friendNickname;
    }

    @Override
    public String toString() {
        return "UserFriend{" +
                "id=" + id +
                ", friendId=" + friendId +
                ", userId=" + userId +
                ", friendNickname='" + friendNickname + '\'' +
                '}';
    }
}