package pers.zhz.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pers.zhz.mapper.CasualWordMapper;
import pers.zhz.mapper.FriendMapper;
import pers.zhz.mapper.PersonalCenterMapper;
import pers.zhz.mapper.SystemMessageMapper;
import pers.zhz.pojo.CasualWord;
import pers.zhz.pojo.CasualWordComment;
import pers.zhz.pojo.CasualWordLike;
import pers.zhz.pojo.SystemMessage;
import pers.zhz.service.CasualWordService;
import pers.zhz.service.PersonalCenterService;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

@Service("casualWordService")//起别名，使spring按名字注入时注入的是实现类而不是接口
public class CasualWordServiceImpl implements CasualWordService {

    private static final Logger logger = Logger.getLogger(CasualWordServiceImpl.class);
    @Resource
    private HttpSession httpSession;
    @Resource
    private CasualWordMapper casualWordMapper;
    @Resource
    private PersonalCenterService personalCenterService;
    @Resource
    private FriendMapper friendMapper;
    @Resource
    private PersonalCenterMapper personalCenterMapper;
    @Resource
    private SystemMessageMapper systemMessageMapper;

    @Override
    public JSONObject publishCasualWord(MultipartFile imgOrVideo, String textContent, int myId) throws Exception {
        logger.debug("随说的文字内容" + textContent);
        JSONObject jsonObject = new JSONObject();
        CasualWord casualWord = new CasualWord();
        if (!textContent.equals("")) {
            casualWord.setContent(textContent);
        }
        Date currentDate = new Date();
        casualWord.setPublishTime(currentDate);
        casualWord.setUserId((myId));

        if (imgOrVideo != null) {//保存随说的视频/图像文件
            String fileParentPath = httpSession.getServletContext().getRealPath("userData") + "/casualWordImgAndVideo/";//文件父路径
            File file = new File(fileParentPath);
            if (!file.exists()) {//不存在该路径则新建
                if (!file.mkdirs()) {
                    jsonObject.put("state", false);
                    return jsonObject;
                }
            }
            String filename = imgOrVideo.getOriginalFilename();
            if (filename != null) {
                filename = UUID.randomUUID().toString().replaceAll("-", "") +
                        filename.substring(filename.lastIndexOf("."));
            }
            file = new File(fileParentPath + filename);
            imgOrVideo.transferTo(file);
            jsonObject.put("src", "/userData/casualWordImgAndVideo/" + filename);
            casualWord.setPictureOrVideo(filename);
        }
        if (casualWordMapper.addCasualWord(casualWord) != 1) {//随说表插入
            jsonObject.put("state", false);
            return jsonObject;
        }

        jsonObject.put("state", true);
        jsonObject.put("casualWordId", casualWord.getId());
        jsonObject.put("name", personalCenterMapper.getUsernameById(myId));
        jsonObject.put("time", currentDate.getTime());
        jsonObject.put("status", 200);
        return jsonObject;
    }

    @Override
    public Map<String, List<JSONObject>> getUserAndFriendCasualWords(int myId, int currentPage) {
        return commonGetCasualWord(myId, myId, currentPage, null);
    }

    @Override
    public Map<String, List<JSONObject>> getMyCasualWords(int myId, int currentPage) {
        return commonGetCasualWord(myId, myId, currentPage, true);
    }

    @Override
    public Map<String, List<JSONObject>> getNotUserFriendCasualWord(int userId, int currentPage) {
        return commonGetCasualWord(null, userId, currentPage, false);
    }

    @Override
    public Map<String, List<JSONObject>> getUserFriendCasualWords(int myId, int friendId, int currentPage) {
        return commonGetCasualWord(myId, friendId, currentPage, true);
    }

