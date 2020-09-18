package com.sailing.dataextraction.service;

import com.sailing.dataextraction.dao.*;
import com.sailing.dataextraction.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className Neo4jService
 * @description Neo4j 数据抽取服务类
 * @date 2020/8/5 14:12
 **/
@Slf4j
@Service
public class Neo4jService {

    @Autowired
    private PopulationRepository populationRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private InternetRepository internetRepository;

    @Autowired
    private HotelRepository hotelRepository;

    /**
     * 户籍数据入库，并建立同户关系及其它的对应关系
     * @author Liufei Yang
     * @date 2020/9/14 11:17
     * @param data 户籍信息
     **/
    public void addPopulation(List<Map<String, String>> data){
        try {
            Population population;
            long timeNow = System.currentTimeMillis();
            for (Map<String, String> datum : data) {
                population = new Population();
                String uuid = datum.get("xxzjbh");
                if (StringUtils.isEmpty(uuid)){
                    uuid = UUID.randomUUID().toString().replaceAll("-","");
                }
                population.setUuid(uuid);
//                population.setName(datum.get("xm"));
//                population.setIdNumber(datum.get("gmsfhm"));
//                population.setHohRelations(datum.get("yhzgx"));
//                population.setHohName(datum.get("hz_xm"));
//                population.setHohIdNumber(datum.get("hz_gmsfhm"));
//                population.setFamilyNumber(datum.get("hh"));
//                population.setHomeAddress(datum.get("hjd_dzmc"));
                population.setSsTime(timeNow);
                populationRepository.save(population);
            }
            if (data.size() > 0){
                int result = populationRepository.createPeerRelation(timeNow);
                log.info("户籍任务--本次同户关系保存成功量：{}", result);
                populationRepository.createBusRelation(timeNow);
                log.info("户籍任务--本次人大巴关系保存成功量：{}", result);
                populationRepository.createFlightRelation(timeNow);
                log.info("户籍任务--本次人飞机关系保存成功量：{}", result);
                populationRepository.createHotelRelation(timeNow);
                log.info("户籍任务--本次人旅店关系保存成功量：{}", result);
                populationRepository.createInternetRelation(timeNow);
                log.info("户籍任务--本次人上网关系保存成功量：{}", result);
                populationRepository.createTrainRelation(timeNow);
                log.info("户籍任务--本次人火车关系保存成功量：{}", result);
            }
            log.info("户籍任务--本次保存成功量：{}", data.size());
        }catch (Exception e){
            log.error("户籍任务--入库失败");
            e.printStackTrace();
        }
    }

    /**
     * 火车数据入库，并建立同行关系及人火车对应关系
     * @author Liufei Yang
     * @date 2020/9/14 11:17
     * @param data 火车信息
     **/
    public void addTrain(List<Map<String, String>> data){
        try{
            Train train;
            long timeNow = System.currentTimeMillis();
            for (Map<String, String> datum : data) {
                train = new Train();
                String uuid = datum.get("xxzjbh");
                if (StringUtils.isEmpty(uuid)){
                    uuid = UUID.randomUUID().toString().replaceAll("-","");
                }
                train.setUuid(uuid);
                train.setTrainNumber(datum.get("cc"));
                train.setTitle(datum.get("cc"));
                train.setDepartureTime(datum.get("fcsj"));
                train.setTime(this.timeToMillis(datum.get("fcsj")));
                train.setDt(datum.get("sfzmc"));
                train.setRt(datum.get("ddzmc"));
                train.setPassengers(datum.get("xm"));
                train.setIdNumber(datum.get("gmsfhm"));
                train.setSsTime(timeNow);
                trainRepository.save(train);
            }
            if (data.size() > 0){
                int result = trainRepository.createPeerRelation(timeNow);
                log.info("火车任务--本次同行关系保存成功量：{}", result);
                result = trainRepository.createPopulationRelation(timeNow);
                log.info("火车任务--本次人关连关系保存成功量：{}", result);
            }
            log.info("火车任务--本次保存成功量：{}", data.size());
        }catch (Exception e){
            log.error("火车任务--入库失败");
            e.printStackTrace();
        }
    }

