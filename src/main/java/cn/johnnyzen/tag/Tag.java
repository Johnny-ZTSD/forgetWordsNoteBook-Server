package cn.johnnyzen.tag;

import cn.johnnyzen.user.User;
import cn.johnnyzen.word.Word;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/10/6  17:52:53
 * @Description: ...
 */
//@Entity
//@Table(name="tb_tag")
public class Tag implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="pk_tag_id")
    private Integer id;

    private String name;

    /*
     * @param String: User.username
     * @param User: User
     * */
    //在不需要的转化json的属性上面设置@JsonIgnore，避免出现无线循环
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//    @MapKey(name="pk_ubt_id")
    @JoinTable( //可接受多个@JoinColumn，用于配置连接表中外键列的信息
            name="r_user_bind_tag",
            joinColumns={@JoinColumn(name="fk_ubt_user_id")}, //主控端ID
            inverseJoinColumns={@JoinColumn(name="fk_ubt_tag_id")})
    private Set<User> users = new HashSet<User>();

    /*
     * @param String: User.username
     * @param User: User
     * */
    //在不需要的转化json的属性上面设置@JsonIgnore，避免出现无线循环
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//    @MapKey(name="pk_wtt_id")
    @JoinTable( //可接受多个@JoinColumn，用于配置连接表中外键列的信息
            name="r_word_tag_tag",
            joinColumns={@JoinColumn(name="fk_wtt_word_id")}, //主控端ID
            inverseJoinColumns={@JoinColumn(name="fk_wtt_tag_id")})
    private Set<Word> words = new HashSet<Word>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Word> getWords() {
        return words;
    }

    public void setWords(Set<Word> words) {
        this.words = words;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", users=" + users +
                ", words=" + words +
                '}';
    }
}
