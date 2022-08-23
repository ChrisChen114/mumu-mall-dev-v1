package com.imooc.malldevv1.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.malldevv1.common.ApiRestResponse;
import com.imooc.malldevv1.common.Constant;
import com.imooc.malldevv1.exception.ImoocMallException;
import com.imooc.malldevv1.exception.ImoocMallExceptionEnum;
import com.imooc.malldevv1.model.pojo.Category;
import com.imooc.malldevv1.model.pojo.User;
import com.imooc.malldevv1.model.request.AddCategoryReq;
import com.imooc.malldevv1.model.request.UpdateCategoryReq;
import com.imooc.malldevv1.service.CategoryService;
import com.imooc.malldevv1.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * 描述：      商品分类模块
 * 2022-08-21创建
 * 功能包括：
 * 后台管理：增加目录分类
 * 后台管理：更新目录分类
 * 后台管理：删除目录分类
 * 后台管理：目录列表（平铺）
 * 前台管理：目录列表（递归）
 *
 */
@RestController
//@RestController = @Controller + @ResponseBody，两者相等
//@ResponseBody注解用于返回JSON数据格式
//@RequestMapping("/admin")  如果全是前台或者全是后台，可以添加这样一个映射前缀
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    //调用checkAdminRole()时用到
    @Autowired
    UserService userService;


    //后台管理：增加目录分类
    //AddCategoryReq类是用于接收请求参数中Body数据
    //@Valid注解，是对AddCategoryReq类中的属性进行校验
    //@RequestBody注解，用于将请求中Body参数映射到AddCategoryReq类的属性中，Spring就可以从Body中获取字段，
    //如body: {"name":"食品","type":1,"parentId":0,"orderNum":1}
    //不能接受的是String p1,String p2,String p3,String p4...这样的传入方式
    @PostMapping("/admin/category/add")
    @ApiOperation("后台管理：增加目录分类")  //增加这个Swagger注解，即可生成swagger API文档
    public ApiRestResponse addCategory(HttpSession session, @Valid  @RequestBody AddCategoryReq addCategoryReq) throws ImoocMallException {
        //C0 校验addCategoryReq的属性，有两种方案
        //第一种：多个if的判断
        if (addCategoryReq.getName() == null){
            return ApiRestResponse.error(ImoocMallExceptionEnum.PARA_NOT_NULL);
        }//其他if....
        //第二种：通过@Valid注解，在请求类AddCategoryReq中的属性上进行校验

        //C1. 检查有没有登录
        //从session中获取用户信息；需要传入Constant中定义的用户常量名
        User currentUser = (User)session.getAttribute(Constant.IMOOC_MALL_DEV_V1_USER);
        //判断是不是为null
        if (currentUser == null){
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);//
        }

        //C2. 校验是不是管理员
        //到Service层（此处是UserService）中，检查角色role是否等于2
        if(userService.checkAdminRole(currentUser)){
            //是管理员，
            //C3. 执行新增目录操作
            categoryService.add(addCategoryReq);
            //无需返回任何信息
            return ApiRestResponse.success();
        }else{
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN);
        }


    }


    //后台管理：更新目录分类
    //@RequestBody,用于将请求中Body参数映射到UpdateCategoryReq类的属性中，Spring就可以从Body中获取字段
    //@Valid注解，是对UpdateCategoryReq类中的属性进行校验
    //s0,新建UpdateCategoryReq请求参数类，作为入参放入updateCategory。记得增加@Valid和@RequestBody注解
    @PostMapping("/admin/category/update")
    @ApiOperation("后台更新目录分类")
    public ApiRestResponse updateCategory(HttpSession session,@Valid @RequestBody UpdateCategoryReq updateCategoryReq){
        //s1, 有没有登录，需校验
        User currentUser = (User) session.getAttribute(Constant.IMOOC_MALL_DEV_V1_USER);
        if (currentUser == null){
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_LOGIN);
        }
        //s2, 校验是不是管理员
        if (!userService.checkAdminRole(currentUser)){
            //不是管理员，抛出异常，退出
            return ApiRestResponse.error(ImoocMallExceptionEnum.NEED_ADMIN);
        }else{
            //是管理员，执行下面操作
            //s3,拷贝属性到Category对象
            Category category = new Category();
            BeanUtils.copyProperties(updateCategoryReq,category);
            //s4,到service层中执行选择性更新操作
            categoryService.update(category);
        }
        return ApiRestResponse.success();
    }



    //后台管理：删除目录分类
    //2022-08-23 创建
    @PostMapping("/admin/category/delete")
    @ApiOperation("后台删除目录分类")
    public ApiRestResponse deleteCategory(@RequestParam("id") Integer id){
        //s1,传入id，进入Service层中
        categoryService.delete(id);
        return ApiRestResponse.success();
    }


    //后台管理：目录列表（平铺）
    //2022-08-23 编写
    //从请求中传入pageNum和pageSize
    //技术点：
    @PostMapping("/admin/category/list")
    @ApiOperation("目录列表（平铺）")
    public ApiRestResponse listCategoryForAdmin(@RequestParam("pageNum") Integer pageNum,@RequestParam("pageSize") Integer pageSize){
        //s1, 传入pageNum和pageSize，进入Service层.
        PageInfo pageInfo = categoryService.listForAdmin(pageNum,pageSize);
        return ApiRestResponse.success(pageInfo);
    }



    //前台管理：目录列表（递归）
    //请求地址 /category/list

}
