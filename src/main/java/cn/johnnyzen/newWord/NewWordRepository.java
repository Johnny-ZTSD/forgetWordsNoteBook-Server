package cn.johnnyzen.newWord;

import cn.johnnyzen.user.User;
import cn.johnnyzen.word.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface NewWordRepository extends JpaRepository<NewWord, Integer> {

    public Collection<NewWord> findAllByUserId(Integer userId);

    /* 原生SQL查询：对指定用户的所有生词进行模糊查询。
     * reference:https://blog.csdn.net/lovequanquqn/article/details/83501121
     */
    @Query(value =
            " SELECT *" +
            " FROM ( " + //嵌套查询
            " SELECT * " + //连接查询
            " FROM tb_word AS word, r_user_focus_word AS newWord,tb_user AS usr" +
            " WHERE word.pk_word_id = newWord.fk_ufw_word_id AND usr.pk_user_id = newWord.fk_ufw_user_id) AS tb" +
            " WHERE tb.pk_user_id = :userId AND tb.english_word LIKE CONCAT('%',:englishWord,'%') ",
            nativeQuery = true)
    public Collection<NewWord> findNewWordsLikeByUserIdAndEnglishWord(@Param("userId") Integer userId, @Param("englishWord") String englishWord);

    @Query("select newWord from NewWord newWord where newWord.user.email = :email")
    public Collection<NewWord> findAllByEmail(@Param("email") String email);

    public Collection<NewWord> findAllByUser(User user);

}
