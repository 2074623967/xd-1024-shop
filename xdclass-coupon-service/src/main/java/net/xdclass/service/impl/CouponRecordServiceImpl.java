package net.xdclass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.xdclass.interceptor.LoginInterceptor;
import net.xdclass.mapper.CouponRecordMapper;
import net.xdclass.model.CouponRecordDO;
import net.xdclass.model.LoginUser;
import net.xdclass.service.CouponRecordService;
import net.xdclass.vo.CouponRecordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
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
public class CouponRecordServiceImpl implements CouponRecordService {

    @Resource
    private CouponRecordMapper couponRecordMapper;

    /**
     * 分页查询我的优惠券列表
     * @param page
     * @param size
     * @return
     */
    @Override
    public Map<String, Object> page(int page, int size) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        //第1页，每页2条
        Page<CouponRecordDO> pageInfo = new Page<>(page, size);
        IPage<CouponRecordDO> recordDOPage = couponRecordMapper.selectPage(pageInfo,
                new QueryWrapper<CouponRecordDO>().eq("user_id",loginUser.getId()).
                        orderByDesc("create_time"));
        Map<String, Object> pageMap = new HashMap<>(3);
        pageMap.put("total_record", recordDOPage.getTotal());
        pageMap.put("total_page", recordDOPage.getPages());
        pageMap.put("current_data", recordDOPage.getRecords().stream().
                map(obj -> beanProcess(obj)).collect(Collectors.toList()));
        return pageMap;
    }

    /**
     * 查询优惠券记录详情
     * @param recordId
     * @return
     */
    @Override
    public CouponRecordVO findById(long recordId) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        CouponRecordDO couponRecordDO = couponRecordMapper.selectOne(new QueryWrapper<CouponRecordDO>()
                .eq("id",recordId).eq("user_id",loginUser.getId()));
        if(couponRecordDO == null ){return null;}
        return beanProcess(couponRecordDO);
    }

    private CouponRecordVO beanProcess(CouponRecordDO couponRecordDO) {
        CouponRecordVO couponRecordVO = new CouponRecordVO();
        BeanUtils.copyProperties(couponRecordDO,couponRecordVO);
        return couponRecordVO;
    }
}
