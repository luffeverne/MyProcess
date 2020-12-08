package com.OpenCVDemo;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

public class CircleAndBorderPlusDemo {
   //静态代码加载动态链接库
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
//        Mat src = Imgcodecs.imread("C:\\Users\\Luffe\\Pictures\\test\\circles\\12.jpg");
        Mat src = Imgcodecs.imread("C:\\Users\\Luffe\\Pictures\\test\\circles\\ImageTest.tif");
        Mat dst = new Mat();

        //将图像变灰
        /**
         * 如果将#cvtColor与8位图像一起使用，转换将丢失一些信息。
         * 进行转化为灰度图，比如保存为了16位的图片，读取出来为8位
         */
        Imgproc.cvtColor(src, dst, Imgproc.COLOR_BGR2GRAY);

        //均值模糊
        /**
         * 均值模糊(降噪) 支持就地过滤（可以操作对象本身）
         * 使用标准的过滤器模糊图像。该函数使用一个内核（奇数的矩阵）来平滑模糊图像：
         * 调用blur（src，dst，ksize，anchor，borderType等效于boxFilter（src，dst，src.type（），anchor，true，borderType。
         * @param src输入图像； 它可以具有任意数量的通道，这些通道可以独立处理，但深度应为CV_8U，CV_16U，CV_16S，CV_32F或CV_64F。
         * @param dst输出图像的大小和类型与src相同。
         * @param ksize模糊内核大小。
         * @param锚点； 默认值Point（-1，-1）表示锚点位于内核中心。
         */
          Imgproc.blur(dst, dst, new Size(3, 3));

          // 将固定级别的阈值应用于每个数组元素。
        /**
         * @param src输入数组（多通道，8位或32位浮点）。
         * @param dst输出数组，其大小和类型与src具有相同的通道数。
         * @param 阈值。
         * @param 与#THRESH_BINARY和#THRESH_BINARY_INV阈值类型一起使用的最大值。
         * @param类型阈值类型
         * THRESH_BINARY = 0, 二进制阈值化 在运用该阈值类型的时候，先要选定一个特定的阈值量，比如：125，这样，新的阈值产生规则
         */
            Imgproc.threshold(dst, dst, 200, 255, Imgproc.THRESH_BINARY);

        // 查找二进制图像中的轮廓

        /**
         * 查找二进制图像中的轮廓。
         * 该函数使用CITE算法：Suzuki85从二进制图像检索轮廓。轮廓是用于形状分析以及对象检测和识别的有用工具。
         * @param image Source，一个8位单通道图像。非零像素被视为1。零像素保持为0，因此图像被视为binary。您可以使用＃compare，＃inRange，
         * ＃threshold，＃adaptiveThreshold，＃Canny等创建灰度或彩色以外的二进制图像。如果模式等于#RETR_CCOMP或#
         * RETR_FLOODFILL，则输入也可以是32位的整数图像（CV_32SC1）。
         * @param轮廓检测到的轮廓。每个轮廓都存储为点的向量。
         * @param层次结构可选的输出向量 包含有关图像拓扑的信息。它具有与轮廓数量一样多的元素。对于每个第i个轮廓轮廓[i]，元素等级[i] [0]，等级[i] [1]，等级[i]
         * [2]和等级[i][3]均设置为0-在相同的层次级别上，基于下一个和上一个轮廓的轮廓的索引，分别是第一个子轮廓和父轮廓。如果对于轮廓i，没有下一个，上一个，父级或嵌套的轮廓
         * ，则hierarchy [i]的相应元素将为负。
         * @param模式轮廓检索模式，请参见#RetrievalModes
         * RETR_EXTERNAL = 0, 只检测最外围轮廓，包含在外围轮廓内的内围轮廓被忽略；
         * RETR_LIST = 1,检测所有的轮廓，包括内围、外围轮廓，但是检测到的轮廓不建立等级关系，彼此之间独立，没有等级关系，这就意味着这个检索模式下不存在父轮廓或内嵌轮廓
         * RETR_CCOMP = 2,检测所有的轮廓，但所有轮廓只建立两个等级关系，外围为顶层，若外围内的内围轮廓还包含了其他的轮廓信息，则内围内的所有轮廓均归属于顶层；
         * RETR_TREE = 3,检测所有轮廓，所有轮廓建立一个等级树结构。外层轮廓包含内层轮廓，内层轮廓还可以继续包含内嵌轮廓
         * RETR_FLOODFILL = 4; 官方没定义 使用此参数需要把输入源转为CV_32SC1
         * Mat dst = new Mat();
         * edges.convertTo(dst,CvType.CV_32SC1);
         * @param方法轮廓近似方法，请参阅#ContourApproximationModes轮廓是从图像ROI中提取的，然后应在整个图像上下文中进行分析。
         * CHAIN_APPROX_NONE = 1,保存物体边界上所有连续的轮廓点到contours向量内；
         * CHAIN_APPROX_SIMPLE = 2,仅保存轮廓的拐点信息，把所有轮廓拐点处的点保存入contours向量内，拐点与拐点之间直线段上的信息点不予保留；
         * CHAIN_APPROX_TC89_L1 = 3,使用teh-Chinl chain 近似算法;
         * CHAIN_APPROX_TC89_KCOS = 4; 使用teh-Chinl chain 近似算法。
         */
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
            /**
             * 以指定的精度最大化接近多边形曲线。
             * 函数cv :: approxPolyDP用一条具有较少顶点的曲线或多边形最大化接近一条曲线或多边形，以使它们之间的距离小于或等于指定的精度。
             * 它使用Douglas-Peucker算法<http://en.wikipedia.org/wiki/Ramer-Douglas-Peucker_algorithm>
             * @param curve存储在std :: vector或Mat中的2D点的输入向量
             * @param roxCurve近似结果。 类型应与输入曲线的类型匹配。
             * @param epsilon指定近似精度的参数。 这是原始曲线与其近似值之间的最大距离。
             * @param 闭合如果为true，则近似曲线将闭合（其第一个和最后一个顶点已连接）。 否则，它不会关闭。
             */
            Imgproc.approxPolyDP(new MatOfPoint2f(list.get(i).toArray()), approxCurve, 3, true);
            approxCurvelList.add(new MatOfPoint(approxCurve.toArray()));

