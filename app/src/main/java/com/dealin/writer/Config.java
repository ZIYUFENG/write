package com.dealin.writer;

import android.os.Environment;

import java.io.File;

/**
 * Created by 兰件 on 2016/9/24.
 * 这是一个储存软件一些默认配置信息的类
 */

public class Config {
    //小说储存目录
    public static String NOVEL_PATH = Environment.getDataDirectory()+ File.pathSeparator+"novel";
    //大纲地址
    public static String OUTLINE_PATH = Environment.getDataDirectory() + File.pathSeparator + "outline";
    //设置地址
    public static String SETTING_PATH = Environment.getDataDirectory() + File.pathSeparator + "setting";
    //默认首行缩进字符
    public static String INDENT = "　　";
}
