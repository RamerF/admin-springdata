package org.ramer.admin.service.common;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;
import org.ramer.admin.entity.AbstractEntity;
import org.ramer.admin.entity.Constant;
import org.ramer.admin.entity.pojo.AbstractEntityPoJo;
import org.ramer.admin.entity.request.AbstractEntityRequest;
import org.ramer.admin.exception.CommonException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * 通用业务方法.
 *
 * @author ramer
 */
public interface BaseService<T extends AbstractEntity, E extends AbstractEntityPoJo> {
  /**
   * 保存/更新{@link U}对应的Domain对象.默认不会覆盖{@link U}中为null的字段,包含{@code
   * includeNullProperties}中的属性,即使值为null.
   *
   * @param <U> Request 实体
   * @param clazz Domain class
   * @param includeNullProperties 包含值为空的属性
   * @return T,如果保存/更新失败,返回null.
   * @throws CommonException the {@link SQLException}
   */
  default <U extends AbstractEntityRequest> T update(
      Class<T> clazz, U u, String... includeNullProperties) throws CommonException {
    final Method[] methods = u.getClass().getDeclaredMethods();
    T entity;
    Long id;
    try {
      final Method getIdMethod =
          Stream.of(methods)
              .filter(method -> method.getName().equals("getId"))
              .findFirst()
              .orElseThrow(() -> new CommonException("getId方法不存在"));
      id = (Long) getIdMethod.invoke(u);
      entity = getById(Objects.isNull(id) ? -1 : id);
      entity = Objects.isNull(entity) ? clazz.newInstance() : entity;
    } catch (Exception e) {
      return null;
    }
    BeanUtils.copyProperties(
        u,
        entity,
        Stream.of(org.ramer.admin.util.BeanUtils.getNullPropertyNames(u))
            .filter(prop -> !Arrays.asList(includeNullProperties).contains(prop))
            .toArray(String[]::new));
    return Objects.isNull(id) ? create(entity) : update(entity);
  }

  T create(T t) throws CommonException;

  /**
   * 条件state={@code Constant.STATE_ON}的总记录数
   *
   * @return long
   */
  long count();

  /** 获取当前对象的POJO对象. */
  default E getPoJoById(long id, Class<E> clazz) {
    final E instance;
    try {
      instance = clazz.newInstance();
    } catch (Exception e) {
      return null;
    }
    return Optional.ofNullable(getById(id)).map(e -> instance.of(e, clazz)).orElse(null);
  }

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

  T update(T t) throws CommonException;

  void delete(long id) throws CommonException;

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
