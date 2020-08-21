package com.sailing.dataextraction.task;

import com.alibaba.fastjson.JSONObject;
import com.sailing.dataextraction.entity.*;
import com.sailing.dataextraction.service.EsToNeo4j;
import com.sailing.dataextraction.service.Neo4jService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @author Liufei Yang
 * @version 1.0
 * @className JobTask
 * @description 抽取Elasticsearch数据到Neo4j
 * @date 2020/8/4 11:32
 **/
@Slf4j
@Service
public class JobTask {

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

    /**
     * 上网信息
     */
    @Value("${surfInternetIdentifiers}")
    private String surfInternetIdentifiers;

    /**
     * CSV 文件
     */
    @Value("${csvFile}")
    private String csvFile;

    /**
     * 数据抽取的时间
     */
    @Value("${startTime}")
    private String startTime;

    @Autowired
    private EsToNeo4j esToNeo4j;

    @Autowired
    private Neo4jService neo4jService;

    private static boolean fileLock=false;

    private static final int PZGE_SIZE = 1000;


    /**
     * 户籍任务
     */
    @Async
    public void populationJob(){
        log.info("户籍任务--开始抽取户籍信息");
        try {
            TimeUnit.SECONDS.sleep(1);
            String condition = "fq_day >= '".concat(startTime).concat("'");
            int page = this.readCsv("Population");
            log.info("读取到页数 Population: {}", page);
            long total = esToNeo4j.queryDataCount("户籍任务", sameInfoIdentifiers, condition);
            if (total == 0){
                log.info("户籍任务--获取数据量为0，退出数据抽取");
                return;
            }

            int pageTotal = total > PZGE_SIZE ? new Double(Math.ceil(total/PZGE_SIZE)).intValue():1;
            while (page <= pageTotal){
                List<Map<String,String>> data = esToNeo4j.queryData("户籍任务", sameInfoIdentifiers, condition, page, PZGE_SIZE);
                if (data != null && data.size() > 0){
                    Population population;
                    for (Map<String, String> datum : data) {
                        population = new Population();
                        population.setUuid(UUID.randomUUID().toString().replaceAll("-",""));
                        population.setName(datum.get("xm"));
                        population.setIdNumber(datum.get("gmsfhm"));
                        population.setHohRelations(datum.get("yhzgx"));
                        population.setHohName(datum.get("hz_xm"));
                        population.setHohIdNumber(datum.get("hz_gmsfhm"));
                        population.setFamilyNumber(datum.get("hh"));
                        population.setHomeAddress(datum.get("hjd_dzmc"));
                        Population result = neo4jService.addPopulation(population);
                        log.debug("户籍任务--保存返回结构：{}", JSONObject.toJSONString(result));
                    }
                    log.info("户籍任务--保存成功量：{}", data.size());
                    page++;
                    this.writeCsv("Population", page);
                } else {
                    log.info("户籍任务--获取数据量为0，退出任务");
                    break;
                }
                TimeUnit.SECONDS.sleep(2);
            }
            log.info("户籍任务--抽取户籍信息已完成");
        }catch (Exception e){
            log.error("户籍任务--抽取户籍信息异常");
            e.printStackTrace();
        }
    }

