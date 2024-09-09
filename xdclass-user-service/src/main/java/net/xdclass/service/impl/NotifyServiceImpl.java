package net.xdclass.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.SendCodeEnum;
import net.xdclass.service.MailService;
import net.xdclass.service.NotifyService;
import net.xdclass.utils.CheckUtil;
import net.xdclass.utils.CommonUtil;
import net.xdclass.utils.JsonData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
    private static final String SUBJECT= "小滴课堂验证码";

    /**
     * 验证码的内容
     */
    private static final String CONTENT= "您的验证码是%s,有效时间是10分钟,打死也不要告诉任何人";

    @Resource
    private MailService mailService;

    @Override
    public JsonData sendCode(SendCodeEnum sendCodeEnum, String to) {
        if(CheckUtil.isEmail(to)){
            //拼接验证码 2322_324243232424324
            String code = CommonUtil.getRandomCode(6);
            //邮箱验证码
            mailService.sendMail(to,SUBJECT,String.format(CONTENT,code));
            return JsonData.buildSuccess();

        }else if(CheckUtil.isPhone(to)){
            //短信验证码
        }
        return JsonData.buildResult(BizCodeEnum.CODE_TO_ERROR);
    }
}
