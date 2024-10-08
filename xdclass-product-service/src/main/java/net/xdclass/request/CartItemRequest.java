package net.xdclass.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 购物车添加请求
 *
 * @author tangcj
 * @date 2024/09/19 10:28
 **/
@ApiModel
@Data
public class CartItemRequest {

    @ApiModelProperty(value = "商品id", example = "11")
    @JsonProperty("product_id")
    private long productId;

    @ApiModelProperty(value = "购买数量", example = "1")
    @JsonProperty("buy_num")
    private int buyNum;
}
