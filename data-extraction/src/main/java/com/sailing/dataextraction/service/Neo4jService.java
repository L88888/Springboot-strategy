package com.sailing.dataextraction.service;

import com.sailing.dataextraction.dao.*;
import com.sailing.dataextraction.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Liufei Yang
 * @version 1.0
 * @className Neo4jService
 * @description Neo4jService
 * @date 2020/8/5 14:12
 **/
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

    public Population addPopulation(Population population){
        return populationRepository.save(population);
    }

    public Train addTrain(Train train){
        return trainRepository.save(train);
    }

    public Flight addFlight(Flight flight){
        return flightRepository.save(flight);
    }

    public Bus addBus(Bus bus){
        return busRepository.save(bus);
    }

    public Internet addInternet(Internet internet){
        return internetRepository.save(internet);
    }

    public Hotel addHotel(Hotel hotel){
        return hotelRepository.save(hotel);
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
}
