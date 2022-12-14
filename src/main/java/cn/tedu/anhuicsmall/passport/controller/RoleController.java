package cn.tedu.anhuicsmall.passport.controller;

import cn.tedu.anhuicsmall.passport.pojo.entity.Role;
import cn.tedu.anhuicsmall.passport.service.IRoleService;
import cn.tedu.anhuicsmall.passport.web.JsonResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "02.角色管理模块")
@Slf4j
@Validated
@RequestMapping("/roles")
@RestController
public class RoleController {

    @Autowired
    IRoleService iRoleService;

    public RoleController() {
        log.info("创建控制器对象：RoleController");
    }

    /**
     * 执行获取角色列表的请求
     * 用于前端的角色下拉框
     * @return 返回JsonResult
     */
    @ApiOperation("查询角色列表")
    @ApiOperationSupport(order = 100)
    @GetMapping("")
    public JsonResult<List<Role>> list(){
        log.debug("开始处理[查询角色列表]的请求");
        List<Role> list = iRoleService.list();
        log.debug("返回角色列表数据:{}",list);
        return JsonResult.ok(list);
    }

    /**
     * 根据id查询角色详情
     * @param id 角色id
     * @return 返回结果集
     */
    @ApiOperation("根据id查询角色详情")
    @ApiOperationSupport(order = 302)
    @ApiImplicitParam(name = "id",value = "角色id",required = true,dataType = "long")
    @GetMapping("/{id:[0-9]+}/select")
    public JsonResult<Role> standardById(@Range(min = 2,message = "查询失败,该id无效!")@PathVariable Long id){
        log.debug("开始处理[根据id{}查询品牌详情]的请求",id);
        Role roleStandardVO = iRoleService.standardById(id);
        return JsonResult.ok(roleStandardVO);
    }
}
