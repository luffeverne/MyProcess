package com.OpenCVDemo;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.FastFeatureDetector;
import org.opencv.features2d.Feature2D;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.HOGDescriptor;

public class WalkerDemo {

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
        Mat src = Imgcodecs.imread("C:\\Users\\Luffe\\Pictures\\test\\Waklker\\1.jpg");//待匹配图片

        HighGui.imshow("原图", src);
        HighGui.waitKey();

        Mat gary=new Mat();
        //图片转灰 https://blog.csdn.net/ren365880/article/details/103869207
        Imgproc.cvtColor(src, gary, Imgproc.COLOR_BGR2GRAY);
        /*
         * 使用默认参数创建HOG检测器。
         * 默认值（Size（64,128），Size（16,16），Size（8,8），Size（8,8），9）
         */
        HOGDescriptor hog=new HOGDescriptor();
        /*
         * 设置线性SVM分类器的系数。 线性SVM分类器的
         * @param svmdetector系数。
         * HOGDescriptor.getDefaultPeopleDetector()返回经过训练可进行人员检测的分类器的系数（对于64x128窗口）。
         */
        hog.setSVMDetector(HOGDescriptor.getDefaultPeopleDetector());

        MatOfRect rect=new MatOfRect();
        /*
         * 检测输入图像中不同大小的对象。 检测到的对象将作为矩形列表返回。
         * @param img类型CV_8U或CV_8UC3的矩阵，其中包含检测到对象的图像。
         * @param foundLocations矩形的向量，其中每个矩形都包含检测到的对象。
         * @param foundWeights向量，它将包含每个检测到的对象的置信度值。
         * @param hitThreshold要素与SVM分类平面之间距离的阈值，通常为0，应在检测器系数中指定
         *  （作为最后一个自由系数），但是如果省略自由系数（允许），则可以指定 在这里手动操作。
         * @param winStride窗口跨度。 它必须是跨步的倍数。
         * @param padding填充
         */
        hog.detectMultiScale(gary, rect, new MatOfDouble(),0,new Size(8,8),new Size(0,0));

        Rect[] rects = rect.toArray();

        for (int i = 0; i < rects.length; i++) {
            /*
             * 绘制一个简单的，粗的或实心的直角矩形。 函数cv :: rectangle绘制一个矩形轮廓或一个填充的矩形，其两个相对角为pt1和pt2。
             * @param img图片。
             * @param pt1矩形的顶点。
             * @param pt2与pt1相反的矩形的顶点。
             * @param color矩形的颜色或亮度（灰度图像）。
             * @param thickness组成矩形的线的粗细。 负值（如#FILLED）表示该函数必须绘制一个填充的矩形。
             * @param lineType线的类型。 请参阅https://blog.csdn.net/ren365880/article/details/103952856
             */
            Imgproc.rectangle(src, new Point(rects[i].x,rects[i].y), new Point(rects[i].x+rects[i].width,rects[i].y+rects[i].height), new Scalar(0,0,255), 2, Imgproc.LINE_AA);
        }

        HighGui.imshow("行人检测", src);
        HighGui.waitKey();

    }


}

