package org.ramer.admin.service.common;

import java.util.Date;
import javax.servlet.http.HttpServletResponse;

/**
 * 通用报表.
 *
 * @author ramer
 */
public interface ReportService {
  /**
   * 根据时间段下载excel.
   *
   * @param startDate 开始时间
   * @param endDate 结束时间
   */
  void downloadByPeriod(Date startDate, Date endDate, HttpServletResponse response)
      throws Exception;
}
