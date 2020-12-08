package com.OpenCVDemo;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class DrawCircleCenterDemo {
    //静态代码加载动态链接库
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public void test() {
        //        Mat src = Imgcodecs.imread("C:\\Users\\Luffe\\Pictures\\test\\circles\\12.jpg");
        String path = "C:\\Users\\Luffe\\Pictures\\test\\circles\\12.jpg";
        Mat src = Imgcodecs.imread(path);
        Mat dst = new Mat();

        //将图像变灰
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2GRAY);

        //均值模糊
        Imgproc.blur(dst, dst, new Size(3, 3));

        // 将固定级别的阈值应用于每个数组元素。
        Imgproc.threshold(dst, dst, 200, 255, Imgproc.THRESH_BINARY);

        // 查找二进制图像中的轮廓

        List<MatOfPoint> list = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();
        // 查找轮廓，返回值放在list
        Imgproc.findContours(dst, list, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));

        List<MatOfPoint> approxCurvelList = new ArrayList<MatOfPoint>();
        List<Rect> rectList = new ArrayList<>();
        List<Point> centerList = new ArrayList<>();
        List<Integer> radiusList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            MatOfPoint2f approxCurve = new MatOfPoint2f();
            Point center = new Point();
            float[] radius = new float[10];

            //approxPolyDP用一条具有较少顶点的曲线或多边形最大化接近一条曲线或多边形，以使它们之间的距离小于或等于指定的精度。
            Imgproc.approxPolyDP(new MatOfPoint2f(list.get(i).toArray()), approxCurve, 3, true);
            approxCurvelList.add(new MatOfPoint(approxCurve.toArray()));

            // 计算点集或灰度图像的非零像素的有边界矩形，返回最小的垂直边界矩形
            Rect boundRect = Imgproc.boundingRect(approxCurve);
            rectList.add(boundRect);

            // 查找包含2D点集的最小面积的圆，使用迭代算法找到2D点集的最小封闭圆
            Imgproc.minEnclosingCircle(approxCurve, center, radius);

            centerList.add(center);
            radiusList.add((int)radius[0]);
        }

        for (int i = 0; i < list.size(); i++) {
            Scalar color = new Scalar(0, 220, 0);
            Scalar color2 = new Scalar(0, 0 , 220);

            //绘制轮廓
//            Imgproc.drawContours(src, approxCurvelList, i, color, 4, Imgproc.LINE_AA, new Mat(), 0, new Point());

            // 绘制一个简单的、粗的或实心的直角矩形
//            Imgproc.rectangle(src, rectList.get(i).tl(), rectList.get(i).br(), color, 1, Imgproc.LINE_AA, 0);

            // 绘制圆
//            Imgproc.circle(src, centerList.get(i), radiusList.get(i), color2, 1, Imgproc.LINE_AA, 0);

            // 绘制圆心
            Imgproc.circle(src, centerList.get(i), 1, color2, 1, Imgproc.LINE_AA);

            // 输出圆心点集
//            System.out.println(centerList.get(i));
            String centerX = String.format("%.1f", centerList.get(i).x);
            String centerY = String.format("%.1f", centerList.get(i).y);
            System.out.println("("+centerX+", "+centerY+")");

        }

        HighGui.imshow("边界框和圆", src);
        HighGui.waitKey();

        Imgcodecs.imwrite("C:\\Users\\Luffe\\Pictures\\test\\circles\\13.jpg", src);
    }

    public static void main(String[] args) {
        new DrawCircleCenterDemo().test();
    }
}
