package org.ramer.admin.service.common;

import org.ramer.admin.entity.Constant;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 通用业务方法.
 *
 * @author ramer
 */
public interface BaseService<T, E> {
  T create(T t) throws Exception;

  /**
   * 条件state={@code Constant.STATE_ON}的总记录数
   *
   * @return long
   */
  long count();

  /** 获取当前对象的POJO对象. */
  E getPoJoById(long id);

  T getById(long id);
  /**
   * 条件查询.
   *
   * @param criteria 查询条件
   * @return {@link List <T>}
   */
  List<T> list(String criteria);
  /**
   * 分页条件查询.
   *
   * @param criteria 查询条件
   * @param page 当前页号 当page和size同时为-1时,将不会分页.
   * @param size 每页条目
   * @return {@link Page<T>}
   */
  Page<T> page(String criteria, int page, int size);

  T update(T t) throws Exception;

  void delete(long id) throws Exception;

  /**
   * 过滤某些属性可能包含的特殊字符.
   *
   * @param trans 页面传递过来的对象
   * @param filtered 过滤后的对象
   */
  default void textFilter(T trans, T filtered) {}
  /**
   * 获取分页对象.
   *
   * @param page 当前页,从1开始
   * @param size 每页大小
   */
  default PageRequest pageRequest(int page, int size) {
    if ((page < 1 || size < 0) && page != size) {
      return null;
    }
    return page == -1
        ? PageRequest.of(0, Integer.MAX_VALUE)
        : PageRequest.of(page - 1, size > 0 ? size : Constant.DEFAULT_PAGE_SIZE);
  }
}
