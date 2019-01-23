package org.ramer.admin.util;

import java.util.Date;
import java.util.Map;
import javassist.tools.reflect.CannotInvokeException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Native sql builder.
 *
 * @author ramer
 */
@Slf4j
public class SqlBuilder {
  /** build with native insert sql. */
  public static class SqlInsertBuilder {
    private StringBuilder rootBuilder = new StringBuilder("INSERT INTO ");
    private StringBuilder fieldBuilder = new StringBuilder("(");
    private StringBuilder valBuilder = new StringBuilder("VALUES(");

    public static SqlInsertBuilder of(String tableName) {
      final SqlInsertBuilder builder = new SqlInsertBuilder();
      builder.rootBuilder.append(tableName);
      return builder;
    }

    public SqlInsertBuilder values(Map<String, FieldValueType> fieldValueMap) {
      if (fieldBuilder.length() > 1 || valBuilder.length() > 7) {
        throw new CannotInvokeException("illegal invoke sort.");
      }
      fieldValueMap.forEach(
          (key, val) -> {
            if (fieldBuilder.length() > 1) {
              fieldBuilder.append(",").append(key);
              if (val.getType().isAssignableFrom(String.class)
                  || val.getType().isAssignableFrom(Date.class)) {
                valBuilder.append(",").append("'").append(val.getValue()).append("'");
              } else {
                valBuilder.append(",").append(val.getValue());
              }
            } else {
              fieldBuilder.append(key);
              if (val.getType().isAssignableFrom(String.class)
                  || val.getType().isAssignableFrom(Date.class)) {
                valBuilder.append("'").append(val.getValue()).append("'");
              } else {
                valBuilder.append(val.getValue());
              }
            }
          });
      return this;
    }

    public String build() {
      if (fieldBuilder.length() == 1 || valBuilder.length() == 7) {
        throw new CannotInvokeException("illegal invoke sort.");
      }
      return rootBuilder
          .append(fieldBuilder)
          .append(") ")
          .append(valBuilder)
          .append(")")
          .toString();
    }

    @Data
    public static class FieldValueType {
      private Object value;
      private Class<?> type;

      FieldValueType(Object value, Class<?> type) {
        this.value = value;
        this.type = type;
      }

      public static FieldValueType of(Object value, Class<?> type) {
        return new FieldValueType(value, type);
      }
    }
  }
}