    @Override
    public JSONObject handelCasualWordLike(int casualWordId, int myId, boolean isLike) {
        int result;
        int casualWordOwnerId = casualWordMapper.getCasualWordUserIdByCasualWordId(casualWordId);
        boolean flag = casualWordOwnerId == myId;//是否属于个人的随说
        SystemMessage systemMessage = new SystemMessage();
        systemMessage.setReceiverId(casualWordOwnerId);
        systemMessage.setSendTime(new Date());
        systemMessage.setSenderId(myId);
        systemMessage.setCasualWordId(casualWordId);

        if (isLike) {//点赞随说，保存系统消息并插入点赞说的数据
            CasualWordLike casualWordLike = new CasualWordLike();
            casualWordLike.setLikeUserId(myId);
            casualWordLike.setCasualWordId(casualWordId);
            casualWordLike.setLikeTime(new Date());
            result = casualWordMapper.likeCasualWord(casualWordLike);
            if (!flag) {//不属于用户的随说才添加系统消息，否则不添加
                systemMessage.setMessageType(4);
                result += systemMessageMapper.insertNewSystemMessage(systemMessage);
            } else {
                result++;
            }
        } else {//取消点赞随说，保存系统消息并删除点赞随说的数据行
            result = casualWordMapper.unlikeCasualWordById(casualWordId, myId);
            if (!flag) {
                systemMessage.setMessageType(5);
                result += systemMessageMapper.insertNewSystemMessage(systemMessage);
            } else {
                result++;
            }
        }
        JSONObject json = new JSONObject();
        json.put("state", result);
        json.put("status", 200);
        return json;
    }

    @Override
    public JSONObject comment(int myId, int id, String content, int flag) {
        JSONObject returnObject = new JSONObject();
        CasualWordComment comment = new CasualWordComment();
        Date currentDate = new Date();
        comment.setCommentTime(currentDate);
        comment.setContent(content);
        comment.setCommenterId(myId);

        if (flag == 0) {//非随说详细界面评论
            comment.setCasualWordId(id);
            if (casualWordMapper.addCasualWordComment(comment) == 1) {//数据库表插入评论信息
                int userId = casualWordMapper.getCasualWordUserIdByCasualWordId(id);//随说所属的用户id
                if (userId != myId) {//非本人评论自己的随说则插入系统消息
                    SystemMessage message = new SystemMessage();
                    message.setCasualWordId(id);
                    message.setMessageType(6);
                    message.setSenderId(myId);
                    message.setReceiverId(userId);
                    message.setSendTime(new Date());
                    systemMessageMapper.insertNewSystemMessage(message);
                }
            }
        } else if (flag == 1) {//随说详细界面评论随说
            comment.setCasualWordId(id);
            if (casualWordMapper.addCasualWordComment(comment) == 1) {//数据库表插入评论信息
                int userId = casualWordMapper.getCasualWordUserIdByCasualWordId(id);
                if (userId != myId) {//非本人评论自己的随说则插入系统消息
                    SystemMessage message = new SystemMessage();
                    message.setMessageType(6);
                    message.setCasualWordId(id);
                    message.setSenderId(myId);
                    message.setReceiverId(userId);
                    message.setSendTime(new Date());
                    systemMessageMapper.insertNewSystemMessage(message);
                }

                returnObject.put("time", currentDate.getTime());
                returnObject.put("commentId", comment.getId());
                returnObject.put("myName", personalCenterMapper.getUsernameById(myId));
            }
        } else {//随说详细界面回复评论
            int casualWordId = casualWordMapper.getCasualWordIdByCommentId(id);
            comment.setReplyCommentId(id);
            comment.setCasualWordId(casualWordId);
            if (casualWordMapper.addCasualWordComment(comment) == 1) {
                int userIdOfCasualWord = casualWordMapper.getCasualWordUserIdByCasualWordId(casualWordId);//随说所属用户账号
                int userIdOfComment = casualWordMapper.getUserIdByCommentId(id);//被回复评论的评论发布者的账号id
                SystemMessage message = new SystemMessage();
                message.setSendTime(new Date());
                message.setCasualWordId(casualWordId);
                message.setSenderId(myId);
                //根据需要通知的对象进行分类
                if (userIdOfCasualWord != myId && userIdOfCasualWord != userIdOfComment && myId != userIdOfComment) {//情况1：三者都不是同一人，需要通知被评论者、随说所属者
                    message.setMessageType(6);
                    message.setReceiverId(userIdOfCasualWord);
                    systemMessageMapper.insertNewSystemMessage(message);//通知随说拥有者
                    message.setMessageType(7);
                    message.setReceiverId(userIdOfComment);
                    systemMessageMapper.insertNewSystemMessage(message);//通知被评论的人
                } else if (userIdOfCasualWord != userIdOfComment && myId == userIdOfComment) {//情况2：评论者和被评论者是同一人，需要通知随说拥有者
                    message.setMessageType(6);
                    message.setReceiverId(userIdOfCasualWord);
                    systemMessageMapper.insertNewSystemMessage(message);
                } else if (userIdOfCasualWord != userIdOfComment) {//情况3：评论者和随说所有者是同一人，需要通知被评论者
                    message.setMessageType(7);
                    message.setReceiverId(userIdOfComment);
                    systemMessageMapper.insertNewSystemMessage(message);
                } else if (userIdOfCasualWord != myId) {//情况4：被评论者和随说所有者是同一人，需要通知被评论者被回复评论
                    message.setMessageType(7);
                    message.setReceiverId(userIdOfComment);
                    systemMessageMapper.insertNewSystemMessage(message);
                }

                returnObject.put("time", currentDate.getTime());
                returnObject.put("commentId", comment.getId());
                returnObject.put("myName", personalCenterMapper.getUsernameById(myId));
                returnObject.put("respondentId", userIdOfComment);
                returnObject.put("respondentName", personalCenterMapper.getUsernameById(userIdOfComment));
            }
        }
        returnObject.put("status", 200);
        return returnObject;
    }

