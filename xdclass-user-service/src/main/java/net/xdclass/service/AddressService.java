package net.xdclass.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.xdclass.model.AddressDO;
import net.xdclass.request.AddressAddReqeust;
import net.xdclass.vo.AddressVO;

import java.util.List;

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
     * 新增收货地址
     *
     * @param addressAddReqeust
     */
    void add(AddressAddReqeust addressAddReqeust);

    /**
     * 根据id查找地址详情
     *
     * @param addressId
     * @return
     */
    AddressVO detail(long addressId);

    /**
     * 删除指定收货地址
     *
     * @param addressId
     * @return
     */
    int del(int addressId);

    /**
     * 查询用户的全部收货地址
     *
     * @return
     */
    List<AddressVO> listUserAllAddress();
}
