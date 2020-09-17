package ${packageName}.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Http工具类
 * @author Bob
 */
public class HttpUtil {

    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    private static SSLConnectionSocketFactory sslsf = null;
    private static PoolingHttpClientConnectionManager cm = null;
    private static SSLContextBuilder builder = null;
    static {
        try {
            builder = new SSLContextBuilder();
            // 全部信任 不做身份鉴定
            builder.loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            });
            sslsf = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP, new PlainConnectionSocketFactory())
                    .register(HTTPS, sslsf)
                    .build();
            cm = new PoolingHttpClientConnectionManager(registry);
            cm.setMaxTotal(200);//max connection
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CloseableHttpClient getHttpClient(String protocol) throws Exception {
        CloseableHttpClient httpClient = null;
        if ("https".equals(protocol)) {
            httpClient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .setConnectionManager(cm)
                    .setConnectionManagerShared(true)
                    .build();
            return httpClient;
        } else {
            httpClient = HttpClients.createDefault();
        }
        return httpClient;
    }

    public static String doGet(String url, Map<String, String> headerMap) throws Exception {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = "";
        String protocol = "http";
        try {
            // 通过址默认配置创建一个httpClient实例
            httpClient = getHttpClient(protocol);
            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            // 设置请求头信息
            if (null != headerMap && headerMap.size() > 0) {
                for (String key : headerMap.keySet()) {
                    httpGet.setHeader(key, headerMap.get(key));
                }
            }
            // 设置配置请求参数
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
                    .setConnectionRequestTimeout(35000)// 请求超时时间
                    .setSocketTimeout(60000)// 数据读取超时时间
                    .build();
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            // 执行get请求得到返回对象
            response = httpClient.execute(httpGet);
            // 状态不是200，抛异常
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                throw new Exception(EntityUtils.toString(response.getEntity()));
            }
            // 通过返回对象获取返回数据
            HttpEntity entity = response.getEntity();
            // 通过EntityUtils中的toString方法将结果转换为字符串
            result = EntityUtils.toString(entity);
        } finally {
            // 关闭资源
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    //post请求方法（Json方式）
    public static String doPost(String url, String jsonData, Map<String, String> headerMap) {
        String response = null;
        String protocol = "http";
        try {
            CloseableHttpClient httpclient = null;
            CloseableHttpResponse httpresponse = null;
            try {
                httpclient = getHttpClient(protocol);
                HttpPost httpPost = new HttpPost(url);
                if (jsonData != null || "".equals(jsonData)) {
                    StringEntity stringEntity = new StringEntity(jsonData, ContentType.create("application/json", "UTF-8"));
                    httpPost.setEntity(stringEntity);
                }
                // 配置请求参数实例
                RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
                        .setConnectionRequestTimeout(35000)// 设置连接请求超时时间
                        .setSocketTimeout(60000)// 设置读取数据连接超时时间
                        .build();
                // 为httpPost实例设置配置
                httpPost.setConfig(requestConfig);
                // 设置请求头信息
                if (null != headerMap && headerMap.size() > 0) {
                    for (String key : headerMap.keySet()) {
                        httpPost.setHeader(key, headerMap.get(key));
                    }
                }
                httpresponse = httpclient.execute(httpPost);
                int statusCode = httpresponse.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    throw new Exception(EntityUtils.toString(httpresponse.getEntity()));
                }
                response = EntityUtils.toString(httpresponse.getEntity());
            } finally {
                if (httpclient != null) {
                    httpclient.close();
                }
                if (httpresponse != null) {
                    httpresponse.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    //post请求方法（form表单方式）
    public static String doPost(String url, Map<String, String> bodyMap, Map<String, String> headerMap) {
        String response = null;
        String protocol = "http";
        try {
            CloseableHttpClient httpclient = null;
            CloseableHttpResponse httpresponse = null;
            try {
                httpclient = getHttpClient(protocol);
                HttpPost httpPost = new HttpPost(url);
                List<BasicNameValuePair> parames = new ArrayList<>();
                if (bodyMap != null) {
                    for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
                        parames.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                    }
                }
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(parames, "UTF-8");
                // 配置请求参数实例
                RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
                        .setConnectionRequestTimeout(35000)// 设置连接请求超时时间
                        .setSocketTimeout(60000)// 设置读取数据连接超时时间
                        .build();
                // 为httpPost实例设置配置
                httpPost.setConfig(requestConfig);
                // 设置请求头信息
                if (null != headerMap && headerMap.size() > 0) {
                    for (String key : headerMap.keySet()) {
                        httpPost.setHeader(key, headerMap.get(key));
                    }
                }
                httpPost.setEntity(urlEncodedFormEntity);
                httpresponse = httpclient.execute(httpPost);
                int statusCode = httpresponse.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    throw new Exception(EntityUtils.toString(httpresponse.getEntity()));
                }
                response = EntityUtils.toString(httpresponse.getEntity());
            } finally {
                if (httpclient != null) {
                    httpclient.close();
                }
                if (httpresponse != null) {
                    httpresponse.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    //put请求方法
    public static String doPut(String url, String jsonData, Map<String, String> headerMap) {
        String response = null;
        String protocol = "http";
        try {
            CloseableHttpClient httpclient = null;
            CloseableHttpResponse httpresponse = null;
            try {
                httpclient = getHttpClient(protocol);
                HttpPut httpPut = new HttpPut(url);
                StringEntity stringentity = new StringEntity(jsonData, ContentType.create("application/json", "UTF-8"));
                // 配置请求参数实例
                RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
                        .setConnectionRequestTimeout(35000)// 设置连接请求超时时间
                        .setSocketTimeout(60000)// 设置读取数据连接超时时间
                        .build();
                // 为httpPost实例设置配置
                httpPut.setConfig(requestConfig);
                // 设置请求头信息
                if (null != headerMap && headerMap.size() > 0) {
                    for (String key : headerMap.keySet()) {
                        httpPut.setHeader(key, headerMap.get(key));
                    }
                }
                httpPut.setEntity(stringentity);
                httpresponse = httpclient.execute(httpPut);
                response = EntityUtils.toString(httpresponse.getEntity());
            } finally {
                if (httpclient != null) {
                    httpclient.close();
                }
                if (httpresponse != null) {
                    httpresponse.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    //delete请求方法
    public static String doDelete(String url, Map<String, String> headerMap) {
        String response = null;
        String protocol = "http";
        try {
            CloseableHttpClient httpclient = null;
            CloseableHttpResponse httpresponse = null;
            try {
                httpclient = getHttpClient(protocol);
                HttpDelete httpDelete = new HttpDelete(url);
                // 配置请求参数实例
                RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 设置连接主机服务超时时间
                        .setConnectionRequestTimeout(35000)// 设置连接请求超时时间
                        .setSocketTimeout(60000)// 设置读取数据连接超时时间
                        .build();
                // 为httpPost实例设置配置
                httpDelete.setConfig(requestConfig);
                // 设置请求头信息
                if (null != headerMap && headerMap.size() > 0) {
                    for (String key : headerMap.keySet()) {
                        httpDelete.setHeader(key, headerMap.get(key));
                    }
                }
                httpresponse = httpclient.execute(httpDelete);
                response = EntityUtils.toString(httpresponse.getEntity());
            } finally {
                if (httpclient != null) {
                    httpclient.close();
                }
                if (httpresponse != null) {
                    httpresponse.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 获取请求明细
     * @param request HTTP Request
     * @return 请求明细
     */
    public static String getRequestDetailInfo(HttpServletRequest request) {
        StringBuilder msgDetailSb = new StringBuilder()
                .append("<<< HTTP请求明细 >>>\r\n")
                .append("【METHOD】" + request.getMethod() + "\r\n")
                .append("【URL】" + request.getRequestURL() + "\r\n")
                .append("【QUERY_STRING】" + (request.getQueryString() == null ? "null" : request.getQueryString()) + "\r\n")
                .append("【USER】" + (request.getUserPrincipal() == null ? "null" : request.getUserPrincipal().getName()) + "\r\n");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            // 获取每个Header内容
            String headerName = headerNames.nextElement();
            msgDetailSb.append("【HEADER：" + headerName + "】" + request.getHeader(headerName) + "\r\n");
        }
        if (request.getContentType() != null && request.getContentType().toLowerCase().contains("application/json")) {
            // 获取HTTP Body中的JSON字符串
            StringBuilder bodySb = new StringBuilder();
            InputStream inputStream = null;
            BufferedReader reader = null;
            try {
                inputStream = request.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                char[] bodyCharBuffer = new char[1024];
                int len = 0;
                while ((len = reader.read(bodyCharBuffer)) != -1) {
                    bodySb.append(new String(bodyCharBuffer, 0, len));
                }
            } catch (IOException e) {
                // do nothing
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        // do nothing
                    }
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        // do nothing
                    }
                }
            }
            msgDetailSb.append("【BODY】\r\n" + bodySb.toString() + "\r\n");
        }
        return msgDetailSb.toString();
    }

}
