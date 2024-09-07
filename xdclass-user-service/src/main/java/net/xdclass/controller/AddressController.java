package net.xdclass.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.xdclass.model.AddressDO;
import net.xdclass.service.AddressService;
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

    @ApiOperation("根据id查询地址详情")
    @GetMapping("find/{address_id}")
    public AddressDO getDetail(
            @ApiParam(value = "地址id", required = true)
            @PathVariable(value = "address_id") String id) {
        return addressService.getDetail(id);
    }
}

