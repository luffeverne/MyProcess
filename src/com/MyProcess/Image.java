package com.MyProcess;

import javax.swing.*;

public class Image {
    private String name; // 图片名称
    private Icon icon; // 存放图片的封面

    public Image() {
    }

    public Image(String name, Icon icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    @Override
    public String toString() {
        return name;
    }
}
