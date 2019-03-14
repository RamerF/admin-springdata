package org.ramer.admin.repository.thirdparty;

import org.ramer.admin.entity.domain.thirdparty.ThirdPartyCertificate;
import org.ramer.admin.repository.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * @author ramer @Date 6/29/2018
 * @see
 */
@Repository
public interface ThirdPartyCertRepository
    extends BaseRepository<ThirdPartyCertificate, Long> {
  ThirdPartyCertificate findByCode(String code);
}
