package net.xdclass.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 发放新用户优惠劵请求类
 *
 * @author tangcj
 * @date 2024/09/20 15:13
 **/
@Data
public class NewUserCouponRequest {

    @JsonProperty("user_id")
    private long userId;

    @JsonProperty("name")
    private String name;
}
