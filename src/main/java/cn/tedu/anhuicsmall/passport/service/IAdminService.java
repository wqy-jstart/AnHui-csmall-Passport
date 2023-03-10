package cn.tedu.anhuicsmall.passport.service;

import cn.tedu.anhuicsmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.anhuicsmall.passport.pojo.dto.AdminLoginDTO;
import cn.tedu.anhuicsmall.passport.pojo.dto.AdminUpdateDTO;
import cn.tedu.anhuicsmall.passport.pojo.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 管理员的业务层接口
 *
 * @Author java@Wqy
 * @Version 0.0.1
 */
@Transactional
public interface IAdminService extends IService<Admin> {

    /**
     * 管理员登录
     *
     * @param adminLoginDTO 封装了管理员的用户名和密码
     * @return 登录成功生成匹配的JWT
     */
    String login(AdminLoginDTO adminLoginDTO);

    /**
     * 添加管理员的方法
     * @param adminAddNewDTO 添加管理员的DTO类
     */
    void addNew(AdminAddNewDTO adminAddNewDTO);

    /**
     * 根据id修改管理员信息
     * @param adminUpdateDTO 修改的信息
     */
    void update(AdminUpdateDTO adminUpdateDTO);

    /**
     * 根据管理员用户名查询管理员信息
     * @param username 用户名
     * @return 返回信息
     */
    Admin selectByUserName(String username);

    /**
     * 根据用户id查询管理员信息
     * @param id 用户id
     * @return 返回用户信息
     */
    Admin selectById(Long id);

    /**
     * 根据id删除管理员
     * @param id 要删除的管理员id
     */
    void deleteById(Long id);

    /**
     * 查询管理员列表的方法
     * @return List
     */
    List<Admin> list();

    /**
     * 启用管理员
     * @param id 启用的管理员id
     */
    void setEnable(Long id);

    /**
     * 禁用管理员
     * @param id 禁用的管理员id
     */
    void setDisable(Long id);
}
