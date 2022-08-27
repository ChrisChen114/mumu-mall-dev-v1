package com.imooc.malldevv1.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.malldevv1.common.Constant;
import com.imooc.malldevv1.exception.ImoocMallException;
import com.imooc.malldevv1.exception.ImoocMallExceptionEnum;
import com.imooc.malldevv1.model.dao.ProductMapper;
import com.imooc.malldevv1.model.pojo.Product;
import com.imooc.malldevv1.model.query.ProductListQuery;
import com.imooc.malldevv1.model.request.AddProductReq;
import com.imooc.malldevv1.model.request.ProductListReq;
import com.imooc.malldevv1.model.request.UpdateProductReq;
import com.imooc.malldevv1.model.vo.CategoryVO;
import com.imooc.malldevv1.service.CategoryService;
import com.imooc.malldevv1.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSessionIdListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 商品服务ProductService实现类
 * Controller层区分前台和后台，但Service层不做区分，因为Service层的背后逻辑很多是一致的
 * 2022-08-23 创建
 *
 * 增加@Service注解
 */
@Service
public class ProductServiceImpl implements ProductService {
    //注入商品持久层映射器
    @Autowired
    ProductMapper productMapper;

    @Autowired
    CategoryService categoryService;

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

    //更新商品
    //2022-08-24 创建
    @Override
    public void updateProduct(UpdateProductReq updateProductReq){
        //s2，拷贝updateProductReq属性到product中
        Product product = new Product();
        BeanUtils.copyProperties(updateProductReq, product);
        product.setUpdateTime(new Date());

        //s3，更新时候，尤其名字不能与别人冲突，因此需要到库里先查询
        //查询的前提是name不能为空
        if (product.getName() != null) {
            Product productOld = productMapper.selectByName(product.getName());
            if (productOld != null ) {
                //这块的判断条件，没想到****************
                //s3-1,名字不为空（同名）、ID不一样，此时需要给拒绝掉，不能更新，抛出异常
                //名字不为空、ID不一样，它的意思是库中已有一条商品记录，但是库里ID和传入不一样，根据传入ID进行更新时，就会有两条相同的商品名字，是不符合要求的.
                //查到的productOld的ID不能等于入参传进来的updateProductReq的ID
                //究其根本，就是要保证商品不能重名，但是代码上需要复合判断
                if (productOld != null && !productOld.getId().equals(product.getId())) {
                    //存在冲突，抛出异常
                    throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
                }
            }
        }

        //s4, 根据主键进行选择性更新
        int count = productMapper.updateByPrimaryKeySelective(product);
        if (count == 0) {
            //更新失败
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);

        }
    }


    //删除商品
    //2022-08-24 创建
    @Override
    public void deleteProduct(Integer id){
        //s2,检查待删除商品是否存在
        Product productOld = productMapper.selectByPrimaryKey(id);
        if (productOld == null){
            //当前找不到这个商品记录
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        }
        //s3,商品存在，则删除
        int count = productMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            //删除失败
            throw new ImoocMallException(ImoocMallExceptionEnum.DELETE_FAILED);
        }
    }

    //批量上下架商品
    //2022-08-24 创建
    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus){
        //第一种更新方案：在service层中，逐个调用更新上下架状态的update语句
        //s2,遍历所有待上下架的商品
//        for (int i = 0; i < ids.length; i++) {
//            //s3,检查当前待上下架商品是否存在
//            Product product = productMapper.selectByPrimaryKey(ids[i]);
//
//            //存在该商品
//            if (product != null){
//                //s4，更新当前商品的上下架状态
//                //s4-1，新增更新上下架状态的update语句 updateSellStatus
//                int count = productMapper.updateSellStatus(ids[i],sellStatus);
//                if (count == 0) {
//                    //更新失败
//                    throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
//                }
//            }
//        }


        //第二种更新方案：传入ids和sellStatus到productmapper中，在update语句中遍历更新上下架状态
        //来自视频中的方案
        int count = productMapper.updateSellStatus1(ids,sellStatus);
        if (count != ids.length) {
            //更新失败
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }


    //后台商品列表
    //2022-08-24 创建
    @Override
    public PageInfo listProductForAdmin(Integer pageNum, Integer pageSize){
        //s2,配置PageHelper的startPage
        //此处还未设置排序方式？什么情况下设置呢？
        PageHelper.startPage(pageNum, pageSize);

        //s3，查询所有商品，以list形式保存
        List<Product> productList = productMapper.selectListProductForAdmin();
        //s4,返回PageInfo
        PageInfo pageInfo = new PageInfo(productList);
        return pageInfo;
    }

    //前台相关模块
    //前台商品列表
    //2022-08-25 创建
    @Override
    public PageInfo listProductForCustomer(ProductListReq productListReq){
        //思路：复杂查询，通常是构建一个Query对象，专门用于查询的.
        //s2,构建ProductListQuery类，Query对象:
        //属性：关键字keyword;目录信息，往往是一个列表 List<Integer> categoryIds;
        ProductListQuery productListQuery = new ProductListQuery();
        
        //s3，根据关键字，进行搜索处理
        //s3-1,判断关键字是否为空;
        //因为在Controller中未对ProductListReq进行@Valid注解的使用，需要进行入参判空，对应技术点2
        if(!StringUtils.isEmpty(productListReq.getKeyword())){
            //s3-2,对应技术点2的加%通配符，达到的拼接效果就是： %keyword%
            //使用这样的一个keyword到数据库中进行查找
            String keyword = new StringBuilder().append("%").append(productListReq.getKeyword()).append("%").toString();
            //s3-3,设置查询关键字
            productListQuery.setKeyword(keyword);//设置搜索关键字的的查询
        }

        //S4，目录处理
        //视频PPT内容：对于查询目录的in处理：
        //1）目录处理：如果查某个目录下的商品，不仅是需要查出该目录下的，还要把所有子目录的所有商品都查出来，所以要拿到一个目录id的List
        //2）所以这里要拿到某一个目录Id下的所有子目录id的List

        //s4-1,判断入参目录id是否为空，即入参有没有传这个
        if (productListReq.getCategoryId() != null){
            //s4-2,从categoryService中的listCategoryForCustomer方法获取目录列表（当前所在的目录id号）
            List<CategoryVO> categoryVOList = categoryService.listCategoryForCustomer(productListReq.getCategoryId());
            //categoryVOList是一个树状结构（里面是一个递归结构），需要把它平铺开来，然后保存每个id号
            //s4-3,创建一个分类目录的id List
            ArrayList<Integer> categoryIds = new ArrayList<>();
            //s4-4,把当前要查询的目录加入，即请求中传入的id
            categoryIds.add(productListReq.getCategoryId());
            //s5,把当前目录下面的子目录Id查询出来，结果包含了所有的目录id
            getCategoryIds(categoryVOList,categoryIds);

            //5-1,加入productListQuery中
            productListQuery.setCategoryIds(categoryIds);//设置目录ids的查询

        }

        //s6，排序处理（请求中传入的排序属性）
        String orderBy = productListReq.getOrderBy();
        //判断排序方式是否存在
        //6-1,在Constant类中，增加一个set集合，判断是否包含入参的排序方式。
        if (Constant.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
            //存在，则传入请求参数中的pageNum,pageSize，和orderBy属性
            PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize(),orderBy);
        }else{
            //不存在不支持排序，则传入请求参数中的pageNum,pageSize
            PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize());
        }


        //s7，根据productListQuery的Query类，查询商品列表
        List<Product> productList = productMapper.selectList(productListQuery);

        //s8，返回pageInfo
        PageInfo pageInfo = new PageInfo(productList);
        return pageInfo;
        
    }

    //s5的具体
    //递归函数，categoryVOList是一个树状结构（里面是一个递归结构），需要把它平铺开来，然后保存每个id号
    //2022-08-26 修改
    private void getCategoryIds(List<CategoryVO> categoryVOList, ArrayList<Integer> categoryIds) {
        //敲itli == Iterate elements of java.util.List
//        for (int i = 0; i < categoryVOList.size(); i++) {
//            CategoryVO categoryVO =  categoryVOList.get(i);
//
//        }

        for (int i = 0; i < categoryVOList.size(); i++) {
            CategoryVO categoryVO = categoryVOList.get(i);
            //判断子节点是否为null
            if (categoryVO != null){
                //把当前这个目录加入
                categoryIds.add(categoryVO.getId());
                //继续进行子孙的递归查找
                getCategoryIds(categoryVO.getChildCategory(),categoryIds);
            }
        }
    }


    //前台商品详情
    //2022-08-25 创建
    @Override
    public Product detail(Integer id){
        //s2, 根据id查询商品
        Product product = productMapper.selectByPrimaryKey(id);
        //s3,返回一个pojo类型的product
        return product;

    }
}
