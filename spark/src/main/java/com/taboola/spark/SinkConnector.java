package com.taboola.spark;

import org.apache.spark.api.java.function.VoidFunction2;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public class SinkConnector implements VoidFunction2<Dataset<Row>, Long> {
    @Override
    public void call(Dataset<Row> dataset, Long l) throws Exception {
        DBService dbService = new DBService();
        dbService.writeToDB(dataset);
    }
}
