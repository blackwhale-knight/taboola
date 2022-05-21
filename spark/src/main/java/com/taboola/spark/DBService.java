package com.taboola.spark;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.sql.Dataset;

import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

import static org.apache.spark.sql.functions.col;

public class DBService implements Serializable {
    public void writeToDB(Dataset<Row> dataset) {
        Dataset<Row> aggregation = dataset.groupBy(col("eventId") , functions.window(col("timestamp"), "5 second")).count().select("eventId", "window.start", "count")
                .withColumnRenamed("start","TIME_BUCKET")
                .withColumnRenamed("eventId", "EVENT_ID");
        aggregation.repartition(col("TIME_BUCKET"));
        JavaRDD<Row> rdd = aggregation.toJavaRDD();
        rdd.foreachPartitionAsync((VoidFunction<Iterator<Row>>) rowIterator -> {
            Connection conn = null;
            try {
                conn = Config.getConnection();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    String sql = "INSERT INTO EVENTS (EVENT_ID, TIME_BUCKET, COUNT) " + "VALUES (?, ?, ?)";
                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                        stmt.setLong(1, row.getLong(0));
                        stmt.setTimestamp(2, row.getTimestamp(1));
                        stmt.setLong(3, row.getLong(2));
                        stmt.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                assert conn != null;
                conn.close();
            }
        });
    }
}
