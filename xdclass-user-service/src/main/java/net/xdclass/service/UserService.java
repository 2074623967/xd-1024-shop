package net.xdclass.service;

import net.xdclass.model.UserDO;
import com.baomidou.mybatisplus.extension.service.IService;
import net.xdclass.request.UserRegisterRequest;
import net.xdclass.utils.JsonData;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 二当家小D
 * @since 2024-09-07
 */
public interface UserService extends IService<UserDO> {

    /**
     * 注册
     * @param registerRequest
     * @return
     */
    JsonData register(UserRegisterRequest registerRequest);
}
