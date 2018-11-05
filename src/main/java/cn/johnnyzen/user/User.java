package cn.johnnyzen.user;

import cn.johnnyzen.authority.Authority;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/10/6  17:54:12
 * @Description: 用户名与邮箱号独立，只不过，使用邮箱号或者用户名均能登陆系统
 */
@Entity
@Table(name="tb_user")
public class User  implements Serializable {
    @Id
    @GeneratedValue
    @Column(name="pk_user_id")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    private String password;

    /* F:女;M:男 */
    private Character sex;

//    @Column(columnDefinition = "comment 'LOCKED:-1/UNACTIVATE:0/ACTIVATED:1'")
    private Integer accountState;

    private String activateCode;

    /*
     * 用户头像的绝对url
     * 根据固定策略生成url:url = http://域名:端口/上下文路径/user/logo/("${user.email}" + ".png")
     **/
    @Transient //临时字段，映射时忽略
    private String logoUrl;

    @Transient //临时字段，映射时忽略
    private Calendar lastActiveDateTime;

    /* 登陆token，主要用于登陆返回token时使用 */
    @Transient //临时字段，映射时忽略
    private String token;

    /* sessionId 临时解决办法*/
    @Transient //临时字段，映射时忽略
    private String sessionId;

    @JsonIgnore
    @OneToOne //主控类
    private Authority authority;

    //在不需要的转化json的属性上面设置@JsonIgnore，避免出现无线循环
//    @JsonIgnore
//    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @MapKey(name="pk_ubt_id")
//    @JoinTable( //可接受多个@JoinColumn，用于配置连接表中外键列的信息
//            name="r_user_bind_tag",
//            joinColumns={
//                    @JoinColumn(name="fk_ubt_user_id")}, //主控端ID
//            inverseJoinColumns={@JoinColumn(name="fk_ubt_tag_id")})
//    private Set<Tag> tags = new HashSet<Tag>();

//    @JsonIgnore
//    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//    @JoinTable(
//            name="r_user_focus_word",
//            joinColumns={@JoinColumn(name="fk_user_id")},
//            inverseJoinColumns={@JoinColumn(name="fk_word_id")})
//    private Set<Word> words = new HashSet<Word>();

//    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
//    private Collection<NewWord> newWords;
//
//    public Collection<NewWord> getNewWords() {
//        return newWords;
//    }
//
//    public void setNewWords(Collection<NewWord> newWords) {
//        this.newWords = newWords;
//    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Character getSex() {
        return sex;
    }

    public void setSex(Character sex) {
        this.sex = sex;
    }

    public Integer getAccountState() {
        return accountState;
    }

    public void setAccountState(Integer accountState) {
        this.accountState = accountState;
    }

    public String getActivateCode() {
        return activateCode;
    }

    public void setActivateCode(String activateCode) {
        this.activateCode = activateCode;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Calendar getLastActiveDateTime() {
        return lastActiveDateTime;
    }

    public void setLastActiveDateTime(Calendar lastActiveDateTime) {
        this.lastActiveDateTime = lastActiveDateTime;
    }

//    public Set<Tag> getTags() {
//        return tags;
//    }
//
//    public void setTags(Set<Tag> tags) {
//        this.tags = tags;
//    }
//
//    public Set<Word> getWords() {
//        return words;
//    }
//
//    public void setWords(Set<Word> words) {
//        this.words = words;
//    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    //方便日志与测试
    public String toStringJustUsernameAndEmail(){
        return "(username:" + this.username + " email: " + this.email + ")";
    }

    @Override
    public String toString() {
        return "\nUser{" +
                "\n\tid=" + id +
                ",\n\t username='" + username + '\'' +
                ",\n\t email='" + email + '\'' +
                ",\n\t password='" + password + '\'' +
                ",\n\t sex=" + sex +
                ",\n\t accountState=" + accountState +
                ",\n\t activateCode='" + activateCode + '\'' +
                ",\n\t logoUrl=" + logoUrl + '\'' +
                ",\n\t lastActiveDateTime=" + lastActiveDateTime +
                ",\n\t token='" + token + '\'' +
                ",\n\t sessionId='" + sessionId + '\'' +
//                ",\n\t authorityCode='" + authority.getAuthorityCode() + '\'' +
//                ",\n\t authorityType='" + authority.getAuthrityType() + '\'' +
                "\n}";
    }
}
