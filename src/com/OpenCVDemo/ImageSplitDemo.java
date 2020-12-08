package com.OpenCVDemo;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImageSplitDemo {

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
        Mat src = Imgcodecs.imread("C:\\Users\\Luffe\\Pictures\\test\\ImageSplit\\1.jpg");//待匹配图片

        HighGui.imshow("原图", src);
        HighGui.waitKey();

        //把白色的背景转换成黑色更容易处理
        int sign = 230;
        for( int x = 0; x < src.rows(); x++ ) {
            for( int y = 0; y < src.cols(); y++ ) {
                if ( src.get(x,y)[0] > sign && src.get(x,y)[1] > sign && src.get(x,y)[2] > sign) {
                    src.put(x, y,0,0,0);
                }
            }
        }

        //拉普拉斯边缘检测 https://blog.csdn.net/ren365880/article/details/103933835
        Mat kernMat = new Mat(3,3,CvType.CV_8SC1);
        kernMat.put(0, 0,0,1,0,1,-4,1,0,1,0);
        Mat imgLaplacian = new Mat();
        Mat sharp = src.clone();
        Imgproc.filter2D(sharp, imgLaplacian, CvType.CV_32F, kernMat);

        //图像格式转换
        src.convertTo(sharp, CvType.CV_32F);

        Mat imgResult = new Mat();
        /*
         * 计算两个数组或数组与标量之间的每个元素的差。 函数减法计算： 上面列表中的第一个函数可以替换为矩阵表达式：
         * dst = src1-src2;
         * dst -= src1;
         * 输入阵列和输出阵列都可以具有相同或不同的深度。例如，您可以减去8位无符号数组，并将差值存储在16位有符号数组中。输出数组的深度由dtype参数确定。
         * 在上面的第二种和第三种情况以及第一种情况下，当src1.depth（）==
         * src2.depth（）时，dtype可以设置为默认值-1。在这种情况下，输出数组将具有与输入数组相同的深度，无论是src1，src2还是两者。
         * 注意：当输出数组的深度为CV_32S时，不应用饱和度。在溢出的情况下，您甚至可能会得到错误符号的结果。
         * @param src1第一个输入数组或标量。
         * @param src2第二个输入数组或标量。
         * @param dst输出数组，其大小和通道数与要更改的输出数组的输入数组相同。
         */
        Core.subtract(sharp, imgLaplacian, imgResult);

        imgResult.convertTo(imgResult, CvType.CV_8UC3);
        imgLaplacian.convertTo(imgLaplacian, CvType.CV_8UC3);

        src = imgResult.clone();
        Mat bw = new Mat();
        //https://blog.csdn.net/ren365880/article/details/103869207
        Imgproc.cvtColor(src, bw, Imgproc.COLOR_BGR2GRAY);
        //阈值化 https://blog.csdn.net/ren365880/article/details/103927636
        Imgproc.threshold(bw, bw, 40, 255, Imgproc.THRESH_BINARY |Imgproc.THRESH_OTSU);

        Mat dist = new Mat();
        /*
         * 计算图像中每一个非零点距离离自己最近的零点的距离
         * @param src 8位单通道（二进制）源映像。
         * @param dst输出具有计算距离的图像。 它是大小与src相同的8位或32位浮点单通道图像。
         * @param distanceType距离类型，请参阅#DistanceTypes
         * @param maskSize距离变换蒙版的大小，请参见#DistanceTransformMasks。
         * 在＃DIST_L1或#DIST_C距离类型的情况下，该参数被强制为3，因为（3 * 3）掩码给出的结果与（5 * 5）相同或更大。
         * 函数的第一个变体和distanceType ==＃DIST_L1。
         */
        Imgproc.distanceTransform(bw, dist, Imgproc.CV_DIST_L2, 3);
        /*
         * 标准化数组的范数或值范围。
         * @param src输入数组。
         * @param dst输出数组，其大小与src相同。
         * @param alpha规范值，以进行范围归一化或范围下限。
         * @param beta范围上限边界范围归一化； 它不用于规范规范化。
         * @param norm_type归一化类型（请参阅cv :: NormTypes）。 通道数为src，深度= CV_MAT_DEPTH（dtype）。
         */
        Core.normalize(dist, dist, 0.0, 1.0, Core.NORM_MINMAX);
        //阈值化 https://blog.csdn.net/ren365880/article/details/103927636
        Imgproc.threshold(dist, dist, .4, 1., Imgproc.THRESH_BINARY);

        Mat kernel1 = Mat.ones(3, 3, CvType.CV_8UC1);

        /*
         * 通过使用特定的结构元素来放大图像。 该函数使用指定的结构化元素来扩展源图像，该结构化元素确定在其上获取最大值的像素邻域的形状： 该功能支持就地模式。
         * 可以进行几次（迭代）扩张。 在多通道图像的情况下，每个通道都是独立处理的。
         * @param src输入图像； 通道数可以是任意的，但深度应为CV_8U，CV_16U，CV_16S，CV_32F或CV_64F之一。
         * @param dst输出图像的大小和类型与src相同。
         * @param用于扩展的内核结构元素； 如果elemenat = Mat（），则使用3 x 3的矩形结构元素。
         * 可以使用#getStructuringElement锚创建锚点，该锚点位于元素中心。
         */
        Imgproc.dilate(dist, dist, kernel1);

        Mat dist_8u = new Mat();
        dist.convertTo(dist_8u, CvType.CV_8U);
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();;
        Mat hierarchy = new Mat();

        // 查找二进制图像中的轮廓。 https://blog.csdn.net/ren365880/article/details/103970023
        Imgproc.findContours(dist_8u, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        Mat markers = Mat.zeros(dist.size(), CvType.CV_32SC1);
        //绘制轮廓或填充轮廓 https://blog.csdn.net/ren365880/article/details/103970023
        for (int i = 0; i < contours.size(); i++) {
            Imgproc.drawContours(markers, contours, i, new Scalar(i+1.0), -1);
        }

        //绘制一个圆 https://blog.csdn.net/ren365880/article/details/103976504
        Imgproc.circle(markers, new Point(5,5), 3,new Scalar(255,255,255), -1);

        /*
         * 使用分水岭算法执行基于标记的图像分割。 该函数实现了分水岭的一种变体，基于非参数标记的分割算法。
         * 在将图像传递给函数之前，您必须使用正（<0）索引在图像标记中大致勾勒出所需区域。因此，每个区域都表示为一个或多个具有像素值1、2、3等的连接组件。可以使用
         * #findContours和#drawContours从二进制掩码中检索此类标记（请参阅watershed.cpp演示）。标记是未来图像区域的“种子”。
         * 标记中与轮廓区域的关系未知并应由算法定义的所有其他像素应设置为0。在函数输出中，标记中的每个像素在区域之间的边界处设置为“种子”分量的值或-1。
         * 注意：任何两个相邻连接的组件都不一定要用分水岭边界（-1的像素）分隔；例如，他们可以在传递给该功能的初始标记图像中相互触摸。
         * @param image输入8位3通道图像。
         * @param markers输入/输出标记的32位单通道图像（图）。它的大小应与image相同。
         */
        Imgproc.watershed(src, markers);
        Mat mark = Mat.zeros(markers.size(), CvType.CV_8UC1);
        markers.convertTo(mark, CvType.CV_8UC1);

        /*
         * 反转数组的每一位。 函数cv :: bitwise_not计算输入数组的每个元素的按位求反：
         * 对于浮点输入数组，其特定于机器的位表示形式（通常符合IEEE754）用于操作。 在多通道阵列的情况下，每个通道都是独立处理的。
         * @param src输入数组。
         * @param dst输出数组，其大小和类型与输入数组相同。 指定要更改的输出数组的元素。
         */
        Core.bitwise_not(mark, mark);

        Mat dst = Mat.zeros(markers.size(), CvType.CV_8UC3);

        for (int i = 0; i < markers.rows(); i++){
            for (int j = 0; j < markers.cols(); j++){
                int index = (int) markers.get(i, j)[0];
                if (index > 0 && index <= contours.size()) {
                    dst.put(i,j, index%90,90,90);
                }else if(index < 0){
                    dst.put(i,j, 0,0,255);
                }else{
                    dst.put(i,j, 0,0,0);
                }
            }
        }
        HighGui.imshow("dst", dst);
        HighGui.waitKey();
    }
}

