
import org.apache.flink.api.common.io.RichOutputFormat;
import org.apache.flink.configuration.Configuration;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MailOtp extends RichOutputFormat<String> {
    private static final Logger LOG = LoggerFactory.getLogger(MailOutputFormat.class);

    private String subject;
    private String url = "http://******/****/*****";//可以是自己的报警平台/短信平台
    CloseableHttpResponse response ;
    private CloseableHttpClient httpClient;
    private HttpPost httpPost;
    private List<NameValuePair> paramList;

    @Override
    public void configure(Configuration parameters) {

    }

    @Override
    public void open(int taskNumber, int numTasks) throws IOException {
        httpClient = HttpClients.createDefault();

        httpPost = new HttpPost(url);
        paramList = new ArrayList<>();
        paramList.add(new BasicNameValuePair("subject",subject));

    }

    @Override
    public void writeRecord(String record) throws IOException {
        paramList.add(new BasicNameValuePair("content",record));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
        httpPost.setEntity(entity);
        response = httpClient.execute(httpPost);

    }

    @Override
    public void close() throws IOException {
        response.close();
    }


    public static MailOtp.MailOtpBuilder buildMailOtp() {
        return new MailOtp.MailOtpBuilder();
    }


    public static class MailOtpBuilder {
        private final MailOtp format;

        public MailOtpBuilder() {
            this.format = new MailOtp();
        }


        public MailOtp.MailOtpBuilder setSubject(String subject) {
            format.subject = subject;
            return this;
        }




        public MailOtp finish() {
            return format;
        }
    }
}