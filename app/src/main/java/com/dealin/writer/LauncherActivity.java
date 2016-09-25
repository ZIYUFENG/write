package com.dealin.writer;
/**
 * 这个活动负责在应用初次启动或更新后显示启动图
 */

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.dealin.writer.activity.NovelListActivity;

public class LauncherActivity extends Activity {
    private String curVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_launcher);

        if (isOldVersion()) {
            //应用未更新或初次启动就直接进入小说列表活动
            enterProjectListActivity();
            Log.d("更新状态", "老版本");
        } else {
            Log.d("更新状态", "新版本");
            //更新或初次启动后显示两秒钟启动图后进入小说列表
            //延迟两秒后启动小说列表活动
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    enterProjectListActivity();
                }
            }, 2000);
        }
    }

    /**
     * 判断是老版本还是新版本
     * @return true=老版本 false=新版本
     */
    public boolean isOldVersion() {
        try {
            PackageInfo packageInfo = getApplication().getPackageManager().getPackageInfo(getPackageName(), 0);
            //获取软件版本名称
            curVersionName = packageInfo.versionName;
            //判断软件版本名称和存在本地的版本名称是否相等
            if (DataManager.getVersionName() == null) {//获取出来的值为null，代表本地没有保存版本名称的文件，所以是首次启动
                return false;
            } else {
                if(DataManager.getVersionName().equals(curVersionName)) {
                    //软件版本和存在本地的版本名相同，代表软件没有进行过更新
                    return true;
                } else {
                    //软件更新了，更新本地储存的版本名称数据
                    DataManager.saveVersionName(curVersionName);
                    return false;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 启动小说列表活动,并结束这个活动
     */
    public void enterProjectListActivity() {
        Intent intent = new Intent(this, NovelListActivity.class);
        startActivity(intent);
        finish();
    }
}
