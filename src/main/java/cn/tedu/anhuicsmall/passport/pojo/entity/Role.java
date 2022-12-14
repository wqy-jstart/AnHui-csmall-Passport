package cn.tedu.anhuicsmall.passport.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 角色实体类
 */
@Data
@TableName("ams_role")
public class Role implements Serializable {

    /**
     * 角色id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称")
    private String name;

    /**
     * 角色描述
     */
    @ApiModelProperty(value = "角色描述")
    private String description;

    /**
     * 角色排序
     */
    @ApiModelProperty(value = "角色排序")
    private Integer sort;

    /**
     * 数据起始时间
     */
    @ApiModelProperty(value = "插入时间")
    private Date gmtCreate;

    /**
     * 数据修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date gmtModified;
}
