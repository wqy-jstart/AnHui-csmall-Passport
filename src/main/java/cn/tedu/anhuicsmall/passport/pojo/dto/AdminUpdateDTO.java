package cn.tedu.anhuicsmall.passport.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 修改管理员的DTO类
 */
@Data
public class AdminUpdateDTO implements Serializable {

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
}
