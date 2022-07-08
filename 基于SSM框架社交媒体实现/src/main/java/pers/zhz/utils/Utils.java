package pers.zhz.utils;

import javax.mail.PasswordAuthentication;
import javax.mail.Authenticator;
import javax.servlet.http.HttpServletRequest;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 工具类，获取ip地址、创建邮件
 */
public class Utils {

    /**
     * 获取Ip地址
     *
     * @param request HttpServletRequest对象
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 创建并发送一封只包含文本的简单163邮件
     *
     * @param receiver    收件人邮箱
     * @param mailSubject 主题
     * @param mailContent 邮件正文
     * @throws Exception 异常
     */
    public static void send163Mail(String receiver, String mailSubject, String mailContent) throws Exception {
        String sender = "发件人邮箱";//发件人邮箱
        String senderName = "发件人名";//发件人名
        String senderPwd = "邮箱的授权码";//授权码

        //1.创建Properties 类用于记录邮箱的一些属性
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");//表示SMTP发送邮件，必须进行身份验证
        props.put("mail.smtp.host", "smtp.163.com");//SMTP服务器
        //props.put("mail.smtp.port", "587");//端口号，QQ邮箱端口587
        //2.构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, senderPwd);
            }
        };
        //3.使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        //4.创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        //5.设置发件人
        message.setFrom(new InternetAddress(sender, senderName));
        //6.设置收件人的邮箱
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiver));
        //7.设置邮件标题
        message.setSubject(mailSubject);
        //8.设置邮件的内容体
        message.setContent(mailContent, "text/html;charset=UTF-8");
        //9.发送邮件
        Transport.send(message);
    }
}