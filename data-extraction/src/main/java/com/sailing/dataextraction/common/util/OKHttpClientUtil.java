package com.sailing.dataextraction.common.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className OKHttpClientUtil
 * @description OKHttpClientUtil
 * @date 2020/3/28 20:02
 **/
@Slf4j
@Data
public class OKHttpClientUtil {


    private int status;
    private Map<String, String> header;
    private String data;

    /**
     * POST请求
     * @param url 请求地址
     * @param headersMap header参数
     * @param content Body参数
     * @return {"status":"请求状态", "header":{"响应header Key":["响应header Value"]},"data":"响应Body"}
     */
    public static OKHttpClientUtil okHttpPost(String url, Map<String,String> headersMap, String content){
        OKHttpClientUtil okResult = new OKHttpClientUtil();
        OkHttpClient client = new OkHttpClient();
        try {
            Request.Builder builder = new Request.Builder().url(url);
            if (StringUtils.isEmpty(content)){
                content = "";
            }
            builder.post(RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),content));
            if (headersMap != null && headersMap.size() > 0){
                headersMap.forEach((key, value) -> {
                    builder.addHeader(key, value);
                });
            }
            Response response = client.newCall(builder.build()).execute();
            Headers headers = response.headers();
            Map<String, String> headerMap = new HashMap<>(headers.size());
            headers.toMultimap().forEach((key, value) -> {
                String v = "";
                for (String str : value) {
                    v = v.concat(str).concat(";");
                }
                if (StringUtils.isNotEmpty(v)){
                    headerMap.put(key,v.substring(0,v.length()-1));
                }
            });
            okResult.setStatus(response.code());
            okResult.setHeader(headerMap);
            okResult.setData(response.body() != null ? response.body().string() : null);
        } catch (Exception e) {
            okResult.setStatus(500);
            log.error("=========WARNING=========okHttpPost=========WARNING=========");
            log.error("=========请求异常URI：{}", url);
            e.printStackTrace();
        }
        return okResult;
    }

    /**
     * GET请求
     * @param url 请求地址
     * @param headersMap header参数
     * @return
     */
    public static OKHttpClientUtil okHttpGet(String url, Map<String,String> headersMap){
        OKHttpClientUtil okResult = new OKHttpClientUtil();
        OkHttpClient client = new OkHttpClient();
        try {
            Request.Builder builder = new Request.Builder().url(url).get();
            if (headersMap != null && headersMap.size() > 0){
                headersMap.forEach((key, value) -> {
                    builder.addHeader(key, value);
                });
            }
            Response response = client.newCall(builder.build()).execute();
            Headers headers = response.headers();
            Map<String, String> headerMap = new HashMap<>(headers.size());
            headers.toMultimap().forEach((key, value) -> {
                String v = "";
                for (String str : value) {
                    v = v.concat(str).concat(";");
                }
                if (StringUtils.isNotEmpty(v)){
                    headerMap.put(key,v.substring(0,v.length()-1));
                }
            });
            okResult.setStatus(response.code());
            okResult.setHeader(headerMap);
            okResult.setData(response.body() != null ? response.body().string() : null);
        } catch (Exception e) {
            okResult.setStatus(500);
            log.error("=========WARNING=========okHttpPost=========WARNING=========");
            log.error("=========请求异常URI：{}", url);
            e.printStackTrace();
        }
        return okResult;
    }
}

