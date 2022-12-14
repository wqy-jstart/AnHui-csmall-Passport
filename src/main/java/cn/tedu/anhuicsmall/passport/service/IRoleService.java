package cn.tedu.anhuicsmall.passport.service;

import cn.tedu.anhuicsmall.passport.pojo.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色的业务层接口
 *
 * @Author java@Wqy
 * @Version 0.0.1
 */
@Transactional
public interface IRoleService extends IService<Role> {

    /**
     * 该方法用来查询角色列表
     * @return List
     */
    List<Role> list();

    /**
     * 根据id查询角色的详情
     * @param id 角色id
     * @return 返回角色的详情VO类
     */
    Role standardById(Long id);
}
