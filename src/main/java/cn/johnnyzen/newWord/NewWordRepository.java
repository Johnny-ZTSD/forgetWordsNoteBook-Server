package cn.johnnyzen.newWord;

import cn.johnnyzen.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface NewWordRepository extends JpaRepository<NewWord, Integer> {
    public Collection<NewWord> findAllByUserId(Integer id);

    @Query("select word from NewWord word where word.user.email = :email")
    public List<NewWord> findAllByEmail(@Param("email") String email);
}
