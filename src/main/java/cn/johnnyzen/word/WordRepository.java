package cn.johnnyzen.word;

import cn.johnnyzen.newWord.NewWord;
import cn.johnnyzen.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface WordRepository  extends JpaRepository<Word, Integer>  {
    public List<Word> findAllById(Integer id);

    public Collection<Word> findDistinctFirstByEnglishWord(String englishWord);

    public Word findFirstByEnglishWord(String englishWord);

    /**
     * 依据english模糊查询+userId，查询所有非用户生词的符合单词
     * 注意：为了与用户的单词区分起见，自动已将每个单词的id置为负ID(>单词ID绝对值不变)
     * @reference https://blog.csdn.net/weixin_41141219/article/details/80649640
     * @param userId
     * @param englishWord
     */
    @Query(value =
            "SELECT -allWords.pk_word_id as pk_word_id,allWords.english_word,allWords.chinese_translate FROM " +
            "  ( SELECT word.pk_word_id,word.english_word,word.chinese_translate " + //数据库所有符合条件的单词
            "    FROM tb_word AS word " +
            "    WHERE word.english_word like CONCAT('%',:englishWord,'%') ) " +
            "    AS allWords " +
            "  LEFT JOIN " +
            "  (SELECT tb2.fk_ufw_word_id,tb2.english_word " + //用户所有符合条件的生词
            "    FROM ( " +
            "      SELECT * " +
            "        FROM tb_word AS word, r_user_focus_word AS newWord,tb_user AS usr " +
            "        WHERE word.pk_word_id = newWord.fk_ufw_word_id and usr.pk_user_id = newWord.fk_ufw_user_id) AS tb2 " +
            "    WHERE tb2.pk_user_id = :userId AND tb2.english_word LIKE CONCAT('%',:englishWord,'%') ) " +
            "    AS userWords " +
            "  ON allWords.pk_word_id = userWords.fk_ufw_word_id " +
            "  WHERE allWords.pk_word_id IS  NULL OR userWords.fk_ufw_word_id IS NULL;",nativeQuery = true
    )
    public Collection<Word> findAllNewWordsOfNotUserByUserIdAndFuzzyEnglishWord(@Param("userId") Integer userId, @Param("englishWord") String englishWord);
}
