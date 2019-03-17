import cn.qihoo.joblogs.logEntity.ExceptionEntity;
import org.apache.flink.api.common.io.RichOutputFormat;
import org.apache.flink.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

public class MailOutputFormat extends RichOutputFormat<String> {
    private static final Logger LOG = LoggerFactory.getLogger(MailOutputFormat.class);


    private Transport ts;
    private MimeMessage message;

    private String subject;



    @Override
    public void configure(Configuration parameters) {

    }

    @Override
    public void open(int taskNumber, int numTasks) throws IOException {

        Properties prop = new Properties();
                prop.setProperty("mail.host", "smtp.qq.com");
                prop.setProperty("mail.transport.protocol", "smtp");
                prop.setProperty("mail.smtp.auth", "true");
                Session session = Session.getInstance(prop);
                session.setDebug(true);

        try {
            ts = session.getTransport();
            ts.connect("smtp.qq.com", "admin@qq.com", "password");

        } catch (Exception e) {
            e.printStackTrace();
        }


        message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("admin@qq.com"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress("admin@qq.com"));
            message.setSubject(subject);
        } catch (MessagingException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void writeRecord(String record) throws IOException {
        try {
            message.setContent(record, "text/html;charset=UTF-8");
            ts.sendMessage(message, message.getAllRecipients());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws IOException {
        try {
            ts.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    public static MailOutputFormatBuilder buildMailOutputFormat() {
        return new MailOutputFormatBuilder();
    }


    public static class MailOutputFormatBuilder {
        private final MailOutputFormat format;

        public MailOutputFormatBuilder() {
            this.format = new MailOutputFormat();
        }





        public MailOutputFormat finish() {
            return format;
        }
    }

}