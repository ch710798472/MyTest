package com.test.ch.domain;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import javax.imageio.stream.FileImageOutputStream;

import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Created by banmo.ch on 17/3/29.
 */
public class http {
    public static void main(String[] args) throws URISyntaxException, IOException {
        String url = "http://tmallhk-id-cards-online.cn-shanghai.oss.aliyun-inc.com/alipay~834581682~%E4%BD%95%E7%9D%BF%E6%AD%86~1?Expires=1494662719&OSSAccessKeyId=AR2iuJ0IbkrsIEwM&Signature=HrPbX0zVyFmk/lxPjesrKa2JRgI%3D";
        CloseableHttpClient client = HttpClients.createDefault();
        RequestConfig defaultRequestConfig = RequestConfig.custom()
            .setConnectTimeout(2000).setConnectionRequestTimeout(1000)
            .setSocketTimeout(2000).build();
        //发送get请求
        HttpGet request = new HttpGet(new URI(url));
        request.setConfig(defaultRequestConfig);
        CloseableHttpResponse response = client.execute(request);
        try {
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                byte[] byteResult = EntityUtils.toByteArray(response.getEntity());
                url = URLDecoder.decode(url, "UTF-8");
                FileImageOutputStream imageOutput = new FileImageOutputStream(new File("2.png"));
                imageOutput.write(byteResult, 0, byteResult.length);
                imageOutput.close();
            } else {
            }
        } catch (Exception e) {
        } finally {
            response.close();
        }
    }
}
