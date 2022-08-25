package com.imooc.malldevv1.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.malldevv1.common.ApiRestResponse;
import com.imooc.malldevv1.common.Constant;
import com.imooc.malldevv1.exception.ImoocMallException;
import com.imooc.malldevv1.exception.ImoocMallExceptionEnum;
import com.imooc.malldevv1.model.request.AddProductReq;
import com.imooc.malldevv1.model.request.UpdateProductReq;
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
    //技术点：1）UUID生成文件名；2）把含地址的文件路径返回给前端；3）配置映射地址，让浏览器能显示图片。
    //补充：upload整个代码均是在Controller中，可以把相关业务代码放到service层中
    @ApiOperation("后台商品上传图片")
    @PostMapping("/upload/file")
    public ApiRestResponse upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) {
        //s0, 传入HttpServletRequest，用于在图片地址中包含host地址，比如url、ip等
        //s0, 传入MultipartFile对象，需要用到注解@RequestParam，从请求参数的body-formdata中获取

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

        //s6，把地址给返回前端
        //返回："data": "http://127.0.0.1:8082/upload/b899f512-3467-4c71-8d2d-2d491b21f429.png"
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
    //2022-08-24 编写
    //技术点：1）名字不为空（同名）、ID不一样，此时需要给拒绝掉，不能更新，抛出异常
    //2) 选择性更新updateByPrimaryKeySelective方法进行更新
    @ApiOperation("后台更新商品")
    @PostMapping("/product/update")
    public ApiRestResponse update(@Valid @RequestBody UpdateProductReq updateProductReq) {
        //note: 更新和新增商品两个模块合并，但是合并写法不可取，始终保持业务逻辑清晰、独立
        //note: 使用UpdateProductReq，而不是AddProductReq，因为更新商品时候需要传入id
        //s0，新建UpdateProductReq，id要传入而且是@notnull，其他属性要去掉@notnull

        //s1,传入updateProductReq，进入service层
        productService.updateProduct(updateProductReq);
        //返回："data": null
        return ApiRestResponse.success();
    }


    //删除商品
    //2022-08-23 创建
    //技术点：1）根据id进行删除
    //注意点：删除商品一般在业务上不是很推荐，可以更新，可以上下架，下架同样达到类似删除的效果（用户看不见即可）
    @ApiOperation("后台删除商品")
    @PostMapping("/product/delete")
    public ApiRestResponse delete(@RequestParam("id") Integer id) {

        //s1，传入id，进入service层
        productService.deleteProduct(id);
        //返回："data": null
        return ApiRestResponse.success();
    }


    //批量上下架商品
    //2022-08-23 创建
    //技术点：传入的ids是数组；在update语句中使用foreach循环（初次使用）
    //入参：1）传入的id是数组，多个id；2）传入sellStatus(0是下架，1是上架)
    @ApiOperation("后台批量上下架商品")
    @PostMapping("/product/batchUpdateSellStatus")
    public ApiRestResponse batchUpdateSellStatus(@RequestParam("ids") Integer[] ids,@RequestParam("sellStatus") Integer sellStatus) {

        //s1，传入ids和sellStatus，进入service层
        productService.batchUpdateSellStatus(ids,sellStatus);

        //返回："data": null
        return ApiRestResponse.success();
    }


    //商品列表（后台）
    //2022-08-23 创建
    //技术点：1）利用PageHelper和PageInfo
    @ApiOperation("后台商品列表")
    @GetMapping("/product/list")
    public ApiRestResponse list(@RequestParam("pageNum") Integer pageNum,@RequestParam("pageSize") Integer pageSize) {
        //s1，传入pageNum和pageSize，进入service层
        PageInfo pageInfo = productService.listProductForAdmin(pageNum, pageSize);

        //返回："data": {"total":20,"list":[{....}],....
        return ApiRestResponse.success(pageInfo);
    }


}
