package cn.johnnyzen.newWord;

import cn.johnnyzen.user.User;
import cn.johnnyzen.word.Word;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface NewWordRepository extends JpaRepository<NewWord, Integer> {

    //通过生词ID查对应生词
    public NewWord findNewWordById(Integer id);

    public Collection<NewWord> findAllByUserId(Integer userId);

    /* 原生SQL查询：对指定用户的所有生词进行模糊查询。
     * reference:
     *      https://blog.csdn.net/lovequanquqn/article/details/83501121
     *      https://www.cnblogs.com/zxlovenet/p/4005256.html
     */
    @Query(value =
            " SELECT *" +
            " FROM ( " + //嵌套查询
            " SELECT * " + //连接查询
            " FROM tb_word AS word, r_user_focus_word AS newWord,tb_user AS usr" +
            " WHERE word.pk_word_id = newWord.fk_ufw_word_id AND usr.pk_user_id = newWord.fk_ufw_user_id) AS tb" +
            " WHERE tb.pk_user_id = :userId AND tb.english_word LIKE CONCAT('%',:englishWord,'%') ", //模糊查询
            nativeQuery = true)
    public Collection<NewWord> findNewWordsLikeByUserIdAndEnglishWord(@Param("userId") Integer userId, @Param("englishWord") String englishWord);

    /* 对指定用户(userId)的所有生词中查询指定单词(englishWord)
     * @param userId
     * @param englishWord
     */
    @Query(value =
            " SELECT *" +
                    " FROM ( " + //嵌套查询
                    " SELECT * " + //连接查询
                    " FROM tb_word AS word, r_user_focus_word AS newWord,tb_user AS usr" +
                    " WHERE word.pk_word_id = newWord.fk_ufw_word_id AND usr.pk_user_id = newWord.fk_ufw_user_id ) AS tb" +
                    " WHERE tb.pk_user_id = :userId AND tb.english_word = :englishWord ", //模糊查询
            nativeQuery = true)
    public NewWord findDistinctFirstByUserIdAndEnglishWord(@Param("userId") Integer userId, @Param("englishWord") String englishWord);

    @Query("select newWord from NewWord newWord where newWord.user.email = :email")
    public Collection<NewWord> findAllByEmail(@Param("email") String email);

    /*
     * 查询用户最近days天新增的单词
     * @reference
     *      1 JPA自定义sql实现分页查询
     *          http://blog.51cto.com/1385903/2058646
     *      2 Spring Hibernate JPA 联表查询 复杂查询
     *          https://www.cnblogs.com/jiangxiaoyaoblog/p/5635152.html
     */
    @Query(value =
            " select * from ( " +
            "   select * from tb_user as usr,r_user_focus_word as newWord,tb_word as word " +
            "      WHERE usr.pk_user_id = newWord.fk_ufw_user_id and word.pk_word_id = newWord.fk_ufw_word_id ) as tmp_tb " +
            " where tmp_tb.pk_user_id = :userId and DATE_SUB(CURDATE(), INTERVAL :days DAY) <= date(tmp_tb.create_new_word_datetime)",
           nativeQuery = true,
           countQuery = // 不具备JPA命名规范的DAO方法，有pageable，必须写countQuery
           " select count(*) from ( " +
                   "   select * from tb_user as usr,r_user_focus_word as newWord,tb_word as word " +
                   "      WHERE usr.pk_user_id = newWord.fk_ufw_user_id and word.pk_word_id = newWord.fk_ufw_word_id ) as tmp_tb " +
                   " where tmp_tb.pk_user_id = :userId and DATE_SUB(CURDATE(), INTERVAL :days DAY) <= date(tmp_tb.create_new_word_datetime)")
    public Page<NewWord> findNewWordsOfLastDaysOfUser(@Param("userId") Integer userId,@Param("days") Integer days,Pageable pageable);


    /**
     * 根据用户ID，查询按照forgetRate值降序排序的生词记录
     * @param userId 用户ID
     * @param pageable 分页器
     */
    @Query(value = "select newWord from NewWord newWord where newWord.user.id = :userId order by newWord.forgetRate",
            countQuery = "select count(newWord) from NewWord newWord where newWord.user.id = :userId order by newWord.forgetRate")
    public Page<NewWord> findNewWordsOfForgetRateTopByUserId(@Param("userId") Integer userId, Pageable pageable);

    /**
     * 根据用户ID，SQL乱序查询生词
     *
     */
//    @Query(value = "select newWord from NewWord newWord where newWord.user.id = :userId order by RAND()")
    @Query( value =
            "select * from ( " +
            " select * from tb_user as usr,r_user_focus_word as newWord,tb_word as word " +
            " WHERE usr.pk_user_id = newWord.fk_ufw_user_id and word.pk_word_id = newWord.fk_ufw_word_id ) as tmp_tb " +
            " where tmp_tb.pk_user_id = :userId ORDER BY RAND()",
            nativeQuery = true,
            countQuery =
            "select * from ( " +
            " select * from tb_user as usr,r_user_focus_word as newWord,tb_word as word " +
            " WHERE usr.pk_user_id = newWord.fk_ufw_user_id and word.pk_word_id = newWord.fk_ufw_word_id ) as tmp_tb " +
            " where tmp_tb.pk_user_id = :userId ORDER BY RAND()")
    public Page<NewWord> findDisorderedNewWordsByUserId(@Param("userId") Integer userId, Pageable pageable);

    /**
     * 通过用户ID，依据用户的排序规则查询用户所有生词
     */
//    @Query(value =
//            " select * from ( " +
//            " select * from tb_user as usr,r_user_focus_word as newWord,tb_word as word " +
//            " WHERE usr.pk_user_id = newWord.fk_ufw_user_id and word.pk_word_id = newWord.fk_ufw_word_id ) as tmp_tb " +
//            " where tmp_tb.pk_user_id = :userId ORDER BY tmp_tb.:searchType :sortType",
//            nativeQuery = true,
//            countQuery =
//            " select * from ( " +
//            " select * from tb_user as usr,r_user_focus_word as newWord,tb_word as word " +
//            " WHERE usr.pk_user_id = newWord.fk_ufw_user_id and word.pk_word_id = newWord.fk_ufw_word_id ) as tmp_tb " +
//            " where tmp_tb.pk_user_id = :userId ORDER BY tmp_tb.:searchType :sortType")
//    public Page<NewWord> findAllBySortRuleOfUser(@Param("userId")Integer userId,@Param("searchType")String searchType,@Param("sortType")String sortType, Pageable pageable);

    /*
     * 通过用户id，按照遗忘次数查询用户所有生词
     * 注：具备JPA命名规范的DAO方法，返回Page分页时，不需要另外配置@Query(countQuery)
     */
    public Page<NewWord> findAllByUserIdOrderByForgetCount(@Param("userId")Integer userId, Pageable pageable);
    public Page<NewWord> findAllByUserIdOrderByForgetCountAsc(@Param("userId")Integer userId, Pageable pageable);
    public Page<NewWord> findAllByUserIdOrderByForgetCountDesc(@Param("userId")Integer userId, Pageable pageable);

    /*
     * 通过用户id，按照遗忘权重指数查询用户所有生词
     * 注：具备JPA命名规范的DAO方法，返回Page分页时，不需要另外配置@Query(countQuery)
     */
    public Page<NewWord> findAllByUserIdOrderByForgetRate(@Param("userId")Integer userId, Pageable pageable);
    public Page<NewWord> findAllByUserIdOrderByForgetRateAsc(@Param("userId")Integer userId, Pageable pageable);
    public Page<NewWord> findAllByUserIdOrderByForgetRateDesc(@Param("userId")Integer userId, Pageable pageable);

    /*
     * 通过用户id，按照最近遗忘生词的时间查询用户所有生词
     * 注：具备JPA命名规范的DAO方法，返回Page分页时，不需要另外配置@Query(countQuery)
     */
    public Page<NewWord> findAllByUserIdOrderByLastForgotDatetime(@Param("userId")Integer userId, Pageable pageable);
    public Page<NewWord> findAllByUserIdOrderByLastForgotDatetimeAsc(@Param("userId")Integer userId, Pageable pageable);
    public Page<NewWord> findAllByUserIdOrderByLastForgotDatetimeDesc(@Param("userId")Integer userId, Pageable pageable);

    /*
     * 通过用户id，按照最近仍记得的生词的时间查询用户所有生词
     * 注：具备JPA命名规范的DAO方法，返回Page分页时，不需要另外配置@Query(countQuery)
     */
    public Page<NewWord> findAllByUserIdOrderByLastStoredDatetime(@Param("userId")Integer userId, Pageable pageable);
    public Page<NewWord> findAllByUserIdOrderByLastStoredDatetimeAsc(@Param("userId")Integer userId, Pageable pageable);
    public Page<NewWord> findAllByUserIdOrderByLastStoredDatetimeDesc(@Param("userId")Integer userId, Pageable pageable);

    public Collection<NewWord> findAllByUser(User user);

}
