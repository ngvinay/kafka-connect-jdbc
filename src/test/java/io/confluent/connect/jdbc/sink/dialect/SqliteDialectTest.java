package io.confluent.connect.jdbc.sink.dialect;


import org.apache.kafka.connect.data.Schema;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import io.confluent.connect.jdbc.sink.SinkRecordField;

import static org.junit.Assert.assertEquals;

public class SqliteDialectTest {
  @Test
  public void validateAlterTable() {
    List<String> queries = new SQLiteDialect().getAlterTable("tableA", Arrays.asList(
        new SinkRecordField(Schema.Type.BOOLEAN, "col1", false),
        new SinkRecordField(Schema.Type.FLOAT32, "col2", false),
        new SinkRecordField(Schema.Type.STRING, "col3", false)
    ));

    assertEquals(3, queries.size());
    assertEquals("ALTER TABLE `tableA` ADD `col1` NUMERIC NULL;", queries.get(0));
    assertEquals("ALTER TABLE `tableA` ADD `col2` REAL NULL;", queries.get(1));
    assertEquals("ALTER TABLE `tableA` ADD `col3` TEXT NULL;", queries.get(2));
  }

  @Test
  public void produceTheRightSqlStatementWhithACompositePK() {
    String insert = new SQLiteDialect().getUpsertQuery("Book", Arrays.asList("ISBN", "year", "pages"), Arrays.asList("author", "title"));
    assertEquals("insert or ignore into `Book`(`ISBN`,`year`,`pages`,`author`,`title`) values(?,?,?,?,?)", insert);

  }
}
