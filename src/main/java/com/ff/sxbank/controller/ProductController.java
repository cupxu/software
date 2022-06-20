package com.ff.sxbank.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ff.sxbank.exception.ResponseResult;
import com.ff.sxbank.mapper.ProductMapper;
import com.ff.sxbank.mapper.SeckillProductMapper;
import com.ff.sxbank.pojo.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xulifeng
 * @since 2022-04-01
 */
@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {

    ProductMapper productMapper;
    @Autowired
    public void setProductMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    Product product;
    @Autowired
    public void setProduct(Product product) {
        this.product = product;
    }

    SeckillProductMapper mapper;
    @Autowired
    public void setMapper(SeckillProductMapper mapper) {
        this.mapper = mapper;
    }



    @PostMapping("/add")
    public ResponseResult add(@RequestBody Map<String,String> map) {

        try {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("seckill_product_name", map.get("seckillProductName"));
            int id = mapper.selectOne(wrapper).getSeckillProductId().intValue();

            Product temp = productMapper.selectOne(wrapper);

            product.setProductId(id);
            product.setSeckillProductName(map.get("seckillProductName"));
            product.setProductRate(Double.parseDouble(map.get("productRate")));
            product.setProductPeriod(map.get("productPeriod"));
            product.setProductReleaseStartDay(map.get("productReleaseStartDay"));
            product.setProductReleaseEndDay(map.get("productReleaseEndDay"));
            product.setProductHarvestDay(map.get("productHarvestDay"));
            product.setProductExpireDay(map.get("productExpireDay"));
            product.setProductScale(Integer.parseInt(map.get("productScale")));
            product.setProductCode(map.get("productCode"));
            product.setProductAddition(map.get("productAddition"));
            if (temp == null){
                productMapper.insert(product);
            }else {
                productMapper.updateById(product);
            }
            return ResponseResult.success("产品更新成功");
        } catch (NumberFormatException e) {
            return ResponseResult.error(e.getMessage());
        }
    }

    @GetMapping("/search/{productName}")
    public ResponseResult get(@PathVariable String productName) throws UnsupportedEncodingException {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("seckill_product_name", URLDecoder.decode(productName,"utf-8"));
        return ResponseResult.success("200","成功获取产品",productMapper.selectOne(wrapper));
    }

    @GetMapping("/all")
    public ResponseResult getAll() {
        return ResponseResult.success(productMapper.selectList(null));
    }

    @GetMapping("/page/{current}/{size}")
    public ResponseResult getPage(@PathVariable String size, @PathVariable String current) {
        Page<Product> page = new Page<Product>(Long.parseLong(current), Long.parseLong(size));
        return ResponseResult.success(productMapper.selectPage(page,null));
    }
}
