package org.ramer.admin.util;

import org.apache.poi.ss.usermodel.*;

/** @author ramer created on 10/31/18 */
public class ExcelUtil {
  @SuppressWarnings({"unchecked"})
  public static <T> T getCellValue(Cell cell) {
    if (cell == null) {
      return null;
    }
    T cellValue;
    switch (cell.getCellType()) {
      case Cell.CELL_TYPE_NUMERIC:
        if (DateUtil.isCellDateFormatted(cell)) {
          cellValue = (T) DateUtil.getJavaDate(cell.getNumericCellValue());
        } else {
          cellValue = (T) Double.valueOf(cell.getNumericCellValue());
        }
        break;
      case Cell.CELL_TYPE_STRING:
        cellValue = (T) cell.getStringCellValue();
        break;
      case Cell.CELL_TYPE_BOOLEAN:
        cellValue = (T) Boolean.valueOf(cell.getBooleanCellValue());
        break;
      case Cell.CELL_TYPE_FORMULA:
        cellValue = (T) cell.getCellFormula();
        break;
      case Cell.CELL_TYPE_BLANK:
        cellValue = (T) "";
        break;
      case Cell.CELL_TYPE_ERROR:
        cellValue = (T) "非法字符";
        break;
      default:
        cellValue = (T) "未知类型";
        break;
    }
    return cellValue;
  }
}
