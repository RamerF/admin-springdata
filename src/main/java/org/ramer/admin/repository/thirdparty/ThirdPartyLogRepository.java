package org.ramer.admin.repository.thirdparty;

import org.ramer.admin.entity.domain.thirdparty.ThirdPartyLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ramer @Date 7/1/2018
 * @see
 */
@Repository
public interface ThirdPartyLogRepository extends JpaRepository<ThirdPartyLog, Long> {}
