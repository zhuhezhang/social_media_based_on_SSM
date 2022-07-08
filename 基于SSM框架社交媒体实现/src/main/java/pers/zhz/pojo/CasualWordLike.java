package pers.zhz.pojo;

import java.util.Date;

/**
 * 随说点赞实体类
 */
public class CasualWordLike {
    private Integer id;//表id
    private Integer casualWordId;//随说id
    private Integer likeUserId;//点赞者id
    private Date likeTime;//点赞随说时间

    public CasualWordLike() {
    }

    public CasualWordLike(Integer id, Integer casualWordId, Integer likeUserId, Date likeTime) {
        this.id = id;
        this.casualWordId = casualWordId;
        this.likeUserId = likeUserId;
        this.likeTime = likeTime;
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

    public Integer getLikeUserId() {
        return likeUserId;
    }

    public void setLikeUserId(Integer likeUserId) {
        this.likeUserId = likeUserId;
    }

    public Date getLikeTime() {
        return likeTime;
    }

    public void setLikeTime(Date likeTime) {
        this.likeTime = likeTime;
    }

    @Override
    public String toString() {
        return "CasualWordLike{" +
                "id=" + id +
                ", casualWordId=" + casualWordId +
                ", likeUserId=" + likeUserId +
                ", likeTime=" + likeTime +
                '}';
    }
}
