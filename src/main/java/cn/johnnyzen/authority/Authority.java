package cn.johnnyzen.authority;

import cn.johnnyzen.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/5  17:34:16
 * @Description: 权限实体，一个用户有一条权限记录
 */
@Entity
@Table(name = "tb_authority")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_authority_id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "authority_code", nullable = true)
    private String authorityCode;

    @Basic
    @Column(name = "authority_type", nullable = false)
    private String authrityType;

    @JsonIgnore
    @OneToOne(mappedBy="authority")
    @JoinColumn(name = "fk_user_id",referencedColumnName="pk_user_id")
    private User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthorityCode() {
        return authorityCode;
    }

    public void setAuthorityCode(String authorityCode) {
        this.authorityCode = authorityCode;
    }

    public String getAuthrityType() {
        return authrityType;
    }

    public void setAuthrityType(String authrityType) {
        this.authrityType = authrityType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "\n\t id=" + id +
                ",\n\t authorityCode='" + authorityCode + '\'' +
                ",\n\t authrityType='" + authrityType + '\'' +
                ",\n\t userId=" + user.getId() +
                ",\n\t username=" + user.getUsername() +
                ",\n\t email=" + user.getEmail() +
                '}';
    }
}
