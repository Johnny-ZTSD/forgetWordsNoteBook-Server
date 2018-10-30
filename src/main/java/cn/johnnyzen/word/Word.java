package cn.johnnyzen.word;

import cn.johnnyzen.tag.Tag;
import cn.johnnyzen.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/10/6  17:57:37
 * @Description: ...
 */
@Entity
@Table(name="tb_word")
public class Word {
    @Id
    @GeneratedValue
    @Column(name="pk_word_id")
    private Integer id;

    private String word;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name="r_user_focus_word",
            joinColumns={@JoinColumn(name="fk_ufw_user_id")},
            inverseJoinColumns={@JoinColumn(name="fk_ufw_word_id")})
    private Set<User> users = new HashSet<User>();;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name="r_word_tag_tag",
            joinColumns={@JoinColumn(name="fk_wtt_word_id")},
            inverseJoinColumns={@JoinColumn(name="fk_wtt_tag_id")})
    private Set<Tag> tags = new HashSet<Tag>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", users=" + users +
                ", tags=" + tags +
                '}';
    }
}
