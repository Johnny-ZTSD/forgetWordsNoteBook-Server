package cn.johnnyzen.relationship;

import cn.johnnyzen.user.User;
import cn.johnnyzen.word.Word;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/10/6  23:20:26
 * @Description: ...
 */
@Entity
@Table(name = "r_user_focus_word")
//在hibernate中同一张表里面存在多个主键，必须要实现序列化接口(Serializable )
public class UserFocusWord implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name="fk_ufw_user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name="fk_ufw_word_id")
    private Word word;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }
}
