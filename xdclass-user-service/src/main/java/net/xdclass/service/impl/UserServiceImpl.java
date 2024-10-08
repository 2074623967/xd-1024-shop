package net.xdclass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.SendCodeEnum;
import net.xdclass.feign.CouponFeignService;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.mapper.UserMapper;
import net.xdclass.model.LoginUser;
import net.xdclass.model.UserDO;
import net.xdclass.request.NewUserCouponRequest;
import net.xdclass.request.UserLoginRequest;
import net.xdclass.request.UserRegisterRequest;
import net.xdclass.service.NotifyService;
import net.xdclass.service.UserService;
import net.xdclass.utils.CommonUtil;
import net.xdclass.utils.JWTUtil;
import net.xdclass.utils.JsonData;
import net.xdclass.vo.UserVO;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户服务实现类
 * </p>
 *
 * @author 二当家小D
 * @since 2024-09-07
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    @Resource
    private NotifyService notifyService;

    @Resource
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CouponFeignService couponFeignService;

    /**
     * 用户注册
     * *邮箱验证码验证
     * *密码加密（TODO）
     * *账号唯一性检查(TODO)
     * *插入数据库
     * *新注册用户福利发放(TODO)
     *
     * @param registerRequest
     * @return
     */
    @Override
    public JsonData register(UserRegisterRequest registerRequest) {
        boolean checkCode = false;
        //校验验证码
        if (StringUtils.isNotBlank(registerRequest.getMail())) {
            checkCode = notifyService.checkCode(SendCodeEnum.USER_REGISTER, registerRequest.getMail(), registerRequest.getCode());
        }
        if (!checkCode) {
            return JsonData.buildResult(BizCodeEnum.CODE_ERROR);
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(registerRequest, userDO);
        userDO.setCreateTime(new Date());
        userDO.setSlogan("人生需要动态规划，学习需要贪心算法");
        //设置密码 生成秘钥 盐
        userDO.setSecret("$1$" + CommonUtil.getStringNumRandom(8));
        //密码+盐处理
        String cryptPwd = Md5Crypt.md5Crypt(registerRequest.getPwd().getBytes(), userDO.getSecret());
        userDO.setPwd(cryptPwd);
        //账号唯一性检查
        if (checkUnique(userDO.getMail())) {
            int rows = userMapper.insert(userDO);
            log.info("rows:{},注册成功:{}", rows, userDO);
            //新用户注册成功，初始化信息，发放福利等 TODO
            userRegisterInitTask(userDO);
            return JsonData.buildSuccess();
        } else {
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_REPEAT);
        }
    }

    @Override
    public JsonData login(UserLoginRequest userLoginRequest) {
        //根据邮箱查询账号
        List<UserDO> list = userMapper.selectList(
                new QueryWrapper<UserDO>().eq("mail", userLoginRequest.getMail()));
        if (list != null && list.size() == 1) {
            UserDO userDO = list.get(0);
            String cryptPwd = Md5Crypt.md5Crypt(userLoginRequest.getPwd().getBytes(), userDO.getSecret());
            if (cryptPwd.equals(userDO.getPwd())) {
                //生成token令牌
                LoginUser userDTO = LoginUser.builder().build();
                BeanUtils.copyProperties(userDO, userDTO);
                String accessToken = JWTUtil.geneJsonWebToken(userDTO);
                // accessToken
                // accessToken的过期时间
                // UUID生成一个token
                //String refreshToken = CommonUtil.generateUUID();
                //redisTemplate.opsForValue().set(refreshToken,"1",1000*60*60*24*30);
                return JsonData.buildSuccess(accessToken);
            }
            //密码错误
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_PWD_ERROR);
        } else {
            //未注册
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_UNREGISTER);
        }
    }

    /**
     * 查找用户详情
     *
     * @return
     */
    @Override
    public UserVO findUserDetail() {
        //从当前线程的ThreadLocal里面获取用户信息
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        //查询用户数据
        UserDO userDO = userMapper.selectOne(new QueryWrapper<UserDO>().eq("id", loginUser.getId()));
        UserVO userVO = new UserVO();
        //值拷贝
        BeanUtils.copyProperties(userDO, userVO);
        return userVO;
    }

    /**
     * 校验用户账号唯一
     *
     * @param mail
     * @return
     */
    private boolean checkUnique(String mail) {
        QueryWrapper queryWrapper = new QueryWrapper<UserDO>().eq("mail", mail);
        List<UserDO> list = userMapper.selectList(queryWrapper);
        return list.size() > 0 ? false : true;
    }

    /**
     * 用户注册，初始化福利信息 TODO
     *
     * @param userDO
     */
    private void userRegisterInitTask(UserDO userDO) {
        NewUserCouponRequest request = new NewUserCouponRequest();
        request.setName(userDO.getName());
        request.setUserId(userDO.getId());
        JsonData jsonData = couponFeignService.addNewUserCoupon(request);
//        if(jsonData.getCode()!=0){
//            throw new RuntimeException("发放优惠券异常");
//        }
        log.info("发放新用户注册优惠券：{},结果:{}", request, jsonData.toString());

    }
}
