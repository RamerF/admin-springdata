package ${basePath}.service${subDir}.impl;

import ${basePath}.entity.Constant;
import ${basePath}.entity.domain${subDir}.${name};
import ${basePath}.CommonException;
import ${basePath}.repository.BaseRepository;
import ${basePath}.repository${subDir}.${name}Repository;
import ${basePath}.service.common.BaseService;
import ${basePath}.service${subDir}.${name}Service;
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

  @SuppressWarnings({"unchecked"})
  @Override
  public <U extends BaseRepository<${name}, Long>> U getRepository() throws CommonException {
    return (U) repository;
  }
}
