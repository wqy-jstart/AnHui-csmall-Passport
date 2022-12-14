package cn.tedu.anhuicsmall.passport.service.impl;

import cn.tedu.anhuicsmall.passport.ex.ServiceException;
import cn.tedu.anhuicsmall.passport.mapper.RoleMapper;
import cn.tedu.anhuicsmall.passport.pojo.entity.Role;
import cn.tedu.anhuicsmall.passport.service.IRoleService;
import cn.tedu.anhuicsmall.passport.web.ServiceCode;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * 角色的业务层接口实现类
 *
 * @Author java@Wqy
 * @Version 0.0.1
 */
@Slf4j
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private RoleMapper roleMapper;

    public RoleServiceImpl() {
        log.debug("创建业务对象：RoleServiceImpl");
    }

    /**
     * 实现Service接口中获取角色列表的功能(实现过程中移除Id为1的角色----系统管理员)
     * @return 返回List集合
     */
    @Override
    public List<Role> list() {
        log.debug("开始处理查询角色列表的业务!");
        List<Role> list = roleMapper.selectList(null);
        Iterator<Role> iterator = list.iterator();
        while (iterator.hasNext()){ // 判断是否含有下一个元素
            Role item = iterator.next(); // 获取该元素
            if (item.getId()==1){ // 如果该角色的id为1
                iterator.remove();// 移除该元素
                break;// 跳出循环
            }
        }
        return list;// 作出返回
    }

    /**
     * 执行根据id查询角色的业务
     *
     * @param id 角色id
     * @return 返回角色业务的Vo类
     */
    @Override
    public Role standardById(Long id) {
        Role role = roleMapper.selectById(id);
        // 判断查询到的结果是否为null
        if (role == null){
            // 抛出异常
            String message = "查询失败,尝试访问的数据不存在!";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND,message);
        }
        log.debug("开始返回结果!");
        return role;
    }
}
