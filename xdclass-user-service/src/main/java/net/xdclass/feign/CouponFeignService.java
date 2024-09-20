package net.xdclass.feign;

import net.xdclass.request.NewUserCouponRequest;
import net.xdclass.utils.JsonData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 优惠劵服务远程调用
 *
 * @author tangcj
 * @date 2024/09/20 15:12
 **/
@FeignClient(name = "xdclass-coupon-service")
public interface CouponFeignService {

    /**
     * 新用户注册发放优惠券
     * @param newUserCouponRequest
     * @return
     */
    @PostMapping("/api/coupon/v1/new_user_coupon")
    JsonData addNewUserCoupon(@RequestBody NewUserCouponRequest newUserCouponRequest);
}