    /**
     * 火车任务
     */
    @Async
    public void trainJob(){
        log.info("火车任务--开始抽取飞机信息");
        try {
            TimeUnit.SECONDS.sleep(2);
            String condition = "gpsj >= '".concat(startTime).concat("'");
            int page = this.readCsv("Train");
            log.info("读取到页数 Train: {}", page);
            long total = esToNeo4j.queryDataCount("火车任务", trainTripIdentifiers, condition);
            if (total == 0){
                log.info("火车任务--获取数据量为0，退出数据抽取");
                return;
            }

            int pageTotal = total > PZGE_SIZE ? new Double(Math.ceil(total/PZGE_SIZE)).intValue():1;
            while (page <= pageTotal){
                List<Map<String,String>> data = esToNeo4j.queryData("火车任务", trainTripIdentifiers, condition, page, PZGE_SIZE);
                if (data != null && data.size() > 0){
                    Train train;
                    for (Map<String, String> datum : data) {
                        train = new Train();
                        train.setUuid(UUID.randomUUID().toString().replaceAll("-",""));
                        train.setTrainNumber(datum.get("cc"));
                        train.setTitle(datum.get("cc"));
                        train.setDepartureTime(datum.get("fcsj"));
                        train.setTime(Long.parseLong(datum.get("fcsj")));
                        train.setDt(datum.get("sfzmc"));
                        train.setRt(datum.get("ddzmc"));
                        train.setPassengers(datum.get("xm"));
                        train.setIdNumber(datum.get("gmsfhm"));
                        Train result = neo4jService.addTrain(train);
                        log.debug("火车任务--保存返回结构：{}", JSONObject.toJSONString(result));
                    }
                    log.info("火车任务--保存成功量：{}", data.size());
                    page++;
                    this.writeCsv("Train", page);
                } else {
                    log.info("火车任务--获取数据量为0，退出任务");
                    break;
                }
                TimeUnit.SECONDS.sleep(2);
            }
            log.info("火车任务--抽取火车信息已完成");
        }catch (Exception e){
            log.error("火车任务--抽取火车信息异常");
            e.printStackTrace();
        }
    }

    /**
     * 飞机任务
     */
    @Async
    public void flightJob(){
        log.info("飞机任务--开始抽取飞机信息");
        try {
            TimeUnit.SECONDS.sleep(3);
            String condition = "ddcf_rq >= '".concat(startTime).concat("'");
            int page = this.readCsv("Flight");
            log.info("读取到页数 Flight: {}", page);
            long total = esToNeo4j.queryDataCount("飞机任务", flightTripIdentifiers, condition);
            if (total == 0){
                log.info("飞机任务--获取数据量为0，退出数据抽取");
                return;
            }

            int pageTotal = total > PZGE_SIZE ? new Double(Math.ceil(total/PZGE_SIZE)).intValue():1;
            while (page <= pageTotal){
                List<Map<String,String>> data = esToNeo4j.queryData("飞机任务", flightTripIdentifiers, condition, page, PZGE_SIZE);
                if (data != null && data.size() > 0){
                    Flight flight;
                    for (Map<String, String> datum : data) {
                        flight = new Flight();
                        flight.setUuid(UUID.randomUUID().toString().replaceAll("-",""));
                        flight.setFlightNumber(datum.get("cyhkgsdm") + datum.get("hbbh") + datum.get("hbhhz"));
                        flight.setAirlines(datum.get("cyhkgsmc"));
                        flight.setTitle(datum.get("cyhkgsdm") + datum.get("hbbh") + datum.get("hbhhz"));
                        flight.setDepartureTime(datum.get("ddcf_sj"));
                        flight.setTime(Long.parseLong(datum.get("ddcf_sj")));
                        flight.setDt(datum.get("qfhz_jcmc"));
                        flight.setDtCode(datum.get("qfhz_jcdm"));
                        flight.setRt(datum.get("ddhz_jcmc"));
                        flight.setRtCode(datum.get("ddhz_jcdm"));
                        flight.setPassengers(datum.get("xm"));
                        flight.setIdNumber(datum.get("zjhm"));
                        Flight result = neo4jService.addFlight(flight);
                        log.debug("飞机任务--保存返回结构：{}", JSONObject.toJSONString(result));
                    }
                    log.info("飞机任务--保存成功量：{}", data.size());
                    page++;
                    this.writeCsv("Flight", page);
                } else {
                    log.info("飞机任务--获取数据量为0，退出任务");
                    break;
                }
                TimeUnit.SECONDS.sleep(2);
            }
            log.info("飞机任务--抽取飞机信息已完成");
        }catch (Exception e){
            log.error("飞机任务--抽取飞机信息异常");
            e.printStackTrace();
        }
    }

