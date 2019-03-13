package ${basePath}.service${subDir}.impl;

import ${basePath}.entity.Constant;
import ${basePath}.entity.domain${subDir}.${name};
import ${basePath}.repository${subDir}.${name}Repository;
import ${basePath}.service.common.BaseService;
import ${basePath}.util.TextUtil;
import java.util.*;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** @author ramer */
@Slf4j
@Service
public class ${name}ServiceImpl implements ${name}Service {
  @Resource private ${name}Repository repository;

  @Transactional
  @Override
  public synchronized ${name} create(${name} obj) {
    textFilter(obj, obj);
    return repository.saveAndFlush(obj);
  }

  @Override
  public long count(String criteria) {
    return repository.count(getSpec(criteria));
  }

  @Override
  public ${name} getById(long id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public List<${name}> list(final String criteria) {
    return page(criteria, -1, -1).getContent();
  }

  @Override
  public Page<${name}> page(String criteria, int page, int size) {
    final PageRequest pageable = pageRequest(page, size);
    return pageable == null
        ? new PageImpl<>(Collections.emptyList())
        : repository.findAll(getSpec(criteria), pageable);
  }

  @Transactional
  @Override
  public synchronized ${name} update(${name} obj) {
    return Optional.ofNullable(getById(obj.getId()))
        .map(
            o -> {
              textFilter(obj, obj);
              return repository.saveAndFlush(obj);
            })
        .orElse(null);
  }

  @Transactional
  @Override
  public synchronized void delete(long id) {
    repository.deleteById(id);
  }

}
