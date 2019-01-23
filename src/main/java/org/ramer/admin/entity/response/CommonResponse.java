package org.ramer.admin.entity.response;

import org.springframework.http.ResponseEntity;

import lombok.*;

/**
 * 通用JSON响应.
 *
 * @author ramer
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse {
  /** 请求处理结果: 成功/失败 */
  private boolean result;
  /** 请求成功时,该值是具体返回内容,否则为空 */
  private Object data;
  /** 请求结果描述 */
  private String msg;

  /**
   * 默认执行成功构造器.
   *
   * @param data
   */
  public static ResponseEntity ok(Object data) {
    return ResponseEntity.ok(new CommonResponse(true, data, "执行成功"));
  }

  public static ResponseEntity ok(Object data, String msg) {
    return ResponseEntity.ok(new CommonResponse(true, data, msg));
  }

  /**
   * 默认执行失败构造器.
   *
   * @param msg 失败提示
   */
  public static ResponseEntity fail(String msg) {
    return ResponseEntity.ok(new CommonResponse(false, null, msg));
  }

  public static ResponseEntity fail(String msg, Object data) {
    return ResponseEntity.ok(new CommonResponse(false, data, msg));
  }

  public static ResponseEntity wrongFormat(String arg) {
    return ResponseEntity.ok(new CommonResponse(false, null, String.format("参数%s格式不正确", arg)));
  }

  public static ResponseEntity canNotBlank(String arg) {
    return ResponseEntity.ok(new CommonResponse(false, null, String.format("参数%s不能为空", arg)));
  }
}
