package org.ramer.admin.service.manage.system;

import org.ramer.admin.entity.domain.manage.DataDict;
import org.ramer.admin.entity.domain.manage.DataDictType;
import org.ramer.admin.entity.pojo.manage.DataDictPoJo;
import org.ramer.admin.service.common.BaseService;
import java.util.List;
import org.springframework.data.domain.Page;

/** @author ramer */
public interface DataDictService extends BaseService<DataDict, DataDictPoJo> {
  List<DataDictType> listType();

  DataDict getByTypeCodeAndCode(String typeCode, String code);

  Page<DataDict> pageByTypeCode(String typeCode, String criteria, final int page, final int size);

  DataDict create(DataDict dataDict, String typeCode);
}
