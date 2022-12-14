package cn.tedu.anhuicsmall.passport.mapper;

import cn.tedu.anhuicsmall.passport.pojo.entity.AdminRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 管理员与角色的关联持久层接口
 *
 * @Author java@Wqy
 * @Version 0.0.1
 */
@Repository
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    /**
     * 批量插入管理员与角色的关联数据
     * @param adminRoleList 若干个管理员与角色的关联数据的集合
     * @return 受影响的行数
     */
    int insertBatch(AdminRole[] adminRoleList);

    /**
     * 根据管理员id删除关联表中的数据
     * @param id 管理员id
     * @return 返回受影响的行数
     */
    int deleteByAdminId(Long id);
}
