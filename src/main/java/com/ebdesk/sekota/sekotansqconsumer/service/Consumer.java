/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebdesk.sekota.sekotansqconsumer.service;

import com.ebdesk.sekota.sekotansqconsumer.util.PropertyConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 *
 * 
 */
@Service
public class Consumer {

  String kafka_group = PropertyConf.getProperty("kafka.payment.notif.group");

  private final Logger logger = LoggerFactory.getLogger(Consumer.class);

  @KafkaListener(topics = "test_notif", groupId = "payment-notif-topic")
  public void listen(@Payload String message) {
    logger.info("received message='{}'", message);
  }
}
