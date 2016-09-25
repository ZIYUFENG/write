package com.dealin.writer;

/**
 * Created by 兰件 on 2016/9/25.
 * 这是一个读取数据的工具类
 * 所有数据存取工作都在这里进行
 */

public class DataManager {
    /**保存版本名称到本地
     * 将参数中的versionName保存在软件设置目录下的veirsionName文件中
     * PS.软件设置目录在类Config中能找到，只需要直接使用
     * @param versionName 新的版本名称
     */
    public static void saveVersionName(String versionName) {

    }

    /**
     * 从本地将versionName读取出来
     * @return
     */
    public static String getVersionName() {
        return null;
    }

    /**
     * 实现saveFile和readFile方法，方便数据存取
     * @param path 文件路径;
     * @param content 文件内容;
     */
    public static void saveFile(String path,String content) {

    }

    /**
     * 读取文件
     * @param path 文件路径
     * @return
     */
    public static String readFile(String path) {
        return null;
    }
}
