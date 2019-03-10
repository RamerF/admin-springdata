package org.ramer.admin.service.manage.system.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.service.ThreadService;
import org.springframework.stereotype.Service;

/** @author ramer */
@Slf4j
@Service
public class ThreadServiceImpl implements ThreadService {
  private ExecutorService executorService;

  @Override
  public void newThread(Runnable runnable) {
    if (executorService == null || executorService.isShutdown()) {
      executorService = Executors.newFixedThreadPool(3);
    }
    try {
      executorService.execute(runnable);
    } catch (Exception e) {
      log.warn(e.getMessage(), e);
    }
  }
}
