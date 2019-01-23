package org.ramer.admin.repository.manage;

import org.ramer.admin.entity.AbstractEntity;
import org.ramer.admin.entity.Constant;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/** */
@NoRepositoryBean
@SuppressWarnings("UnusedDeclaration")
public interface BaseRepository<T extends AbstractEntity, ID>
    extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
  T findByIdAndState(ID id, int state);

  @Override
  default long count() {
    return count((root, query, builder) -> builder.equal(root.get("state"), Constant.STATE_ON));
  };

  @Override
  default void deleteById(ID id) {
    findById(id)
        .ifPresent(
            entity -> {
              entity.setState(Constant.STATE_OFF);
              saveAndFlush(entity);
            });
  }

  @Override
  default void delete(T entity) {
    entity.setState(Constant.STATE_OFF);
    saveAndFlush(entity);
  }

  default void deleteByIds(Iterable<ID> ids) {
    long time = System.currentTimeMillis();
    List<T> ts = findAllById(ids);
    for (T t : ts) {
      t.setState(Constant.STATE_OFF);
      t.setCreateTime(new Date());
      saveAndFlush(t);
    }
  }

  @Override
  default List<T> findAll() {
    return findAll(
        (root, query, builder) -> builder.and(builder.equal(root.get("state"), Constant.STATE_ON)));
  }

  @Override
  default Page<T> findAll(Pageable pageable) {
    return findAll(
        (root, query, builder) -> builder.and(builder.equal(root.get("state"), Constant.STATE_ON)),
        pageable);
  }

  /**
   * 根据条件分页取得对象
   *
   * @param example 过滤条件(非空)
   * @param page 第几页(从1开始)
   * @param matcher 模糊查询条件
   * @param size 每页数量(默认10条)
   * @param valid 是否只取未被删除的(取所有的传null值,取未删取的true，取已删除的取false)
   */
  default Page<T> page(
      T example, ExampleMatcher matcher, Integer page, Integer size, Boolean valid) {
    if (example == null) throw new RuntimeException("example can not be null");
    if (page == null || page <= 0) page = 1;
    if (size == null || size <= 0) size = Constant.DEFAULT_PAGE_SIZE;
    Pageable pageable = PageRequest.of(page - 1, size);
    if (valid == null) {

    } else if (valid) {
      example.setState(Constant.STATE_ON);
    } else {
      example.setState(Constant.STATE_OFF);
    }
    Example<T> ex = Example.of(example, matcher);
    return findAll(ex, pageable);
  }

  /**
   * 根据条件分页取得对象
   *
   * @param example 过滤条件
   * @param page 第几页(从1开始)
   * @param size 每页数量(默认10条)
   */
  default Page<T> page(T example, Integer page, Integer size) {
    if (page == null || page <= 0) page = 1;
    if (size == null || size <= 0) size = Constant.DEFAULT_PAGE_SIZE;
    Example<T> ex = Example.of(example);
    return findAll(ex, PageRequest.of(page - 1, size));
  }

  /**
   * 根据sql查询记录
   *
   * @param em EntityManager 请在调用类中注解注入
   * @param sql 带参数的sql语句，如：select * from table where type=?1 name like ?2
   * @param params 参数列表 顺序和sql参数一致 如：new Object[]{1,"张%"}
   */
  @SuppressWarnings("unchecked")
  default List<Object> findBySQL(EntityManager em, String sql, Object[] params) {
    Query q = em.createNativeQuery(sql);
    for (int i = 0; i < params.length; i++) {
      q.setParameter(i + 1, params[i]);
    }
    return q.getResultList();
  }

  /**
   * 根据jpql查询记录
   *
   * @param em EntityManager 请在调用类中注解注入
   * @param jpql 带参数的jpql语句，如：select * from table where type=?1 name like ?2
   * @param params 参数列表 顺序和sql参数一致 如：new Object[]{1,"张%"}
   */
  @SuppressWarnings("unchecked")
  default List<Object> findByJPQL(EntityManager em, String jpql, Object[] params) {
    Query q = em.createQuery(jpql);
    for (int i = 0; i < params.length; i++) {
      q.setParameter(i + 1, params[i]);
    }
    return q.getResultList();
  }

  /**
   * 执行sql语句
   *
   * @param em EntityManager 请在调用类中注解注入
   * @param sql 带参数的sql语句，如：update table set type = ?1,name = ?2
   * @param params 参数列表 顺序和sql参数一致 如：new Object[]{1,"张三"}
   */
  default void executeBySQL(EntityManager em, String sql, Object[] params) {
    Query q = em.createNativeQuery(sql);
    for (int i = 0; i < params.length; i++) {
      q.setParameter(i + 1, params[i]);
    }
    q.executeUpdate();
  }

  /**
   * 执行jpql语句
   *
   * @param em EntityManager 请在调用类中注解注入
   * @param jpql 带参数的jpql语句，如：update table set type = ?1,name = ?2
   * @param params 参数列表 顺序和sql参数一致 如：new Object[]{1,"张三"}
   */
  default void executeByJPQL(EntityManager em, String jpql, Object[] params) {
    Query q = em.createQuery(jpql);
    for (int i = 0; i < params.length; i++) {
      q.setParameter(i + 1, params[i]);
    }
    q.executeUpdate();
  }

  /**
   * 根据sql查询记录
   *
   * @param em EntityManager 请在调用类中注解注入
   * @param sql 带参数的sql语句，如：select * from table where type=?1 name like ?2
   * @param params 参数列表 顺序和sql参数一致 如：new Object[]{1,"张%"}
   */
  @SuppressWarnings({"unchecked", "deprecated"})
  default List<Map<String, Object>> findBySQLToMap(EntityManager em, String sql, Object[] params) {
    Query query = em.createNativeQuery(sql);
    for (int i = 0; i < params.length; i++) {
      query.setParameter(i + 1, params[i]);
    }
    query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    return new ArrayList<>(query.getResultList());
  }
}
