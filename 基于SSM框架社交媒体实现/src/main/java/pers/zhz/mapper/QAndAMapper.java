package pers.zhz.mapper;

import org.apache.ibatis.annotations.Param;
import pers.zhz.pojo.QAndAAnswer;
import pers.zhz.pojo.QAndAQuestion;

import java.util.List;

/**
 * 问答相关的mapper
 */
public interface QAndAMapper {

    /**
     * 插入新发布的问题
     *
     * @param qAndAQuestion 问答-问题实体类
     */
    void addQAndAQuestion(QAndAQuestion qAndAQuestion);

    /**
     * 返回全部用户的问题个数
     *
     * @return 返回问题的个数
     */
    int getAllUserQuestionNum();

    /**
     * 根据时间降序返回全部用户的问题
     *
     * @param pageIndex 获取的数据行起始位置，从0开始，每次20行
     * @return 返回问题实体类的list
     */
    List<QAndAQuestion> getAllUserQuestion(@Param("pageIndex") int pageIndex);

    /**
     * 根据用户id返回自己发布问题的个数
     *
     * @param userId 用户账号
     * @return 返回问题的个数
     */
    int getUserQuestionNumByUserId(@Param("userId") int userId);

    /**
     * 根据用户id返回所属的问题
     *
     * @param userId    用户账号
     * @param pageIndex 获取数据行起始位置(从0开始),每次20行
     * @return 返回问题实体类的list
     */
    List<QAndAQuestion> getUserQuestionsByUserId(@Param("userId") int userId, @Param("pageIndex") int pageIndex);

    /**
     * 根据问题的内容模糊查询问题
     *
     * @param content   问题内容
     * @return 返回问题实体类的list
     */
    List<QAndAQuestion> getQuestionByContent(@Param("content") String content);

    /**
     * 根据问题id返回该问题的提问者id
     *
     * @param questionId 问题id
     * @return 提问者账号
     */
    Integer getQuestionerIdByQuestionId(@Param("questionId") int questionId);

    /**
     * 通过答案id获取答案所属的问题id
     *
     * @param answerId 答案id
     * @return 问题id
     */
    Integer getQuestionIdByAnswerId(@Param("answerId") int answerId);

    /**
     * 问答-回答数据库表插入新的回答
     *
     * @param qAndAAnswer 问答-回答实体类
     */
    void addQAndAAnswer(QAndAAnswer qAndAAnswer);

    /**
     * 根据问题id获取问题
     *
     * @param questionId 问题id
     * @return 随说实体类
     */
    QAndAQuestion getQuestionByQuestionId(@Param("questionId") int questionId);

    /**
     * 根据问题id返回属于该问题的回答数量
     *
     * @param questionId 问题id
     * @return 回答数量
     */
    int getAnswersNumByQuestionId(@Param("questionId") int questionId);

    /**
     * 根据问题id返回属于该问题的回答列表
     *
     * @param questionId 问题id
     * @param pageIndex  获取数据行起始位置(从0开始),每次10行
     * @return 回答列表
     */
    List<QAndAAnswer> getAnswersByQuestionId(@Param("questionId") int questionId, @Param("pageIndex") int pageIndex);

    /**
     * 根据回答id获取所属的用户id
     *
     * @param answerId 回答id
     * @return 回答所属的用户id
     */
    int getUserIdByAnswerId(@Param("answerId") int answerId);

    /**
     * 根据问题id删除问题
     *
     * @param questionId 问题id
     */
    void delQuestionByQuestionId(@Param("questionId") int questionId);

    /**
     * 根据问题id删除其相关的回答
     *
     * @param questionId 问题id
     */
    void delAnswerByQuestionId(@Param("questionId") int questionId);
}
