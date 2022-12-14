package cn.tedu.anhuicsmall.passport.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 管理员登录的DTO类
 */
@Data
public class AdminLoginDTO implements Serializable {

    /**
     * 管理员用户名
     */
    @ApiModelProperty(value = "管理员用户名")
    @NotNull
    private String username;

    /**
     * 管理员密码
     */
    @ApiModelProperty(value = "管理员密码(密文)")
    @NotNull
    private String password;
}
