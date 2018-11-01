package cn.johnnyzen.word;

import cn.johnnyzen.newWord.NewWord;

import javax.persistence.*;
import java.util.Collection;

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

    @Column(nullable = false, unique = true)
    private String englishWord;

    @Column(nullable = false)
    private String chineseTranslate;

    @OneToMany(mappedBy = "word")
    private Collection<NewWord> newWords;

    //临时展示给前端的数据，如：遗忘次数、遗忘天数等
    @Transient
    private String tmpData;

//    @JsonIgnore
//    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//    @JoinTable(
//            name="r_user_focus_word",
//            joinColumns={@JoinColumn(name="fk_ufw_user_id")},
//            inverseJoinColumns={@JoinColumn(name="fk_ufw_word_id")})
//    private Set<User> users = new HashSet<User>();;

//    @JsonIgnore
//    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//    @JoinTable(
//            name="r_word_tag_tag",
//            joinColumns={@JoinColumn(name="fk_wtt_word_id")},
//            inverseJoinColumns={@JoinColumn(name="fk_wtt_tag_id")})
//    private Set<Tag> tags = new HashSet<Tag>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public String getChineseTranslate() {
        return chineseTranslate;
    }

    public void setChineseTranslate(String chineseTranslate) {
        this.chineseTranslate = chineseTranslate;
    }

    public Collection<NewWord> getNewWords() {
        return newWords;
    }

    public void setNewWords(Collection<NewWord> newWords) {
        this.newWords = newWords;
    }

    public String getTmpData() {
        return tmpData;
    }

    public void setTmpData(String tmpData) {
        this.tmpData = tmpData;
    }

    //    public Set<User> getUsers() {
//        return users;
//    }
//
//    public void setUsers(Set<User> users) {
//        this.users = users;
//    }
//
//    public Set<Tag> getTags() {
//        return tags;
//    }
//
//    public void setTags(Set<Tag> tags) {
//        this.tags = tags;
//    }

    @Override
    public String toString() {
        return "Word{" +
                "\n\tid=" + id +
                ",\n\t englishWord='" + englishWord + '\'' +
                ",\n\t chineseTranslate=" + chineseTranslate + '\'' +
                ",\n\t tmpData=" + tmpData + '\'' +
                '}';
    }
}
