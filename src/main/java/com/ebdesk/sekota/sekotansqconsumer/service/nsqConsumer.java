///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ebdesk.sekota.sekotansqconsumer.service;
//
//import com.ebdesk.sekota.sekotansqconsumer.builder.MongoBuilder;
//import com.ebdesk.sekota.sekotansqconsumer.util.PropertyConf;
////import com.ebdesk.sekota.sekotansqconsumer.util.Constant;
//import com.ebdesk.sekota.sekotansqconsumer.util.StringUtil;
//import com.github.brainlag.nsq.NSQConsumer;
//import com.github.brainlag.nsq.lookup.DefaultNSQLookup;
//import com.github.brainlag.nsq.lookup.NSQLookup;
//import com.jcraft.jsch.SftpException;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.ListIterator;
//import java.util.Map;
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
//import org.bson.Document;
//import org.bson.types.ObjectId;
//
///**
// *
// * 
// */
//public class nsqConsumer {
//
//  private static final String nsqHost = "192.168.150.49";
//  private static final int nsqPort = 4161;
//  private static final String nsqTopic = "lapak_order";
//  private static final String nsqChannel = "ch_lapak_order";
//  String NGINX_URL = PropertyConf.getProperty("nginx.url");
//  String MONGO_URL = PropertyConf.getProperty("mongo.url");
//  String DB_CONFIG = PropertyConf.getProperty("db.config");
//  String DB_NAME = PropertyConf.getProperty("db.name");
//  private final StringUtil stringUtil = new StringUtil();
//  private final GlobalService globalService = new GlobalService();
//  private final Logger logger = Logger.getLogger(NSQConsumer.class);
//
//  public static void main(String[] args) {
//    PropertyConfigurator.configure("log4j.properties");
//    nsqConsumer consumer = new nsqConsumer();
//    consumer.ConsumerV2();
////    consumer.updateLapakOrderImage(0, "", 0, "", "");
//  }
//
//  public void Consumer() {
//    NSQLookup lookup = new DefaultNSQLookup();
//    lookup.addLookupAddress(nsqHost, nsqPort);
//    NSQConsumer consumer = new NSQConsumer(lookup, nsqTopic, nsqChannel, (message) -> {
//      try {
//        String mess = new String(message.getMessage());
//        mess = java.net.URLDecoder.decode(mess, "UTF-8");
//        mess = mess.replace("message=", "");
//        logger.info("========== new message ==========");
//        logger.info("Message : " + mess);
//
//        Map dataNsq = stringUtil.stringToClass(mess, Map.class);
//        String _id = dataNsq.get("_id") == null ? "" : dataNsq.get("_id").toString();
//
//        if (!_id.isEmpty()) {
//          Map mData = globalService.getDataWithID("_id", 2, _id, "lapak_order_detail", "9999");
//          logger.info("mData : " + mData);
//          List lProducts = (ArrayList) mData.get("products");
//          changeStockOfName(lProducts, true);
//          ListIterator<Map> liProducts = lProducts.listIterator();
//          int productsIndex = 0;
//          while (liProducts.hasNext()) {
//            Map mProduct = liProducts.next();
//            String productId = mProduct.get("product_id").toString();
//            String productLocation = mProduct.get("product_location").toString();
//            Map mProductResult = globalService.getDataWithID("_id", 1, productId, "lapak", productLocation);
//            List lProductMedia = (ArrayList) mProductResult.get("product_media");
//            if (!lProductMedia.isEmpty()) {
//              Map mProdcutMedia = (Map) lProductMedia.get(0);
//              String image = copyFile(_id, mData, mProdcutMedia);
//              updateLapakOrderImage(productsIndex, _id, productId, image, "9999");
//            }
//            productsIndex++;
//          }
//        }
//      } catch (UnsupportedEncodingException e) {
//        logger.error(stringUtil.getError(e));
//      } catch (IOException ex) {
//        logger.error(stringUtil.getError(ex));
//      } finally {
//        logger.info("========== message finished ==========");
//        message.finished();
//      }
//    });
//
//    Executor executor = Executors.newSingleThreadExecutor();
//    consumer.setExecutor(executor);
//    consumer.start();
//  }
//
//  public void ConsumerV2() {
//    NSQLookup lookup = new DefaultNSQLookup();
//    lookup.addLookupAddress(nsqHost, nsqPort);
//    NSQConsumer consumer = new NSQConsumer(lookup, nsqTopic, nsqChannel, (message) -> {
//      try {
//        String mess = new String(message.getMessage());
//        mess = java.net.URLDecoder.decode(mess, "UTF-8");
//        mess = mess.replace("message=", "");
//        logger.info("========== new message ==========");
//        logger.info("Message : " + mess);
//
////        Map dataNsq = stringUtil.stringToClass(mess, Map.class);
//        List<Object> dataNsq = stringUtil.jsonToList(mess);
////        String _id = dataNsq.get("_id") == null ? "" : dataNsq.get("_id").toString();
//
////        if (!_id.isEmpty()) {
//        dataNsq.forEach(data -> {
//          Map mData = globalService.getDataWithID("_id", 2, data.toString(), "lapak_order_detail", "9999");
//          logger.info("mData : " + mData);
//          List lProducts = (ArrayList) mData.get("products");
//          changeStockOfName(lProducts, true);
//          ListIterator<Map> liProducts = lProducts.listIterator();
//          int productsIndex = 0;
//          while (liProducts.hasNext()) {
//            Map mProduct = liProducts.next();
//            String productId = mProduct.get("product_id").toString();
//            String productLocation = mProduct.get("product_location").toString();
//            Map mProductResult = globalService.getDataWithID("_id", 1, productId, "lapak", productLocation);
//            List lProductMedia = (ArrayList) mProductResult.get("product_media");
//            if (!lProductMedia.isEmpty()) {
//              Map mProdcutMedia = (Map) lProductMedia.get(0);
//              String image = copyFile(data.toString(), mData, mProdcutMedia);
//              updateLapakOrderImage(productsIndex, data.toString(), productId, image, "9999");
//            }
//            productsIndex++;
//          }
//        });
//
////        }
//      } catch (UnsupportedEncodingException e) {
//        logger.error(stringUtil.getError(e));
//      } catch (IOException ex) {
//        logger.error(stringUtil.getError(ex));
//      } finally {
//        logger.info("========== message finished ==========");
//        message.finished();
//      }
//    });
//
//    Executor executor = Executors.newSingleThreadExecutor();
//    consumer.setExecutor(executor);
//    consumer.start();
//  }
//
//  private String getFile(String img) {
//    List<String> lStringImage = Arrays.asList(img.split("/"));
//
//    return lStringImage.get(lStringImage.size() - 1);
//  }
//
//  private String copyFile(String _id, Map mData, Map mProductMedia) {
//    String result = "";
//    try {
//      long customer_id = (long) mData.get("customer_id");
//      long mod = customer_id % 256;
//      String img = mProductMedia.get("name").toString();
//      String path = "/img/sekota/lapak_order/" + mod + "/" + customer_id + "/" + _id;
//      String src = NGINX_URL + img;
//      String dst = NGINX_URL + path;
//      globalService.copyFileOnNginx(src, dst);
//      result = path + "/" + getFile(img);
//    } catch (SftpException e) {
//      logger.error(stringUtil.getError(e));
//    }
//    return result;
//  }
//
//  private void updateLapakOrderImage(int productsIndex, String _id, String product_id, String image, String location) {
//    MongoBuilder builder = null;
//    try {
//      builder = new MongoBuilder().getBuilder(DB_NAME + location, MONGO_URL).from("lapak_order_detail");
//      Document doc = new Document();
//      Document query = new Document();
//
//      query.append("_id", _id);
//      query.append("products.product_id", product_id);
//
//      doc.append("products." + productsIndex + ".product_thumb", image);
//
//      logger.info("update lapak_order : " + _id + " | index : " + productsIndex + "| " + "product_id : " + product_id + " | image : " + image);
//      builder.insert(query, doc);
//    } catch (NumberFormatException e) {
//      logger.error(stringUtil.getError(e));
//    } finally {
//      if (builder != null) {
//        builder.close();
//      }
//    }
//  }
//
//  private void changeStockOfName(List lProduct, boolean is_decrease) {
//    MongoBuilder builder = null;
//    try {
//      ListIterator<Map> liProduct = lProduct.listIterator();
//      while (liProduct.hasNext()) {
//        Map mProduct = liProduct.next();
//        String product_id = mProduct.get("product_id").toString();
//        String product_location = mProduct.get("product_location").toString();
//        int product_quantity = is_decrease ? -(int) mProduct.get("product_quantity") : (int) mProduct.get("product_quantity");
//        String id_detail = mProduct.get("product_id_detail") == null ? "" : mProduct.get("product_id_detail").toString();
//
//        builder = new MongoBuilder().getBuilder(DB_NAME + product_location).from("lapak");
//        Document doc = new Document();
//        Document query = new Document();
//
//        query.append("_id", new ObjectId(product_id));
//        if (!id_detail.isEmpty()) {
//          query.append("product_quantity_detail", new Document("$elemMatch", new Document("id_detail", id_detail)));
//          query.append("product_quantity_detail", new Document("$elemMatch", new Document("quantity_unlimited", false)));
//
//          doc.append("$inc", new Document("product_quantity_detail.$.quantity", product_quantity).
//            append("product_quantity", product_quantity));
//        } else {
//          query.append("product_quantity_unlimited", false);
//          doc.append("$inc", new Document("product_quantity", is_decrease ? -product_quantity : product_quantity));
//        }
//        logger.info("Change StockOfName : " + product_id + " | is_decrease : " + is_decrease + " | " + (is_decrease ? -product_quantity : product_quantity));
//        builder.findAndModify(query, doc);
//      }
//    } catch (Exception e) {
//      logger.error(stringUtil.getError(e));
//    } finally {
//      if (builder != null) {
//        builder.close();
//      }
//    }
//  }
//
//}
