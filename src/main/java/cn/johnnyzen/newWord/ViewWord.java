package cn.johnnyzen.newWord;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/3  19:58:09
 * @Description: 视图Bean，针对某些字段由于json需要屏蔽等因素，而特设此bean转发给客户端特定字段可见
 */

public class ViewWord implements Serializable {
    /* 生词ID */
    private Integer newWordId;
    private Timestamp createNewWordDatetime;
    private Timestamp lastStoredDatetime;
    private Timestamp lastForgotDatetime;
    /* 遗忘次数 */
    private Byte forgetCount;
    /* 遗忘权重指数 */
    private Double forgetRate;

    /* 单词ID */
    private Integer wordId;
    private String englishWord;
    private String chineseTranslate;

    /* 用户ID */
    private Integer userId;
    private String username;
    private String email;

    public ViewWord(){
        super();
    }

    public ViewWord(NewWord newWord){
        this();
        this.setNewWordId(newWord.getId());
        this.setCreateNewWordDatetime(newWord.getCreateNewWordDatetime());
        this.setLastStoredDatetime(newWord.getLastStoredDatetime());
        this.setLastForgotDatetime(newWord.getLastForgotDatetime());
        this.setForgetCount(newWord.getForgetCount());
        this.setForgetRate(newWord.getForgetRate());

        this.setWordId(newWord.getWord().getId());
        this.setEnglishWord(newWord.getWord().getEnglishWord());
        this.setChineseTranslate(newWord.getWord().getChineseTranslate());

        this.setUserId(newWord.getUser().getId());
        this.setUsername(newWord.getUser().getUsername());
        this.setEmail(newWord.getUser().getEmail());
    }

    public static List<ViewWord> newWordsToViewWords(Collection<NewWord> newWords){
        Iterator<NewWord> nwIter = newWords.iterator();
        List<ViewWord> viewWords = new ArrayList<ViewWord>();
        while(nwIter.hasNext()){
            viewWords.add(new ViewWord(nwIter.next()));
        }
        return viewWords;
    }

    public static ViewWord newWordToViewWord(NewWord neWord){
        return new ViewWord(neWord);
    }

    public Integer getNewWordId() {
        return newWordId;
    }

    public void setNewWordId(Integer newWordId) {
        this.newWordId = newWordId;
    }

    public Timestamp getCreateNewWordDatetime() {
        return createNewWordDatetime;
    }

    public void setCreateNewWordDatetime(Timestamp createNewWordDatetime) {
        this.createNewWordDatetime = createNewWordDatetime;
    }

    public Timestamp getLastStoredDatetime() {
        return lastStoredDatetime;
    }

    public void setLastStoredDatetime(Timestamp lastStoredDatetime) {
        this.lastStoredDatetime = lastStoredDatetime;
    }

    public Timestamp getLastForgotDatetime() {
        return lastForgotDatetime;
    }

    public void setLastForgotDatetime(Timestamp lastForgotDatetime) {
        this.lastForgotDatetime = lastForgotDatetime;
    }

    public Byte getForgetCount() {
        return forgetCount;
    }

    public void setForgetCount(Byte forgetCount) {
        this.forgetCount = forgetCount;
    }

    public Double getForgetRate() {
        return forgetRate;
    }

    public void setForgetRate(Double forgetRate) {
        this.forgetRate = forgetRate;
    }

    public Integer getWordId() {
        return wordId;
    }

    public void setWordId(Integer wordId) {
        this.wordId = wordId;
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    @Override
    public String toString() {
        return "ViewWord{" +
                "\n\t newWordId='" + newWordId + '\'' +
                ",\n\t createNewWordDatetime=" + createNewWordDatetime +
                ",\n\t lastStoredDatetime=" + lastStoredDatetime +
                ",\n\t lastForgotDatetime=" + lastForgotDatetime +
                ",\n\t forgetCount=" + forgetCount +
                ",\n\t forgetRate=" + forgetRate +
                ",\n\t wordId='" + wordId + '\'' +
                ",\n\t englishWord='" + englishWord + '\'' +
                ",\n\t chineseTranslate='" + chineseTranslate + '\'' +
                ",\n\t userId='" + userId + '\'' +
                ",\n\t username='" + username + '\'' +
                ",\n\t email='" + email + '\'' +
                '}';
    }
}
