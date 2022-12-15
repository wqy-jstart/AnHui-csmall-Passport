package cn.tedu.anhuicsmall.passport.mapper;

import cn.tedu.anhuicsmall.passport.pojo.vo.AdminLoginInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class AdminMapperTests {

    @Autowired
    private AdminMapper adminMapper;

    @Test
    public void selectByAdmin(){
        AdminLoginInfoVO root = adminMapper.getLoginInfoByUsername("root");
        log.debug("查询的结果为:{}",root);
    }
}
