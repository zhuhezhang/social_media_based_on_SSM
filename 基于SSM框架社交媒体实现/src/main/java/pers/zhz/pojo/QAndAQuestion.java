package pers.zhz.pojo;

import java.util.Date;

/**
 * 问答-问题表实体类
 */
public class QAndAQuestion {
    private Integer id;//表id
    private Integer questionerId;//提问者id
    private Date questionTime;//提问时间
    private String content;//提问内容

    public QAndAQuestion() {
    }

    public QAndAQuestion(Integer id, Integer questionerId, Date questionTime, String content) {
        this.id = id;
        this.questionerId = questionerId;
        this.questionTime = questionTime;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuestionerId() {
        return questionerId;
    }

    public void setQuestionerId(Integer questionerId) {
        this.questionerId = questionerId;
    }

    public Date getQuestionTime() {
        return questionTime;
    }

    public void setQuestionTime(Date questionTime) {
        this.questionTime = questionTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "QAndAQuestion{" +
                "id=" + id +
                ", questionerId=" + questionerId +
                ", questionTime=" + questionTime +
                ", content='" + content + '\'' +
                '}';
    }
}
