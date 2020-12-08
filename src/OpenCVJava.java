import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;

import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.imgproc.Imgproc.*;

/**
 * Created by better on 2014/10/4.
 */
public class OpenCVJava{

    static{
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        Mat img = Imgcodecs.imread("C:\\Users\\Luffe\\Pictures\\test\\1.jpg");
        if (img == null) {
           System.out.println("图片不存在");
        }
        imshow("原图", img);

//        Mat dst(img.size(), img.type());



       /* Mat img2 = null, img3 = null, img4 = null;
        cvtColor(img, img2, COLOR_GRAY2BGR); // 将彩色图片变成灰度图片
        GaussianBlur(img2, img2, new Size(9, 9), 2, 2);
        threshold(img2, img3, 90, 255, THRESH_BINARY);
        Object namedWindow;
        */





    }

}