package net.xdclass.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.xdclass.model.BannerDO;
import net.xdclass.mapper.BannerMapper;
import net.xdclass.service.BannerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.xdclass.vo.BannerVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 二当家小D
 * @since 2024-09-18
 */
@Service
public class BannerServiceImpl implements BannerService {

    @Resource
    private BannerMapper bannerMapper;

    /**
     * 轮播图列表接口
     * @return
     */
    @Override
    public List<BannerVO> list() {
        List<BannerDO> bannerDOList =  bannerMapper.selectList(new QueryWrapper<BannerDO>().
                orderByAsc("weight"));
        List<BannerVO> bannerVOList =  bannerDOList.stream().map(obj->{
            BannerVO bannerVO = new BannerVO();
            BeanUtils.copyProperties(obj,bannerVO);
            return bannerVO;
        }).collect(Collectors.toList());
        return bannerVOList;
    }
}
