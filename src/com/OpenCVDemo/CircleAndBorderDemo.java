package com.OpenCVDemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class CircleAndBorderDemo {

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

        Mat src = Imgcodecs.imread("C:\\Users\\Luffe\\Pictures\\test\\circles\\circle.jpg");//待匹配图片
//        Mat src = Imgcodecs.imread("C:\\Users\\Luffe\\Pictures\\test\\circles\\ImageTest.tif");//待匹配图片
        Mat dst = new Mat();
        //将图像从一种颜色空间转换为另一种颜色空间 https://blog.csdn.net/ren365880/article/details/103869207
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2GRAY);
        //均值模糊 https://blog.csdn.net/ren365880/article/details/103878059
        Imgproc.blur( dst, dst, new Size(3,3) );

        HighGui.imshow("原图", src);
        HighGui.waitKey();

        /* 将固定级别的阈值应用于每个数组元素。
         * https://blog.csdn.net/ren365880/article/details/103927636
         */
        Imgproc.threshold(dst, dst, 200, 255, Imgproc.THRESH_BINARY);

        /*
         * 查找二进制图像中的轮廓
         * https://blog.csdn.net/ren365880/article/details/103970023
         */
        List<MatOfPoint> list = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(dst, list, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE,new Point(0, 0));

        List<MatOfPoint> approxCurveList = new ArrayList<MatOfPoint>();
        List<Rect> rectList = new ArrayList<Rect>();
        List<Point> centerList = new ArrayList<Point>();
        List<Integer> radiusList = new ArrayList<Integer>();
        for(int i=0,size = list.size();i<size;i++) {

            MatOfPoint2f approxCurve = new MatOfPoint2f();
            Point center = new Point();
            float[] radius = new float[10];

            /*
             * 以指定的精度最大化接近多边形曲线。
             * 函数cv :: approxPolyDP用一条具有较少顶点的曲线或多边形最大化接近一条曲线或多边形，以使它们之间的距离小于或等于指定的精度。
             * 它使用Douglas-Peucker算法<http://en.wikipedia.org/wiki/Ramer-Douglas-Peucker_algorithm>
             * @param curve存储在std :: vector或Mat中的2D点的输入向量
             * @param roxCurve近似结果。 类型应与输入曲线的类型匹配。
             * @param epsilon指定近似精度的参数。 这是原始曲线与其近似值之间的最大距离。
             * @param 闭合如果为true，则近似曲线将闭合（其第一个和最后一个顶点已连接）。 否则，它不会关闭。
             */
            Imgproc.approxPolyDP( new MatOfPoint2f(list.get(i).toArray()), approxCurve, 3, true );
            approxCurveList.add(new MatOfPoint(approxCurve.toArray()));
            /*
             * 计算点集或灰度图像的非零像素的右边界矩形。 该函数为指定的点集或灰度图像的非零像素计算并返回最小的垂直边界矩形。
             * @param数组输入存储在std :: vector或Mat中的灰度图像或2D点集。
             */
            Rect boundRect = Imgproc.boundingRect(approxCurve);
            rectList.add(boundRect);
            /*
             * 查找包含2D点集的最小面积的圆。 该函数使用迭代算法找到2D点集的最小封闭圆。
             * @param points 2D点的输入向量，存储在std :: vector <>或Mat中
             * @param center输出圆的中心。
             * @param radius输出圆的半径。
             */
            Imgproc.minEnclosingCircle(approxCurve, center, radius);
            centerList.add(center);
            radiusList.add((int)radius[0]);
        }

        for(int i=0,size = list.size();i<size;i++) {

            Scalar color = new Scalar(0,220,0);
            Scalar color2 = new Scalar(0,0,220);

            //https://blog.csdn.net/ren365880/article/details/103970023
            Imgproc.drawContours(src, approxCurveList, i, color, 1, Imgproc.LINE_AA,new Mat(),0,new Point());

            /*
             * 绘制一个简单的，粗的或实心的直角矩形。 函数cv :: rectangle绘制一个矩形轮廓或一个填充的矩形，其两个相对角为pt1和pt2。
             * @param img图片。
             * @param pt1矩形的顶点。
             * @param pt2与pt1相反的矩形的顶点。
             * @param color矩形的颜色或亮度（灰度图像）。
             * @param thickness组成矩形的线的粗细。 负值（如#FILLED）表示该函数必须绘制一个填充的矩形。
             * @param lineType线的类型。 请参阅https://blog.csdn.net/ren365880/article/details/103952856
             * @param shift点坐标中的小数位数。
             */
            Imgproc.rectangle( src, rectList.get(i).tl(), rectList.get(i).br(), color, 2, Imgproc.LINE_AA, 0 );

            /*
             * 画一个圆。 函数cv :: circle绘制具有给定中心和半径的简单或实心圆。
             * @param img绘制圆的图像。
             * @param center圆心。
             * @param radius圆的半径。
             * @param color圆形颜色。
             * @param thickness圆轮廓的宽度（如果为正）。 负值（如#FILLED）表示要绘制一个实心圆。
             * @param lineType圆边界的类型。 请参阅https://blog.csdn.net/ren365880/article/details/103952856
             * @param shift中心坐标和半径值中的小数位数。
             */
            Imgproc.circle( src, centerList.get(i), radiusList.get(i), color2, 2, Imgproc.LINE_AA, 0 );
        }

        HighGui.imshow("边界框和圆", src);
        HighGui.waitKey();
    }
}

