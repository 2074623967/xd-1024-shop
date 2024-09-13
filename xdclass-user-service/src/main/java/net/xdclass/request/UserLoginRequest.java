package net.xdclass.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户登录请求类
 *
 * @author tangcj
 * @date 2024/09/13 19:49
 **/
@ApiModel(value = "用户登录对象",description = "用户登录对象")
@Data
public class UserLoginRequest {

    @ApiModelProperty(value = "邮箱", example = "794666918@qq.com")
    private String mail;

    @ApiModelProperty(value = "密码", example = "123456")
    private String pwd;
}
