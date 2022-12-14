package cn.tedu.anhuicsmall.passport.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 这是MyBatis-Plus的配置类
 *
 * @Author java@Wqy
 * @Version 0.0.1
 */
@Slf4j
@Configuration
@MapperScan("cn.tedu.anhuicsmall.passport.mapper")
@EnableTransactionManagement
public class MyBatisPlusConfiguration {
    public MyBatisPlusConfiguration(){
        log.debug("创建配置类对象:MyBatisPlusConfiguration");
    }
}
