package cn.tedu.anhuicsmall.passport.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 这是一个处理类
 * 填充数据库中两个特殊字段的值gmt_create和gmt_modified
 *
 * @Author java@Wqy
 * @Version 0.0.1
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    public MyMetaObjectHandler(){
        log.debug("创建组件类对象:MyMetaObjectHandler");
    }

    @Override// 插入时的填充策略
    public void insertFill(MetaObject metaObject) {
        log.info("==start insert ······==");

        this.setFieldValByName("gmtCreate",new Date(),metaObject);
        this.setFieldValByName("gmtModified",new Date(),metaObject);
    }

    @Override// 更新时的填充策略
    public void updateFill(MetaObject metaObject) {
        log.info("==start update ······==");
        this.setFieldValByName("gmtModified",new Date(),metaObject);
    }
}
