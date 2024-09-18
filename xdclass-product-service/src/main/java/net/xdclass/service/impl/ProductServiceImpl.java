package net.xdclass.service.impl;

import net.xdclass.model.ProductDO;
import net.xdclass.mapper.ProductMapper;
import net.xdclass.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 二当家小D
 * @since 2024-09-18
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, ProductDO> implements ProductService {

}
