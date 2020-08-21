package com.sailing.di.sysservice.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.sailing.di.sysservice.config.ConnectionManager;
import com.sailing.di.sysservice.oracle.mapper.DagrjbxxZPMapper;
import com.sailing.di.sysservice.postgres.mapper.SysrwbMapper;
import com.sailing.di.sysservice.service.DagrjbxxZPService;
import com.sailing.di.sysservice.utils.DateUtil;
import com.sailing.di.sysservice.utils.ESUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

@Service
@Slf4j
public class DagrjbxxZPServiceImpl implements DagrjbxxZPService {

    @Autowired
    private SysrwbMapper sysrwbMapper;

    @Autowired
    private DagrjbxxZPMapper dagrjbxxZPMapper;

    @Value("${filePath}")
    private String filePath;


    @Value("${picUrl}")
    private String picUrl;

    /**
     * 上传图片
     */
    @Override
    public void transPhoto() throws Exception {
        ConnectionManager instance = ConnectionManager.newInstance();
        BulkRequestBuilder bulkRequest = ESUtil.client.prepareBulk();
        Map rwMap = (Map) sysrwbMapper.queryRwlb().get(0);
        String tjsj = (String) rwMap.get("tjsj");
        if(StringUtils.isEmpty(tjsj)){
            tjsj = dagrjbxxZPMapper.getRyzpzxsj();
            rwMap.put("tjsj",tjsj);
            sysrwbMapper.updateRwzhgxsj(rwMap);
        }
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        InputStream input = null;
        FileOutputStream out = null;
        while(true){
            try{
                log.info("统计时间：>{}",tjsj);
                int count = 0;
                String sql = "SELECT PID as \"zjhm\"," +
                        "          PHOTO_ID as \"photoId\"," +
                        "          IMAGE as \"image\"," +
                        "          TO_CHAR(ZHGXSJ,'yyyy-MM-dd HH24:MI:SS') as \"zhgxsj\"" +
                        "        FROM T_PHOTO\n" +
                        "        WHERE TO_CHAR(ZHGXSJ,'yyyy-MM-dd HH24:mi:ss') = '" + tjsj + "'";
                connection = instance.connection();
                statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                while(resultSet.next()){
                    Map streamMap = new HashMap();
                    Map esMap = new HashMap();
                    String zhgxsj = resultSet.getString("zhgxsj");
                    String photoId = resultSet.getString("photoId");
                    String year = zhgxsj.substring(0,4);
                    String month = zhgxsj.substring(5,7);
                    String day = zhgxsj.substring(8,10);
                    String hour = zhgxsj.substring(11,13);
                    String minute = zhgxsj.substring(14,16);
                    String datePath = year + "/" + month + "/" + day + "/" + hour + "/" + minute + "/";
                    String path = filePath + datePath;
                    oracle.sql.BLOB blob = (oracle.sql.BLOB) resultSet.getBlob("image");
                    if (blob == null) continue;
                    File file = new File(path);
                    if(!file.exists()){
                        file.mkdirs();
                    }
                    String zjhm = resultSet.getString("zjhm");
                    String fileName = zjhm +"_"+ photoId + ".jpg";
                    String pictureUrl = picUrl + datePath + fileName;
                    String filepath = path + fileName;
                    esMap.put("zpdz",pictureUrl);
                    esMap.put("zjhm",zjhm);
                    esMap.put("jlsj",zhgxsj);
                    bulkRequest.add(ESUtil.client.prepareIndex("da_grjbxx_zpxx", "docs").setSource(esMap).setId(photoId));
                    streamMap.put("stream",blob.getBinaryStream());
                    streamMap.put("size",(int)blob.length());
                    streamMap.put("filepath",filepath);
                    try{
                        input = blob.getBinaryStream();
                        log.info(filepath);
                        out = new FileOutputStream(filepath);
                        int len = (int) blob.length();
                        byte buffer[] = new byte[len];
                        while ((len = input.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                            out.flush();
                        }
                    }catch (Exception e){
                        log.info(e.getMessage());
                    }finally {
                        out.close();
                        input.close();
                    }
                    if(++count%1000 == 0) {
                        bulkRequest.execute().actionGet();
                        bulkRequest = ESUtil.client.prepareBulk();
                    }
                }
                if(bulkRequest.numberOfActions() > 0){
                    bulkRequest.execute().actionGet();
                    bulkRequest = ESUtil.client.prepareBulk();
                }
                int tc = tjsj.compareTo(DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
                if(tc < 0 ){
                    tjsj = DateUtil.getAfterSecondsDate(tjsj,1,"yyyy-MM-dd HH:mm:ss");
                    rwMap.put("tjsj",tjsj);
                    sysrwbMapper.updateRwzhgxsj(rwMap);
                }
            }catch(Exception e){
                e.printStackTrace();
            }finally {
                ConnectionManager.close(resultSet,statement,connection);
            }
        }
    }
}
