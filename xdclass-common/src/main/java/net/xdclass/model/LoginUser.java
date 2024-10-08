package net.xdclass.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 登录用户
 *
 * @author tangcj
 * @date 2024/09/14 09:41
 **/
@Data
@Builder
public class LoginUser {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 头像
     */
    @JsonProperty("head_img")
    private String headImg;

    /**
     * 邮箱
     */
    private String mail;
}
