package cn.johnnyzen.relationship;

import cn.johnnyzen.tag.Tag;
import cn.johnnyzen.user.User;

import javax.persistence.*;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/10/6  23:02:44
 * @Description: 多对多关系实体表（主要针对：关系表多余字段问题，一般情况下无需设置多余的实体表）
 * @Reference: hibernate多对多之中间表有多个字段 https://blog.csdn.net/u014038534/article/details/50153317
 */
@Entity
@Table(name = "r_user_bind_tag")
//在hibernate中同一张表里面存在多个主键，必须要实现序列化接口(Serializable )
public class UserBindTag implements java.io.Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name="fk_ubt_user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name="fk_ubt_tag_id")
    private Tag tag;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
