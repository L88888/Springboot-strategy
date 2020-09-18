package com.sailing.dataextraction.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sailing.dataextraction.common.util.ApacheZipUtil;
import com.sailing.dataextraction.common.util.FtpClientUtil;
import com.sailing.dataextraction.common.util.SUtils;
import com.sailing.dataextraction.entity.GaZipIndexJson;
import com.sailing.dataextraction.entity.IndexJsonData;
import com.sailing.dataextraction.entity.Peers;
import com.sailing.dataextraction.entity.Population;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className dataFileAnalysis
 * @description 数据文件解析
 * @date 2020/8/25 10:50
 **/
@Slf4j
@Service
public class DataFileAnalysis {

    @Autowired
    private FtpClientUtil ftpClientUtil;

    @Autowired
    private CommonService commonService;

    @Value("${ftp.directory}")
    private String ftpDir;

    @Value("${dataTempDir}")
    private String dataTempDir;

    @Value("${gb.indexFileName}")
    private String indexFileName;

    @Value("${cleanCache}")
    private boolean cleanCache;

    @Value("${ftpCleanCache}")
    private boolean ftpCleanCache;

    /**
     * 同户信息
     */
    @Value("${sameInfoIdentifiers}")
    private String sameInfoIdentifiers;

    /**
     * 火车信息
     */
    @Value("${trainTripIdentifiers}")
    private String trainTripIdentifiers;

    /**
     * 飞机信息
     */
    @Value("${flightTripIdentifiers}")
    private String flightTripIdentifiers;

    /**
     * 客运信息
     */
    @Value("${busTripIdentifiers}")
    private String busTripIdentifiers;

    /** 旅店信息 */
    @Value("${accommodationIdentifiers}")
    private String accommodationIdentifiers;

    /**
     * 上网信息
     */
    @Value("${surfInternetIdentifiers}")
    private String surfInternetIdentifiers;

