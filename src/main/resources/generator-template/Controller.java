 package ${basePath}.controller${subDir};

import ${basePath}.entity.Constant.AccessPath;
import ${basePath}.entity.domain${subDir}.${name};
import ${basePath}.entity.pojo${subDir}.${name}PoJo;
import ${basePath}.entity.request${subDir}.${name}Request;
import ${basePath}.entity.request${subDir}.${name}Response;
import ${basePath}.entity.response.CommonResponse;
import ${basePath}.service.common.CommonService;
import ${basePath}.service${subDir}.${name}Service;
import ${basePath}.util.TextUtil;
import ${basePath}.validator${subDir}.${name}Validator;
import io.swagger.annotations.*;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller("${alia}c")
@PreAuthorize("hasAnyAuthority('global:read','${alia}:read')")
@RequestMapping( "${subDirRequest}/${alia}")
@Api(tags = "${description}接口")
@SuppressWarnings("UnusedDeclaration")
public class ${name}Controller {
  @Resource private ${name}Service service;
  @Resource private CommonService commonService;
  @Resource private ${name}Validator validator;

  @InitBinder
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("${description}页面")
  public String index() {
    return "${alia}/index";
  }

  @GetMapping("/list")
  @ResponseBody
  @ApiOperation("获取${description}列表")
  public ResponseEntity list(
      @RequestParam(value = "page", required = false, defaultValue = "1") String pageStr,
      @RequestParam(value = "size", required = false, defaultValue = "10") String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return commonService.page(
        service.page(criteria, pageAndSize[0], pageAndSize[1]), ${name}Response::of);
  }

  @GetMapping
  @ApiOperation("添加${description}页面")
  public String create() {
    return "${alia}/create";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','${alia}:create')")
  @ApiOperation("添加${description}")
  public ResponseEntity create(@Valid ${name}Request ${alia}Request, BindingResult bindingResult) throws Exception {
    log.info(" ${name}Controller.create : [{}]", ${alia}Request);
    return commonService.create(
        service, ${name}.class, ${alia}Request, bindingResult);
  }

  @GetMapping("/{id}")
  @ApiOperation("更新${description}页面")
  public String update(@PathVariable("id") String idStr, Map<String, Object> map) throws Exception {
    return commonService.update(
        service,
        ${name}PoJo.class,
        idStr,
        "${alia}/update",
        map, "${alia}");
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','${alia}:write')")
  @ApiOperation("更新${description}")
  public ResponseEntity update(
      @PathVariable("id") String idStr, @Valid ${name}Request ${alia}Request, BindingResult bindingResult)
      throws Exception {
    log.info(" ${name}Controller.update : [{}]", ${alia}Request);
    return commonService.update(
        service, ${name}.class, ${alia}Request, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','${alia}:delete')")
  @ApiOperation("删除${description}")
  public ResponseEntity delete(@PathVariable("id") String idStr) throws Exception {
    log.info(" ${name}Controller.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }
}
