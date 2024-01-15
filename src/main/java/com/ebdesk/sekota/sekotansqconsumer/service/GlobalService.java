///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ebdesk.sekota.sekotansqconsumer.service;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.ebdesk.sekota.sekotansqconsumer.builder.MongoBuilder;
//import com.ebdesk.sekota.sekotansqconsumer.util.PropertyConf;
////import com.ebdesk.sekota.sekotansqconsumer.util.Constant;
//import com.ebdesk.sekota.sekotansqconsumer.util.StringUtil;
//import com.jcraft.jsch.Channel;
//import com.jcraft.jsch.ChannelExec;
//import com.jcraft.jsch.ChannelSftp;
//import com.jcraft.jsch.JSch;
//import com.jcraft.jsch.JSchException;
//import com.jcraft.jsch.Session;
//import com.jcraft.jsch.SftpException;
//import static com.mongodb.client.model.Aggregates.match;
//import com.mongodb.client.model.Filters;
//import static com.mongodb.client.model.Filters.eq;
//import static com.mongodb.client.model.Filters.in;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.LinkedHashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.ListIterator;
//import java.util.Map;
//import java.util.Random;
//import java.util.Vector;
//import java.util.logging.Level;
//import org.apache.log4j.Logger;
//import org.bson.Document;
//import org.bson.conversions.Bson;
//import org.bson.types.ObjectId;
//
///**
// *
// * 
// */
//public class GlobalService {
//
//  private final Logger LOGGER = Logger.getLogger(GlobalService.class);
//  private final StringUtil stringUtil = new StringUtil();
//  String MONGO_URL = PropertyConf.getProperty("mongo.url");
//  String DB_CONFIG = PropertyConf.getProperty("db.config");
//  String DB_NAME = PropertyConf.getProperty("db.name");
//  String APP_SECRET = PropertyConf.getProperty("app_secret");
//  String NGINX_URL = PropertyConf.getProperty("nginx.url");
//  String NGINX_SERVER_HOST = PropertyConf.getProperty("nginx.server.port");
//  int NGINX_SERVER_PORT = Integer.valueOf(PropertyConf.getProperty("nginx.server.port"));
//  String NGINX_SERVER_USERNAME = PropertyConf.getProperty("nginx.server.username");
//  String NGINX_SERVER_PASS = PropertyConf.getProperty("nginx.server.pass");
//  private Session session = null;
//  private ChannelSftp cFTP = null;
//
//  public Map getDataWithID(String param, int type, String id, String from, String location) {
//    MongoBuilder builder = null;
//    Map result = new LinkedHashMap();
//    try {
//      String DB = location.equals("0") ? DB_CONFIG : DB_NAME + location;
//      builder = new MongoBuilder().getBuilder(DB, MONGO_URL).from(from);
//      List<Bson> aggregate = new ArrayList();
//
//      switch (type) {
//        case 1:
//          aggregate.add(match(eq(param, new ObjectId(id))));
//          break;
//        case 2:
//          aggregate.add(match(eq(param, id)));
//          break;
//        case 3:
//          try {
//            aggregate.add(match(eq(param, Integer.parseInt(id))));
//          } catch (NumberFormatException e) {
//            aggregate.add(match(eq(param, Double.parseDouble(id))));
//          }
//          break;
//        default:
//          break;
//      }
//
//      List<Map> data = builder.aggregate(aggregate);
//      if (!data.isEmpty()) {
//        result = data.get(0);
//      }
//    } catch (NumberFormatException e) {
//      LOGGER.error(stringUtil.getError(e));
//    } finally {
//      if (builder != null) {
//        builder.close();
//      }
//    }
//    return result;
//  }
//
//  public String createJWT(Integer id, String email, String issuer, String subject, long expireTime, String location) {
//    String jwtToken = "";
//    try {
//      Algorithm algorithm = Algorithm.HMAC256(APP_SECRET);
//      Date date = new Date();
//      Date expTime = new Date((expireTime * 1000L) + date.getTime());
//      jwtToken = JWT.create()
//        .withIssuer(issuer)
//        .withSubject(subject)
//        .withClaim("u_id", id)
//        .withClaim("u_email", email)
//        .withClaim("city_code", location)
//        .withIssuedAt(date)
//        .withExpiresAt(expTime)
//        .sign(algorithm);
//    } catch (IllegalArgumentException | UnsupportedEncodingException e) {
//      LOGGER.error(stringUtil.getError(e));
//    }
//    return jwtToken;
//  }
//
//  public List<Document> getAllDataWithID(String param, String id, int type_id,
//    String from, boolean _id, String location) {
//    MongoBuilder builder = null;
//    List<Document> result = new LinkedList<>();
//    try {
//      String db = DB_NAME + location;
//      builder = new MongoBuilder().getBuilder(db, MONGO_URL).from(from);
//      List<Bson> aggregate = new ArrayList();
//
//      if (!id.isEmpty()) {
//        if (type_id == 1) {
//          aggregate.add(match(Filters.eq(param, new ObjectId(id))));
//        } else if (type_id == 2) {
//          aggregate.add(match(Filters.eq(param, Integer.valueOf(id))));
//        } else if (type_id == 3) {
//          aggregate.add(match(Filters.eq(param, id)));
//        }
//      }
//
//      List<Map> data = builder.aggregate(aggregate);
//      ListIterator<Map> liData = data.listIterator();
//      while (liData.hasNext()) {
//        Document doc = new Document();
//        Map<String, Object> mData = liData.next();
//        for (Map.Entry<String, Object> entry : mData.entrySet()) {
//          if (_id == false) {
//            if (!entry.getKey().equals("_id")) {
//              doc.append(entry.getKey(), entry.getValue());
//            }
//          } else {
//            doc.append(entry.getKey(), entry.getValue());
//          }
//        }
//
//        result.add(doc);
//      }
//    } catch (NumberFormatException e) {
//      LOGGER.error(stringUtil.getError(e));
//    } finally {
//      if (builder != null) {
//        builder.close();
//      }
//    }
//    return result;
//  }
//
//  public List<Document> getAllDataWithID(String param, String id, int type_id,
//    String from, boolean _id, String location, boolean is_cluster) {
//    MongoBuilder builder = null;
//    List<Document> result = new LinkedList<>();
//    try {
//      String db = DB_NAME + location;
//      builder = new MongoBuilder().getBuilder(db, MONGO_URL).from(from);
//      List<Bson> aggregate = new ArrayList();
//
//      if (!id.isEmpty()) {
//        if (type_id == 1) {
//          aggregate.add(match(Filters.eq(param, new ObjectId(id))));
//        } else if (type_id == 2) {
//          aggregate.add(match(Filters.eq(param, Integer.valueOf(id))));
//        } else if (type_id == 3) {
//          aggregate.add(match(Filters.eq(param, id)));
//        }
//      }
//
//      List<Map> data = builder.aggregate(aggregate);
//      ListIterator<Map> liData = data.listIterator();
//      while (liData.hasNext()) {
//        Document doc = new Document();
//        Map<String, Object> mData = liData.next();
//        for (Map.Entry<String, Object> entry : mData.entrySet()) {
//          if (_id == false) {
//            if (!entry.getKey().equals("_id")) {
//              doc.append(entry.getKey(), entry.getValue());
//            }
//          } else {
//            doc.append(entry.getKey(), entry.getValue());
//          }
//        }
//
//        result.add(doc);
//      }
//    } catch (NumberFormatException e) {
//      LOGGER.error(stringUtil.getError(e));
//    } finally {
//      if (builder != null) {
//        builder.close();
//      }
//    }
//    return result;
//  }
//
//  public void copyFileOnNginx(String src, String dst) throws SftpException {
//    try {
//      setChannelSftp();
//      String cp = "cp " + src + " " + dst;
//      LOGGER.info("copy : " + src + " to " + dst);
//      createFolder(dst);
//      ChannelExec channel = (ChannelExec) session.openChannel("exec");
//      channel.setCommand(cp);
//      channel.connect();
//      channel.setErrStream(System.err);
//      session.disconnect();
//    } catch (JSchException ex) {
//      stringUtil.getError(ex);
//    }
//  }
//
//  public void createFolder(String path) throws SftpException {
//    try {
//      String[] folders = path.split("/");
//      if (folders[0].isEmpty()) {
//        folders[0] = "/";
//      }
//      String fullPath = folders[0];
//      for (int i = 1; i < folders.length; i++) {
//        Vector ls = cFTP.ls(fullPath);
//        boolean isExist = false;
//        for (Object o : ls) {
//          if (o instanceof ChannelSftp.LsEntry) {
//            ChannelSftp.LsEntry e = (ChannelSftp.LsEntry) o;
//            if (e.getAttrs().isDir() && e.getFilename().equals(folders[i])) {
//              isExist = true;
//            }
//          }
//        }
//        if (!isExist && !folders[i].isEmpty()) {
//          cFTP.mkdir(fullPath + folders[i]);
//        }
//        fullPath = fullPath + folders[i] + "/";
//      }
//    } catch (SftpException e) {
//      LOGGER.error(stringUtil.getError(e));
//    }
//  }
//
//  public ChannelSftp setChannelSftp() throws JSchException {
//    connectSession();
//    Channel channel = session.openChannel("sftp");
//    channel.connect();
//    cFTP = (ChannelSftp) channel;
//    return cFTP;
//  }
//
//  public void connectSession() throws JSchException {
//    JSch jsch = new JSch();
//    session = jsch.getSession(NGINX_SERVER_USERNAME, NGINX_SERVER_HOST, NGINX_SERVER_PORT); //port is usually 22
//    session.setPassword(NGINX_SERVER_PASS);
//    java.util.Properties config = new java.util.Properties();
//    config.put("StrictHostKeyChecking", "no");
//    session.setConfig(config);
//    session.connect();
//
//  }
//
//}
