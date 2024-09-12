package net.xdclass.service;

import net.xdclass.model.AddressDO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 电商-公司收发货地址表 服务类
 * </p>
 *
 * @author 二当家小D
 * @since 2024-09-07
 */
public interface AddressService extends IService<AddressDO> {

    /**
     * 获取地址明细
     * @param id
     * @return
     */
    AddressDO getDetail(Long id);
}
