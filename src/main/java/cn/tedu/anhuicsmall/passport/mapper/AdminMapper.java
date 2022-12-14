package cn.tedu.anhuicsmall.passport.mapper;

import cn.tedu.anhuicsmall.passport.pojo.entity.Admin;
import cn.tedu.anhuicsmall.passport.pojo.vo.AdminLoginInfoVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * 管理员的持久层接口
 *
 * @Author java@Wqy
 * @Version 0.0.1
 */
@Repository
public interface AdminMapper extends BaseMapper<Admin> {

    /**
     * 根据用户名查询登录的信息
     * @param username 用户名
     * @return 返回登录的VO类信息(id,username,password,enable,permissions)
     */
    AdminLoginInfoVO getLoginInfoByUsername(String username);

}
