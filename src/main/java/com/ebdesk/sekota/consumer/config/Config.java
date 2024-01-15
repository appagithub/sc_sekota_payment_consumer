/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebdesk.sekota.consumer.config;

import com.ebdesk.sekota.sekotansqconsumer.util.PropertyConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 *
 * 
 */
@Configuration
@PropertySource("file:${global.conf}/global.properties")
public class Config {

  @Autowired
  private Environment env;

  @Autowired
  private void dataSource() {
    PropertyConf.setEnv(env);
  }
}
