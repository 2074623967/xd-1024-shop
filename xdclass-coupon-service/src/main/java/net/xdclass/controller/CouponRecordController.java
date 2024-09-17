package net.xdclass.controller;


import io.swagger.annotations.ApiOperation;
import net.xdclass.service.CouponRecordService;
import net.xdclass.utils.JsonData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("/api/coupon_record/v1")
public class CouponRecordController {

    @Resource
    private CouponRecordService couponRecordService;


    @ApiOperation("分页查询我的优惠券列表")
    @GetMapping("page")
    public JsonData page(@RequestParam(value = "page",defaultValue = "1")int page,
                         @RequestParam(value = "size",defaultValue = "20")int size){
        Map<String,Object> pageInfo = couponRecordService.page(page,size);
        return JsonData.buildSuccess(pageInfo);
    }
}

