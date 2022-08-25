package com.imooc.malldevv1.controller;

import com.imooc.malldevv1.common.ApiRestResponse;
import com.imooc.malldevv1.common.Constant;
import com.imooc.malldevv1.exception.ImoocMallException;
import com.imooc.malldevv1.exception.ImoocMallExceptionEnum;
import com.imooc.malldevv1.model.request.AddProductReq;
import com.imooc.malldevv1.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * 用于后台管理员的商品ProductAdminController
 * 2022-08-23 创建
 * <p>
 * 商品模块：
 * 后台管理：***************
 * 增加商品
 * 上传图片
 * 更新商品
 * 删除商品
 * 批量上下架商品
 * 商品列表（后台）
 * 前台管理：
 * 商品列表
 * 商品详情
 */

@RestController
@RequestMapping("/admin")   //增加请求地址的前缀
public class ProductAdminController {
    //注入商品ProductService
    @Autowired
    ProductService productService;


    //
    //后台管理：
    //
    //后台增加商品
    //2022-08-23 创建
    //技术点：1） 新建添加商品的一个请求类AddProductReq，用于接收Body中的请求参数
    //2）
    @ApiOperation("后台增加商品")   //增加这个Swagger注解，即可生成swagger API文档
    @PostMapping("/product/add")
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq) {
        //前置自动处理：
        // @Valid注解，是对AddProductReq类中的属性进行校验
        //@RequestBody,这样body中的参数与AddProductReq进行绑定
        //检查有没有登录和校验是不是管理员，已通过过滤器包filter中的AdminFilter拦截并校验

        //S1，进入Service层中，执行增加商品操作
        productService.addProduct(addProductReq);

        //返回："data": null
        return ApiRestResponse.success();
    }


    //上传图片
    //2022-08-23 创建
    //技术点：1）UUID生成文件名；2）把含地址的全路径返回给前端；3）配置映射地址。
    //补充：upload整个代码均是在Controller中，可以把相关业务代码放到service层中
    @ApiOperation("后台商品上传图片")
    @PostMapping("/upload/file")
    public ApiRestResponse upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) {
        //s0, 传入HttpServletRequest，用于在图片地址中保存地址，比如url、ip等
        //s0, 传入MultipartFile对象，需要用到注解@RequestParam，从请求参数中获取

        //s1,获取原始文件名，再拿到后缀；后缀肯定是不变的，后缀复用
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //s2，生成文件名称UUID
        UUID uuid = UUID.randomUUID();
        //s2-1，新的文件名
        String newFileName = uuid.toString() + suffixName;
        //s3，创建文件夹和含全路径的文件名
        //补充：在Constant常量类中增加FILE_UPLOAD_DIR，通过注解@Value就可以注入数据，数据从application.properties中配置和修改
        //具体见s3-1（在Constant中），s3-2（在application.properties），s3-3（在ImoocMallWebMvcConfig）
        //这三步对应：定义FILE_UPLOAD_DIR对象，注入数据，配置映射地址
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR);//文件夹
        File destFile = new File(Constant.FILE_UPLOAD_DIR + newFileName);//含全路径的目标文件

        //s4，判断文件夹是否存在，不存在则创建mkdir
        if (!fileDirectory.exists()) {
            //不存在，则创建目录make mkdir
            if (!fileDirectory.mkdir()) {
                //创建失败，则抛出异常
                throw new ImoocMallException(ImoocMallExceptionEnum.MAKE_DIR_FAILED);
            }
        }

        //s5，将传入的file文件写到目标文件destFile中
        //因为s3的destFile，此时还是一个空文件
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //把地址给返回回去
        //s6，返回："data": "http://127.0.0.1:8082/upload/b899f512-3467-4c71-8d2d-2d491b21f429.png"
        try {
            //路径中包括ip和端口号
            //注意：是getRequestURL(),不是.getRequestURI()，之前写错了
            //加""，是因为httpServletRequest.getRequestURL()对象为StringBuffer，而需要的是String，因此加""
            return ApiRestResponse.success(getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/images/" + newFileName);
        } catch (URISyntaxException e) {
            //s6-2
            return ApiRestResponse.error(ImoocMallExceptionEnum.UPLOAD_FAILED);
            //返回上传失败，不抛出异常
            //throw new RuntimeException(e);
        }
    }

    private URI getHost(URI uri) {
        //s6-1
        URI effectiveURI;
        try {
            effectiveURI = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
        } catch (URISyntaxException e) {
            effectiveURI = null;
            //设置为null，不抛出异常
//            throw new RuntimeException(e);
        }
        return effectiveURI;
    }


    //更新商品
    //2022-08-23 创建
    //技术点：1）
    @ApiOperation("后台更新商品")
    @PostMapping("/product/update")
    public ApiRestResponse update() {


        //返回："data": null
        return ApiRestResponse.success();
    }


    //删除商品
    //2022-08-23 创建
    //技术点：1）
    @ApiOperation("后台删除商品")
    @PostMapping("/product/delete")
    public ApiRestResponse delete() {


        //返回："data": null
        return ApiRestResponse.success();
    }


    //批量上下架商品
    //2022-08-23 创建
    //技术点：1）
    @ApiOperation("后台批量上下架商品")
    @PostMapping("/product/batchUpdateSellStatus")
    public ApiRestResponse batchUpdateSellStatus() {


        //返回："data": null
        return ApiRestResponse.success();
    }


    //商品列表（后台）
    //2022-08-23 创建
    //技术点：1）
    @ApiOperation("后台删除商品")
    @GetMapping("/product/list")
    public ApiRestResponse list() {


        //返回："data": {"total":20,"list":[{....}],....
        return ApiRestResponse.success();
    }


}
