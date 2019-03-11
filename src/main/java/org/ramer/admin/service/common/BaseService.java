package org.ramer.admin.service.common;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;
import javax.transaction.Transactional;
import org.ramer.admin.entity.AbstractEntity;
import org.ramer.admin.entity.Constant;
import org.ramer.admin.entity.pojo.AbstractEntityPoJo;
import org.ramer.admin.entity.request.AbstractEntityRequest;
import org.ramer.admin.exception.CommonException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

/**
 * 通用业务方法.
 *
 * @author ramer
 */
public interface BaseService<T extends AbstractEntity, E extends AbstractEntityPoJo> {

  T create(T t) throws CommonException;

  /**
   * 条件state={@code Constant.STATE_ON}的总记录数
   *
   * @return long
   */
  long count();

  /** 获取当前对象的POJO对象. */
  default E getPoJoById(final long id, Class<E> clazz) {
    final E instance;
    try {
      instance = clazz.newInstance();
    } catch (Exception e) {
      return null;
    }
    return Optional.ofNullable(getById(id)).map(e -> instance.of(e, clazz)).orElse(null);
  }

  T getById(final long id);

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
  Page<T> page(String criteria, final int page, final int size);

  /**
   * 保存/更新{@link U}对应的Domain对象.默认不会覆盖{@link U}中为null的字段,包含{@code
   * includeNullProperties}中的属性,即使值为null.
   *
   * @param <U> Request 实体.
   * @param clazz Domain class.
   * @param u 页面请求对象 {@link AbstractEntityRequest}.
   * @param includeNullProperties 覆写这些属性值,即使值为null.
   * @return T <br>
   *     null,如果保存/更新失败,或者更新时记录不存在.
   * @throws CommonException the {@link SQLException}
   * @see SQLException
   */
  @Transactional
  default <U extends AbstractEntityRequest> T update(
      Class<T> clazz, U u, String... includeNullProperties) throws CommonException {
    T domain;
    Long id;
    try {
      id =
          (Long)
              Objects.requireNonNull(
                      BeanUtils.findDeclaredMethod(u.getClass(), "getId"), "getId方法不存在")
                  .invoke(u);
      domain = getById(Objects.isNull(id) ? -1 : id);
      if (!Objects.isNull(id) && Objects.isNull(domain)) {
        return null;
      }
      domain = Objects.isNull(domain) ? clazz.newInstance() : domain;
    } catch (Exception e) {
      return null;
    }
    BeanUtils.copyProperties(
        u,
        domain,
        Stream.of(org.ramer.admin.util.BeanUtils.getNullPropertyNames(u))
            .filter(prop -> !Arrays.asList(includeNullProperties).contains(prop))
            .toArray(String[]::new));
    u.of(u, domain);
    return Objects.isNull(id) ? create(domain) : update(domain);
  }

  T update(T t) throws CommonException;

  void delete(final long id) throws CommonException;

  /**
   * 过滤某些属性可能包含的特殊字符.
   *
   * @param trans 页面传递过来的对象
   * @param filtered 过滤后的对象
   */
  default void textFilter(T trans, T filtered) {}

  /** 获取模糊查询条件,子类应该根据需要覆写该方法. */
  default Specification<T> getSpec(String criteria) {
    return StringUtils.isEmpty(criteria)
        ? (root, query, builder) -> builder.and(builder.equal(root.get("state"), Constant.STATE_ON))
        : (root, query, builder) ->
            builder.and(
                builder.equal(root.get("state"), Constant.STATE_ON),
                builder.or(builder.like(root.get("name"), "%" + criteria + "%")));
  }

  /**
   * 获取分页对象.
   *
   * @param page 当前页,从1开始
   * @param size 每页大小
   */
  default PageRequest pageRequest(final int page, final int size) {
    return pageRequest(page, size, Sort.unsorted());
  }
  /**
   * 获取分页对象,支持排序.
   *
   * @param page 当前页,从1开始
   * @param size 每页大小
   * @param sort 排序规则
   */
  default PageRequest pageRequest(final int page, final int size, Sort sort) {
    if ((page < 1 || size < 0) && page != size) {
      return null;
    }
    return page == -1
        ? PageRequest.of(0, Integer.MAX_VALUE, sort)
        : PageRequest.of(page - 1, size > 0 ? size : Constant.DEFAULT_PAGE_SIZE, sort);
  }
}
