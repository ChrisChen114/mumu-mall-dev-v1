package com.imooc.malldevv1.service.impl;

import com.imooc.malldevv1.exception.ImoocMallException;
import com.imooc.malldevv1.exception.ImoocMallExceptionEnum;
import com.imooc.malldevv1.model.dao.ProductMapper;
import com.imooc.malldevv1.model.pojo.Product;
import com.imooc.malldevv1.model.request.AddProductReq;
import com.imooc.malldevv1.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 商品服务ProductService实现类
 * 2022-08-23 创建
 *
 * 增加@Service注解
 */
@Service
public class ProductServiceImpl implements ProductService {
    //注入商品持久层映射器
    @Autowired
    ProductMapper productMapper;

    //后台增加商品
    //2022-08-23 创建
    @Override
    public void addProduct(AddProductReq addProductReq){
        //s2,新增商品名字不能重复，即重名情况进行检测，通过select查询并校验
        //从库中查找商品是否存在
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if (productOld != null){
            //商品已存在，抛出异常
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }

        //s3, BeanUtils拷贝Product属性
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq, product);
//        product.setCreateTime(new Date());
//        product.setUpdateTime(new Date());
        //s4, 插入商品到库中
        //用insert还是insertSelective？之前使用过insert，但是视频里用的是insertSelective
        int count = productMapper.insertSelective(product);
        if (count == 0){
            //插入失败
            throw new ImoocMallException(ImoocMallExceptionEnum.CREATED_FAILED);
        }
    }




}
