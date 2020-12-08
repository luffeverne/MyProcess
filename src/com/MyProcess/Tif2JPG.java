package com.MyProcess;

import java.io.FileOutputStream;
import java.io.OutputStream;


import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;


import com.sun.media.jai.codec.BMPEncodeParam;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageEncoder;
import com.sun.media.jai.codec.JPEGEncodeParam;


/**
 * 可以实现jpg/tif/bmp 等图片之间格式得互相转换
 *
 */
public class Tif2JPG {


    public static void main(String[] args) throws Exception {

        String srcFile = "src/images/ImageTest.tif";
        String descFile = "src/images/test3.jpg";
        RenderedOp src = JAI.create("fileload", srcFile);
        OutputStream os = new FileOutputStream(descFile);
        JPEGEncodeParam param = new JPEGEncodeParam();
        //指定格式类型，jpg 属于 JPEG 类型
        ImageEncoder enc = ImageCodec.createImageEncoder("JPEG", os, param);
        enc.encode(src);
        os.close();
    }
}

