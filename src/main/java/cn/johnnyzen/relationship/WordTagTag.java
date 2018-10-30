package cn.johnnyzen.relationship;

import cn.johnnyzen.tag.Tag;
import cn.johnnyzen.word.Word;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/10/6  23:18:26
 * @Description: ...
 */
@Entity
@Table(name = "r_word_tag_tag")
//在hibernate中同一张表里面存在多个主键，必须要实现序列化接口(Serializable )
public class WordTagTag implements Serializable{
    @Id
    @ManyToOne
    @JoinColumn(name="fk_wtt_word_id")
    private Word word;

    @Id
    @ManyToOne
    @JoinColumn(name="fk_wtt_tag_id")
    private Tag tag;

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
