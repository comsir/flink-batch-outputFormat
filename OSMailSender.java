
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.List;

/**
 * 在Java中调用linux sendmail命令发邮件
 * https://vosamo.github.io/2016/09/java-shell-mail/
 */
public class EmailMsgSender {
    private static final Logger LOG = LoggerFactory.getLogger(EmailMsgSender.class);
    private static final String FROM;
    private static final String MAIL_MESSAGE_TEMPLATE =
            "SUBJECT: {0} \n"    //邮件标题
                    + "TO: {1} \n"       //收件人
                    + "FROM: {2} \n"     //发件人
                    + "MIME-VERSION: 1.0 \n"
                    + "Content-type: text/html \n"
                    + "{3}";    //邮件正文

    @Override
    public void send(String msg) {
        String mailMessage = MessageFormat.format(MAIL_MESSAGE_TEMPLATE, subject, receiver, FROM, msg);
        String mailCommand = "echo '" + mailMessage + "'| sendmail -toi";
        callShell(mailCommand);
    }

    /**
     * @param shellString
     */
    public void callShell(String shellString) {
        String[] command = {"/bin/sh", "-c", shellString};
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String message = "";
            String line = "";
            while ((line = input.readLine()) != null) {
                message += line;    //获取命令执行结果
            }
            input.close();
            int exitValue = process.waitFor();    //获取命令执行返回状态码
            if (0 != exitValue) {
                LOG.error("call shell failed. error code is :" + exitValue + "  Reason: " + message);
            }
        } catch (Throwable e) {
            LOG.error("call shell failed. " + e);
        }
    }
}