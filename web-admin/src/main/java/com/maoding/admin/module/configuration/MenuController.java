package com.maoding.admin.module.configuration;

import com.maoding.admin.module.system.dto.SaveOperatePermissionDTO;
import com.maoding.admin.module.system.dto.SavePermissionRoleDTO;
import com.maoding.admin.module.system.dto.SaveViewDTO;
import com.maoding.admin.module.system.service.SystemService;
import com.maoding.core.base.BaseController;
import com.maoding.core.bean.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Wuwq on 2017/07/11.
 */
@Controller
@RequestMapping("/configuration")
public class MenuController extends BaseController {

    @Autowired
    private SystemService systemService;

    @RequestMapping("/menuSet")
    public void menuSet() {}

    @RequestMapping("/roleViews")
    public void roleViews() {}

    @RequestMapping("/rulesSet")
    public void rulesSet() {}

    @RequestMapping(value = "/getViewList",method = RequestMethod.GET)
    @ResponseBody
    public ApiResult getViewList() throws Exception{
        return ApiResult.success(systemService.getViewList());
    }

    @RequestMapping(value = "/getParentViewList",method = RequestMethod.GET)
    @ResponseBody
    public ApiResult getParentViewList() throws Exception{
        return ApiResult.success(systemService.getParentViewList());
    }

    @RequestMapping("/saveView")
    @ResponseBody
    public ApiResult saveView(@RequestBody SaveViewDTO dto) throws Exception{
        return systemService.saveView(dto);
    }

    @RequestMapping("/saveOperatePermission")
    @ResponseBody
    public ApiResult saveOperatePermission(@RequestBody SaveOperatePermissionDTO dto) throws Exception{
        return systemService.saveOperatePermission(dto);
    }

    @RequestMapping("/saveRolePermission")
    @ResponseBody
    public ApiResult saveRolePermission(@RequestBody SavePermissionRoleDTO dto) throws Exception{
        return systemService.savePermissionRole(dto);
    }

    /************角色权限配置****************/
    @RequestMapping(value = "/getRoleForProject",method = RequestMethod.GET)
    @ResponseBody
    public ApiResult getRoleForProject() throws Exception{
        return ApiResult.success(systemService.getDefaultRole());
    }

    @RequestMapping(value = "/getAllViewByRoleCode/{roleCode}",method = RequestMethod.GET)
    @ResponseBody
    public ApiResult getAllViewByRoleCode(@PathVariable("roleCode") String roleCode) throws Exception{
        return ApiResult.success(systemService.getAllViewByRoleCode(roleCode));
    }

}
