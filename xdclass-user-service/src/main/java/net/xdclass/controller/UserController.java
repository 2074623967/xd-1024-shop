package net.xdclass.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.service.FileService;
import net.xdclass.utils.JsonData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 二当家小D
 * @since 2024-09-07
 */
@Api(tags = "用户模块")
@RestController
@RequestMapping("/api/user/v1")
public class UserController {

    @Resource
    private FileService fileService;

    /**
     * 传用户头像
     * 默认文件大小 1M,超过会报错
     * @param file
     * @return
     */
    @ApiOperation("用户头像上传")
    @PostMapping(value = "upload")
    public JsonData uploadHeaderImg(@ApiParam(value = "文件上传",required = true)
                                        @RequestPart("file") MultipartFile file){
        String result = fileService.uploadUserHeadImg(file);
        return result != null? JsonData.buildSuccess(result):
                JsonData.buildResult(BizCodeEnum.FILE_UPLOAD_USER_IMG_FAIL);
    }

}

