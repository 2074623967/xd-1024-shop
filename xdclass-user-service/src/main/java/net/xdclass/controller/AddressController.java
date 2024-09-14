package net.xdclass.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.model.AddressDO;
import net.xdclass.request.AddressAddReqeust;
import net.xdclass.service.AddressService;
import net.xdclass.utils.JsonData;
import net.xdclass.vo.AddressVO;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 电商-公司收发货地址表 前端控制器
 * </p>
 *
 * @author 二当家小D
 * @since 2024-09-07
 */
@Api(tags = "收货地址模块")
@RestController
@RequestMapping("/api/address/v1/")
public class AddressController {

    @Resource
    private AddressService addressService;

    @ApiOperation("新增收货地址")
    @PostMapping("add")
    public JsonData add(@ApiParam("地址对象") @RequestBody AddressAddReqeust addressAddReqeust){
        addressService.add(addressAddReqeust);
        return JsonData.buildSuccess();
    }

    /**
     * 根据id查找地址详情
     * @param addressId
     * @return
     */
    @ApiOperation("根据id查找地址详情")
    @GetMapping("/find/{address_id}")
    public Object detail(
            @ApiParam(value = "地址id",required = true)
            @PathVariable("address_id") long addressId){
        AddressVO addressVO = addressService.detail(addressId);
        return addressVO == null ? JsonData.buildResult(BizCodeEnum.ADDRESS_NO_EXITS)
                :JsonData.buildSuccess(addressVO);
    }

    /**
     * 删除指定收货地址
     * @param addressId
     * @return
     */
    @ApiOperation("删除指定收货地址")
    @DeleteMapping("/del/{address_id}")
    public JsonData del(
            @ApiParam(value = "地址id",required = true)
            @PathVariable("address_id")int addressId){
        int rows = addressService.del(addressId);
        return rows == 1 ? JsonData.buildSuccess(): JsonData.buildResult(BizCodeEnum.ADDRESS_DEL_FAIL);
    }
}

