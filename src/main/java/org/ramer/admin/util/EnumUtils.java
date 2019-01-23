package org.ramer.admin.util;

import java.util.*;

/** @author ramer */
public class EnumUtils {
  /**
   * Get ordinal list.
   *
   * @param enumClass
   * @param <E>
   * @return
   */
  public static <E extends Enum<E>> List<Integer> getIndexList(final Class<E> enumClass) {
    List<Integer> indexList = new ArrayList<>();
    for (E enumConstant : enumClass.getEnumConstants()) indexList.add(enumConstant.ordinal());
    return indexList;
  }

  /**
   * Get ordinal list.
   *
   * @param enumClass
   * @param <E>
   * @return
   */
  public static <E extends Enum<E>> Map<Integer, String> getIndexValueMap(
      final Class<E> enumClass) {
    Map<Integer, String> indexValueMap = new HashMap<>();
    for (E enumConstant : enumClass.getEnumConstants())
      indexValueMap.put(enumConstant.ordinal(), enumConstant.name());
    return indexValueMap;
  }
}
