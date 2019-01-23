package org.ramer.admin.exception;

import lombok.extern.slf4j.*;

/** @author ramer created on 11/15/18 */
@Slf4j
public class CommonException extends RuntimeException {
  public CommonException(final String message) {
    super(message);
    log.error(message);
  }

  public CommonException(final String message, final Throwable cause) {
    super(message, cause);
    log.error(message, cause);
  }

  public CommonException(final Throwable cause) {
    super(cause);
    log.error(cause.getMessage(), cause);
  }

  protected CommonException(
      final String message,
      final Throwable cause,
      final boolean enableSuppression,
      final boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
    log.error(cause.getMessage(), cause);
  }
}
