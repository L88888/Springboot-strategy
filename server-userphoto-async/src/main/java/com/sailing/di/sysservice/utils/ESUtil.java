package com.sailing.di.sysservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class ESUtil {

    public static TransportClient client = null;


    public final static String HOST = "10.162.121.191";
    //public final static String HOST = "172.20.32.192";

    public final static int PORT = 9300;

    static Settings settings = Settings.builder().put("cluster.name","elasticsearch")
            .put("client.transport.sniff",true)
            .build();

    //static Settings settings = Settings.builder().put("cluster.name","es6.7.1-trunk")
    //        .put("client.transport.sniff",true)
    //        .build();
    static {
        getConnection();
    }
    @SuppressWarnings({ "resource", "unchecked" })
    public static void getConnection() {
        try {
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(HOST), PORT));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        log.info("Elasticsearch connect info:" + client.toString());
    }
    public void closeClient() {
        if(client != null) {
            client.close();
        }
    }
    public static void main(String[] args) {
        System.out.println(client);
    }

}
