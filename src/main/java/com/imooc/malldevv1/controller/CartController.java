package com.imooc.malldevv1.controller;

import com.imooc.malldevv1.common.ApiRestResponse;
import com.imooc.malldevv1.filter.UserFilter;
import com.imooc.malldevv1.model.vo.CartVO;
import com.imooc.malldevv1.service.CartService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：      购物车CartController模块
 * 2022-08-26 创建
 * 购物车列表
 * 添加商品到购物车
 * 更新购物车某个商品的数量
 * 删除购物车的某个商品
 * 选中/不选中购物车的某个商品
 * 全选/全不选购物车的某个商品
 *
 */


@RestController      //@RestController = @Controller + @ResponseBody，两者相等
@RequestMapping("/cart")  //增加映射地址前缀
public class CartController {
    @Autowired
    CartService cartService;

    //购物车列表
    //2022-08-26 创建
    //技术点：1）在CartMapper.xml的SQL语句中，需要用到left join左连接表，即cart表left join product表 .本质就是要通过两个表的连接，组装成CartVO，返回给前端
    //补充，left join：保留左表所有的记录，然后跟右表去连接；如果右表有符合条件的记录，就直接连接，没有的话，用null值连接
    //有难度
    @GetMapping("/list")
    @ApiOperation("购物车列表")   //增加这个Swagger注解，即可生成swagger API文档
    public ApiRestResponse list(){
        //s0，内部获取用户ID，防止横向越权（已经在UserFilter中实现拦截）
        //note：横向越权：用户与用户之间的越权；纵向越权：普通用户处理管理员才拥有的权限

        //s1,传入userid，进入Service层
        List<CartVO> cartVOList = cartService.list(UserFilter.currentUser.getId());

        return ApiRestResponse.success(cartVOList);
    }


    //添加商品到购物车
    //2022-08-26 创建
    //技术点：1）通过UserFilter进行过滤拦截处理，实现用户登录校验；2）兜底的异常处理，对于id商品是否存在和上下架和库存数量的校验；3）根据用户id和商品id，查询商品；4)判断这个商品在不在购物车.
    //业务流程：添加商品到购物车---> 商品是否在售、是否有库存---->否--->提示用户
    //..................................................是--->该商品之前就在购物车里？--->否--->添加新商品
    //...............................................................................是--->原有基础上添加数量
    //有难度
    @PostMapping("/add")
    @ApiOperation("添加商品到购物车")   //增加这个Swagger注解，即可生成swagger API文档
    public ApiRestResponse add(@RequestParam("productId") Integer productId,@RequestParam("count") Integer count){
        //要求用户先登录，才能得到购物车，需要得到用户信息
        //一种是通过HttpSession获取用户信息，第二是建立一个Filter，建立一个通用的逻辑获取用户信息
        //s0,已通过UserFilter进行过滤拦截处理，实现用户登录校验

        //s1,传入userid、productId、count，进入Service层
        List<CartVO> cartVOList = cartService.add(UserFilter.currentUser.getId(), productId, count);
        //返回的是“购物车列表”
        return ApiRestResponse.success(cartVOList);
    }


    //更新购物车某个商品的数量
    //2022-08-26 创建
    //2022-08-28 编写
    //技术点：1）兜底的异常处理；2）根据userId和productId，查询到该商品；3）数量更新（数量并不是相加，而是直接更新数量）
    //productId商品ID，count数量
    @PostMapping("/update")
    @ApiOperation("更新购物车某个商品的数量")   //增加这个Swagger注解，即可生成swagger API文档
    public ApiRestResponse update(@RequestParam("productId") Integer productId,@RequestParam("count") Integer count){
        //s0,内部获取用户ID，防止横向越权（已经在UserFilter中实现拦截）
        //s1,传入productId和count，进入service层
        List<CartVO> cartVOList = cartService.update(UserFilter.currentUser.getId(),productId, count);

        //s6，返回的是“购物车列表”
        return ApiRestResponse.success(cartVOList);
    }


    //删除购物车的某个商品
    //2022-08-26 创建
    //2022-08-28 编写
    //技术点：1）params中，不能传入userId和cartId，否则可以删除别人的购物车
    //productId商品ID
    @PostMapping("/delete")
    @ApiOperation("删除购物车的某个商品")   //增加这个Swagger注解，即可生成swagger API文档
    public ApiRestResponse delete(@RequestParam("productId") Integer productId){
        //s0,内部获取用户ID，防止横向越权（已经在UserFilter中实现拦截）
        //s1,传入userId和productId，进入service层
        //params中，不能传入userId和cartId，否则可以删除别人的购物车（黑客推测等手法）
        List<CartVO> cartVOList = cartService.delete(UserFilter.currentUser.getId(),productId);
        //返回的是“购物车列表”
        return ApiRestResponse.success(cartVOList);
    }


    //选中/不选中购物车的某个商品
    //2022-08-26 创建
    //2022-08-28 编写
    //技术点：1）mapper.xml中selectOrNot实现复用，通过增加productId可选字段if判断
    //productId商品ID，selected: 0 是不选中，1 是选中
    @PostMapping("/select")
    @ApiOperation("选中/不选中购物车的某个商品")   //增加这个Swagger注解，即可生成swagger API文档
    public ApiRestResponse select(@RequestParam("productId") Integer productId,@RequestParam("selected")  Integer selected){
        //s0,内部获取用户ID，防止横向越权（已经在UserFilter中实现拦截）
        //s1,传入userId、productId和selected，进入service层
        List<CartVO> cartVOList = cartService.selectOrNot(UserFilter.currentUser.getId(),productId,selected);

        //s5，返回的是“购物车列表”
        return ApiRestResponse.success(cartVOList);
    }


    //全选/全不选购物车的某个商品
    //2022-08-26 创建
    //2022-08-28 编写
    //技术点：1）mapper.xml中selectOrNot实现复用，通过增加productId可选字段if判断；productId传入null，就会把userId下的所有商品进行选中/不选中
    //selected: 0 是不选中，1 是选中
    @PostMapping("/selectAll")
    @ApiOperation("全选/全不选购物车的某个商品")   //增加这个Swagger注解，即可生成swagger API文档
    public ApiRestResponse selectAll(@RequestParam("selected") Integer selected){
        //s0,内部获取用户ID，防止横向越权（已经在UserFilter中实现拦截）
        //s1,传入userId和selected，进入service层
        List<CartVO> cartVOList = cartService.selectAllOrNot(UserFilter.currentUser.getId(),selected);
        //返回的是“购物车列表”
        return ApiRestResponse.success(cartVOList);
    }



}
