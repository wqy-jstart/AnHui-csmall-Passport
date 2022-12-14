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
 * 管理员的实体类
 */
@Data
@TableName("ams_admin")
public class Admin implements Serializable {
    /**
     * 管理员id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 管理员用户名
     */
    @ApiModelProperty(value = "管理员用户名")
    private String username;

    /**
     * 管理员密码
     */
    @ApiModelProperty(value = "管理员密码(密文)")
    private String password;

    /**
     * 管理员昵称
     */
    @ApiModelProperty(value = "管理员昵称")
    private String nickname;

    /**
     * 管理员头像
     */
    @ApiModelProperty(value = "管理员头像")
    private String avatar;

    /**
     * 管理员电话
     */
    @ApiModelProperty(value = "管理员电话")
    private String phone;

    /**
     * 管理员电子邮箱
     */
    @ApiModelProperty(value = "管理员电子邮箱")
    private String email;

    /**
     * 管理员描述
     */
    @ApiModelProperty(value = "管理员描述")
    private String description;

    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用")
    private Integer enable;

    /**
     * 累计登录次数
     */
    @ApiModelProperty(value = "累计登录次数")
    private Integer loginCount;

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
