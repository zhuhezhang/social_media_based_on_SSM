package pers.zhz.pojo;

import java.util.Date;

/**
 * 问答-回答表实体类
 */
public class QAndAAnswer {
    private Integer id;//表id
    private Integer questionId;//问答-问题id
    private Integer responderId;//回复者id
    private Date responseTime;//回答时间
    private String content;//回复内容
    private Integer replyAnswerId;//回复的问题-回答id'

    public QAndAAnswer() {
    }

    public QAndAAnswer(Integer id, Integer questionId, Integer responderId, Date responseTime, String content, Integer replyAnswerId) {
        this.id = id;
        this.questionId = questionId;
        this.responderId = responderId;
        this.responseTime = responseTime;
        this.content = content;
        this.replyAnswerId = replyAnswerId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getResponderId() {
        return responderId;
    }

    public void setResponderId(Integer responderId) {
        this.responderId = responderId;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getReplyAnswerId() {
        return replyAnswerId;
    }

    public void setReplyAnswerId(Integer replyAnswerId) {
        this.replyAnswerId = replyAnswerId;
    }

    @Override
    public String toString() {
        return "QAndAAnswer{" +
                "id=" + id +
                ", questionId=" + questionId +
                ", responderId=" + responderId +
                ", responseTime=" + responseTime +
                ", content='" + content + '\'' +
                ", replyAnswerId=" + replyAnswerId +
                '}';
    }
}
