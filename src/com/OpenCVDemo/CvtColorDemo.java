package com.OpenCVDemo;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class CvtColorDemo {
    public static void main(String[] args) {
        /*	1.加载本地动态链接库
         * 	加载动态链接库有很多种方法，也可以写成static静态代码块。
         */
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        /*	2 读取测试图片
         *	imread：输入图片所在地址，返回Mat矩阵对象。
         *	Mat对象中保存着加载图片的大小、通道、像素数据等等与图片有关的数据
         */
        Mat image = Imgcodecs.imread("C:\\Users\\Luffe\\Pictures\\test\\circles\\4.jpg");
        /*	3. 图片转为单通道
         *  将图像从一种颜色空间转换为另一种颜色空间。
         *	该功能将输入图像从一种颜色空间转换为另一种颜色空间。
         *	在从RGB颜色空间转换的情况下，应明确指定通道的顺序（RGB或BGR）。
         *	请注意，OpenCV中的默认颜色格式通常称为RGB，但实际上是BGR（字节是相反的）。因此，
         *	标准（24位）彩色图像中的第一个字节将是8位蓝色分量，第二个字节将是绿色分量，第三个字节将是红色分量。
         *	第四，第五和第六个字节将是第二个像素（蓝色，然后是绿色，然后是红色），依此类推。
         *	R，G和B通道值的常规范围是：
         *	CV_8U图像为0至255
         *	CV_16U图像为0至65535
         *	CV_32F图像为0到1
         *	在线性变换的情况下，范围无关紧要。
         *	但是在进行非线性转换的情况下，应将输入的RGB图像规范化为适当的值范围，以获得正确的结果，
         *	例如，对于RGB到LUV（LUV色彩空间全称CIE 1976(L*,u*,v*) （也作CIELUV）色彩空间，L*表示物体亮度，
         *	u*和v*是色度。于1976年由国际照明委员会CIE 提出，由CIE XYZ空间经简单变换得到，具视觉统一性。类似的色
         *	彩空间有CIELAB。对于一般的图像，u*和v*的取值范围为-100到+100，亮度为0到100。）转换。
         *	例如，如果您有一个32位浮点图像直接从8位图像转换而没有任何缩放，则它将具有0..255的值范围，
         *	而不是该函数假定的0..1。因此，在调用#cvtColor之前，您需要先按比例缩小图像：
         *	img = 1./255;
         *	cvtColor（img，img，COLOR_BGR2Luv）;
         *	如果将#cvtColor与8位图像一起使用，转换将丢失一些信息。对于许多应用程序来说，这不会引起注意，
         *	但是建议在需要全部颜色范围的应用程序中使用32位图像，或者在进行操作之前先转换图像然后再转换回来。
         *	如果转换添加了Alpha通道，则其值将设置为相应通道范围的最大值：CV_8U为255，CV_16U为65535，CV_32F为1。
         *	@param src输入图像：8位无符号，16位无符号（CV_16UC ...）或单精度浮点数。
         *	@param dst输出图像的大小和深度与src相同。
         *	@param代码颜色空间转换代码。
         *	通道是自动从src和代码派生的。
         */
        /*
         * 下面附上第三个参数的详细解释
         */
        Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY);

        int channels = image.channels();//获取图像通道数

        for (int i = 0, rlen = image.rows(); i < rlen; i++) {
            for (int j = 0, clen = image.cols(); j < clen; j++) {
                if (channels == 3) {//图片为3通道即平常的(B,G,R)
                    System.out.println(i+","+j+"B:"+image.get(i, j)[0]);
                    System.out.println(i+","+j+"G:"+image.get(i, j)[1]);
                    System.out.println(i+","+j+"R:"+image.get(i, j)[2]);
                } else {//图片为单通道
                    System.out.println(i+","+j+":"+image.get(i, j)[0]);
                }
            }
        }
        HighGui.imshow("image", image);
        HighGui.waitKey();
    }
}

