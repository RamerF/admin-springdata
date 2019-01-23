package org.ramer.admin.service.common;

import org.ramer.admin.entity.AbstractEntity;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

/**
 * 共用业务方法.
 *
 * @author ramer
 */
public interface CommonService {
  void writeMenuAndSiteInfo(HttpSession session, Map<String, Object> map);

  /**
   * @param invoke 服务层实现类.
   * @param entity 要保存的对象.
   * @param bindingResult 校验器校验结果.
   * @param <T> 服务层实现类.
   * @param <E> 要保存的对象.
   * @return {@link ResponseEntity}
   */
  <T extends BaseService<E, U>, E extends AbstractEntity, U> ResponseEntity create(
      T invoke, E entity, BindingResult bindingResult) throws Exception;

  /**
   * 校验更新url正确性.
   *
   * @param invoke 服务层实现类.
   * @param idStr 页面传递的id.
   * @param page 返回页面.
   * @param map 用于写入数据到request.
   * @return 返回页面
   */
  <T extends BaseService<E, U>, E extends AbstractEntity, U> String update(
      T invoke, String idStr, String page, Map<String, Object> map, String propName)
      throws Exception;

  /**
   * @param invoke 服务层实现类.
   * @param entity 要更新的对象.
   * @param idStr 页面传递的id.
   * @param bindingResult 校验器校验结果.
   * @param <T> 服务层实现类.
   * @param <E> 要更新的对象.
   * @return {@link ResponseEntity}
   */
  <T extends BaseService<E, U>, E extends AbstractEntity, U> ResponseEntity update(
      T invoke, E entity, String idStr, BindingResult bindingResult) throws Exception;

  /**
   * @param invoke 服务层实现类.
   * @param idStr 页面传递的id.
   * @param <T> 服务层实现类.
   * @param <E> 要删除的对象.
   * @return {@link ResponseEntity}
   */
  <T extends BaseService<E, U>, E extends AbstractEntity, U> ResponseEntity delete(
      T invoke, String idStr) throws Exception;

  /**
   * 拼接表单错误信息.
   *
   * @param bindingResult {@link BindingResult}
   * @return 错误提示
   */
  String collectBindingResult(BindingResult bindingResult);
}
