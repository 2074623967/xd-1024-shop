package net.xdclass.service;

import net.xdclass.model.ProductOrderDO;
import com.baomidou.mybatisplus.extension.service.IService;
import net.xdclass.request.ConfirmOrderRequest;
import net.xdclass.utils.JsonData;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 二当家小D
 * @since 2024-09-19
 */
public interface ProductOrderService {

    /**
     * 创建订单
     *
     * @param orderRequest
     * @return
     */
    JsonData confirmOrder(ConfirmOrderRequest orderRequest);
}
