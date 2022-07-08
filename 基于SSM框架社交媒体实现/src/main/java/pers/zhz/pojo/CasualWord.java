package pers.zhz.pojo;

import java.util.Date;

/**
 * 随说实体类
 */
public class CasualWord {
    private Integer id;//表id
    private Integer userId;//发布者id
    private Date publishTime;//随说发布时间
    private String content;//文字内容，该字段和图/视频字段至少有一个不为空
    private String pictureOrVideo;//图/视频文件名

    public CasualWord() {
    }

    public CasualWord(Integer id, Integer userId, Date publishTime, String content, String pictureOrVideo) {
        this.id = id;
        this.userId = userId;
        this.publishTime = publishTime;
        this.content = content;
        this.pictureOrVideo = pictureOrVideo;
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

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPictureOrVideo() {
        return pictureOrVideo;
    }

    public void setPictureOrVideo(String pictureOrVideo) {
        this.pictureOrVideo = pictureOrVideo;
    }

    @Override
    public String toString() {
        return "CasualWord{" +
                "id=" + id +
                ", userId=" + userId +
                ", publishTime=" + publishTime +
                ", content='" + content + '\'' +
                ", pictureOrVideo='" + pictureOrVideo + '\'' +
                '}';
    }
}
