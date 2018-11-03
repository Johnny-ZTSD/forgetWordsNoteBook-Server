package cn.johnnyzen.util.request;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/11/3  09:08:56
 * @Description: ...
 */

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @ConfigurationProperties:
 *      使本Bean与application.yml配置文件中前缀为boy的实体的属性值形成属性与值一一映射
 *       即 将application.yml的属性值【通过本Bean形成实体规约】
 *
 * @Component:
 *      将本Bean置入Spring容器中，形成绑定
 *      把普通pojo实例化到spring容器中，相当于配置文件中的<bean id="" class=""/>
 *      定义Spring管理Bean.
 *
 * @Autowired：
 *      从Spring容器取值使用【前提：Component生效】
 */
@Component
@ConfigurationProperties(prefix="requestproperties")
public class RequestProperties {
    private String cacheControl;
    private String connection;
    private String accept;
    private String acceptEncoding;
    private String acceptLanguage;
    private String cookie;
    private String userAgent;

    public String getCacheControl() {
        return cacheControl;
    }

    public void setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
    }

    public String getConnection() {
        return connection;
    }

    public void setConnection(String connection) {
        this.connection = connection;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getAcceptEncoding() {
        return acceptEncoding;
    }

    public void setAcceptEncoding(String acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
    }

    public String getAcceptLanguage() {
        return acceptLanguage;
    }

    public void setAcceptLanguage(String acceptLanguage) {
        this.acceptLanguage = acceptLanguage;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public String toString() {
        return "RequestProperties{" +
                "\n\t cacheControl='" + cacheControl + '\'' +
                ",\n\t connection='" + connection + '\'' +
                ",\n\t accept='" + accept + '\'' +
                ",\n\t acceptEncoding='" + acceptEncoding + '\'' +
                ",\n\t acceptLanguage='" + acceptLanguage + '\'' +
                ",\n\t cookie='" + cookie + '\'' +
                ",\n\t userAgent='" + userAgent + '\'' +
                "\n}";
    }
}