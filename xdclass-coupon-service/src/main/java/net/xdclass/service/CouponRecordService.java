package net.xdclass.service;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 二当家小D
 * @since 2024-09-15
 */
public interface CouponRecordService {

    /**
     * 分页查询我的优惠券列表
     * @param page
     * @param size
     * @return
     */
    Map<String,Object> page(int page, int size);
}
