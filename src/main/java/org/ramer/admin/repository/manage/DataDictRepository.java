package org.ramer.admin.repository.manage;

import java.util.List;
import org.ramer.admin.entity.domain.manage.DataDict;
import org.ramer.admin.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DataDictRepository extends BaseRepository<DataDict, Long> {
  DataDict findByDataDictTypeCodeAndCodeAndState(String dataDictTypeCode, String code, int state);

  @Query(
      "from org.ramer.admin.entity.domain.manage.DataDict dd where dd.dataDictType.code= :typeCode and dd.state= :state")
  List<DataDict> findByTypeCodeAndState(
      @Param("typeCode") String typeCode, @Param("state") int state);

  @Query(
      "from org.ramer.admin.entity.domain.manage.DataDict dd where dd.dataDictType.code= :typeCode and dd.state= :state")
  Page<DataDict> findByTypeCodeAndState(
      @Param("typeCode") String typeCode, @Param("state") int state, Pageable pageable);

  @Query(
      "from org.ramer.admin.entity.domain.manage.DataDict dd where dd.dataDictType.code= :typeCode and dd.state= :state and (dd.name like :criteria or dd.code like :criteria or dd.remark like :criteria )")
  Page<DataDict> findByTypeCodeAndState(
      @Param("typeCode") String typeCode,
      @Param("criteria") String criteria,
      @Param("state") int state,
      Pageable pageable);
}
