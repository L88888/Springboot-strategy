package com.sailing.dataextraction.service;

import com.alibaba.fastjson.JSONObject;
import com.sailing.dataextraction.util.EncryptSignature;
import com.sailing.dataextraction.util.OKHttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className EsToNeo4j
 * @description EsToNeo4j
 * @date 2020/8/4 11:48
 **/
@Slf4j
@Service
public class EsToNeo4j {

    @Value("${queryFullTextURI}")
    private String queryFullTextURI;

    @Value("${queryFullTextCountURI}")
    private String queryFullTextCountURI;

    @Value("${dataGovernanceHOST}")
    private String dataGovernanceHOST;

    /**
     * 数据治理接口数据发送方标识
     */
    @Value("${dataGovernanceFrom}")
    private String dataGovernanceFrom;
    /**
     * 数据治理 MessageSequence
     */
    @Value("${dataGovernanceMessage}")
    private String dataGovernanceMessage;
    /**
     * SecretId
     */
    @Value("${dataGovernanceSecretId}")
    private String dataGovernanceSecretId;
    /**
     * 数据治理接口数据接收方标识
     */
    @Value("${dataGovernanceTo}")
    private String dataGovernanceTo;
    /**
     * AccessKey
     */
    @Value("${dataGovernanceAccessKey}")
    private String dataGovernanceAccessKey;

    private static boolean netLock = false;

    private static boolean netCountLock = false;

    /**
     * 获取数据信息统计
     * @param resourceId 数据资源ID
     * @param condition 条件
     * @return
     */
    public long queryDataCount(String title, String resourceId, String condition){
        while (netCountLock){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e){}
        }
        netCountLock = true;
        Map<String, String> params = this.setQueryFullTextParams(resourceId, queryFullTextCountURI, condition, 0, 0);
        log.info("{}--获取数据信息统计-请求地址：{}", title, params.get("url"));
        log.info("{}--获取数据信息统计-请求参数：{}", title, params.get("params"));
        OKHttpClientUtil result = OKHttpClientUtil.okHttpPost(params.get("url"), null, params.get("params"));
        log.info("{}--获取数据信息统计-返回结果：{}", JSONObject.toJSONString(result));
        if (result.getStatus() == 200 && !StringUtils.isEmpty(result.getData())){
            try {
                Map resultMap = JSONObject.parseObject(result.getData(), Map.class);
                Object rp = resultMap.get("ResponseParam");
                if (rp != null && ((Map)rp).get("ResourceInfos") != null){
                    Map ri = (Map)((List)((Map)rp).get("ResourceInfos")).get(0);
                    Object di = ri.get("DataInfo");
                    if (di != null && ((List)di).size() > 0){
                        List dataInfos = (List)di;
                        netCountLock = false;
                        return Long.parseLong(String.valueOf(dataInfos.get(1)));
                    }
                }
            }catch (Exception e){
                log.error("获取数据治理的数据异常，参数：", JSONObject.toJSONString(params));
                e.printStackTrace();
            }
        }
        netCountLock = false;
        return 0;
    }

    /**
     * 获取数据信息
     */
    public List<Map<String, String>> queryData(String title, String resourceId, String condition,String dataType, long page, long size){
        return this.queryDataCore(title, resourceId, condition,dataType, page, size);
    }

    public List<Map<String, String>> queryData(String title, String resourceId, String condition, long page, long size){
        return this.queryDataCore(title, resourceId, condition,"",page, size);
    }

    /**
     * 获取数据信息
     */
    private List<Map<String, String>> queryDataCore(String title, String resourceId, String condition, String dataType, long page, long size){
        while (netLock){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e){}
        }
        netLock = true;
        Map<String, String> params = this.setQueryFullTextParams(resourceId, queryFullTextURI, condition, page, size);
        log.info("{}--获取数据信息-请求地址：{}", title, params.get("url"));
        log.info("{}--获取数据信息-请求参数：{}", title, params.get("params"));
        OKHttpClientUtil result = OKHttpClientUtil.okHttpPost(params.get("url"), null, params.get("params"));
        if (result.getStatus() == 200 && !StringUtils.isEmpty(result.getData())){
            try {
                Map resultMap = JSONObject.parseObject(result.getData(), Map.class);
                Object rp = resultMap.get("ResponseParam");
                if (rp != null && ((Map)rp).get("ResourceInfos") != null){
                    Map ri = (Map)((List)((Map)rp).get("ResourceInfos")).get(0);
                    Object di = ri.get("DataInfo");
                    if (di != null && ((List)di).size() > 0){
                        List dataInfos = (List)di;
                        List<Map<String, String>> dataItems = (List<Map<String, String>>) ri.get("DataItems");
                        List<Map<String, String>> data = new ArrayList<>();
                        for (Object obj : dataInfos) {
                            List dataInfo = (List)obj;
                            Map tempData = new HashMap(dataInfo.size());
                            for (int i = 0; i < dataInfo.size(); i++) {
                                // 解析行对象属性
                                tempData.put(dataItems.get(i).get("Name"), String.valueOf(dataInfo.get(i)));
                            }
                            data.add(tempData);
                        }
                        netLock = false;
                        return data;
                    }
                }
            }catch (Exception e){
                log.error("获取数据治理的数据异常，参数：", JSONObject.toJSONString(params));
                e.printStackTrace();
            }
        }
        netLock = false;
        return null;
    }


    /**
     * 组装全文检索接口的请求地址和参数
     * @author Liufei Yang
     * @date 2020/7/27 14:55
     * @param resourceId 资源标识ID
     * @param uri 请求URI
     * @param condition 检索参数 例：all:'郭刚'
     * @return {"URL":"请求地址", "params":"请求参数"}
     **/
    private Map<String, String> setQueryFullTextParams(String resourceId, String uri, String condition, long page, long size){
        String timestamp = String.valueOf(System.currentTimeMillis());
        String identifier = resourceId.split(",")[0];
        String url = dataGovernanceHOST + uri;
        // 获取签名
        String signature = EncryptSignature.encryptSig(
                dataGovernanceFrom,
                dataGovernanceMessage,
                dataGovernanceSecretId,
                timestamp,
                dataGovernanceTo,
                resourceId,
                dataGovernanceAccessKey
        );

        // 设置查询条件
        Map<String, Object> requestParam = new HashMap<>(2);
        requestParam.put("Condition", condition);

        // 设置资源标识符
        List<Map> resourceList = new ArrayList<>();
        Map<String, Object> resourceMap = new HashMap<>(1);
        resourceMap.put("ResourceName", resourceId);
        resourceList.add(resourceMap);
        requestParam.put("ResourceInfos", resourceList);

        if (page != 0 || size != 0){
            // 设置分页
            Map<String, Long> extend = new HashMap<>(2);
            // 优先处理损坏点的数据重复问题
            extend.put("Page", page != 1 ? page++ : page);
            extend.put("Size", size);
            requestParam.put("Extend", extend);
        }

        // 其它参数
        Map<String, Object> body = new HashMap<>(4);
        body.put("From", dataGovernanceFrom);
        body.put("MessageSequence", dataGovernanceMessage);
        body.put("RequestParam", requestParam);
        body.put("To", dataGovernanceTo);
        body.put("Identifiers", resourceId);

        String urlPath = url + "?Identifiers=" + identifier + "&SecretId=" + dataGovernanceSecretId
                + "&Timestamp=" + timestamp + "&Signature=" + signature;

        Map<String, String> params = new HashMap<>(2);
        params.put("params", JSONObject.toJSONString(body));
        params.put("url", urlPath);
        return params;
    }

}
