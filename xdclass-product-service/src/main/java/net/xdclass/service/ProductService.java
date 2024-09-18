package net.xdclass.service;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 二当家小D
 * @since 2024-09-18
 */
public interface ProductService {

    /**
     * 分页查询商品列表
     * @param page
     * @param size
     * @return
     */
    Map<String, Object> page(int page, int size);
}
