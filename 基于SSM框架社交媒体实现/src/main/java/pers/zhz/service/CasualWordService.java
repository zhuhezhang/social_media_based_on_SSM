package pers.zhz.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * 随说相关的服务层
 */
public interface CasualWordService {

    /**
     * 处理发布随说的
     *
     * @param imgOrVideo  随说的视频或图片
     * @param textContent 随说内容
     * @param myId        我的账号
     * @return state-true保存成功，否则失败；casualWordId-随说账号；name-用户名；time-随说发布时间；status-200，操作成功
     * @throws Exception 文件相关异常
     */
    JSONObject publishCasualWord(MultipartFile imgOrVideo, String textContent, int myId) throws Exception;

    /**
     * 获取所属用户的好友、本人自己的随说相关信息
     *
     * @param myId        本人id
     * @param currentPage 页码
     * @return casualWords-随说列表；totalPages-随说页数
     */
    Map<String, List<JSONObject>> getUserAndFriendCasualWords(int myId, int currentPage);

    /**
     * 获取用户本人自己的随说相关信息
     *
     * @param myId        本人账号
     * @param currentPage 页码
     * @return casualWords-随说列表；totalPages-随说页数
     */
    Map<String, List<JSONObject>> getMyCasualWords(int myId, int currentPage);

    /**
     * 获取非用户好友的随说相关信息
     *
     * @param userId      用户id
     * @param currentPage 页码
     * @return casualWords-随说列表；totalPages-随说页数
     */
    Map<String, List<JSONObject>> getNotUserFriendCasualWord(int userId, int currentPage);

    /**
     * 获取用户好友的随说相关信息
     *
     * @param myId        本人账号
     * @param friendId    好友id
     * @param currentPage 页码
     * @return casualWords-随说列表；totalPages-随说页数
     */
    Map<String, List<JSONObject>> getUserFriendCasualWords(int myId, int friendId, int currentPage);

    /**
     * 处理点赞或者取消点赞的
     *
     * @param casualWordId 随说id
     * @param myId         我的id
     * @param isLike       true点赞/false取消点赞
     * @return 数据库影响的行数
     */
    JSONObject handelCasualWordLike(int casualWordId, int myId, boolean isLike);

    /**
     * 处理评论
     *
     * @param myId    我的id
     * @param content 评论内容
     * @param id      评论/随说id
     * @param flag    0(非随说界面评论)，1（随说详细界面评论随说），2（随说详细界面评论评论）
     * @return json相关数据，time-时间；commentId-评论id；myName-我的名字；respondentId-被回复者id；respondentName-被回复者姓名
     */
    JSONObject comment(int myId, int id, String content, int flag);

    /**
     * 根据随说id获取随说详细信息
     *
     * @param myId         本人账号
     * @param casualWordId 随说id
     * @param currentPage  当前评论页码
     * @return comments-随说评论列表；casualWordInfo-说说相关信息
     */
    Map<String, List<JSONObject>> getCasualWordDetail(int myId, int casualWordId, int currentPage);

    /**
     * 根据随说id删除随说，及其相关的点赞、评论、系统通知信息
     *
     * @param casualWordId 随说id
     * @param myId         我的账号
     */
    void deleteCasualWordById(int casualWordId, int myId);
}
