package net.xdclass.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.xdclass.model.UserDO;
import net.xdclass.request.UserLoginRequest;
import net.xdclass.request.UserRegisterRequest;
import net.xdclass.utils.JsonData;
import net.xdclass.vo.UserVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 二当家小D
 * @since 2024-09-07
 */
public interface UserService extends IService<UserDO> {

    /**
     * 注册
     *
     * @param registerRequest
     * @return
     */
    JsonData register(UserRegisterRequest registerRequest);

    /**
     * 登录
     *
     * @param userLoginRequest
     * @return
     */
    JsonData login(UserLoginRequest userLoginRequest);

    /**
     * 查询用户个人信息
     *
     * @return
     */
    UserVO findUserDetail();
}