    /**
     * 航班数据入库，并建立同行关系及人航班对应关系
     * @author Liufei Yang
     * @date 2020/9/14 12:10
     * @param data 航班信息
     **/
    public void addFlight(List<Map<String, String>> data){
        try{
            Flight flight;
            long timeNow = System.currentTimeMillis();
            for (Map<String, String> datum : data) {
                flight = new Flight();
                String uuid = datum.get("xxzjbh");
                if (StringUtils.isEmpty(uuid)){
                    uuid = UUID.randomUUID().toString().replaceAll("-","");
                }
                flight.setUuid(uuid);
                flight.setFlightNumber(datum.get("cyhkgsdm") + datum.get("hbbh"));
                flight.setAirlines(datum.get("cyhkgsmc"));
                flight.setTitle(datum.get("cyhkgsdm") + datum.get("hbbh"));
                flight.setDepartureTime(datum.get("ddcf_sj"));
                flight.setTime(this.timeToMillis(datum.get("ddcf_sj")));
                flight.setDt(datum.get("qfhz_jcmc"));
                flight.setDtCode(datum.get("qfhz_jcdm"));
                flight.setRt(datum.get("ddhz_jcmc"));
                flight.setRtCode(datum.get("ddhz_jcdm"));
                flight.setPassengers(datum.get("xm"));
                flight.setIdNumber(datum.get("zjhm"));
                flight.setSsTime(timeNow);
                flightRepository.save(flight);
            }
            if (data.size() > 0){
                int result = flightRepository.createPeerRelation(timeNow);
                log.info("飞机任务--本次同行关系保存成功量：{}", result);
                result = flightRepository.createPopulationRelation(timeNow);
                log.info("飞机任务--本次人关连关系保存成功量：{}", result);
            }
            log.info("飞机任务--本次保存成功量：{}", data.size());
        }catch (Exception e){
            log.error("飞机任务--入库失败");
            e.printStackTrace();
        }
    }

    /**
     * 大巴数据入库，并建立同行关系及人大巴对应关系
     * @author Liufei Yang
     * @date 2020/9/14 12:11
     * @param data 大巴信息
     **/
    public void addBus(List<Map<String, String>> data){
        try{
            Bus bus;
            long timeNow = System.currentTimeMillis();
            for (Map<String, String> datum : data) {
                bus = new Bus();
                String uuid = datum.get("xxzjbh");
                if (StringUtils.isEmpty(uuid)){
                    uuid = UUID.randomUUID().toString().replaceAll("-","");
                }
                bus.setUuid(uuid);
                bus.setShiftNumber(datum.get("bcbh"));
                bus.setTitle(datum.get("bcbh"));
                bus.setDepartureTime(datum.get("kcsj"));
                bus.setTime(this.timeToMillis(datum.get("kcsj")));
                bus.setDt(datum.get("cczmc"));
                bus.setRt(datum.get("ddzmc"));
                bus.setPassengers(datum.get("xm"));
                bus.setIdNumber(datum.get("gmsfhm"));
                bus.setSsTime(timeNow);
                busRepository.save(bus);
            }
            if (data.size() > 0){
                int result = busRepository.createPeerRelation(timeNow);
                log.info("大巴任务--本次同行关系保存成功量：{}", result);
                result = busRepository.createPopulationRelation(timeNow);
                log.info("大巴任务--本次人关连关系保存成功量：{}", result);
            }
            log.info("大巴任务--本次保存成功量：{}", data.size());
        }catch (Exception e){
            log.error("大巴任务--入库失败");
            e.printStackTrace();
        }
    }

