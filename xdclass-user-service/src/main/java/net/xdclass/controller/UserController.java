package net.xdclass.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.request.UserLoginRequest;
import net.xdclass.request.UserRegisterRequest;
import net.xdclass.service.FileService;
import net.xdclass.service.UserService;
import net.xdclass.utils.JsonData;
import net.xdclass.vo.UserVO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
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

    @Resource
    private UserService userService;

    /**
     * 传用户头像
     * 默认文件大小 1M,超过会报错
     *
     * @param file
     * @return
     */
    @ApiOperation("用户头像上传")
    @PostMapping(value = "upload")
    public JsonData uploadHeaderImg(@ApiParam(value = "文件上传", required = true)
                                    @RequestPart("file") MultipartFile file) {
        String result = fileService.uploadUserHeadImg(file);
        return result != null ? JsonData.buildSuccess(result) :
                JsonData.buildResult(BizCodeEnum.FILE_UPLOAD_USER_IMG_FAIL);
    }

    @ApiOperation("用户注册")
    @PostMapping("register")
    public JsonData register(@ApiParam("用户注册对象") @RequestBody UserRegisterRequest registerRequest) {
        JsonData jsonData = userService.register(registerRequest);
        return jsonData;
    }

    /**
     * 用户登录
     *
     * @return
     */
    @ApiOperation("用户登录")
    @PostMapping("login")
    public JsonData login(@ApiParam("用户登录对象") @RequestBody UserLoginRequest userLoginRequest) {
        //Anna小姐姐 12345
        JsonData jsonData = userService.login(userLoginRequest);
        return jsonData;
    }

    /**
     * 查询用户个人信息
     *
     * @return
     */
    @ApiOperation("个人信息查询")
    @GetMapping("detail")
    public JsonData detail() {
        UserVO userVO = userService.findUserDetail();
        return JsonData.buildSuccess(userVO);
    }

    /**
     * 刷新token的方案
     * @param param
     * @return
     */
//    @PostMapping("refresh_token")
//    public JsonData getRefreshToken(Map<String, Object> param) {
//        //先去redis,找refresh_token是否存在
//        //refresh_token存在，解密accessToken
//        //重新调用JWTUtil.geneJsonWebToken() 生成accessToken
//        //重新生成refresh_token，并存储redis，设置30天过期时间
//        //返回给前端
//        return null;
//    }
}

