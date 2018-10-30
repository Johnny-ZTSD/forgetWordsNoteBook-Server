package cn.johnnyzen.mail;

import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @IDE: Created by IntelliJ IDEA.
 * @Author: 千千寰宇
 * @Date: 2018/10/6  20:44:09
 * @Description: 参考文献 https://www.cnblogs.com/liuzhihu/p/8184883.html
 */
@Component
public class MailUtil implements Runnable {
    @Autowired
    private MailProperties mailProperties;

    private String reciverEmail = null;// 收件人邮箱
    private String activateCode = null;// 代注册用户激活码

    public MailUtil(){}

    public MailUtil(String reciverEmail, String activateCode) {
        this();
        this.reciverEmail = reciverEmail;
        this.activateCode = activateCode;
    }

    public void setReciverEmail(String reciverEmail){
        this.reciverEmail = reciverEmail;
    }

    public void setActivateCode(String activateCode){
        this.activateCode = activateCode;
    }

    /*
     * 仅当 reciverEmail 和 activateCode配置完成，方可运行成功
     * */
    public void run() {
        if(activateCode == null || reciverEmail == null){
            throw new RuntimeException("[MailUtil.run] activateCode or reciverEmail is empty!");
        }
        // 1.创建连接对象javax.mail.Session
        // 2.创建邮件对象 javax.mail.Message
        // 3.发送一封激活邮件
        String from = mailProperties.getSenderEmail();// 发件人电子邮箱
        String host = mailProperties.getHost(); // 指定发送邮件的主机smtp.qq.com(QQ)|smtp.163.com(网易)

        Properties properties = System.getProperties();// 获取系统属性
        properties.setProperty("mail.smtp.host", host);// 设置邮件服务器
        properties.setProperty("mail.smtp.auth", "true");// 打开认证

        try {
            //QQ邮箱需要下面这段代码，163邮箱不需要
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.ssl.socketFactory", sf);

            // 1.获取默认session对象
            Session session =
                    Session.getDefaultInstance(properties, new Authenticator() {
                        public PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(
                                    mailProperties.getSenderEmail(),
                                    mailProperties.getAuthCode()); // 发件人邮箱账号、授权码
                        }
                    });

            // 2.创建邮件对象
            Message message = new MimeMessage(session);
            // 2.1设置发件人
            message.setFrom(new InternetAddress(from));
            // 2.2设置接收人
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(reciverEmail));
            // 2.3设置邮件主题
            String contextPath = mailProperties.getContextPath();
            if(contextPath.startsWith("/")){//判断context-path有无/符去，如果有统一除/
                mailProperties.setContextPath(contextPath.substring(1));
            }

            message.setSubject("[ 账号激活    --" + mailProperties.getContextPath() + "]");
            // 2.4设置邮件内容
            String actionPath = mailProperties.getActionPath();
            if(actionPath.startsWith("/")){//判断actionPath有无/符去，如果有统一除/
                mailProperties.setActionPath(actionPath.substring(1));
            }
            String url = mailProperties.getProtocol() + "://"
                        + mailProperties.getServerDomain() + ":"
                        + mailProperties.getPort() + "/"
                        + mailProperties.getContextPath() + "/"
                        + mailProperties.getActionPath()
                        + "?code=" + activateCode;
            String content =
                    "<html><head></head><body><h1>这是一封激活邮件,激活请点击以下链接</h1><h3>" +
                            "<a href='" + url + "'>" + url + "</href></h3></body></html>";
            message.setContent(content, "text/html;charset=UTF-8");
            // 3.发送邮件
            Transport.send(message);
            System.out.println("邮件成功发送!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MailProperties getMailProperties() {
        return mailProperties;
    }

    public void setMailProperties(MailProperties mailProperties) {
        this.mailProperties = mailProperties;
    }

    public String getReciverEmail() {
        return reciverEmail;
    }

    public String getActivateCode() {
        return activateCode;
    }
}
