package com.liji.as.pictureselect.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.liji.as.pictureselect.R;
import com.liji.as.pictureselect.adapter.PhotoRecyclerViewAdapter;
import com.liji.as.pictureselect.utils.XCallbackListener;
import com.liji.takephoto.TakePhoto;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

public class MainActivity extends AppCompatActivity {
    
    RecyclerView mRecyclerAddPhoto;
    
    private final int REQUEST_CODE_CAMERA = 1000;
    
    private final int REQUEST_CODE_GALLERY = 1001;
    
    FunctionConfig functionConfig;
    
    private List<PhotoInfo> mPhotoList;
    
    PhotoRecyclerViewAdapter mPhotoRecyclerViewAdapter;
    
    private LinearLayoutManager mLayoutManager;
    
    private ImageView img;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initView();
    }
    
    private void initView() {
        
        mRecyclerAddPhoto = (RecyclerView) findViewById(R.id.my_recycler_addphoto);
        img = (ImageView) findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                

                
                //                openChoiceDialog(MainActivity.this,getSupportFragmentManager());
            }
        });
        
        //配置功能
        functionConfig = new FunctionConfig.Builder().setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnablePreview(true)
                .build();
        
        mPhotoList = new ArrayList<>();
        mPhotoRecyclerViewAdapter = new PhotoRecyclerViewAdapter(MainActivity.this, getSupportFragmentManager(),
                mPhotoList, new XCallbackListener() {
                    @Override
                    protected void callback(Object... obj) {
                        int pos = Integer.parseInt(obj[0].toString());
                    }
                });
        
        //设置水平布局
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerAddPhoto.setLayoutManager(mLayoutManager);
        mRecyclerAddPhoto.setAdapter(mPhotoRecyclerViewAdapter);
        
    }
    
    /**
     * 打开相机和相册功能
     */
    public void openChoiceDialog(Context context, FragmentManager fragmentManager) {
        ActionSheet.createBuilder(context, fragmentManager)
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("打开相册", "拍照")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                        
                    }
                    
                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        switch (index) {
                            case 0://相册
                                GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY,
                                        functionConfig,
                                        mOnHanlderResultCallback);
                                break;
                            
                            case 1://相机
                                GalleryFinal.openCamera(REQUEST_CODE_CAMERA, functionConfig, mOnHanlderResultCallback);
                                break;
                        }
                        
                    }
                })
                .show();
        
    }
    
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, final List<PhotoInfo> resultList) {
            if (resultList != null) {
                ImageLoader.getInstance().displayImage("file://" + resultList.get(0).getPhotoPath(), img);
                //                Drawable defaultDrawable = getResources().getDrawable(cn.finalteam.galleryfinal.R.drawable.ic_gf_default_photo);
                //                GalleryFinal.getCoreConfig().getImageLoader().displayImage(MainActivity.this, resultList.get(0).getPhotoPath(), (GFImageView) img, defaultDrawable, 100, 100);
                //                mPhotoList.addAll(resultList);
                //                mPhotoRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
        
        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
        }
    };
    
}
