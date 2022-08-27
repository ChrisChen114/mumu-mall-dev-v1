package com.imooc.malldevv1.common;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 保存常量的类
 * 2022-08-23 修改
 *
 * @Component，其中一个作用是用于setFileUploadDir方法的
 */
@Component
public class Constant {
    //盐值,此处是写的固定的。当然也可以把盐值写入数据库
    public static final String SALT = "SNfha&ivAp,.@!]";

    //字符串里面的内容，可大写，可小写
    public static final String IMOOC_MALL_DEV_V1_USER = "IMOOC_MALL_DEV_V1_USER";


    //后台商品上传图片s3-1,
    //视频6-4 图片上传接口开发 中提到
    //往FILE_UPLOAD_DIR注入的话，可以通过增加@Value注解注入（从application.properties获取），然后在配置文件application.properties中增加或修改
    //注意不能写成final关键字
    //但是这样写会报nullpointerexception异常，这是第一个坑
    //原因如下：FILE_UPLOAD_DIR是静态变量，普通的方式注入不进去
    //解决方案：写一个set方法进行赋值
//    @Value("${file.upload.dir}")       这两行报错
//    public static String FILE_UPLOAD_DIR;
    //第二个坑：这个地址http://127.0.0.1:8083/images/c368eef4-2e71-4b13-9210-57f0fbc2d73b.jpg 在浏览器无法显示
    //解决方案：自定义静态资源映射目录：上传图片后回显，需配置SpringBootWebMvcConfig，进而达到静态资源到本地目录的映射

    public static String FILE_UPLOAD_DIR;
    //相当于通过set方法，将静态变量进行了一个赋值
    //此外如果想让Spring帮其注入，需要在Constant类外面增加@Component注解
    @Value("${file.upload.dir}")
    public void setFileUploadDir(String fileUploadDir){
        FILE_UPLOAD_DIR = fileUploadDir;
    }


    //前台商品列表：排序功能
    //枚举：order by，即要求所有的排序方式必须在我们的掌控之中，而不是前端传什么我们就去查什么
    //2022-08-26
    public interface ProductListOrderBy{
        //
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price desc","price asc");

    }

    //定义在售和停售标识
    //商品上架状态：0-下架，1-上架
    //2022-08-26 创建
    //视频7-3 添加商品接口开发
    public interface SaleStatus {
        //自己写法
//        public static final Integer SALE = 1;
//        public static final Integer NOT_SALE = 0;//下架

        Integer SALE = 1;
        Integer NOT_SALE = 0;//下架
    }

    //是否已勾选：0代表未勾选，1代表已勾选
    //2022-08-26 创建
    //视频7-3 添加商品接口开发
    public interface Cart {
        //自己写法
//        public static final Integer CHECKED = 1;
//        public static final Integer NOT_CHECKED = 0;
        Integer CHECKED = 1;
        Integer NOT_CHECKED = 0;
    }



}
