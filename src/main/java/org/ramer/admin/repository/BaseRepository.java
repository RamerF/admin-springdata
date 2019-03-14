package org.ramer.admin.repository;

import java.util.Date;
import java.util.List;
import org.ramer.admin.entity.AbstractEntity;
import org.ramer.admin.entity.Constant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/** */
@NoRepositoryBean
@SuppressWarnings({"unused"})
public interface BaseRepository<T extends AbstractEntity, ID>
    extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
  T findByIdAndState(ID id, int state);

  @Override
  default long count() {
    return count((root, query, builder) -> builder.equal(root.get("state"), Constant.STATE_ON));
  }

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
}