    /**
     * 大巴任务
     */
    @Async
    public void busJob(){
        log.info("大巴任务--开始抽取大巴信息");
        try {
            TimeUnit.SECONDS.sleep(4);
            String condition = "fcsj >= '".concat(startTime).concat("'");
            int page = this.readCsv("Bus");
            log.info("读取到页数 Bus: {}", page);
            long total = esToNeo4j.queryDataCount("大巴任务", busTripIdentifiers, condition);
            if (total == 0){
                log.info("大巴任务--获取数据量为0，退出数据抽取");
                return;
            }

            int pageTotal = total > PZGE_SIZE ? new Double(Math.ceil(total/PZGE_SIZE)).intValue():1;
            while (page <= pageTotal){
                List<Map<String,String>> data = esToNeo4j.queryData("大巴任务", busTripIdentifiers, condition, page, PZGE_SIZE);
                if (data != null && data.size() > 0){
                    Bus bus;
                    for (Map<String, String> datum : data) {
                        bus = new Bus();
                        bus.setUuid(UUID.randomUUID().toString().replaceAll("-",""));
                        bus.setShiftNumber(datum.get("bcbh"));
                        bus.setTitle(datum.get("bcbh"));
                        bus.setDepartureTime(datum.get("fcsj"));
                        bus.setTime(Long.parseLong(datum.get("fcsj")));
                        bus.setDt(datum.get("sfzmc"));
                        bus.setRt(datum.get("ddzmc"));
                        bus.setPassengers(datum.get("xm"));
                        bus.setIdNumber(datum.get("gmsfhm"));
                        Bus result = neo4jService.addBus(bus);
                        log.debug("大巴任务--保存返回结构：{}", JSONObject.toJSONString(result));
                    }
                    log.info("大巴任务--保存成功量：{}", data.size());
                    page++;
                    this.writeCsv("Bus", page);
                } else {
                    log.info("大巴任务--获取数据量为0，退出任务");
                    break;
                }
                TimeUnit.SECONDS.sleep(2);
            }
            log.info("大巴任务--抽取大巴信息已完成");
        }catch (Exception e){
            log.error("大巴任务--抽取大巴信息异常");
            e.printStackTrace();
        }
    }

    /**
     * 上网任务
     */
    @Async
    public void internetJob(){
        log.info("上网任务--开始抽取上网信息");
        try {
            TimeUnit.SECONDS.sleep(5);
            String condition = "swsj01 >= '".concat(startTime).concat("'");
            int page = this.readCsv("Internet");
            log.info("读取到页数 Internet: {}", page);
            long total = esToNeo4j.queryDataCount("上网任务", surfInternetIdentifiers, condition);
            if (total == 0){
                log.info("上网任务--获取数据量为0，退出数据抽取");
                return;
            }

            int pageTotal = total > PZGE_SIZE ? new Double(Math.ceil(total/PZGE_SIZE)).intValue():1;
            while (page <= pageTotal){
                List<Map<String,String>> data = esToNeo4j.queryData("上网任务", surfInternetIdentifiers, condition, page, PZGE_SIZE);
                if (data != null && data.size() > 0){
                    Internet internet;
                    for (Map<String, String> datum : data) {
                        internet = new Internet();
                        internet.setUuid(UUID.randomUUID().toString().replaceAll("-",""));
                        internet.setPlaceNumber(datum.get("yycsdm"));
                        internet.setTitle(datum.get("yycsmc"));
                        internet.setDepartureTime(datum.get("swsj01"));
                        internet.setTime(Long.parseLong(datum.get("swsj01")));
                        internet.setDt(datum.get("dzmc"));
                        internet.setPassengers(datum.get("xm"));
                        internet.setIdNumber(datum.get("gmsfhm"));
                        Internet result = neo4jService.addInternet(internet);
                        log.debug("上网任务--保存返回结构：{}", JSONObject.toJSONString(result));
                    }
                    log.info("上网任务--保存成功量：{}", data.size());
                    page++;
                    this.writeCsv("Internet", page);
                } else {
                    log.info("上网任务--获取数据量为0，退出任务");
                    break;
                }
                TimeUnit.SECONDS.sleep(2);
            }
            log.info("上网任务--抽取上网信息已完成");
        }catch (Exception e){
            log.error("上网任务--抽取上网信息异常");
            e.printStackTrace();
        }
    }


