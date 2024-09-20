package net.xdclass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.xdclass.enums.BizCodeEnum;
import net.xdclass.enums.CouponCategoryEnum;
import net.xdclass.enums.CouponPublishEnum;
import net.xdclass.enums.CouponStateEnum;
import net.xdclass.exception.BizException;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.mapper.CouponMapper;
import net.xdclass.mapper.CouponRecordMapper;
import net.xdclass.model.CouponDO;
import net.xdclass.model.CouponRecordDO;
import net.xdclass.model.LoginUser;
import net.xdclass.request.NewUserCouponRequest;
import net.xdclass.service.CouponService;
import net.xdclass.utils.CommonUtil;
import net.xdclass.utils.JsonData;
import net.xdclass.vo.CouponVO;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 二当家小D
 * @since 2024-09-15
 */
@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    @Resource
    private CouponMapper couponMapper;

    @Resource
    private CouponRecordMapper couponRecordMapper;

    @Resource
    private RedissonClient redissonClient;

    /**
     * 分页查询优惠券
     *
     * @param page
     * @param size
     * @return
     */
    @Override
    public Map<String, Object> pageCouponActivity(int page, int size) {
        Page<CouponDO> pageInfo = new Page<>(page, size);
        IPage<CouponDO> couponDOIPage = couponMapper.selectPage(pageInfo, new QueryWrapper<CouponDO>()
                .eq("publish", CouponPublishEnum.PUBLISH)
                .eq("category", CouponCategoryEnum.PROMOTION)
                .orderByDesc("create_time"));
        Map<String, Object> pageMap = new HashMap<>(3);
        //总条数
        pageMap.put("total_record", couponDOIPage.getTotal());
        //总页数
        pageMap.put("total_page", couponDOIPage.getPages());
        pageMap.put("current_data", couponDOIPage.getRecords().stream().
                map(this::beanProcess).collect(Collectors.toList()));
        return pageMap;
    }

    /**
     * 领取优惠券
     * 1、获取优惠券是否存在
     * 2、校验优惠券是否可以领取：时间、库存、超过限制
     * 3、扣减库存
     * 4、保存领劵记录
     * <p>
     * 始终要记得，羊毛党思维很厉害，社会工程学 应用的很厉害
     *
     * @param couponId
     * @param couponCategory
     * @return
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public JsonData addCoupon(long couponId, CouponCategoryEnum couponCategory) {
//        synchronized (this){
//            String key="coupon:"+couponId;
//            //setnx
//            if(stringRedisTemplate.opsForValue().setIfAbsent(key,"1")){
//                stringRedisTemplate.expire(key,30, TimeUnit.SECONDS);
//            }
//            //setnx setex
//            if(stringRedisTemplate.opsForValue().setIfAbsent(key,"1",30,TimeUnit.SECONDS)){
//                
//            }else {
//
//            }
//        }
        //String uuid = CommonUtil.generateUUID();
        //String lockKey = "lock:coupon" + couponId;
//        Boolean lockFlag = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, uuid, Duration.ofSeconds(30));
//        if (lockFlag) {
        //Lock lock = redisson.getLock("lock:coupon:" + couponId);
        //阻塞式等待，一个线程获取锁后，其他线程只能等待，和原生的方式循环调用不一样
        //lock.lock();
        String lockKey = "lock:coupon:" + couponId;
        RLock rLock = redissonClient.getLock(lockKey);
        //多个线程进入，会阻塞等待释放锁，默认30秒，然后有watch dog自动续期
        rLock.lock();
        //加锁10秒钟过期，没有watch dog功能，无法自动续期
        //rLock.lock(10,TimeUnit.SECONDS);
        log.info("领劵接口加锁成功:{}", Thread.currentThread().getId());
//        try {
//            TimeUnit.SECONDS.sleep(90);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        //加锁成功
        try {
            //执行业务逻辑
            LoginUser loginUser = LoginInterceptor.threadLocal.get();
            CouponDO couponDO = couponMapper.selectOne(new QueryWrapper<CouponDO>().eq("id", couponId)
                    .eq("category", couponCategory)
                    .eq("publish", CouponPublishEnum.PUBLISH));
            //优惠券检查
            this.couponCheck(couponDO, loginUser.getId());
            CouponRecordDO couponRecordDO = new CouponRecordDO();
            BeanUtils.copyProperties(couponDO, couponRecordDO);
            couponRecordDO.setCreateTime(new Date());
            couponRecordDO.setUseState(CouponStateEnum.NEW.name());
            couponRecordDO.setUserId(loginUser.getId());
            couponRecordDO.setUserName(loginUser.getName());
            couponRecordDO.setCouponId(couponId);
            couponRecordDO.setId(null);
            //高并发下扣减劵库存，采用乐观锁,当前stock做版本号,延伸多种防止超卖的问题,一次只能领取1张, TODO
            int rows = couponMapper.reduceStock(couponId, couponDO.getStock());
            if (rows == 1) {
                //库存扣减成功才保存
                couponRecordMapper.insert(couponRecordDO);
            } else {
                log.warn("发放优惠券失败:{},用户:{}", couponDO, loginUser);
                throw new BizException(BizCodeEnum.COUPON_NO_STOCK);
            }
        } finally {
//            String script = "if redis.call('get',KEYS[1]) == ARGV[1] " +
//                    "then return redis.call('del',KEYS[1]) else return 0 end";
//            Integer result = stringRedisTemplate.execute(new DefaultRedisScript<>(script, Integer.class),
//                    Arrays.asList(lockKey), uuid); // 脚本 keys列表 ARGV列表
//            log.info("解锁：{}", result);
            rLock.unlock();
        }
//    } else
//
//    {
//        //加锁失败
//        try {
//            TimeUnit.SECONDS.sleep(1);
//        } catch (InterruptedException e) {
//            log.error("自旋失败:{}", e);
//        }
//        addCoupon(couponId, couponCategory);
//    }
        //保存领卷记录
        return JsonData.buildSuccess();
    }

    /**
     * 新用户注册发放优惠券接口
     * 用户微服务调用的时候，没传递token
     * 本地直接调用发放优惠券的方法，需要构造一个登录用户存储在threadlocal
     *
     * @param newUserCouponRequest
     * @return
     */
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public JsonData initNewUserCoupon(NewUserCouponRequest newUserCouponRequest) {
        LoginUser loginUser = LoginUser.builder().build();
        loginUser.setId(newUserCouponRequest.getUserId());
        loginUser.setName(newUserCouponRequest.getName());
        LoginInterceptor.threadLocal.set(loginUser);
        //查询新用户有哪些优惠券
        List<CouponDO> couponDOList = couponMapper.selectList(new QueryWrapper<CouponDO>()
                .eq("category", CouponCategoryEnum.NEW_USER.name()));
        for (CouponDO couponDO : couponDOList) {
            //幂等操作，调用需要加锁
            this.addCoupon(couponDO.getId(), CouponCategoryEnum.NEW_USER);
        }
        return JsonData.buildSuccess();
    }

    /**
     * 优惠券检查
     *
     * @param couponDO
     * @param userId
     */
    private void couponCheck(CouponDO couponDO, Long userId) {
        //优惠券不存在
        if (couponDO == null) {
            throw new BizException(BizCodeEnum.COUPON_NO_EXITS);
        }
        //库存不足
        if (couponDO.getStock() <= 0) {
            throw new BizException(BizCodeEnum.COUPON_NO_STOCK);
        }
        //是否在领取时间范围
        long time = CommonUtil.getCurrentTimestamp();
        long start = couponDO.getStartTime().getTime();
        long end = couponDO.getEndTime().getTime();
        if (time < start || time > end) {
            throw new BizException(BizCodeEnum.COUPON_OUT_OF_TIME);
        }
        //用户是否超过限制
        int recordNum = couponRecordMapper.selectCount(new QueryWrapper<CouponRecordDO>()
                .eq("coupon_id", couponDO.getId())
                .eq("user_id", userId));
        if (recordNum >= couponDO.getUserLimit()) {
            throw new BizException(BizCodeEnum.COUPON_OUT_OF_LIMIT);
        }
    }

    private CouponVO beanProcess(CouponDO couponDO) {
        CouponVO couponVO = new CouponVO();
        BeanUtils.copyProperties(couponDO, couponVO);
        return couponVO;

    }
}
