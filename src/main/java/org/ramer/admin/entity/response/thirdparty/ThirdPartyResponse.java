package org.ramer.admin.entity.response.thirdparty;

import lombok.*;
import org.ramer.admin.entity.Constant.ResultCode;

/** @author ramer */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartyResponse {
  private boolean success;
  private String message;
  private String errcode;
  private Object data;

  public ThirdPartyResponse(boolean success, ResultCode errorCode) {
    this.success = success;
    this.message = errorCode.toString();
    this.errcode = errorCode.name();
  }
}
