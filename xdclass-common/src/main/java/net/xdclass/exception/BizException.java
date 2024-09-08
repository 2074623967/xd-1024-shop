package net.xdclass.exception;

import lombok.Data;
import net.xdclass.enums.BizCodeEnum;

/**
 * 全局异常处理
 *
 * @author tangcj
 * @date 2024/09/08 10:09
 **/
@Data
public class BizException extends RuntimeException {

    private Integer code;
    private String msg;

    public BizException(Integer code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }

    public BizException(BizCodeEnum bizCodeEnum) {
        super(bizCodeEnum.getMessage());
        this.code = bizCodeEnum.getCode();
        this.msg = bizCodeEnum.getMessage();
    }

}
