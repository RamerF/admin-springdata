package org.ramer.admin.entity.pojo.manage;

import lombok.*;
import org.ramer.admin.entity.domain.manage.DataDict;
import org.ramer.admin.entity.pojo.AbstractEntityPoJo;

/** 系统数据字典. */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DataDictPoJo extends AbstractEntityPoJo {
  private Long dataDictTypeId;
  private String name;
  private String code;
  private String remark;

  public static DataDictPoJo of(DataDict dataDict) {
    DataDictPoJo poJo = new DataDictPoJo();
    if (dataDict.getDataDictType() != null) {
      poJo.setDataDictTypeId(dataDict.getDataDictType().getId());
    }
    poJo.setName(dataDict.getName());
    poJo.setCode(dataDict.getCode());
    poJo.setRemark(dataDict.getRemark());

    AbstractEntityPoJo.of(poJo, dataDict);
    return poJo;
  }
}
