package cn.johnnyzen.word;

import java.io.Serializable;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/3  10:10:34
 * @Description: ...
 */

public class ThirdWord  implements Serializable {
    //tranlate source
    private String src;

    //transla result
    private String tgt;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTgt() {
        return tgt;
    }

    public void setTgt(String tgt) {
        this.tgt = tgt;
    }

    @Override
    public String toString() {
        return "ThirdWord{" +
                "\n\t src='" + src + '\'' +
                ",\n\t tgt='" + tgt + '\'' +
                '}';
    }
}
