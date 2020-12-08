package com.MyProcess;

import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.regex.Pattern;

public class MainGUI {
    /*自定义图片*/
    Image[] images  = {
            new Image("图片1          ", new ImageIcon("")),
            new Image("图片2          ", new ImageIcon("")),
            new Image("图片3          ", new ImageIcon("")),
    };
    int index = 0;

    JFrame jf = new JFrame("程序accurate");
    // 创建打开文件对话框
    JFileChooser chooser = new JFileChooser("./src/images");

    /* 创建组件*/
    // 创建菜单条
    JMenuBar jMenuBar = new JMenuBar();

    // 创建菜单
    JMenu file = new JMenu("文件");
    JMenu help = new JMenu("帮助");

    // 创建菜单条目
    // 用来记录用户选择的图片
    BufferedImage image;
    String currentPath; // 记录当前路径
    int currentIndex; //记录当前图片序号
    JMenuItem openFileItem = new JMenuItem(new AbstractAction("打开文件") {
        @Override
        public void actionPerformed(ActionEvent e) {
            chooser.showOpenDialog(imageCover);
            File imageFile = chooser.getSelectedFile();

            String imageName = imageFile.getName();
            String imagePath = imageFile.getPath();

        /*    System.out.println(imageName);
            System.out.println(imagePath);*/

            boolean isTif = imageName.endsWith(".tif");
            if (isTif) {
                String str = imageName.replaceAll(".tif", ".jpg");
                System.out.println(str);

            }

            images[index%3].setName(imageName);
            images[index%3].setIcon(new ImageIcon(imagePath));
            index++;

            currentIndex = index == 0 ? 0 : index%3 - 1;
            currentPath = imagePath;
        }
    });

    JMenuItem exit = new JMenuItem(new AbstractAction("关闭程序") {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    });

    // 创建工具条
    JToolBar toolBar = new JToolBar();

    // create Acion steps , then JtoolBar.add(steps)
    Action findCenterOfCircle = new AbstractAction("1.抓点处理") {
        @Override
        public void actionPerformed(ActionEvent e) {
            new Step1_FindCenterOfCircle().handlePicture(currentPath, currentIndex);
            images[currentIndex].setName("src_process" + currentIndex + ".jpg");
            images[currentIndex].setIcon(new ImageIcon("src/images/src_process"+currentIndex+".jpg"));
            System.out.println(images[currentIndex].getIcon());
        }
    };
    Action selectBaseVector = new AbstractAction("2.选择基矢点") {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };
    Action reviseBaseVector = new AbstractAction("3.修正基矢值") {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };
    Action normalizedBaseVector = new AbstractAction("4.归一化基矢") {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };

    // 创建分隔栏
    // 左边显示图片名称
    JList<Image> imagesJList = new JList<>(images);
    // 右边显示图片
    JLabel imageCover = new JLabel();


    public void init() {
        /* 组装视图*/
        file.add(openFileItem);
        file.add(exit);
        jMenuBar.add(file);
        jMenuBar.add(help);
        jf.setJMenuBar(jMenuBar);

        toolBar.add(findCenterOfCircle);
        toolBar.add(selectBaseVector);
        toolBar.add(reviseBaseVector);
        toolBar.add(normalizedBaseVector);
        jf.add(toolBar, BorderLayout.NORTH);

        // 组装分割面板
        imagesJList.setPreferredSize(new Dimension(220, 800));
        imageCover.setPreferredSize(new Dimension(780, 800));

        // add Action Listener for imagesJList
        imagesJList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Image image = imagesJList.getSelectedValue();
                imageCover.setIcon(image.getIcon());
            }
        });
        //create a horizontal split pane
        JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, imagesJList, imageCover);
//        pane.setOneTouchExpandable(true);
        pane.setContinuousLayout(true);
        pane.resetToPreferredSizes();
        pane.setDividerSize(10);
        jf.add(pane);


        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        jf.pack();
        jf.setBounds(100, 10, 1000, 800);
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        new MainGUI().init();
    }
}