    /**
     * FTP数据抽取主服务，从FTP上获取数据文件，然后进行数据解析并推送至事件中
     * @author Liufei Yang
     * @date 2020/9/14 12:37
     **/
    @Scheduled(cron="${pullData.cron}")
    public void getFileData(){
        try {
            log.info("初始化Ftp服务连接");
            ftpClientUtil.initFtpClient();
            if (!ftpClientUtil.checkFTPClient()) {
                log.error("FTPClient初始化失败请检查相应配置");
                return;
            }
            Set<String> arrDirs = ftpClientUtil.initArrDirs();
            log.info("获取Ftp数据文件目录");
            ftpClientUtil.listDir(ftpDir, "zip");

            if (arrDirs.size() > 0){
                for (String arrDir : arrDirs) {
                    log.info("获取Ftp目录下的文件");
                    List<FTPFile> ftpFiles = ftpClientUtil.getFiles(arrDir);
                    log.info("获取Ftp服务器上文件数量：{}", ftpFiles.size());
                    for (FTPFile ftpFile : ftpFiles) {
                        String uuid = UUID.randomUUID().toString();
                        // 下载ZIP文件，获取下载文件路径
                        log.info("下载ZIP文件{}", ftpFile.getName());
                        String filePath = ftpClientUtil.downloadFile(arrDir, dataTempDir, ftpFile);
                        // 文件解压目录
                        String unzipDir = dataTempDir.concat(File.separator).concat(uuid);
                        // 解压文件
                        log.info("解压文件ZIP文件{}到{}目录", filePath, unzipDir);
                        ApacheZipUtil.unZipFile(filePath, unzipDir);
                        // ZIP索引文件路径
                        String indexFilePath = unzipDir.concat(File.separator).concat(indexFileName);
                        // 解析国标ZIP索引文件
                        log.info("解析国标ZIP索引文件：{}", indexFilePath);
                        List<IndexJsonData> indexJsonDatas = this.readIndexJson(this.readAllFileData(indexFilePath));
                        log.info("国标ZIP索引文件内容：{}", JSONArray.toJSONString(indexJsonDatas));
                        if (indexJsonDatas.size() > 0){
                            for (IndexJsonData indexJsonData : indexJsonDatas) {
                                IndexJsonData  jsonData;
                                for (String dataFile : indexJsonData.getDataFiles()) {
                                    jsonData = new IndexJsonData();
                                    String dataFilePath = unzipDir.concat(File.separator).concat(dataFile);
                                    // 解析文件数据
                                    List<Map<String, String>> list = this.readLineFileData(indexJsonData.getDataItems(), dataFilePath);
                                    jsonData.setData(list);
                                    jsonData.setResourceId(indexJsonData.getResourceId());
                                    // 推送数据至事件
                                    commonService.onApplicationEvent(jsonData);
                                    log.info("{}数据文件{}解析条数：{},推送数据事件", indexJsonData.getResourceId(),dataFile, list.size());
                                }
                            }
                        }
                        // 清理解析数据时产生的临时文件
                        log.info("清理临时文件{}, {}, {}", unzipDir, filePath, ftpFile.getName());
                        SUtils.deleteFile(unzipDir);
                        if (cleanCache){
                            SUtils.deleteFile(filePath);
                        }
                        // 清理已抽取的FTP数据文件
                        if (ftpCleanCache){
                            ftpClientUtil.removeFile(arrDir, ftpFile.getName());
                        }
                    }
                }
            }
            // 关闭FTP连接
            ftpClientUtil.colseFtpClient();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 解析国标ZIP索引文件
     * @param indexStr 索引json字符串
     * @return 索引数据集合
     */
    private List<IndexJsonData> readIndexJson(String indexStr){
        List<IndexJsonData> result = new ArrayList<>();
        if (StringUtils.isNotEmpty(indexStr)){
            GaZipIndexJson indexJson = JSONObject.parseObject(indexStr, GaZipIndexJson.class);

            for (GaZipIndexJson.DataDescription dataDescription : indexJson.getDATA_DESCRIPTIONS()) {
                IndexJsonData indexJsonData = new IndexJsonData();
                String dataResourceId = dataDescription.getDATA_RESOURCE_ID();
                List<String> dataFiles = new ArrayList<>();
                List<String> dataItems = new ArrayList<>();
                for (GaZipIndexJson.DataDescription.DataFile dataFile : dataDescription.getDATA_FILES()) {
                    int recordNum = Integer.parseInt(dataFile.getDATA_FILE_RECORD_NUM());
                    if (recordNum > 0){
                        dataFiles.add(dataFile.getDATA_FILE_PATH());
                    }
                }
                for (GaZipIndexJson.DataDescription.DataItem dataItem : dataDescription.getDATA_ITEMS()) {
                    dataItems.add(dataItem.getIDENTITIER());
                }
                if (dataFiles.size() > 0){
                    indexJsonData.setDataFiles(dataFiles);
                    indexJsonData.setDataItems(dataItems);
                    indexJsonData.setResourceId(dataResourceId);
                    result.add(indexJsonData);
                }
            }
        }
        return result;
    }

    /**
     * 逐行读取数据文件
     * @param dataItems 数据标识符
     * @param filePath 数据文件路径
     * @return List<Map<String, String>>
     */
    private List<Map<String, String>> readLineFileData(List<String> dataItems, String filePath){
        List<Map<String, String>> result = new ArrayList<>();
        InputStreamReader in = null;
        BufferedReader br = null;
        try {
            in = new InputStreamReader(new FileInputStream(new File(filePath)),"UTF-8");
            br = new BufferedReader(in);
            String content;
            String[] line;
            Map<String, String> map;
            while ((content = br.readLine()) != null){
                line = content.split("\t");
                map = new HashMap<>(dataItems.size());
                for (int i = 0; i < line.length; i++) {
                    map.put(dataItems.get(i), line[i]);
                }
                if (map.size() > 0){
                    result.add(map);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null){
                    br.close();
                }
                if (in != null){
                    in.close();
                }
            } catch (IOException ignored) { in = null; br = null; }
        }
        return result;
    }

    /**
     * 解析FTP中六类同行信息对象
     * @param dataItems 同行信息对象索引
     * @param filePath ftp文科访问路径
     * @param resourceId 资源id
     * @return 具体同行对象接口
     */
    private List<Peers> readLineFileData(List<String> dataItems, String filePath, String resourceId){
        List<Peers> result = new ArrayList<>();
        InputStreamReader in = null;
        BufferedReader br = null;
        // 人员信息
        Population population = null;

        try {
            in = new InputStreamReader(new FileInputStream(new File(filePath)),"UTF-8");
            br = new BufferedReader(in);
            String content;
            String[] line;
            Map<String, String> map;
            while ((content = br.readLine()) != null){
                line = content.split("\t");
                map = new HashMap<>(dataItems.size());
                for (int i = 0; i < line.length; i++) {
                    map.put(dataItems.get(i), line[i]);
                }
                if (map.size() > 0){
                    // 处理人员信息同行
                    if (sameInfoIdentifiers.equals(resourceId)){
                        population = new Population();
                        // 人员信息对象拷贝
                        BeanMap beanMap = BeanMap.create(population);
                        // TODO 此处需要注意唯一属性字段的处理
                        beanMap.putAll(map);
                        result.add(population);
                    } else if (trainTripIdentifiers.equals(resourceId)){
                    } else if (flightTripIdentifiers.equals(resourceId)){
                    } else if (busTripIdentifiers.equals(resourceId)){
                    } else if (accommodationIdentifiers.equals(resourceId)){
                    } else if (surfInternetIdentifiers.equals(resourceId)){
                    } else {
                        log.info("{}索引数据未匹配到对应所需索引", resourceId);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null){
                    br.close();
                }
                if (in != null){
                    in.close();
                }
            } catch (IOException ignored) { in = null; br = null; }
        }
        return result;
    }

    /**
     * 读取文件数据
     * @param filePath 文件绝对路径
     * @return String
     */
    private String readAllFileData(String filePath){
        String result = "";
        InputStreamReader in = null;
        BufferedReader br = null;
        try {
            in = new InputStreamReader(new FileInputStream(new File(filePath)),"UTF-8");
            br = new BufferedReader(in);
            StringBuffer content = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null){
                content = content.append(line);
            }
            result = content.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null){
                    br.close();
                }
                if (in != null){
                    in.close();
                }
            } catch (IOException ignored) { in = null; br = null; }
        }
        return result;
    }
}