    /**
     * 上网数据入库，并建立同行关系及人上网对应关系
     * @author Liufei Yang
     * @date 2020/9/14 12:11
     * @param data 上网信息
     **/
    public void addInternet(List<Map<String, String>> data){
        try {
            Internet internet;
            long timeNow = System.currentTimeMillis();
            for (Map<String, String> datum : data) {
                internet = new Internet();
                String uuid = datum.get("xxzjbh");
                if (StringUtils.isEmpty(uuid)){
                    uuid = UUID.randomUUID().toString().replaceAll("-","");
                }
                internet.setUuid(uuid);
                internet.setPlaceNumber(datum.get("yycsdm"));
                internet.setTitle(datum.get("yycsmc"));
                internet.setDepartureTime(datum.get("swsj01"));
                String swsj = datum.get("swsj01");
                if (swsj.length() == 14){
                    swsj = swsj.substring(0, swsj.length()-3) + "000";
                }
                internet.setTime(this.timeToMillis(swsj));
                internet.setDt(datum.get("dzmc"));
                internet.setPassengers(datum.get("xm"));
                internet.setIdNumber(datum.get("gmsfhm"));
                internet.setSsTime(timeNow);
                internetRepository.save(internet);
            }
            if (data.size() > 0){
                int result = internetRepository.createPeerRelation(timeNow);
                log.info("上网任务--本次同行关系保存成功量：{}", result);
                result = internetRepository.createPopulationRelation(timeNow);
                log.info("上网任务--本次人关连关系保存成功量：{}", result);
            }
            log.info("上网任务--本次保存成功量：{}", data.size());
        }catch (Exception e){
            log.error("上网任务--入库失败");
            e.printStackTrace();
        }
    }

    /**
     * 旅店数据入库，并建立同行关系及人旅店对应关系
     * @author Liufei Yang
     * @date 2020/9/14 12:11
     * @param data 旅店信息
     **/
    public void addHotel(List<Map<String, String>> data){
        try {
            Hotel hotel;
            long timeNow = System.currentTimeMillis();
            for (Map<String, String> datum : data) {
                hotel = new Hotel();
                String uuid = datum.get("xxzjbh");
                if (StringUtils.isEmpty(uuid)){
                    uuid = UUID.randomUUID().toString().replaceAll("-","");
                }
                hotel.setUuid(uuid);
                hotel.setPlaceNumber(datum.get("lgbm"));
                hotel.setTitle(datum.get("lgmc"));
                hotel.setDepartureTime(datum.get("rzsj"));
                String rzsj = datum.get("rzsj");
                if (rzsj.length() == 14){
                    rzsj = rzsj.substring(0, rzsj.length()-4) + "0000";
                }
                hotel.setTime(this.timeToMillis(rzsj));
                hotel.setDt(datum.get("fjh"));
                hotel.setPassengers(datum.get("xm"));
                hotel.setIdNumber(datum.get("gmsfhm"));
                hotel.setSsTime(timeNow);
                hotelRepository.save(hotel);
            }
            if (data.size() > 0){
                int result = hotelRepository.createPeerRelation(timeNow);
                log.info("旅店任务--本次同行关系保存成功量：{}", result);
                result = hotelRepository.createPopulationRelation(timeNow);
                log.info("旅店任务--本次人关连关系保存成功量：{}", result);
            }
            log.info("旅店任务--本次保存成功量：{}", data.size());
        }catch (Exception e){
            log.error("旅店任务--入库失败");
            e.printStackTrace();
        }
    }

    /**
     * 两种方式解决损坏的业务数据对象
     * 1、初始化的时候page自加1
     * 2、记录每次分页时的实体数据ID,每次启动时批量删除损坏的实体数据
     * @param ids
     * @return 等待损坏的数据都被删除完了之后再进行，从损坏点point进行从新抽取业务数据
     */
    public boolean deleteById(List<Long> ids){
        for (int i =0;i < ids.size();i++){
            hotelRepository.deleteById(ids.get(i));
        }
        return true;
    }

    /**
     * 时间转时间戳
     * @param time 数据字符串
     * @return 时间戳
     */
    private long timeToMillis(String time){
        long resultTime;
        time = time.replaceAll("-","").replaceAll(" ", "").replaceAll(":","");
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            resultTime = timeFormat.parse(time).getTime();
        } catch (ParseException e) {
            log.error("时间转换异常");
            resultTime = System.currentTimeMillis();
        }
        return resultTime;
    }
}
