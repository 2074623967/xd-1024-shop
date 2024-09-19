package net.xdclass.service;

import net.xdclass.request.CartItemRequest;
import net.xdclass.vo.CartVO;

public interface CartService {

    /**
     * 添加到购物车
     * @param cartItemRequest
     */
    void addToCart(CartItemRequest cartItemRequest);

    /**
     * 修改购物车数量
     * @param cartItemRequest
     */
    void changeItemNum(CartItemRequest cartItemRequest);

    /**
     * 清空购物车
     */
    void clear();

    /**
     * 查看我的购物车
     * @return
     */
    CartVO getMyCart();

    /**
     * 删除购物项
     * @param productId
     */
    void deleteItem(long productId);
}
