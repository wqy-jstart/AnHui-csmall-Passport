package cn.tedu.anhuicsmall.passport.controller;

import cn.tedu.anhuicsmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.anhuicsmall.passport.pojo.dto.AdminLoginDTO;
import cn.tedu.anhuicsmall.passport.pojo.dto.AdminUpdateDTO;
import cn.tedu.anhuicsmall.passport.pojo.entity.Admin;
import cn.tedu.anhuicsmall.passport.security.LoginPrincipal;
import cn.tedu.anhuicsmall.passport.service.IAdminService;
import cn.tedu.anhuicsmall.passport.web.JsonResult;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;

/**
 * 管理员的控制器类
 *
 * @Author java@Wqy
 * @Version 0.0.1
 */
@Api(tags = "01.管理员管理模块")
@Slf4j
@Validated
@RequestMapping("/admins")
@RestController
public class AdminController {
    // 注入AdminService实现类
    @Autowired
    IAdminService adminService;

    public AdminController() {
        log.info("创建控制器对象：AdminController");
    }

    /**
     * 处理管理员登录的请求
     * @param adminLoginDTO 接收传入的管理员数据
     * @return 返回JsonResult对象(包含状态码,和反馈信息)
     */
    // http://localhost:9081/admins/login
    @ApiOperation("管理员登录")
    @ApiOperationSupport(order = 50)
    @PostMapping("/login")
    public JsonResult<String> login(AdminLoginDTO adminLoginDTO){
        log.debug("开始处理[管理员登录]的请求,参数:{}",adminLoginDTO);
        String jwt = adminService.login(adminLoginDTO);
        return JsonResult.ok(jwt);
    }

    /**
     * 处理添加管理员的请求
     * @param adminAddNewDTO 接收传入的管理员数据
     * @return 返回JsonResult对象(包含状态码,和反馈信息)
     */
    // http://localhost:9081/admins/add-new
    @ApiOperation("添加管理员")
    @ApiOperationSupport(order = 100)
    @PreAuthorize("hasAuthority('/ams/admin/add-new')")
    @PostMapping("/add-new")
    public JsonResult<Void> addNew(@Valid AdminAddNewDTO adminAddNewDTO){
        adminService.addNew(adminAddNewDTO);
        return JsonResult.ok();
    }

    /**
     * 根据id修改管理员信息
     * @param adminUpdateDTO 修改的数据
     * @return 返回结果集
     */
    @ApiOperation("根据id修改管理员信息")
    @ApiOperationSupport(order = 200)
    @PreAuthorize("hasAuthority('/ams/admin/update')")
    @PostMapping("/update")
    public JsonResult<Void> update(AdminUpdateDTO adminUpdateDTO){
        log.debug("开始处理修改id为{}的管理员信息",adminUpdateDTO.getId());
        adminService.update(adminUpdateDTO);
        return JsonResult.ok();
    }

    /**
     * 根据用户名查询管理员信息
     * @param username 用户名
     * @return 返回信息
     */
    @ApiOperation("根据用户名查询管理员信息")
    @ApiOperationSupport(order = 500)
    @GetMapping("/selectByUsername")
    public JsonResult<Admin> selectByUsername(@RequestParam(value = "username")
                                              String username){
        log.debug("开始处理根据用户名查询管理员信息的请求,参数:{}",username);
        Admin admin = adminService.selectByUserName(username);
        return JsonResult.ok(admin);
    }

    /**
     * 根据id查询管理员信息
     * @param id 管理员id
     * @return 返回信息
     */
    @ApiOperation("根据用户名查询管理员信息")
    @ApiOperationSupport(order = 501)
    @ApiImplicitParam(name = "id",value = "管理员id",required = true,dataType = "long")
    @PreAuthorize("hasAuthority('/ams/admin/read')")
    @GetMapping("/{id:[0-9]+}/selectById")
    public JsonResult<Admin> selectById(@Range(min = 1,message = "查询失败,该管理员id无效!")   @PathVariable Long id){
        log.debug("开始处理查询id为{}的管理员信息",id);
        Admin admin = adminService.selectById(id);
        return JsonResult.ok(admin);
    }

    /**
     * 处理删除管理员的请求
     * @param id 要删除的管理员id
     * @return 返回JsonResult
     */
    @ApiOperation("根据id删除管理员")
    @ApiOperationSupport(order = 200)
    @ApiImplicitParam(name = "id",value = "管理员id",required = true,dataType = "long")
    @PreAuthorize("hasAuthority('/ams/admin/delete')")
    @PostMapping("/{id:[0-9]+}/deleteById")
    public JsonResult<Void> delete(@Range(min = 1,message = "删除管理员失败,尝试删除的管理员id无效") @PathVariable Long id){
        log.debug("开始处理[删除管理员]的请求,管理员id为{}",id);
        adminService.deleteById(id);
        return JsonResult.ok();
    }

    /**
     * 处理查询管理员列表的请求
     * @return JsonResult
     * ★添加@ApiIgnore注解告诉Api文档忽略当前的输入框
     * ★添加@AuthenticationPrincipal注解可使SpringSecurity去获取认证成功的当事人
     */
    @ApiOperation("管理员列表")
    @ApiOperationSupport(order = 210)//排序
    @PreAuthorize("hasAuthority('/ams/admin/read')")
    @GetMapping("")
    public JsonResult<List<Admin>> list(
            @ApiIgnore @AuthenticationPrincipal LoginPrincipal loginPrincipal){
        log.debug("开始处理[查询管理员列表]的请求,无参数");
        log.debug("当前登录的当事人:{}",loginPrincipal);
        List<Admin> list = adminService.list();
        return JsonResult.ok(list);
    }

    /**
     * 处理启用管理员的业务
     * @param id 要启用的管理员id
     * @return 返回JsonResult
     */
    @ApiOperation("启用管理员")
    @ApiOperationSupport(order = 310)
    @ApiImplicitParam(name = "id",value = "启用的管理员id",required = true,dataType = "long")
    @PreAuthorize("hasAuthority('/ams/admin/update')")
    @PostMapping("/{id:[0-9]+}/enable")
    public JsonResult<Void> setEnable(@Range(min = 1,message = "启用管理员失败,尝试启用的id无效!")
                                      @PathVariable Long id){
        log.debug("开始将id为{}的管理员设置为启用状态",id);
        adminService.setEnable(id);
        return JsonResult.ok();
    }

    /**
     * 处理禁用管理员的业务
     * @param id 要禁用的管理员id
     * @return 返回JsonResult
     */
    @ApiOperation("禁用管理员")
    @ApiOperationSupport(order = 311)
    @ApiImplicitParam(name = "id",value = "禁用的管理员id",required = true,dataType = "long")
    @PreAuthorize("hasAuthority('/ams/admin/update')")
    @PostMapping("/{id:[0-9]+}/disable")
    public JsonResult<Void> setDisable(@Range(min = 1,message = "禁用管理员失败,尝试启用的id无效!")
                                       @PathVariable Long id){
        log.debug("开始将id为{}的管理员设置为禁用状态",id);
        adminService.setDisable(id);
        return JsonResult.ok();
    }
}
