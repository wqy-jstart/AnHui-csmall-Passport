package cn.tedu.anhuicsmall.passport.service.impl;

import cn.tedu.anhuicsmall.passport.ex.ServiceException;
import cn.tedu.anhuicsmall.passport.mapper.AdminMapper;
import cn.tedu.anhuicsmall.passport.mapper.AdminRoleMapper;
import cn.tedu.anhuicsmall.passport.pojo.dto.AdminAddNewDTO;
import cn.tedu.anhuicsmall.passport.pojo.dto.AdminLoginDTO;
import cn.tedu.anhuicsmall.passport.pojo.dto.AdminUpdateDTO;
import cn.tedu.anhuicsmall.passport.pojo.entity.Admin;
import cn.tedu.anhuicsmall.passport.pojo.entity.AdminRole;
import cn.tedu.anhuicsmall.passport.security.AdminDetails;
import cn.tedu.anhuicsmall.passport.service.IAdminService;
import cn.tedu.anhuicsmall.passport.util.BCryptEncode;
import cn.tedu.anhuicsmall.passport.web.ServiceCode;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 管理员的业务层接口实现类
 *
 * @Author java@Wqy
 * @Version 0.0.1
 */
@Slf4j
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    // 读取配置文件application-dev.yml中的自定义配置
    @Value("${anhuicsmall.jwt.secret-key}")
    private String secretKey;
    @Value("${anhuicsmall.jwt.duration-in-minute}")
    private long durationInMinute;

    // 注入管理员的持久层接口
    @Autowired
    private AdminMapper adminMapper;

    // 注入关联表的mapper,向该表中批量插入角色数据(一个管理员对应多个角色)
    @Autowired
    private AdminRoleMapper adminRoleMapper;

    // 注入认证信息接口对象
    @Autowired
    private AuthenticationManager authenticationManager;

    public AdminServiceImpl() {
        log.info("创建业务对象：AdminServiceImpl");
    }

    /**
     * 管理员登录的业务
     *
     * @param adminLoginDTO 封装了管理员的用户名和密码
     */
    @Override
    public String login(AdminLoginDTO adminLoginDTO) {
        log.debug("开始处理[管理员登录]的业务,参数:{}", adminLoginDTO);

        // 1.处理认证
        Authentication authentication
                = new UsernamePasswordAuthenticationToken(
                adminLoginDTO.getUsername(), adminLoginDTO.getPassword()
        );
        // 开始认证
        Authentication authenticateResult
                = authenticationManager.authenticate(authentication);

        log.debug("认证通过,认证管理器返回:{}", authenticateResult);

        // 2.认证成功后获取当事人对象
        Object principal = authenticateResult.getPrincipal();
        log.debug("认证结果中的当事人类型:{}", principal.getClass().getName());
        AdminDetails adminDetails = (AdminDetails) principal; // 类型转换成自己的认证对象

        // 3.获取认证结果
        String username = adminDetails.getUsername();
        Long id = adminDetails.getId();
        Collection<GrantedAuthority> authorities = adminDetails.getAuthorities();
        String authoritiesJsonString = JSON.toJSONString(authorities);

        // 生成JWT数据前将数据填充到Map中
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("id", id);
        claims.put("authoritiesJsonString", authoritiesJsonString);
        log.debug("向JWT中存入用户名:{}", username);
        log.debug("向JWT中存入id:{}", id);
        log.debug("向JWT中存入authoritiesJsonString:{}", authoritiesJsonString);

        // 4.生成JWT数据
        Date date = new Date(System.currentTimeMillis() + durationInMinute * 60 * 1000L);
        String jwt = Jwts.builder() //构建者模式
                // Header
                .setHeaderParam("alg", "HS256") // 指定算法
                .setHeaderParam("trp", "JWT") // 指定类型
                // Payload 载荷
                .setClaims(claims) // 传入Map
                // Signature 签名
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
        log.debug("生成的JWT：{}", jwt);
        return jwt;// 最终返回认证后的JWT
    }

    /**
     * 添加管理员的业务
     *
     * @param adminAddNewDTO 接收客户端的DTO对象
     */
    @Override
    public void addNew(AdminAddNewDTO adminAddNewDTO) {
        log.debug("开始处理添加[添加管理员]的业务!,参数：{}", adminAddNewDTO);

        log.debug("即将检查用户名是否被占用……");
        QueryWrapper<Admin> wrapperToUsername = new QueryWrapper<>();
        wrapperToUsername.eq("username", adminAddNewDTO.getUsername());
        Admin queryAdmin = adminMapper.selectOne(wrapperToUsername);
        if (queryAdmin != null) {
            String message = "添加管理员失败,用户名[" + adminAddNewDTO.getUsername() + "]已经被占用!";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        log.debug("即将检查手机号码是否被占用……");
        QueryWrapper<Admin> wrapperToPhone = new QueryWrapper<>();
        wrapperToPhone.eq("phone", adminAddNewDTO.getPhone());
        Admin queryAdminToPhone = adminMapper.selectOne(wrapperToPhone);
        if (queryAdminToPhone != null) {
            String message = "添加管理员失败,手机号[" + adminAddNewDTO.getPhone() + "]已经被占用!";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        log.debug("即将检查电子邮箱是否被占用……");
        QueryWrapper<Admin> wrapperToEmail = new QueryWrapper<>();
        wrapperToEmail.eq("email", adminAddNewDTO.getEmail());
        Admin queryAdminToEmail = adminMapper.selectOne(wrapperToEmail);
        if (queryAdminToEmail != null) {
            String message = "添加管理员失败,电子邮箱[" + adminAddNewDTO.getEmail() + "]已经被占用!";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }

        // 创建Admin对象
        Admin admin = new Admin();
        BeanUtils.copyProperties(adminAddNewDTO, admin);
        admin.setPassword(BCryptEncode.encryptionPassword(adminAddNewDTO.getPassword()));
        // 补全Admin对象中的属性值：loginCount >>> 0
        admin.setLoginCount(0);// 设置累计登录次数
        int rows = adminMapper.insert(admin);
        if (rows != 1) {
            String message = "添加管理员失败，服务器忙，请稍后再次尝试！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_INSERT, message);
        }

        /*
         执行插入管理员与角色关联的数据
         条件:
         (1).上次插入管理员后获取的roleIds=[?,?,?]
         (2).选择的角色对应的id数组
         */
        Long[] roleIds = adminAddNewDTO.getRoleIds();// 获取客户端选择角色传入的多个角色roleIds
        AdminRole[] adminRoles = new AdminRole[roleIds.length];// 创建一个角色管理员的引用数组,长度为用户选择的数量
        Date now = new Date();
        for (int i = 0; i < roleIds.length; i++) { // 遍历等长的数组向角色管理员关联表中插入数据(一个管理员对应多个角色)
            AdminRole adminRole = new AdminRole();// 创建角色管理员对象
            adminRole.setAdminId(admin.getId());// 设置插入后的管理员id
            adminRole.setRoleId(roleIds[i]);// 设置该管理员id下选择的所有角色id
            adminRole.setGmtCreate(now); // 设置当前时间
            adminRole.setGmtModified(now);
            adminRoles[i] = adminRole;// 将封装好的角色管理员对象放到AdminRole[]数组中
        }
        rows = adminRoleMapper.insertBatch(adminRoles);
        if (rows != roleIds.length) { // 判断如果影响的行数与插入的结果不一致时,抛出异常
            String message = "添加管理员失败，服务器忙，请稍后再次尝试！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_INSERT, message);
        }
    }

    /**
     * 根据id修改管理员信息
     * @param adminUpdateDTO 修改的信息
     */
    @Override
    public void update(AdminUpdateDTO adminUpdateDTO) {
        log.debug("开始处理修改id为{}的管理员信息",adminUpdateDTO.getId());
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.ne("id",adminUpdateDTO.getId());
        wrapper.eq("phone",adminUpdateDTO.getPhone());
        Admin adminToPhone = adminMapper.selectOne(wrapper);
        if (adminToPhone != null){
            String message = "修改失败,该手机号已被占用!";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT,message);
        }
        QueryWrapper<Admin> wrapperToEmail = new QueryWrapper<>();
        wrapperToEmail.ne("id",adminUpdateDTO.getId());
        wrapperToEmail.eq("email",adminUpdateDTO.getEmail());
        Admin adminToEmail = adminMapper.selectOne(wrapperToEmail);
        if (adminToEmail != null){
            String message = "修改失败,该电子邮箱已被占用!";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT,message);
        }

        Admin admin = new Admin();
        BeanUtils.copyProperties(adminUpdateDTO,admin);
        admin.setPassword(BCryptEncode.encryptionPassword(adminUpdateDTO.getPassword()));
        int rows = adminMapper.updateById(admin);
        if (rows>1){
            String message = "修改失败,服务器忙,请稍后再试...";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_UPDATE,message);
        }
    }

    /**
     * 根据用户名查询管理员信息
     * @param username 用户名
     * @return 返回管理员信息
     */
    @Override
    public Admin selectByUserName(String username) {
        log.debug("开始处理根据用户名查询管理员信息,参数:{}",username);
        QueryWrapper<Admin> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        Admin admin = adminMapper.selectOne(wrapper);
        if (admin == null){
            String message = "查询失败,该管理员不存在!";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND,message);
        }
        return admin;
    }

    /**
     * 根据id查询管理员信息
     * @param id 管理员id
     * @return 返回管理员信息
     */
    @Override
    public Admin selectById(Long id) {
        log.debug("开始处理查询id为{}的管理员信息",id);
        Admin admin = adminMapper.selectById(id);
        if (admin == null){
            String message = "查询失败,该管理员信息不存在!";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND,message);
        }
        return admin;
    }

    /**
     * 删除管理员的功能
     * (1).判断id是否为1
     * (2).判断id下有无数据
     *
     * @param id 要删除的管理员id
     */
    @Override
    public void deleteById(Long id) {
        log.debug("开始处理[删除管理员]的业务,参数{}", id);
        // id值为1的管理不允许被删除
        if (id == 1) {
            String message = "删除失败,管理员数据不存在!";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_DELETE, message);
        }
        // 判断该id是否存在
        Admin admin = adminMapper.selectById(id);
        if (admin == null) {
            String message = "删除失败，该管理员id不存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }

        int rows = adminMapper.deleteById(id);
        if (rows != 1) {
            String message = "删除管理员失败，服务器忙，请稍后再尝试！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_DELETE, message);
        }

        // 删除管理员与角色的关联数据(★因为添加了事务注解,若此处发生异常,则会发生事务回滚)
        rows = adminRoleMapper.deleteByAdminId(id);
        if (rows < 1) { // 若影响的行数小于1,抛出删除异常
            String message = "删除管理员失败，服务器忙，请稍后再尝试！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_DELETE, message);
        }
    }

    /**
     * 实现查询管理员列表的方法
     *
     * @return List
     */
    @Override
    public List<Admin> list() {
        log.debug("开始处理[查询管理员列表]的业务!");
        List<Admin> list = adminMapper.selectList(null);
        Iterator<Admin> iterator = list.iterator();
        while (iterator.hasNext()) { // 判断是否含有下一个元素
            Admin item = iterator.next();// 获取下一个元素
            if (item.getId() == 1) { // 判断该元素对象的id是否为1
                iterator.remove(); // 如果是,则移除该对象
                break; // 跳出分支
            }
        }
        return list;
    }

    /**
     * 处理启用管理员的业务
     * 通过调用启用或禁用的方法,传入id和值,
     *
     * @param id 启用的管理员id
     */
    @Override
    public void setEnable(Long id) {
        updateEnableById(id, 1);// 调用该方法传入id,设置为启用状态
    }

    /**
     * 处理禁用管理员的业务
     *
     * @param id 禁用的管理员id
     */
    @Override
    public void setDisable(Long id) {
        updateEnableById(id, 0);// 调用该方法传入id,设置为禁用状态
    }

    /**
     * 该方法用来处理启用与禁用的逻辑
     *
     * @param id     id
     * @param enable 是否启用或禁用
     */
    private void updateEnableById(Long id, Integer enable) {
        String[] tips = {"禁用", "启用"};
        log.debug("开始处理【{}管理员】的业务，id参数：{}", tips[enable], id);
        // 判断id是否为1(系统管理员)
        if (id == 1) {
            String message = tips[enable] + "管理员失败，尝试访问的数据不存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }
        // 根据id查询管理员详情
        Admin queryResult = adminMapper.selectById(id);
        if (queryResult == null) {
            String message = tips[enable] + "管理员失败,尝试访问的数据不存在！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERR_NOT_FOUND, message);
        }
        // 判断查询结果中的enable与方法参数enable是否相同
        if (enable.equals(queryResult.getEnable())) {
            String message = tips[enable] + "管理员失败，管理员账号已经处于" + tips[enable] + "状态！";
            log.debug(message);
            throw new ServiceException(ServiceCode.ERROR_CONFLICT, message);
        }
        // 创建admin对象,并封装id和enable这2个属性的值,并进行修改
        Admin admin = new Admin();
        admin.setId(id);
        admin.setEnable(enable);
        int rows = adminMapper.updateById(admin);
        if (rows != 1) {
            String message = tips[enable] + "管理员失败，服务器忙，请稍后再次尝试！";
            throw new ServiceException(ServiceCode.ERR_UPDATE, message);
        }
        log.debug("修改成功!");
    }
}