    @Override
    public Map<String, List<JSONObject>> getCasualWordDetail(int myId, int casualWordId, int currentPage) {
        Map<String, List<JSONObject>> casualWordDetailInfoMap = new HashMap<>();//返回给控制层的全部数据，本条随说信息、评论信息等
        List<JSONObject> casualWordInfoList = new ArrayList<>();//随说信息
        JSONObject casualWordInfoJsObj = new JSONObject();
        int userId = casualWordMapper.getCasualWordUserIdByCasualWordId(casualWordId);
        if (myId != userId && friendMapper.getFriendNicknameById(myId, userId) == null) {//非本人、非好友
            return null;
        }

        casualWordInfoJsObj.put("userHeadPortrait", personalCenterService.getHeadPortraitById(userId));
        casualWordInfoJsObj.put("publisherId", userId);
        if (userId == myId) {//本人的随说
            casualWordInfoJsObj.put("username", personalCenterMapper.getUsernameById(myId));
            casualWordInfoJsObj.put("isMyCasualWord", true);
        } else {
            casualWordInfoJsObj.put("username", friendMapper.getFriendNicknameById(userId, myId));
            casualWordInfoJsObj.put("isMyCasualWord", false);
        }
        CasualWord casualWord = casualWordMapper.getCasualWordByCasualWordId(casualWordId);
        casualWordInfoJsObj.put("time", casualWord.getPublishTime().getTime());
        casualWordInfoJsObj.put("textContent", casualWord.getContent());
        String filename = casualWord.getPictureOrVideo();
        if (filename != null) {
            if (filename.endsWith(".mp4")) {
                casualWordInfoJsObj.put("video", "/userData/casualWordImgAndVideo/" + filename);
            } else {
                casualWordInfoJsObj.put("image", "/userData/casualWordImgAndVideo/" + filename);
            }
        }
        casualWordInfoJsObj.put("isLike", casualWordMapper.checkIsLikeById(myId, casualWordId));

        int commentNum = casualWordMapper.getCommentsNumByCasualWordId(casualWordId);
        int totalPages = commentNum % 10 == 0 ? commentNum / 10 : commentNum / 10 + 1;//页数，10表示每页最多有10条消息
        if (totalPages == 0) {//防止页数为0影响前端分页插件的初始化
            totalPages++;
        }
        boolean getCommentFlag = true;
        if (currentPage > totalPages) {//防止当前页码大于总页码引发前端分页插件的问题
            totalPages = currentPage;
            getCommentFlag = false;//不再需要获取评论
        }
        casualWordInfoJsObj.put("totalPages", totalPages);
        casualWordInfoList.add(casualWordInfoJsObj);
        casualWordDetailInfoMap.put("casualWordInfo", casualWordInfoList);
        if (commentNum == 0 || !getCommentFlag) {//没有评论消息，直接返回
            return casualWordDetailInfoMap;
        }

        //处理返回的随说评论信息
        List<CasualWordComment> casualWordCommentList = casualWordMapper.getCommentsByCasualWordId(casualWordId, (currentPage - 1) * 10);
        List<JSONObject> comments = new ArrayList<>();
        for (CasualWordComment comment : casualWordCommentList) {
            JSONObject object = new JSONObject();
            object.put("commentId", comment.getId());
            object.put("time", comment.getCommentTime().getTime());
            int commenterId = comment.getCommenterId();
            object.put("headPortrait", personalCenterService.getHeadPortraitById(commenterId));
            object.put("commenterId", commenterId);
            if (myId == commenterId) {//用户本人的随说，获取本人的用户名
                object.put("commenterName", personalCenterMapper.getUsernameById(myId));
            } else {//非本人
                String nickname = friendMapper.getFriendNicknameById(commenterId, myId);
                if (nickname == null) {//非好友
                    object.put("commenterName", personalCenterMapper.getUsernameById(commenterId));
                } else {//好友
                    object.put("commenterName", nickname);
                }
            }
            if (comment.getReplyCommentId() != null) {//有回复的评论
                object.put("hasRespondent", true);
                int respondentId = casualWordMapper.getUserIdByCommentId(comment.getReplyCommentId());
                object.put("respondentId", respondentId);
                if (myId == respondentId) {//本人回复本人
                    object.put("respondentName", personalCenterMapper.getUsernameById(myId));
                } else {
                    String nickname = friendMapper.getFriendNicknameById(respondentId, myId);
                    if (nickname == null) {//非好友
                        object.put("respondentName", personalCenterMapper.getUsernameById(respondentId));
                    } else {//好友
                        object.put("respondentName", nickname);
                    }
                }
            } else {
                object.put("hasRespondent", false);
            }
            object.put("commentText", comment.getContent());
            comments.add(object);
        }
        casualWordDetailInfoMap.put("comments", comments);
        return casualWordDetailInfoMap;
    }

