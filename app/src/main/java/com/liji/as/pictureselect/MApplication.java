package com.liji.as.pictureselect;


import android.app.Application;
import android.content.Context;

import com.liji.as.pictureselect.activity.photo.GlideImageLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
 * 作者：liji on 2015/12/10 10:04
 * 邮箱：lijiwork@sina.com
 */
public class MApplication extends Application {
    private static Context CONTEXT;
    

    @Override
    public void onCreate() {
        
        super.onCreate();
        CONTEXT = getApplicationContext();

//        initImageLoader();
//        initGalleryFinal();
        
    }


    private void initGalleryFinal() {
        //设置主题
        //ThemeConfig.CYAN
        ThemeConfig theme = ThemeConfig.DEFAULT;
        //配置功能
        FunctionConfig functionConfig = new FunctionConfig.Builder().setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build();
        
        //配置imageloader
        cn.finalteam.galleryfinal.ImageLoader imageloader = new GlideImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageloader, theme).setFunctionConfig(functionConfig)
                .build();
        GalleryFinal.init(coreConfig);
    }
    

    
    /**
     * 设置图片加载方式
     */
    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .discCacheSize(50 * 1024 * 1024)//
                .discCacheFileCount(100)//缓存一百张图片
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
    }
    

    
    public static Context getContext() {
        return CONTEXT;
    }
    
}
