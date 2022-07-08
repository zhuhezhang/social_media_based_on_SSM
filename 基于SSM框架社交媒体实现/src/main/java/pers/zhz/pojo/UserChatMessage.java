package pers.zhz.pojo;

import java.util.Date;

/**
 * 用户聊天消息表
 */
public class UserChatMessage {
    private Integer id;//表id
    private String content;//消息内容
    private Boolean receiveState;//接收状态，0未接收，1接收，默认未接收
    private Date sendTime;//消息发送时间
    private Integer senderId;//发送者账号
    private Integer receiverId;//接收者账号

    public UserChatMessage() {
    }

    public UserChatMessage(Integer id, String content, Boolean receiveState, Date sendTime, Integer senderId, Integer receiverId) {
        this.id = id;
        this.content = content;
        this.receiveState = receiveState;
        this.sendTime = sendTime;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getReceiveState() {
        return receiveState;
    }

    public void setReceiveState(Boolean receiveState) {
        this.receiveState = receiveState;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    public String toString() {
        return "UserChatMessage{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", receiveState=" + receiveState +
                ", sendTime=" + sendTime +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                '}';
    }
}