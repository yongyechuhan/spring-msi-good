package com.example.springjdsale;

import com.sun.mail.util.MailSSLSocketFactory;
import lombok.extern.slf4j.Slf4j;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@Slf4j
public class EmailUtils {

    private static String emailServer = "smtp.qq.com";
    private static String emailAccount = "1640734459@qq.com";

    private static String accountToken = "qccjvbbfoxjsfbdi";

    public static void sendEmail(String emailContent) {
        Properties props = new Properties();

        // 开启debug调试
        props.setProperty("mail.debug", "true");

        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");

        // 端口号
        props.put("mail.smtp.port", 465);

        // 设置邮件服务器主机名
        props.setProperty("mail.smtp.host", emailServer);

        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        /**SSL认证，注意腾讯邮箱是基于SSL加密的，所以需要开启才可以使用**/
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();

            sf.setTrustAllHosts(true);

            //设置是否使用ssl安全连接（一般都使用）
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);

            //创建会话
            Session session = Session.getInstance(props);

            //获取邮件对象
            //发送的消息，基于观察者模式进行设计的
            Message msg = new MimeMessage(session);

            //设置邮件标题
            msg.setSubject("京东抢购MSI 4090显卡通知");

            //设置邮件内容
            //使用StringBuilder，因为StringBuilder加载速度会比String快，而且线程安全性也不错
            StringBuilder builder = new StringBuilder();

            //写入内容
            builder.append("\n" + emailContent);

            //定义要输出日期字符串的格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            //在内容后加入邮件发送的时间
            builder.append("\n时间：" + sdf.format(new Date()));

            //设置显示的发件时间
            msg.setSentDate(new Date());

            //设置邮件内容
            msg.setText(builder.toString());

            //设置发件人邮箱
            // InternetAddress 的三个参数分别为: 发件人邮箱, 显示的昵称(只用于显示, 没有特别的要求), 昵称的字符集编码
            msg.setFrom(new InternetAddress(emailAccount,"我的QQ邮箱", "UTF-8"));

            //得到邮差对象
            Transport transport = session.getTransport();

            //连接自己的邮箱账户
            //密码不是自己QQ邮箱的密码，而是在开启SMTP服务时所获取到的授权码
            //connect(host, user, password)
            transport.connect(emailServer, emailAccount, accountToken);

            //发送邮件
            transport.sendMessage(msg, new Address[] { new InternetAddress("liuxinaihui@live.com") });

            transport.close();
        } catch (Exception e) {
            log.error("send notice mail failed", e);
        }
    }
}