            /**
             * 计算点集或灰度图像的非零像素的右边界矩形。 该函数为指定的点集或灰度图像的非零像素计算并返回最小的垂直边界矩形。
             * @param数组输入存储在std :: vector或Mat中的灰度图像或2D点集。
             */
            Rect boundRect = Imgproc.boundingRect(approxCurve);
            rectList.add(boundRect);

            /**
             * 查找包含2D点集的最小面积的圆。 该函数使用迭代算法找到2D点集的最小封闭圆。
             * @param points 2D点的输入向量，存储在std :: vector <>或Mat中
             * @param center输出圆的中心。
             * @param radius输出圆的半径。
             */
            Imgproc.minEnclosingCircle(approxCurve, center, radius);
            centerList.add(center);
            radiusList.add((int)radius[0]);
        }

        for (int i = 0; i < list.size(); i++) {
            Scalar color = new Scalar(0, 220, 0);
            Scalar color2 = new Scalar(0, 0 , 220);

            /**
             * 绘制轮廓
             */
//            Imgproc.drawContours(src, approxCurvelList, i, color, 4, Imgproc.LINE_AA, new Mat(), 0, new Point());
            /**
             * 绘制一个简单的，粗的或实心的直角矩形。 函数cv :: rectangle绘制一个矩形轮廓或一个填充的矩形，其两个相对角为pt1和pt2。
             * @param img图片。
             * @param pt1矩形的顶点。
             * @param pt2与pt1相反的矩形的顶点。
             * @param color矩形的颜色或亮度（灰度图像）。
             * @param thickness组成矩形的线的粗细。 负值（如#FILLED）表示该函数必须绘制一个填充的矩形。
             * @param lineType线的类型。 请参阅https://blog.csdn.net/ren365880/article/details/103952856
             * 			 *  LINE_4 = 4, 下一个点和上一个点边相连（没有角了），这样就消除了8联通法线断裂的瑕疵
             * 			 *  LINE_8 = 8, 下一个点连接上一个点的边或角
             * 			 *  LINE_AA = 16; 边缘像素采用高斯滤波，抗锯齿
             * @param shift点坐标中的小数位数。
             */
//            Imgproc.rectangle(src, rectList.get(i).tl(), rectList.get(i).br(), color, 1, Imgproc.LINE_AA, 0);


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
//            Imgproc.circle(src, centerList.get(i), radiusList.get(i), color2, 1, Imgproc.LINE_AA, 0);

            /*int
               绘制圆心int
            * */
            Imgproc.circle(src, centerList.get(i), 1, color2, 1, Imgproc.LINE_AA);
//            Imgproc.circle(src, new Point((int)date[0],(int)date[1]), 2, new Scalar(0, 0, 255),2,Imgproc.LINE_AA);//圆心
            System.out.println(centerList.get(i));
        }

        HighGui.imshow("边界框和圆", src);
        HighGui.waitKey();
    }
}

