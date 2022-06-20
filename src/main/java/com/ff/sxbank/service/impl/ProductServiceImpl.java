package com.ff.sxbank.service.impl;

import com.ff.sxbank.pojo.Product;
import com.ff.sxbank.mapper.ProductMapper;
import com.ff.sxbank.service.IProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xulifeng
 * @since 2022-04-01
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

}
