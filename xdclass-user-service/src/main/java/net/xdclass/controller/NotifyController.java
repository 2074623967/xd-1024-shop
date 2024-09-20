package net.xdclass.controller;

import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.SendCodeEnum;
import net.xdclass.service.NotifyService;
import net.xdclass.utils.CommonUtil;
import net.xdclass.utils.JsonData;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 通知模块
 *
 * @author tangcj
 * @date 2024/09/08 18:54
 **/
@Api(tags = "通知模块")
@RestController
@RequestMapping("/api/notify/v1/")
@Slf4j
public class NotifyController {

    /**
     * 临时使用10分钟有效，方便测试
     */
    private static final long CAPTCHA_CODE_EXPIRED = 60 * 1000 * 10;

    @Resource
    private Producer producer;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private NotifyService notifyService;

    @ApiOperation("获取图形验证码")
    @GetMapping("captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) {
        String cacheKey = getCaptchaKey(request);
        String capText = producer.createText();
        //存储
        stringRedisTemplate.opsForValue().set(cacheKey, capText, CAPTCHA_CODE_EXPIRED, TimeUnit.MILLISECONDS);
        BufferedImage bi = producer.createImage(capText);
        ServletOutputStream out = null;
        try {
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "create_date-check=0, pre-check=0");
            response.setHeader("Pragma", "no-cache");
            out = response.getOutputStream();
            ImageIO.write(bi, "jpg", out);
            out.flush();
            out.close();

        } catch (IOException e) {
            log.error("获取验证码失败:{}", e);
        }
    }

    /**
     * 发送验证码
     * 1. 匹配图形验证码是否正常
     * 2. 发送验证码
     *
     * @param to
     * @param captcha
     * @param request
     * @return
     */
    @ApiOperation("发送邮箱注册验证码")
    @GetMapping("send_code")
    public JsonData sendRegisterCode(@RequestParam(value = "to", required = true) String to,
                                     @RequestParam(value = "captcha", required = true) String captcha,
                                     HttpServletRequest request) {
        String key = getCaptchaKey(request);
        String cacheCaptcha = stringRedisTemplate.opsForValue().get(key);
        //匹配图形验证码是否一样
        if (captcha != null && cacheCaptcha != null && captcha.equalsIgnoreCase(cacheCaptcha)) {
            //成功
            stringRedisTemplate.delete(key);
            JsonData jsonData = notifyService.sendCode(SendCodeEnum.USER_REGISTER, to);
            return jsonData;
        } else {
            return JsonData.buildResult(BizCodeEnum.CODE_CAPTCHA_ERROR);
        }
    }


    /**
     * 获取缓存的key
     *
     * @param request
     * @return
     */
    private String getCaptchaKey(HttpServletRequest request) {
        String ip = CommonUtil.getIpAddr(request);
        String userAgent = request.getHeader("User-Agent");
        String key = "user-service:captcha:" + CommonUtil.MD5(ip + userAgent);
        log.info("ip={}", ip);
        log.info("userAgent={}", userAgent);
        log.info("key={}", key);
        return key;
    }
}
