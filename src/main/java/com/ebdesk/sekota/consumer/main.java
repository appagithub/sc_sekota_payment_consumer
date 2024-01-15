/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebdesk.sekota.consumer;

import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * 
 */
@SpringBootApplication
public class main {
    
//    PropertyConfigurator.configure("log4j.properties");
//    nsqConsumer consumer = new nsqConsumer();
//    consumer.ConsumerV2();
    public static void main(String[] args) throws IOException {
    if (args.length == 1) {
      System.getProperties().put("server.port", args[0]);
    } else {
      System.getProperties().put("server.port", 8820);
    }
    if (System.getProperty("logging.config") == null) {
      System.getProperties().put("logging.config", "./conf/logback.xml");
    }
    SpringApplication.run(main.class, args);
  }
  
}
