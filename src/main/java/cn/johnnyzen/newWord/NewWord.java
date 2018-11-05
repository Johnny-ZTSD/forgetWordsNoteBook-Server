package cn.johnnyzen.newWord;


import cn.johnnyzen.user.User;
import cn.johnnyzen.word.Word;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/1  17:19:57
 * @Description: ...
 */

@Entity
@Table(name = "r_user_focus_word")
public class NewWord implements Serializable{

    private Integer id;
    private Timestamp createNewWordDatetime;
    private Timestamp lastStoredDatetime;
    private Timestamp lastForgotDatetime;
    /* 遗忘次数 */
    private Byte forgetCount;
    /* 遗忘权重指数 */
    private Double forgetRate;

    @JsonIgnore
    private User user;

    @JsonIgnore
    private Word word;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_ufw_id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "create_new_word_datetime", nullable = true)
    public Timestamp getCreateNewWordDatetime() {
        return createNewWordDatetime;
    }

    public void setCreateNewWordDatetime(Timestamp createNewWordDatetime) {
        this.createNewWordDatetime = createNewWordDatetime;
    }

    @Basic
    @Column(name = "last_stored_datetime", nullable = true)
    public Timestamp getLastStoredDatetime() {
        return lastStoredDatetime;
    }

    public void setLastStoredDatetime(Timestamp lastStoredDatetime) {
        this.lastStoredDatetime = lastStoredDatetime;
    }

    @Basic
    @Column(name = "last_forgot_datetime", nullable = true)
    public Timestamp getLastForgotDatetime() {
        return lastForgotDatetime;
    }

    public void setLastForgotDatetime(Timestamp lastForgotDatetime) {
        this.lastForgotDatetime = lastForgotDatetime;
    }

    @Basic
    @Column(name = "forget_count", nullable = true)
    public Byte getForgetCount() {
        return forgetCount;
    }

    public void setForgetCount(Byte forgetCount) {
        this.forgetCount = forgetCount;
    }

    @Basic
    @Column(name = "forget_rate", nullable = true, precision = 0)
    public Double getForgetRate() {
        return forgetRate;
    }

    public void setForgetRate(Double forgetRate) {
        this.forgetRate = forgetRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewWord newWord = (NewWord) o;

        if (id != newWord.id) return false;
        if (createNewWordDatetime != null ? !createNewWordDatetime.equals(newWord.createNewWordDatetime) : newWord.createNewWordDatetime != null)
            return false;
        if (lastStoredDatetime != null ? !lastStoredDatetime.equals(newWord.lastStoredDatetime) : newWord.lastStoredDatetime != null)
            return false;
        if (lastForgotDatetime != null ? !lastForgotDatetime.equals(newWord.lastForgotDatetime) : newWord.lastForgotDatetime != null)
            return false;
        if (forgetCount != null ? !forgetCount.equals(newWord.forgetCount) : newWord.forgetCount != null) return false;
        if (forgetRate != null ? !forgetRate.equals(newWord.forgetRate) : newWord.forgetRate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (createNewWordDatetime != null ? createNewWordDatetime.hashCode() : 0);
        result = 31 * result + (lastStoredDatetime != null ? lastStoredDatetime.hashCode() : 0);
        result = 31 * result + (lastForgotDatetime != null ? lastForgotDatetime.hashCode() : 0);
        result = 31 * result + (forgetCount != null ? forgetCount.hashCode() : 0);
        result = 31 * result + (forgetRate != null ? forgetRate.hashCode() : 0);
        return result;
    }

    @JsonBackReference
    @JsonFilter("NewWord-user")
    @ManyToOne
    @JoinColumn(name = "fk_ufw_user_id", referencedColumnName = "pk_user_id", nullable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonBackReference
    @JsonFilter("NewWord-word")
    @ManyToOne
    @JoinColumn(name = "fk_ufw_word_id", referencedColumnName = "pk_word_id", nullable = false)
    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return "NewWord{" +
                "\n\tid=" + id +
                ",\n\t createNewWordDatetime=" + createNewWordDatetime +
                ",\n\t lastStoredDatetime=" + lastStoredDatetime +
                ",\n\t lastForgotDatetime=" + lastForgotDatetime +
                ",\n\t forgetCount=" + forgetCount +
                ",\n\t forgetRate=" + forgetRate +
//                ",\n\t user=" + user +
//                ",\n\t word=" + word +
                '}';
    }
}
