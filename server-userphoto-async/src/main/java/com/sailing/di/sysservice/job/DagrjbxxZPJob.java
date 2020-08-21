package com.sailing.di.sysservice.job;

import com.sailing.di.sysservice.service.DagrjbxxZPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DagrjbxxZPJob implements CommandLineRunner {

    @Autowired
    private DagrjbxxZPService dagrjbxxZPServiceImpl;

    @Override
    public void run(String... args) throws Exception {
        dagrjbxxZPServiceImpl.transPhoto();
    }
}
