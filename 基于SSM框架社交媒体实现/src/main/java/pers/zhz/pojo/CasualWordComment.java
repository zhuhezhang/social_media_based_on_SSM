package pers.zhz.pojo;

import java.util.Date;

/**
 * 随说评论实体类
 */
public class CasualWordComment {
    private Integer id;//表id
    private Integer casualWordId;//随说id
    private Integer commenterId;//评论者id
    private Date commentTime;//评论时间
    private String content;//评论内容
    private Integer replyCommentId;//回复的评论id

    public CasualWordComment() {
    }

    public CasualWordComment(Integer id, Integer casualWordId, Integer commenterId, Date commentTime, String content, Integer replyCommentId) {
        this.id = id;
        this.casualWordId = casualWordId;
        this.commenterId = commenterId;
        this.commentTime = commentTime;
        this.content = content;
        this.replyCommentId = replyCommentId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCasualWordId() {
        return casualWordId;
    }

    public void setCasualWordId(Integer casualWordId) {
        this.casualWordId = casualWordId;
    }

    public Integer getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(Integer commenterId) {
        this.commenterId = commenterId;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getReplyCommentId() {
        return replyCommentId;
    }

    public void setReplyCommentId(Integer replyCommentId) {
        this.replyCommentId = replyCommentId;
    }

    @Override
    public String toString() {
        return "CasualWordComment{" +
                "id=" + id +
                ", casualWordId=" + casualWordId +
                ", commenterId=" + commenterId +
                ", commentTime=" + commentTime +
                ", content='" + content + '\'' +
                ", replyCommentId=" + replyCommentId +
                '}';
    }
}
