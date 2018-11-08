package cn.johnnyzen.word;

import java.io.Serializable;
import java.util.List;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/3  10:18:00
 * @Description: 第三方【翻译接口结果集】的映射实体
 */

public class ThirdWordResult implements Serializable {
    private String type;

    private int errorCode;

    private int elapsedTime;

    private List<List<ThirdWord>> translateResult;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public List<List<ThirdWord>> getTranslateResult() {
        return translateResult;
    }

    public void setTranslateResult(List<List<ThirdWord>> translateResult) {
        this.translateResult = translateResult;
    }

    @Override
    public String toString() {
        return "ThirdWordResult{" +
                "\n\t type='" + type + '\'' +
                ",\n\t errorCode=" + errorCode +
                ",\n\t elapsedTime=" + elapsedTime +
                ",\n\t translateResult=" + translateResult +
                '}';
    }
}
