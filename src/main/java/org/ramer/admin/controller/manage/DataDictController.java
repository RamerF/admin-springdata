package org.ramer.admin.controller.manage;

import io.swagger.annotations.*;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.ramer.admin.entity.domain.manage.DataDict;
import org.ramer.admin.entity.response.CommonResponse;
import org.ramer.admin.exception.CommonException;
import org.ramer.admin.service.common.CommonService;
import org.ramer.admin.service.manage.system.DataDictService;
import org.ramer.admin.util.TextUtil;
import org.ramer.admin.validator.DataDictValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/manage/dataDict")
@PreAuthorize("hasAnyAuthority('global:read','dataDict:read')")
@Api(description = "管理端系统字典接口")
@SuppressWarnings("UnusedDeclaration")
public class DataDictController {
  @Resource private DataDictService service;
  @Resource private CommonService commonService;
  @Resource private DataDictValidator validator;

  @InitBinder
  void initBinder(WebDataBinder binder) {
    binder.addValidators(validator);
  }

  @GetMapping("/index")
  @ApiOperation("数据字典页面")
  String index(Map<String, Object> map) {
    map.put("types", service.listType());
    return "manage/data_dict/index";
  }

  @GetMapping("/listType")
  @ResponseBody
  @ApiOperation("获取系统数据字典类型列表")
  ResponseEntity listType() {
    return CommonResponse.ok(service.listType());
  }

  @GetMapping("/list")
  @ResponseBody
  @ApiOperation("根据数据字典类型获取数据字典列表")
  ResponseEntity list(
      @RequestParam(value = "typeCode", required = false) String code,
      @RequestParam(value = "page", required = false) String pageStr,
      @RequestParam(value = "size", required = false) String sizeStr,
      @ApiParam("查询条件") @RequestParam(value = "criteria", required = false) String criteria) {
    final int[] pageAndSize = TextUtil.validFixPageAndSize(pageStr, sizeStr);
    return CommonResponse.ok(
        service.pageByTypeCode(code, criteria, pageAndSize[0], pageAndSize[1]));
  }

  @GetMapping
  @ApiOperation("添加数据字典页面")
  String create(
      @RequestParam(value = "typeCode", required = false) String typeCode,
      Map<String, Object> map) {
    if (typeCode == null) {
      throw new CommonException(String.format("参数%s不能为空", "typeCode"));
    }
    map.put("typeCode", typeCode);
    return "manage/data_dict/create";
  }

  @PostMapping
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:create','dataDict:create')")
  @ApiOperation("添加数据字典")
  ResponseEntity create(
      @RequestParam(value = "typeCode", required = false) String typeCode,
      @Valid DataDict dataDict,
      BindingResult bindingResult)
      throws Exception {
    log.info(" DataDictController.create : [{},{}]", dataDict, typeCode);
    if (typeCode == null) {
      return CommonResponse.fail("字典类型code不能为空");
    }
    if (bindingResult.hasErrors()) {
      StringBuilder errorMsg = new StringBuilder("提交信息有误: ");
      bindingResult
          .getAllErrors()
          .forEach(error -> errorMsg.append("\n").append(error.getDefaultMessage()));
      return CommonResponse.fail(errorMsg.toString());
    }
    return CommonResponse.ok(service.create(dataDict, typeCode));
  }

  @GetMapping("/{id}")
  @ApiOperation("更新数据字典页面")
  String update(
      @PathVariable(value = "id") String idStr,
      @RequestParam(value = "typeCode", required = false) String typeCode,
      Map<String, Object> map)
      throws Exception {
    if (typeCode == null) {
      throw new CommonException(String.format("参数%s不能为空", "typeCode"));
    }
    return commonService.update(service, idStr, "manage/data_dict/update", map, "dataDict");
  }

  @PutMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:write','dataDict:write')")
  @ApiOperation("更新数据字典")
  ResponseEntity update(
      @PathVariable(value = "id") String idStr,
      @Valid DataDict dataDict,
      BindingResult bindingResult)
      throws Exception {
    log.info(" ConfigController.update : [{}]", dataDict);
    return commonService.update(service, dataDict, idStr, bindingResult);
  }

  @DeleteMapping("/{id}")
  @ResponseBody
  @PreAuthorize("hasAnyAuthority('global:delete','dataDict:delete')")
  @ApiOperation("删除数据字典")
  ResponseEntity delete(@PathVariable("id") String idStr) throws Exception {
    log.info(" ConfigController.delete : [{}]", idStr);
    return commonService.delete(service, idStr);
  }
}
