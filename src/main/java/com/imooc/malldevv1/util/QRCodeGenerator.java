package com.imooc.malldevv1.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * 描述：  生成二维码工具
 * 2022-08-31 创建
 * 来自视频8-9 二维码接口开发
 * S4步骤 - 对应qrcode模块中的
 */

public class QRCodeGenerator {
    //

    /**
     *
     * 设置成静态static方法，外部就可以轻松调用
     * @param text 要加密的内容
     * @param width
     * @param height
     * @param filePath
     * @throws WriterException
     */
    public static void generateQRCodeImage(String text,Integer width,Integer height,String filePath) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        //调用encode方法，返回一个bit位矩阵
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        Path path = FileSystems.getDefault().getPath(filePath);
        //二维码格式用png格式比较合适
        MatrixToImageWriter.writeToPath(bitMatrix,"PNG",path);
    }

    public static void main(String[] args) {
        try {
            //path:传入的是全路径
            generateQRCodeImage("hello world",350,350, "D:/Programs/java/javaweb/imooc-mall-prepare-static/QRTest831.png");

            //https://tool.oschina.net/qr  使用这个地址，查看二维码图片内容，表达的是什么含义
            //解码结果：hello world
        } catch (WriterException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