    @Override
    public void deleteCasualWordById(int casualWordId, int myId) {
        if (myId != casualWordMapper.getCasualWordUserIdByCasualWordId(casualWordId)) {
            logger.warn("用户（id=" + myId + "）非法删除非本人的随说的请求（" + casualWordId + "）");
            return;
        }

        String fileName = casualWordMapper.getImageOrVideoFileNameByCasualWordId(casualWordId);
        String parentPath = httpSession.getServletContext().getRealPath("userData") + "/casualWordImgAndVideo/";//文件父路径
        if (fileName != null) {//删除随说相关的视频/图片，如果存在的话
            if (new File(parentPath + fileName).delete()) {
                logger.debug("随说" + casualWordId + "视频/图片" + fileName + "删除成功");
            }
        }
        casualWordMapper.delCasualWordById(casualWordId);//删除随说
        casualWordMapper.delCasualWordCommentById(casualWordId);//删除随说相关的评论
        casualWordMapper.delCasualWordLikeById(casualWordId);//删除随说相关的点赞
        systemMessageMapper.delSystemMsgByCasualWordId(casualWordId);//删除随说相关的系统消息
    }

    /**
     * 获取随说通用函数
     *
     * @param myId        本人id
     * @param userId      用户账号
     * @param currentPage 当前页
     * @param flag        null-获取本人及好友随说的;true-获取好友、本人随说的；false-获取非好友随说的
     * @return 包含随说信息map，casualWords-随说列表；totalPages-随说个数
     */
    private Map<String, List<JSONObject>> commonGetCasualWord(Integer myId, int userId, int currentPage, Boolean flag) {
        Map<String, List<JSONObject>> casualWordInfoMap = new HashMap<>();//返回给控制层的数据
        int casualWordNum;
        if (flag == null) {//获取本人userId及好友的随说数量
            casualWordNum = casualWordMapper.getUserAndFriendCasualWordNumByUserId(userId);
        } else {//只获取userId的
            casualWordNum = casualWordMapper.getUserCasualWordNumByUserId(userId);
        }
        List<JSONObject> totalPagesList = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        int totalPages = casualWordNum % 20 == 0 ? casualWordNum / 20 : casualWordNum / 20 + 1;//页数，20表示每页最多有20条消息
        if (totalPages == 0) {//防止页数为0影响前端分页插件的初始化
            totalPages = 1;
        }
        boolean getCwFlag = true;
        if (currentPage > totalPages) {//防止当前页码大于总页码引发前端分页插件的问题
            totalPages = currentPage;
            getCwFlag = false;//不再需要获取随说
        }
        jsonObject.put("totalPages", totalPages);
        totalPagesList.add(jsonObject);
        casualWordInfoMap.put("totalPages", totalPagesList);
        if (casualWordNum == 0 || !getCwFlag) {//没有消息，直接返回
            return casualWordInfoMap;
        }

        //处理返回的随说信息
        List<CasualWord> casualWordsList;
        if (flag == null) {//获取本人userId及好友的随说
            casualWordsList = casualWordMapper.getUserAndFriendCasualWordsByUserId(userId, (currentPage - 1) * 20);
        } else {
            casualWordsList = casualWordMapper.getUserCasualWordsByUserId(userId, (currentPage - 1) * 20);
        }
        logger.debug("随说列表" + casualWordsList);
        List<JSONObject> casualWords = new ArrayList<>();
        for (CasualWord casualWord : casualWordsList) {
            JSONObject object = new JSONObject();
            if (flag == null) {//只有获取本人以及本人好友的随说才获取以下字段
                int publisherId = casualWord.getUserId();
                object.put("headPortrait", personalCenterService.getHeadPortraitById(publisherId));
                object.put("publisherId", publisherId);
                if (publisherId == userId) {//用户本人的随说，获取本人的用户名
                    object.put("name", personalCenterMapper.getUsernameById(userId));
                } else {//否则获取该好友的昵称
                    object.put("name", friendMapper.getFriendNicknameById(publisherId, userId));
                }
            }
            object.put("time", casualWord.getPublishTime().getTime());
            object.put("text", casualWord.getContent());
            String fileName = casualWord.getPictureOrVideo();
            if (fileName != null) {
                if (fileName.endsWith(".mp4")) {//如果是视频文件
                    object.put("video", "/userData/casualWordImgAndVideo/" + fileName);
                } else {//如果是图片
                    object.put("image", "/userData/casualWordImgAndVideo/" + fileName);
                }
            }
            if (flag == null || flag) {//获取本人及好友随说、获取本人随说、获取好友随说需要此字段，获取非好友随说不需要
                object.put("casualWordId", casualWord.getId());
                object.put("isLike", casualWordMapper.checkIsLikeById(myId, casualWord.getId()));
            }
            casualWords.add(object);
        }
        casualWordInfoMap.put("casualWords", casualWords);
        return casualWordInfoMap;
    }
}
