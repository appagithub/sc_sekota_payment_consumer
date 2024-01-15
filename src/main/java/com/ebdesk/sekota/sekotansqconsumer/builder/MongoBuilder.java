///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.ebdesk.sekota.sekotansqconsumer.builder;
//
//import com.ebdesk.sekota.sekotansqconsumer.util.PropertyConf;
//import com.ebdesk.sekota.sekotansqconsumer.util.StringUtil;
//import com.mongodb.Block;
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientOptions;
//import com.mongodb.MongoClientURI;
//import com.mongodb.client.AggregateIterable;
//import com.mongodb.client.DistinctIterable;
//import com.mongodb.client.FindIterable;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//import static com.mongodb.client.model.Accumulators.sum;
//import static com.mongodb.client.model.Aggregates.group;
//import com.mongodb.client.model.FindOneAndUpdateOptions;
//import com.mongodb.client.model.UpdateOptions;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Iterator;
//import java.util.LinkedHashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import org.apache.log4j.Logger;
//import org.bson.Document;
//import org.bson.conversions.Bson;
//
//public class MongoBuilder {
//  private final Logger logger = Logger.getLogger(MongoBuilder.class);
//  private final StringUtil stringUtil = new StringUtil();
//
//  public MongoClient mongo;
//  public MongoDatabase db;
//  private MongoCollection<Document> collection;
//  private long count = 0;
//
//  public MongoBuilder getBuilder(String dbName) {
//    MongoClientOptions.Builder options = MongoClientOptions.builder();
//    options.socketKeepAlive(true);
//    MongoClientURI uri = new MongoClientURI(PropertyConf.getProperty("mongo.url"), options);
//    mongo = new MongoClient(uri);
//    db = mongo.getDatabase(dbName);
//    return this;
//  }
//
//  public MongoBuilder getBuilder(String dbName, String mongoUrl) {
//    MongoClientURI uri = new MongoClientURI(mongoUrl);
//    mongo = new MongoClient(uri);
//    db = mongo.getDatabase(dbName);
//    return this;
//  }
//
//  public MongoBuilder from(String aTable) {
//    collection = db.getCollection(aTable);
////    collection.ensureIndex(new BasicDBObject("loc", "2d"), "geospacialIdx");
//    return this;
//  }
//
//  public long getCount(Bson doc) {
//    count = collection.count(doc);
//    return count;
//  }
//
//  public List getDistinct(String param, int type) {
//    List result = new LinkedList();
//
//    if (type == 1) {
//      DistinctIterable<String> documents = collection.distinct(param, String.class);
//      for (String document : documents) {
//        result.add(document);
//      }
//    }
//    result.remove("");
//    return result;
//  }
//
//  public long getCountAggregate(List<Bson> aggregate) {
//    aggregate.remove(aggregate.size() - 1);
//    aggregate.remove(aggregate.size() - 1);
//    List<Map> data = aggregate(aggregate);
//    return data.size();
//  }
//
//  public long getCountAggregateWithIndex(List aggregate, int val) {
//    for (int i = 0; i < val; i++) {
//      aggregate.remove(aggregate.size() - 1);
//    }
//
//    List<Map> data = aggregate(aggregate);
//    return data.size();
//  }
//
//  public long getCountAggregateNoLimit(List<Bson> aggregate) {
//    aggregate.remove(aggregate.size() - 1);
//    List<Map> data = aggregate(aggregate);
//    return data.size();
//  }
//
//  public List<Map> find(Bson query, Bson fields, Bson sort, int skip, int limit) {
//    List<Map> result = new ArrayList();
//    try {
//      FindIterable<Document> data = collection.find(query);
//      if (fields != null) {
//        data.projection(fields);
//      }
//      if (sort != null) {
//        data.sort(sort);
//      }
//      if (skip > 0) {
//        data.skip(skip);
//      }
//      if (limit > 0) {
//        data.limit(limit);
//      }
//
//      for (Document d : data) {
//        LinkedHashMap newData = new LinkedHashMap();
//        for (Map.Entry<String, Object> entry : d.entrySet()) {
////        if (!entry.getKey().equals("_id")) {
//          newData.put(entry.getKey(), entry.getValue());
////        }
//        }
//        result.add(newData);
//      }
//    } catch (Exception e) {
//      logger.error(e.toString());
//    }
//    return result;
//  }
//
//  public int insert(Document query, Document doc) {
//    int result;
//    try {
//      if (query == null || query.isEmpty()) {
//        collection.insertOne(doc);
//      } else {
//        UpdateOptions options = new UpdateOptions().upsert(true);
//        if (doc.get("$push") == null) {
//          Document document = new Document("$set", doc);
//          collection.updateOne(query, document, options);
//        } else {
//          collection.updateOne(query, doc, options);
//        }
//      }
//      result = 1;
//    } catch (Exception e) {
//      logger.error(stringUtil.getError(e));
//      result = 0;
//    } finally {
////      close();
//    }
//    return result;
//  }
//
//  public int insertWithoutPush(Document query, Document doc) {
//    int result;
//    try {
//      collection.updateOne(query, doc);
//      result = 1;
//    } catch (Exception e) {
//      logger.error(stringUtil.getError(e));
//      result = 0;
//    }
//    return result;
//  }
//
//  public List<Map> aggregate(List<Bson> aggregate) {
//    List<Map> result = new ArrayList();
//    AggregateIterable<Document> data = collection.aggregate(aggregate).allowDiskUse(true);
//    data.forEach((Block<Document>) doc -> {
//      Map rows = new LinkedHashMap();
//      doc.entrySet().forEach(entry -> rows.put(entry.getKey(), entry.getValue()));
//      result.add(rows);
//    });
////    for (Document doc : data) {
////      LinkedHashMap rows = new LinkedHashMap();
////      for (Map.Entry<String, Object> entry : doc.entrySet()) {
////        rows.put(entry.getKey(), entry.getValue());
////      }
////      result.add(rows);
////    }
//    return result;
//  }
//
//  public double aggregateCount(List<Bson> aggregate, int index) {
//    try {
//      for (int i = 0; i < index; i++) {
//        aggregate.remove(aggregate.size() - 1);
//      }
//      aggregate.add(group("_id", sum("count", 1)));
//      Iterator<Document> data = collection.aggregate(aggregate).iterator();
//
//      count = data.hasNext() ? (Integer) data.next().get("count") : 0;
//    } catch (Exception e) {
//      logger.error(stringUtil.getError(e));
//    } finally {
//      close();
//    }
//
//    return count;
//  }
//
//  public double aggregateCountV2(List<Bson> aggregate, int index) {
//    try {
//      for (int i = 0; i < index; i++) {
//        aggregate.remove(aggregate.size() - 1);
//      }
//      aggregate.add(new Document("$count", "count"));
//      Iterator<Document> data = collection.aggregate(aggregate).iterator();
//
//      count = data.hasNext() ? (Integer) data.next().get("count") : 0;
//    } catch (Exception e) {
//      logger.error(stringUtil.getError(e));
//    }finally{
//      close();
//    }
//
//    return count;
//  }
//
//  public void close() {
//    if (mongo != null) {
//      mongo.close();
//    }
//  }
//
//  public List setField(String fields) {
//    return Arrays.asList(fields.split(","));
//  }
//
////  public Document subtract(String param) {
////    Document result = new Document();
////    try {
////      List list = new LinkedList();
////
////      list.add("$" + param);
////      list.add(stringUtil.parseDateFormat("1970-01-01", "yyyy-MM-dd"));
////
////      result.append("timestamp", new Document("$subtract", list));
////    } catch (Exception e) {
////      stringUtil.getError(e);
////    }
////    return result;
////  }
//
//  public Document fieldsCreator(String[] fields) {
//    Document result = new Document();
//    for (String field : fields) {
//      if (field.contains(":")) {
//        List<String> f = Arrays.asList(field.split(":"));
//        if (f.get(1).contains("$")) {
//          result.append(f.get(0), f.get(1));
//        } else {
//          result.append(f.get(0), Integer.valueOf(f.get(1)));
//        }
//      } else {
//        result.append(field, 1);
//      }
//    }
//    return result;
//  }
//
//  public Document fieldsCreator(String fields) {
//    String[] arrFields = fields.split(",");
//    return fieldsCreator(arrFields);
//  }
//
//  public Document findAndModify(Bson query, Document doc) {
//    Document result = collection.findOneAndUpdate(query, doc);
//
//    return result;
//  }
//
//  public void updateMany(Bson query, Bson doc) {
//    collection.updateMany(query, doc);
//  }
//
//  public Document findAndModify(Bson query, Document doc, boolean upsert) {
//    Document result = new Document();
//    try {
//      FindOneAndUpdateOptions option = new FindOneAndUpdateOptions();
//      option.upsert(upsert);
//
//      result = collection.findOneAndUpdate(query, doc, option);
//    } catch (Exception e) {
//      stringUtil.getError(e);
//    } finally {
//      close();
//    }
//
//    return result;
//  }
//
//  public int insertMany(List<Document> doc) {
//    int result;
//    try {
//      if (!doc.isEmpty()) {
//        collection.insertMany(doc);
//      }
//
//      result = 1;
//    } catch (Exception e) {
//      logger.error(stringUtil.getError(e));
//      result = 0;
//    } finally {
//      close();
//    }
//    return result;
//  }
//}
