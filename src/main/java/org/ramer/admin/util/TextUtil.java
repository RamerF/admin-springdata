package org.ramer.admin.util;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import org.ramer.admin.entity.Constant;
import org.springframework.util.Assert;

public class TextUtil {
  private static String[] FILTER_CHARACTER = new String[] {"<", ">"};

  /**
   * 校验并修正页面传过来的page和size属性.
   *
   * @param pageStr page字符串
   * @param sizeStr size字符串
   * @return 0: page 1: size
   */
  public static int[] validFixPageAndSize(String pageStr, String sizeStr) {
    int[] args = new int[2];
    int page;
    int size;
    page = validInt(pageStr, 0);
    if (page < 1) {
      page = -1;
    }
    size = validInt(sizeStr, Constant.DEFAULT_PAGE_SIZE);
    if (size < 1) {
      size = -1;
    }
    args[0] = page;
    args[1] = size;
    return args;
  }

  /**
   * 过滤给定字符串中的空格和脚本字符.
   *
   * @param str 待过滤字符串
   */
  public static String filter(String str) {
    if (str == null) {
      return null;
    }
    str = str.trim();
    for (String filterCharacter : FILTER_CHARACTER) {
      str = str.replace(filterCharacter, "");
    }
    return str;
  }

  /**
   * 将给定字符串转换为int.
   *
   * @param intStr int型字符串
   * @param defaultValue 默认值
   * @return 如果是正整数返回int,否则defaultValue
   */
  public static int validInt(String intStr, int defaultValue) {
    return isPositiveNumber(intStr) && Long.parseLong(intStr) <= Integer.MAX_VALUE
        ? Integer.parseInt(intStr)
        : defaultValue;
  }

  /**
   * 将给定字符串转换为long.
   *
   * @param longStr long型字符串
   * @param defaultValue 默认值
   * @return 如果是正整数返回long,否则返回defaultValue
   */
  public static long validLong(String longStr, long defaultValue) {
    return isPositiveNumber(longStr) ? Long.parseLong(longStr) : defaultValue;
  }

  /**
   * 将给定字符串数组转换为long[],该方法会过滤掉非正整数..
   *
   * @param longStrs long型字符串数组
   * @return 正整数 list
   */
  public static List<Long> validLongs(String[] longStrs) {
    Assert.notNull(longStrs, "param can not be null");
    List<Long> list = new ArrayList<>(longStrs.length);
    Stream.of(longStrs)
        .map((longStr) -> validLong(longStr, 0))
        .filter((validLong) -> validLong != 0)
        .forEach(list::add);
    return list.size() == longStrs.length ? list : new ArrayList<>();
  }

  /**
   * 将给定字符串转换为date.
   *
   * @param dateStr date字符串
   * @param pattern 时间格式化pattern
   * @return 返回date,如果字符串格式不匹配返回defaultValue.
   */
  public static Date validDate(String dateStr, String pattern, Date defaultValue) {
    try {
      return new SimpleDateFormat(pattern).parse(dateStr);
    } catch (Exception e) {
      return defaultValue;
    }
  }

  /**
   * 判断一个字符串是否是数字.
   *
   * @param str 数字字符串
   * @return true: 是,false: 否
   */
  public static boolean isPositiveNumber(String str) {
    if (str == null || str.length() == 0) {
      return false;
    }
    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      if (c <= '/' || c >= ':') {
        return false;
      }
    }
    return true;
  }
}
