package net.xdclass.service;

import net.xdclass.vo.BannerVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 二当家小D
 * @since 2024-09-18
 */
public interface BannerService {

    /**
     * 轮播图列表接口
     *
     * @return
     */
    List<BannerVO> list();
}
