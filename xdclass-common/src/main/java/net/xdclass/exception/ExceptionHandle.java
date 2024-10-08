package net.xdclass.exception;

import lombok.extern.slf4j.Slf4j;
import net.xdclass.utils.JsonData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 自定义异常处理器
 *
 * @author tangcj
 * @date 2024/09/08 10:13
 **/
@ControllerAdvice
@Slf4j
public class ExceptionHandle {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonData Handle(Exception e) {

        if (e instanceof BizException) {
            BizException bizException = (BizException) e;
            log.info("[业务异常]{}", e);
            return JsonData.buildCodeAndMsg(bizException.getCode(), bizException.getMsg());

        } else {
            log.info("[系统异常]{}", e);
            return JsonData.buildError("全局异常，未知错误");
        }

    }
}
