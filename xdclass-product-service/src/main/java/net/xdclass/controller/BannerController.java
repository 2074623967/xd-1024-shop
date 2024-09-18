package net.xdclass.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.xdclass.service.BannerService;
import net.xdclass.utils.JsonData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 二当家小D
 * @since 2024-09-18
 */
@Api(tags = "轮播图模块")
@RestController
@RequestMapping("/api/banner/v1/")
public class BannerController {

    @Resource
    private BannerService bannerService;

    @ApiOperation("轮播图列表接口")
    @GetMapping("list")
    public JsonData list(){
        return JsonData.buildSuccess(bannerService.list());
    }
}

