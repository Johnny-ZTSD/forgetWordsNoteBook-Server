package cn.johnnyzen.word;

import cn.johnnyzen.newWord.NewWord;
import cn.johnnyzen.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WordRepository  extends JpaRepository<Word, Integer>  {
    public List<Word> findAllById(Integer id);
}
