package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.constant.CacheKey;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.SendCodeEnum;
import net.xdclass.service.MailService;
import net.xdclass.service.NotifyService;
import net.xdclass.utils.CheckUtil;
import net.xdclass.utils.CommonUtil;
import net.xdclass.utils.JsonData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 通知模块
 *
 * @author tangcj
 * @date 2024/09/09 11:05
 **/
@Service
@Slf4j
public class NotifyServiceImpl implements NotifyService {

    /**
     * 验证码的标题
     */
    private static final String SUBJECT = "小滴课堂验证码";

    /**
     * 验证码的内容
     */
    private static final String CONTENT = "您的验证码是%s,有效时间是10分钟,打死也不要告诉任何人";

    /**
     * 验证码10分钟有效
     */
    private static final int CODE_EXPIRED = 60 * 1000 * 10;

    @Resource
    private MailService mailService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 前置: 判断是否重复发送
     * 发送验证码，存储验证码到缓存
     * 1. 存储验证码到缓存
     * 2. 发送邮箱验证码
     * 后置: 存储发送记录
     *
     * @param sendCodeEnum
     * @param to
     * @return
     */
    @Override
    public JsonData sendCode(SendCodeEnum sendCodeEnum, String to) {
        String cacheKey = String.format(CacheKey.CHECK_CODE_KEY, sendCodeEnum.name(), to);
        String cacheValue = stringRedisTemplate.opsForValue().get(cacheKey);
        //如果不为空，则判断是否60秒内重复发送
        if (StringUtils.isNotBlank(cacheValue)) {
            long ttl = Long.parseLong(cacheValue.split("_")[1]);
            //当前时间戳-验证码发送时间戳，如果小于60秒，则不给重复发送
            if (CommonUtil.getCurrentTimestamp() - ttl < 1000 * 60) {
                log.info("重复发送验证码,时间间隔:{} 秒", (CommonUtil.getCurrentTimestamp() - ttl) / 1000);
                return JsonData.buildResult(BizCodeEnum.CODE_LIMITED);
            }
        }
        //拼接验证码 2322_324243232424324
        String code = CommonUtil.getRandomCode(6);
        String value = code + "_" + CommonUtil.getCurrentTimestamp();
        stringRedisTemplate.opsForValue().set(cacheKey, value, CODE_EXPIRED, TimeUnit.MILLISECONDS);
        if (CheckUtil.isEmail(to)) {
            //邮箱验证码
            mailService.sendMail(to, SUBJECT, String.format(CONTENT, code));
            return JsonData.buildSuccess();
        } else if (CheckUtil.isPhone(to)) {
            //短信验证码
        }
        return JsonData.buildResult(BizCodeEnum.CODE_TO_ERROR);
    }

    /**
     * 校验验证码
     *
     * @param sendCodeEnum
     * @param to
     * @param code
     * @return
     */
    @Override
    public boolean checkCode(SendCodeEnum sendCodeEnum, String to, String code) {
        String cacheKey = String.format(CacheKey.CHECK_CODE_KEY, sendCodeEnum.name(), to);
        String cacheValue = stringRedisTemplate.opsForValue().get(cacheKey);
        if (StringUtils.isNotBlank(cacheValue)) {
            String cacheCode = cacheValue.split("_")[0];
            if (cacheCode.equals(code)) {
                //删除验证码
                stringRedisTemplate.delete(cacheKey);
                return true;
            }
        }
        return false;
    }
}
