package com.OpenCVDemo;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class imgThresholdDemo {

    //静态代码块加载动态链接库
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {

        /*
         * IMREAD_UNCHANGED = -1 ：不进行转化，比如保存为了16位的图片，读取出来仍然为16位。
         * IMREAD_GRAYSCALE = 0 ：进行转化为灰度图，比如保存为了16位的图片，读取出来为8位，类型为CV_8UC1。
         * IMREAD_COLOR = 1 ：进行转化为三通道图像。
         * IMREAD_ANYDEPTH = 2 ：如果图像深度为16位则读出为16位，32位则读出为32位，其余的转化为8位。
         * IMREAD_ANYCOLOR = 4 ：图像以任何可能的颜色格式读取
         * IMREAD_LOAD_GDAL = 8 ：使用GDAL驱动读取文件，GDAL(Geospatial Data Abstraction
         * Library)是一个在X/MIT许可协议下的开源栅格空间数据转换库。它利用抽象数据模型来表达所支持的各种文件格式。
         *	它还有一系列命令行工具来进行数据转换和处理。
         */
        Mat src = Imgcodecs.imread("C:\\Users\\Luffe\\Pictures\\test\\1.png",0);

        Mat image = src.clone();

        HighGui.imshow("原图", src);
        HighGui.waitKey();

        // 直接阈值化
        imgThreshold(src);
        // 自定义阈值
        ImgAdaptiveThreshold(image);

    }

    //直接阈值化 给定一个输入数组和一个阈值，数组中的每个元素将根据其与阈值之间的大小发生相应的改变
    public static void imgThreshold(Mat src) {
        /*
         * 将固定级别的阈值应用于每个数组元素。
         * 该功能将固定级别的阈值应用于多通道阵列。该功能通常用于从灰度图像中获取双层（二进制）图像或去除噪声，即滤除具有太小或太大值的像素。
         * 该功能支持几种类型的阈值处理。它们由类型参数确定。
         * 此外，特殊值#THRESH_OTSU或#THRESH_TRIANGLE可以与上述值之一组合。在这些情况下，
         * 该函数使用Otsu或Triangle算法确定最佳阈值，并使用该阈值代替指定的阈值。 注意：目前，仅对8位单通道图像实现Otsu和Triangle方法。
         * @param src输入数组（多通道，8位或32位浮点）。
         * @param dst输出数组，其大小和类型与src具有相同的通道数。
         * @param 阈值。
         * @param 与#THRESH_BINARY和#THRESH_BINARY_INV阈值类型一起使用的最大值。
         * @param类型阈值类型，如下：
         * @param thresholdType阈值类型。
         * THRESH_BINARY = 0, 二进制阈值化 在运用该阈值类型的时候，先要选定一个特定的阈值量，比如：125，这样，新的阈值产生规则
         * 可以解释为大于125的像素点的灰度值设定为最大值(如8位灰度值最大为255)，灰度值小于125的像素点的灰度值设定为0。
         * THRESH_BINARY_INV = 1,反二进制阈值化 该阈值化与二进制阈值化相似，先选定一个特定的灰度值作为阈值，不过最后的设定值相反。
         * （在8位灰度图中，例如大于阈值的设定为0，而小于该阈值的设定为255）
         * THRESH_TRUNC = 2,截断阈值化 同样首先需要选定一个阈值，图像中大于该阈值的像素点被设定为该阈值，小于该阈值的保持不变。（
         * 例如：阈值选取为125，那小于125的阈值不改变，大于125的灰度值（230）的像素点就设定为该阈值）。
         * THRESH_TOZERO = 3, 阈值化为0 先选定一个阈值，然后对图像做如下处理：1 像素点的灰度值大于该阈值的不进行任何改变；2 像素点的灰度值小于该阈值的，其灰度值全部变为0。
         * THRESH_TOZERO_INV = 4,反阈值化为0 原理类似于0阈值，但是在对图像做处理的时候相反，即：像素点的灰度值小于该阈值的不进行任何改变，而大于该阈值的部分，其灰度值全部变为0。
         * THRESH_MASK = 7,
         * THRESH_OTSU = 8,
         * THRESH_TRIANGLE = 16;
         * THRESH_OTSU和THRESH_TRIANGLE是作为优化算法配合THRESH_BINARY、THRESH_BINARY_INV、THRESH_TRUNC、THRESH_TOZERO以及THRESH_TOZERO_INV来使用的。
         * 当使用了THRESH_OTSU和THRESH_TRIANGLE两个标志时，输入图像必须为单通道。
         */
        Imgproc.threshold(src, src, 125, 255, Imgproc.THRESH_BINARY);
        HighGui.imshow("直接阈值化", src);
        HighGui.waitKey();
    }

    //自适应阈值化能够根据图像不同区域亮度分布的，改变阈值
    public static void ImgAdaptiveThreshold(Mat src) {
        /*
         * 图像二值化（ Image Binarization）就是将图像上的像素点的灰度值设置为0或255，也就是将整个图像呈现出明显的黑白效果的过程。
         * 转换为二值类型 将自适应阈值应用于数组。  该功能可以就地处理图像。
         * @param src源8位单通道图像。
         * @param dst与src大小和类型相同的目标映像。
         * @param maxValue分配给满足条件的像素的非零值
         *
         * @paramAdaptiveMethod要使用的自适应阈值算法。
         * ADAPTIVE_THRESH_MEAN_C的计算方法是计算出领域的平均值再减去第七个参数double C的值。
         * ADAPTIVE_THRESH_GAUSSIAN_C的计算方法是计算出领域的高斯均值再减去第七个参数double C的值。
         *
         * @param thresholdType阈值类型。
         * THRESH_BINARY = 0, 二进制阈值化 在运用该阈值类型的时候，先要选定一个特定的阈值量，比如：125，这样，新的阈值产生规则
         * 可以解释为大于125的像素点的灰度值设定为最大值(如8位灰度值最大为255)，灰度值小于125的像素点的灰度值设定为0。
         * THRESH_BINARY_INV = 1,反二进制阈值化 该阈值化与二进制阈值化相似，先选定一个特定的灰度值作为阈值，不过最后的设定值相反。
         * （在8位灰度图中，例如大于阈值的设定为0，而小于该阈值的设定为255）
         * THRESH_TRUNC = 2,截断阈值化 同样首先需要选定一个阈值，图像中大于该阈值的像素点被设定为该阈值，小于该阈值的保持不变。（
         * 例如：阈值选取为125，那小于125的阈值不改变，大于125的灰度值（230）的像素点就设定为该阈值）。
         * THRESH_TOZERO = 3, 阈值化为0 先选定一个阈值，然后对图像做如下处理：1 像素点的灰度值大于该阈值的不进行任何改变；2 像素点的灰度值小于该阈值的，其灰度值全部变为0。
         * THRESH_TOZERO_INV = 4,反阈值化为0 原理类似于0阈值，但是在对图像做处理的时候相反，即：像素点的灰度值小于该阈值的不进行任何改变，而大于该阈值的部分，其灰度值全部变为0。
         * THRESH_MASK = 7,
         * THRESH_OTSU = 8,
         * THRESH_TRIANGLE = 16;
         * THRESH_OTSU和THRESH_TRIANGLE是作为优化算法配合THRESH_BINARY、THRESH_BINARY_INV、THRESH_TRUNC、THRESH_TOZERO以及THRESH_TOZERO_INV来使用的。
         * 当使用了THRESH_OTSU和THRESH_TRIANGLE两个标志时，输入图像必须为单通道。
         * @param blockSize用于计算像素阈值的像素邻域的大小：3、5、7，依此类推。
         * @param C从平均值或加权平均值中减去常数。 通常，它为正，但也可以为零或负。
         */
        Imgproc.adaptiveThreshold(src, src, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 3, 0);
        HighGui.imshow("自适应阈值", src);
        HighGui.waitKey();
    }


}
