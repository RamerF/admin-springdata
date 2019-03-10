package org.ramer.admin.service.common;

import java.util.Map;
import java.util.function.Function;
import javax.servlet.http.HttpSession;
import org.ramer.admin.entity.AbstractEntity;
import org.ramer.admin.entity.pojo.AbstractEntityPoJo;
import org.ramer.admin.entity.request.AbstractEntityRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

/**
 * 通用业务方法.
 *
 * @author ramer
 */
public interface CommonService {
  void writeMenuAndSiteInfo(HttpSession session, Map<String, Object> map);

  /**
   * @param invoke 服务层实现类.
   * @param entity 要保存的对象.
   * @param bindingResult 校验器校验结果.
   * @param <S> 服务层实现类service {@link BaseService}.
   * @param <T> Domain对象 {@link AbstractEntity}.
   * @param <E> PoJo对象 {@link AbstractEntityPoJo}.
   * @return {@link ResponseEntity}
   */
  <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      ResponseEntity create(S invoke, T entity, BindingResult bindingResult) throws Exception;

  /**
   * 跳转到更新页面.校验更新url正确性,写入对象用于回显.
   *
   * @param invoke 服务层实现类.
   * @param clazz 当前写入对象的Class.
   * @param idStr 页面传递的id.
   * @param page 页面路径.
   * @param map 用于写入数据到request.
   * @param propName 写入的属性名
   * @return 返回更新页面
   */
  <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      String update(
          S invoke,
          Class<E> clazz,
          String idStr,
          String page,
          Map<String, Object> map,
          String propName)
          throws Exception;

  /**
   * 更新.
   *
   * @param invoke 服务层实现类.
   * @param entity 要更新的 {@link AbstractEntity} 对象.
   * @param idStr 页面传递的id.
   * @param bindingResult 校验器校验结果.
   * @param <T> 服务层实现类.
   * @param <E> 要更新的对象.
   * @return {@link ResponseEntity}
   */
  <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      ResponseEntity update(S invoke, T entity, String idStr, BindingResult bindingResult)
          throws Exception;

  /**
   * 创建或更新.
   *
   * @param invoke 服务层实现类.
   * @param entity 要更新的request {@link AbstractEntityRequest} 对象.
   * @param bindingResult 校验器校验结果.
   * @param <T> 服务层实现类.
   * @param <E> 要更新的对象.
   * @return {@link ResponseEntity}
   */
  <
          S extends BaseService<T, E>,
          T extends AbstractEntity,
          E extends AbstractEntityPoJo,
          R extends AbstractEntityRequest>
      ResponseEntity update(
          final S invoke,
          Class<T> clazz,
          final R entity,
          final BindingResult bindingResult,
          String... includeNullProperties)
          throws Exception;

  /**
   * 逻辑删除.
   *
   * @param invoke 服务层实现类.
   * @param idStr 页面传递的id.
   * @param <T> 服务层实现类.
   * @param <E> 要删除的对象.
   * @return {@link ResponseEntity}
   */
  <S extends BaseService<T, E>, T extends AbstractEntity, E extends AbstractEntityPoJo>
      ResponseEntity delete(S invoke, String idStr) throws Exception;

  /**
   * 转换分页对象.将Page domain对象转换为 Page 任意对象,并封装为页面响应对象.
   *
   * @param page Page domain对象
   * @param function 转换函数表达式
   * @param <T> domain对象
   * @return {@link ResponseEntity}
   */
  <T extends AbstractEntity> ResponseEntity page(Page<T> page, final Function<T, ?> function);

  /**
   * 拼接表单错误信息.
   *
   * @param bindingResult {@link BindingResult}
   * @return 错误提示
   */
  String collectBindingResult(BindingResult bindingResult);
}
