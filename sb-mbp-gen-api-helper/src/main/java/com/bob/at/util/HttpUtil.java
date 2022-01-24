package com.bob.at.util;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Enumeration;

public class HttpUtil {

    /**
     * 获取请求明细
     * @param request HTTP Request
     * @return 请求明细
     */
    public static String getRequestDetailInfo(HttpServletRequest request) {
        StringBuilder msgDetailSb = new StringBuilder()
                .append("<<< HTTP请求明细 >>>")
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