    /**
     * 旅店任务
     */
    @Async
    public void hotelJob(){
        log.info("旅店任务--开始抽取旅店信息");
        try {
            TimeUnit.SECONDS.sleep(6);
            String condition = "rzsj >= '".concat(startTime).concat("'");
            int page = this.readCsv("Hotel");
            log.info("读取到页数 Hotel: {}", page);
            long total = esToNeo4j.queryDataCount("旅店任务", surfInternetIdentifiers, condition);
            if (total == 0){
                log.info("旅店任务--获取数据量为0，退出数据抽取");
                return;
            }

            int pageTotal = total > PZGE_SIZE ? new Double(Math.ceil(total/PZGE_SIZE)).intValue():1;
            while (page <= pageTotal){
                List<Map<String,String>> data = esToNeo4j.queryData("上网任务", surfInternetIdentifiers, condition, page, PZGE_SIZE);
                if (data != null && data.size() > 0){
                    Hotel hotel;
                    for (Map<String, String> datum : data) {
                        hotel = new Hotel();
                        hotel.setUuid(UUID.randomUUID().toString().replaceAll("-",""));
                        hotel.setPlaceNumber(datum.get("lg_lgbm"));
                        hotel.setTitle(datum.get("lgmc"));
                        hotel.setDepartureTime(datum.get("rzsj"));
                        hotel.setTime(Long.parseLong(datum.get("rzsj")));
                        hotel.setDt(datum.get("fjh"));
                        hotel.setPassengers(datum.get("lk_xm"));
                        hotel.setIdNumber(datum.get("lk_gmsfhm"));
                        Hotel result = neo4jService.addHotel(hotel);
                        log.debug("旅店任务--保存返回结构：{}", JSONObject.toJSONString(result));
                    }
                    log.info("旅店任务--保存成功量：{}", data.size());
                    page++;
                    // 还原点
                    this.writeCsv("Hotel", page);
                } else {
                    log.info("旅店任务--获取数据量为0，退出任务");
                    break;
                }
                TimeUnit.SECONDS.sleep(2);
            }
            log.info("旅店任务--抽取旅店信息已完成");
        }catch (Exception e){
            log.error("旅店任务--抽取旅店信息异常");
            e.printStackTrace();
        }
    }

    /**
     * 读取CSV获取记录数
     * @param index
     * @return
     */
    private int readCsv(String index){
        BufferedReader reader = null;
        int total = 1;
        try {
            while (fileLock){
                TimeUnit.SECONDS.sleep(5);
            }
            fileLock = true;
            if (this.isExist(csvFile.concat("/page-record.csv"))){
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile.concat("/page-record.csv")), "utf-8"));
                String line = null;
                while((line=reader.readLine())!=null){
                    String item[] = line.split(",");
                    if (index.equals(item[0])){
                        total = Integer.parseInt(item[1]);
                    }
                }
            }
        } catch (Exception e) {
            log.info("获取文件记录失败");
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fileLock = false;
        return total;
    }

    /**
     * 写入记录
     */
    private void writeCsv(String index, int total){
        BufferedWriter writer = null;
        try {
            while (fileLock){
                TimeUnit.SECONDS.sleep(5);
            }
            fileLock = true;
            if (!this.isExist(csvFile.concat("/page-record.csv"))){
                new File(csvFile).mkdirs();
                new File(csvFile.concat("/page-record.csv")).createNewFile();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile.concat("/page-record.csv")), "utf-8"));
            String line = null;
            boolean flag = true;
            List<String> oldData = new ArrayList<>();
            while((line=reader.readLine())!=null){
                String item[] = line.split(",");
                if (index.equals(item[0])){
                    oldData.add(item[0] + "," + total);
                    flag = false;
                }else {
                    oldData.add(item[0] + "," + item[1]);
                }
            }
            if (flag){
                oldData.add(index + "," + total);
            }
            if (oldData.size() > 0){
                writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile.concat("/page-record.csv")), "utf-8"));
                for (String data : oldData) {
                    writer.write(data);
                    writer.newLine();
                }
            }
        } catch (Exception e) {
            log.info("获取文件记录失败,异常后在写一次。保证页码能够正常写入");
            e.printStackTrace();
        }finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fileLock = false;
    }

    private boolean isExist(String filePath){
        File file = new File(filePath);
        if (file.exists()){
            return true;
        }
        return false;
    }

}
