package net.xdclass.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.ClientType;
import net.xdclass.enums.ProductOrderPayTypeEnum;
import net.xdclass.request.ConfirmOrderRequest;
import net.xdclass.service.ProductOrderService;
import net.xdclass.utils.JsonData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 二当家小D
 * @since 2024-09-19
 */
@Api(tags = "订单模块")
@RestController
@RequestMapping("/api/order/v1/")
@Slf4j
public class ProductOrderController {

    @Resource
    private ProductOrderService orderService;

    @ApiOperation("提交订单")
    @PostMapping("confirm")
    public void confirmOrder(@ApiParam("订单对象") @RequestBody ConfirmOrderRequest orderRequest,
                             HttpServletResponse response) {
        JsonData jsonData = orderService.confirmOrder(orderRequest);
        if (jsonData.getCode() == 0) {
            String client = orderRequest.getClientType();
            String payType = orderRequest.getPayType();
            //如果是支付宝网页支付，都是跳转网页，APP除外
            if (payType.equalsIgnoreCase(ProductOrderPayTypeEnum.ALIPAY.name())) {
                log.info("创建支付宝订单成功:{}", orderRequest);
                if (client.equalsIgnoreCase(ClientType.H5.name())) {
                    writeData(response, jsonData);
                } else if (client.equalsIgnoreCase(ClientType.APP.name())) {
                    //APP SDK支付  TODO
                }
            } else if (payType.equalsIgnoreCase(ProductOrderPayTypeEnum.WECHAT.name())) {
                //微信支付 TODO
            }
        } else {
            log.error("创建订单失败{}", jsonData);
        }
    }

    private void writeData(HttpServletResponse response, JsonData jsonData) {
        try {
            response.setContentType("text/html;charset=UTF8");
            response.getWriter().write(jsonData.getData().toString());
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            log.error("写出Html异常：{}", e);
        }
    }
}

