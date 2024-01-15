///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ebdesk.sekota.sekotansqconsumer.service;
//
//import com.github.brainlag.nsq.NSQProducer;
//import com.github.brainlag.nsq.exceptions.NSQException;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.concurrent.TimeoutException;
//import org.codehaus.jettison.json.JSONObject;
//
///**
// *
// * 
// */
//public class Producers {
//
//  private static String nsqHost = "192.168.150.49";
//  private static int nsqPort = 4150;
//  private static String nsqTopic = "lapak_order";
//  private static String nsqChannel = "ch_lapak_order";
//
//  public static void main(String[] args) {
//    Producers producers = new Producers();
//    producers.Produces();
//  }
//
//  public void Produces() {
//    try {
//      NSQProducer producer = new NSQProducer().addAddress(nsqHost, nsqPort).start();
//
//      Map message = new LinkedHashMap();
//      message.put("_id", "5d492f01b33f7c46ade07dc6");
//
//      producer.produce(nsqTopic, new JSONObject(message).toString().getBytes());
////      Executor executor = Executors.newSingleThreadExecutor();
//       
////      producer.getPool().clear();
//      producer.shutdown();
//    } catch (NSQException | TimeoutException e) {
//      e.printStackTrace();
//    }
//
//  }
//}
