package net.xdclass.service;

import net.xdclass.vo.ProductVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 二当家小D
 * @since 2024-09-18
 */
public interface ProductService {

    /**
     * 分页查询商品列表
     *
     * @param page
     * @param size
     * @return
     */
    Map<String, Object> page(int page, int size);

    /**
     * 商品详情
     *
     * @param productId
     * @return
     */
    ProductVO findDetailById(long productId);

    /**
     * 批量查询商品信息
     *
     * @param productIdList
     * @return
     */
    List<ProductVO> findProductsByIdBatch(List<Long> productIdList);
}
