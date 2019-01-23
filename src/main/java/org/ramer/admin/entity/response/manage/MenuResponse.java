package org.ramer.admin.entity.response.manage;

import java.util.List;
import lombok.*;

/** @author ramer */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class MenuResponse {
  private Long id;
  private String name;
  private String url;
  private Boolean leaf;
  private String icon;
  private Long pId;
  private List<MenuResponse> children;

  public MenuResponse(Long id, String name, String url, Boolean leaf, String icon, Long pId) {
    this.id = id;
    this.name = name;
    this.url = url;
    this.leaf = leaf;
    this.icon = icon;
    this.pId = pId;
  }
}
