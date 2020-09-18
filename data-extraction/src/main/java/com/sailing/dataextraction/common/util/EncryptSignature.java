package com.sailing.dataextraction.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;

/**
 * @author XLXX-B065
 * @version v1.0
 * @date 2020/7/15 15:43
 * @description TODO
 **/
@Slf4j
public class EncryptSignature {

    public static void main(String[] args) {
        String time = String.valueOf(System.currentTimeMillis());
        System.out.println(time);
        System.out.println(encryptSig(
                "10000001",
                "12345678900000001",
                "f979d4905094423593415b746b04c86c",
                time,
                "10000002",
                "R-52030032010000000014",
                "c717f4c4b8a04bf292b47d6bd1fa2b2b"));
    }

    public static String encryptSig(
            String from,
            String messageSequence,
            String secretId,
            String timestamp,
            String to,
            String identifiers,
            String accessKey
    ) {
        String accessUrl = "";
        String signature = "";
        if (!StringUtils.isEmpty(identifiers)) {
            accessUrl = "From=" + from + "&Identifiers=" + identifiers + "&MessageSequence=" + messageSequence
                    + "&SecretId=" + secretId + "&Timestamp=" + timestamp + "&To=" + to;
            //log.info("accessUrl:{}", accessUrl);
        }
        try {
            signature  = URLEncoder.encode(EncryptUtil.aesEncrypt(accessUrl, accessKey), "UTF-8" );
        } catch (Exception e) {
            log.info("生成Signature异常:{}", e);
        }
        return signature;
    }

}
