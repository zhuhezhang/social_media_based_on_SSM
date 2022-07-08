package pers.zhz.pojo;

import java.util.Date;

/**
 * 系统消息类
 */
public class SystemMessage {
    private Integer id;//表id
    private Date sendTime;//消息发送时间
    private Integer senderId;//发送者id
    private Integer receiverId;//接收者id
    private String content;//评论内容
    private Integer messageType;//消息类型，0请求添加好友;1同意添加好友;2拒绝添加好友;3删除好友;4点赞随说;5取消点赞随说;6评论本人的随说;7回复本人的评论;8回复本人的问题;9回复本人的回答
    private Integer casualWordId;//消息相关的随说id
    private Integer questionId;//消息相关的问题id
    private Boolean isEffective;//是否生效，1生效，0失效，默认生效

    public SystemMessage() {
    }

    public SystemMessage(Integer id, Date sendTime, Integer senderId, Integer receiverId, String content, Integer messageType, Integer casualWordId, Integer questionId, Boolean isEffective) {
        this.id = id;
        this.sendTime = sendTime;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.messageType = messageType;
        this.casualWordId = casualWordId;
        this.questionId = questionId;
        this.isEffective = isEffective;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Integer getCasualWordId() {
        return casualWordId;
    }

    public void setCasualWordId(Integer casualWordId) {
        this.casualWordId = casualWordId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Boolean getIsEffective() {
        return isEffective;
    }

    public void setIsEffective(Boolean isEffective) {
        this.isEffective = isEffective;
    }

    @Override
    public String toString() {
        return "SystemMessage{" +
                "id=" + id +
                ", sendTime=" + sendTime +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", content='" + content + '\'' +
                ", messageType=" + messageType +
                ", casualWordId=" + casualWordId +
                ", questionId=" + questionId +
                ", isEffective=" + isEffective +
                '}';
    }
}