package cn.tedu.anhuicsmall.passport.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 添加管理员的DTO类
 */
@Data
public class AdminAddNewDTO implements Serializable {
    /**
     * 管理员用户名
     */
    @ApiModelProperty(value = "管理员用户名")
    private String username;

    /**
     * 管理员密码(密文)
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
     * 添加管理员时勾选的角色
     */
    @ApiModelProperty(value = "角色id数组",required = true)
    private Long[] roleIds;
}
