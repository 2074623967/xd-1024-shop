package net.xdclass.service;

import net.xdclass.enums.CouponCategoryEnum;
import net.xdclass.request.NewUserCouponRequest;
import net.xdclass.utils.JsonData;

import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 二当家小D
 * @since 2024-09-15
 */
public interface CouponService {

    /**
     * 分页查询优惠券
     *
     * @param page
     * @param size
     * @return
     */
    Map<String, Object> pageCouponActivity(int page, int size);

    /**
     * 领取优惠券
     *
     * @param couponId
     * @param couponCategory
     * @return
     */
    JsonData addCoupon(long couponId, CouponCategoryEnum couponCategory);

    /**
     * 新用户注册发放优惠券接口
     * @param newUserCouponRequest
     * @return
     */
    JsonData initNewUserCoupon(NewUserCouponRequest newUserCouponRequest);
}
