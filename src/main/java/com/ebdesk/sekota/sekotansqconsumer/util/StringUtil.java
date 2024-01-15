/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebdesk.sekota.sekotansqconsumer.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
//import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
//import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * 
 */
public class StringUtil {

  private final Logger logger = LoggerFactory.getLogger(StringUtil.class);

  public List splitToList(String val, int type) {
    List result = new LinkedList();
    if (!val.isEmpty()) {
      if (val.contains(",")) {
        val = val.replace(" ", "");
        if (type == 1) {
          Pattern pattern = Pattern.compile(",");
          result = pattern.splitAsStream(val)
            .map(String::valueOf)
            .collect(Collectors.toList());
        } else {
          Pattern pattern = Pattern.compile(",");
          result = pattern.splitAsStream(val)
            .map(Integer::valueOf)
            .collect(Collectors.toList());
        }
      } else {
        result.add(val);
      }
    }

    return result;
  }

  public Map StringToMap(String val) {
    Map result = new LinkedHashMap();
    try {
      JSONObject json = new JSONObject(val);
      ObjectMapper mapper = new ObjectMapper();
      result = mapper.readValue(json.toString(), new TypeReference<Map>() {
      });
    } catch (IOException | JSONException e) {
      logger.error(e.toString());
    }

    return result;
  }

  public String getError(Exception e) {
    StringWriter errors = new StringWriter();
    e.printStackTrace(new PrintWriter(errors));
    return errors.toString();
  }

  public String now(String aFormat) {
    SimpleDateFormat sdf = new SimpleDateFormat(aFormat);
    Calendar cal = Calendar.getInstance();
    return sdf.format(cal.getTime());
  }

  public static <T> T stringToClass(String from, Class<T> toClass) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(from, toClass);
  }

  public List jsonToList(String data) {
    List result = new LinkedList();
    try {
      ObjectMapper mapper = new ObjectMapper();
      result = mapper.readValue(data, mapper.getTypeFactory().constructParametricType(List.class, Object.class));
    } catch (IOException ex) {
      logger.error(ex.toString());
    }

    return result;
  }
}
