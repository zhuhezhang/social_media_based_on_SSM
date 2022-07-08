package pers.zhz.mapper;

import org.apache.ibatis.annotations.Param;
import pers.zhz.pojo.CasualWord;
import pers.zhz.pojo.CasualWordComment;
import pers.zhz.pojo.CasualWordLike;

import java.util.List;

/**
 * 随说相关的mapper
 */
public interface CasualWordMapper {

    /**
     * 插入新发布的随说
     *
     * @param casualWord 随说实体类
     * @return 数据库受影响的行数
     */
    int addCasualWord(CasualWord casualWord);

    /**
     * 根据用户id返回所属好友以及自己的随说的个数
     *
     * @param userId 用户账号
     * @return 返回随说的个数
     */
    int getUserAndFriendCasualWordNumByUserId(@Param("userId") int userId);

    /**
     * 根据用户id返回所属好友以及自己的随说
     *
     * @param userId    用户账号
     * @param pageIndex 获取数据行起始位置，从0开始，每次20行
     * @return 返回随说实体类的list
     */
    List<CasualWord> getUserAndFriendCasualWordsByUserId(@Param("userId") int userId, @Param("pageIndex") int pageIndex);

    /**
     * 根据用户id返回自己随说的个数
     *
     * @param userId 用户账号
     * @return 返回随说的个数
     */
    int getUserCasualWordNumByUserId(@Param("userId") int userId);

    /**
     * 根据用户id返回所属的随说
     *
     * @param userId    用户账号
     * @param pageIndex 获取数据行起始位置(从0开始),每次20行
     * @return 返回随说实体类的list
     */
    List<CasualWord> getUserCasualWordsByUserId(@Param("userId") int userId, @Param("pageIndex") int pageIndex);

    /**
     * 根据用户id和随说id检查用户是否点赞了该随说
     *
     * @param userId       用户id
     * @param casualWordId 随说id
     * @return true点赞；false没有点赞
     */
    Boolean checkIsLikeById(@Param("userId") int userId, @Param("casualWordId") int casualWordId);

    /**
     * 点赞随说，随说点赞数据库表插入
     *
     * @param casualWordLike 随说实体类
     * @return 数据库受影响的行数
     */
    int likeCasualWord(CasualWordLike casualWordLike);

    /**
     * 根据随说id返回随说所属的用户账号
     *
     * @param casualWordId 随说id
     * @return 个人账号
     */
    Integer getCasualWordUserIdByCasualWordId(@Param("casualWordId") int casualWordId);

    /**
     * 取消点赞随说，根据点赞者id和随说的id删除指定记录
     *
     * @param casualWordId 随说id
     * @param likeUserId   点赞者id
     * @return 数据库受影响的行数
     */
    int unlikeCasualWordById(@Param("casualWordId") int casualWordId, @Param("likeUserId") int likeUserId);

    /**
     * 通过评论id获取评论所属的随说id
     *
     * @param commentId 评论id
     * @return 随说id
     */
    int getCasualWordIdByCommentId(@Param("commentId") int commentId);

    /**
     * 随说评论数据库表插入新的评论
     *
     * @param casualWordComment 随说评论实体类
     * @return 数据库受影响的行数
     */
    int addCasualWordComment(CasualWordComment casualWordComment);

    /**
     * 根据随说id获取随说
     *
     * @param casualWordId 随说id
     * @return 随说实体类
     */
    CasualWord getCasualWordByCasualWordId(@Param("casualWordId") int casualWordId);

    /**
     * 根据随说id返回属于该随说的评论数量
     *
     * @param casualWordId 随说id
     * @return 评论数量
     */
    int getCommentsNumByCasualWordId(@Param("casualWordId") int casualWordId);

    /**
     * 根据随说id返回属于该随说的评论列表
     *
     * @param casualWordId 随说id
     * @param pageIndex    获取数据行起始位置(从0开始),每次10行
     * @return 评论列表
     */
    List<CasualWordComment> getCommentsByCasualWordId(@Param("casualWordId") int casualWordId, @Param("pageIndex") int pageIndex);

    /**
     * 根据评论id获取所属的用户id
     *
     * @param commentId 评论id
     * @return 评论所属的用户id
     */
    int getUserIdByCommentId(@Param("commentId") int commentId);

    /**
     * 根据随说id返回其相关的图片/视频文件名
     *
     * @param casualWordId 随说id
     * @return 文件名
     */
    String getImageOrVideoFileNameByCasualWordId(@Param("casualWordId") int casualWordId);

    /**
     * 根据随说id删除随说
     *
     * @param casualWordId 随说id
     */
    void delCasualWordById(@Param("casualWordId") int casualWordId);

    /**
     * 根据随说id删除其相关的点赞数据
     *
     * @param casualWordId 随说评论
     */
    void delCasualWordLikeById(@Param("casualWordId") int casualWordId);

    /**
     * 根据随说id删除其相关的评论
     *
     * @param casualWordId 随说id
     */
    void delCasualWordCommentById(@Param("casualWordId") int casualWordId);

    /**
     * 根据用户账号获取用户的随说id列表
     *
     * @param id 用户账号
     * @return 随说id列表
     */
    List<Integer> getCasualWordIdsByUserId(int id);

    /**
     * 根据本人、好友的账号、两者的随说id列表删除相关的随说点赞记录
     *
     * @param friendCasualWordIdList 好友随说id的list
     * @param friendId               好友账号
     * @param myCasualWordIdList     我的随说id的list
     * @param myId                   我的账号
     */
    void deleteCasualWordLikeById(@Param("friendCasualWordIdList") List<Integer> friendCasualWordIdList, @Param("friendId") int friendId,
                                  @Param("myCasualWordIdList") List<Integer> myCasualWordIdList, @Param("myId") int myId);
}
