package net.xdclass.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.CouponCategoryEnum;
import net.xdclass.request.NewUserCouponRequest;
import net.xdclass.service.CouponService;
import net.xdclass.utils.JsonData;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 二当家小D
 * @since 2024-09-15
 */
@Api("优惠券模块")
@RestController
@RequestMapping("/api/coupon/v1")
@Slf4j
public class CouponController {

    @Resource
    private CouponService couponService;

    /**
     * 分页查询优惠券
     *
     * @param page
     * @param size
     * @return
     */
    @ApiOperation("分页查询优惠券")
    @GetMapping("page_coupon")
    public JsonData pageCouponList(
            @ApiParam(value = "当前页") @RequestParam(value = "page", defaultValue = "1") int page,
            @ApiParam(value = "每页显示多少条") @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Map<String, Object> pageMap = couponService.pageCouponActivity(page, size);
        return JsonData.buildSuccess(pageMap);
    }

    /**
     * 领取优惠券
     *
     * @param couponId
     * @return
     */
    @ApiOperation("领取优惠券")
    @GetMapping("/add/promotion/{coupon_id}")
    public JsonData addPromotionCoupon(@ApiParam(value = "优惠券id", required = true)
                                       @PathVariable("coupon_id") long couponId) {
        JsonData jsonData = couponService.addCoupon(couponId, CouponCategoryEnum.PROMOTION);
        return jsonData;
    }

    /**
     * 新用户注册发放优惠券接口
     *
     * @return
     */
    @ApiOperation("RPC-新用户注册接口")
    @PostMapping("/new_user_coupon")
    public JsonData addNewUserCoupon(@ApiParam("用户对象") @RequestBody NewUserCouponRequest newUserCouponRequest) {
        JsonData jsonData = couponService.initNewUserCoupon(newUserCouponRequest);
        return jsonData;
    }
}

