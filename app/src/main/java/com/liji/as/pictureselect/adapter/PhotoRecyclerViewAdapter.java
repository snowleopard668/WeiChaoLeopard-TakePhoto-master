package com.liji.as.pictureselect.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.liji.as.pictureselect.R;
import com.liji.as.pictureselect.activity.MainActivity;
import com.liji.as.pictureselect.activity.photo.PreviewPhotoActivity;
import com.liji.as.pictureselect.utils.XCallbackListener;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.widget.GFImageView;

/**
 * Created by liji on 2016/5/13.
 */
public class PhotoRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    /**
     * 一行最多显示的图片数量
     */
    public static final int NUM_ITEM = 6;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<PhotoInfo> mPhotoInfoList;

    /**
     * 底部添加图片view
     */
    private final int ITEM_FOOTER = 0;
    /**
     * 内容区域，显示图片
     */
    private final int ITEM_CONTENT = 1;

    XCallbackListener mXCallbackListener;

    FragmentManager fragmentManager;
    public PhotoRecyclerViewAdapter(Context mContext, FragmentManager fragmentManager, List<PhotoInfo> mPhotoInfoList, XCallbackListener mXCallbackListener) {
        this.mContext = mContext;
        this.fragmentManager=fragmentManager;
        this.mPhotoInfoList = mPhotoInfoList;
        mInflater = LayoutInflater.from(mContext);
        this.mXCallbackListener = mXCallbackListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_FOOTER) {
            return new FootViewHolder(mInflater.inflate(R.layout.item_photo_add, parent, false));
        } else {
            return new ContentViewHolder(mInflater.inflate(R.layout.item_adapter_photo_list, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getDatas().size() <= NUM_ITEM) {
            if (getDatas().size() == 0) {//没有数据，显示添加按钮
                return ITEM_FOOTER;
            } else {
                if (position == getDatas().size()) {//在范围内最后一个显示添加按钮，其余显示数据
                    return ITEM_FOOTER;
                } else {
                    return ITEM_CONTENT;
                }
            }
        } else {
            return ITEM_CONTENT;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ContentViewHolder) {//显示图片数据
            String path = "";
            PhotoInfo photoInfo = getDatas().get(position);
            if (photoInfo != null) {
                path = photoInfo.getPhotoPath();
            }
            ((ContentViewHolder) holder).mIvPhoto.setImageResource(cn.finalteam.galleryfinal.R.drawable.ic_gf_default_photo);
            ((ContentViewHolder) holder).mIvDelete.setImageResource(GalleryFinal.getGalleryTheme().getIconDelete());
            Drawable defaultDrawable = mContext.getResources().getDrawable(cn.finalteam.galleryfinal.R.drawable.ic_gf_default_photo);
            GalleryFinal.getCoreConfig().getImageLoader().displayImage((Activity) mContext, path, ((ContentViewHolder) holder).mIvPhoto, defaultDrawable, 100, 100);
            ((ContentViewHolder) holder).mIvDelete.setVisibility(View.VISIBLE);
            ((ContentViewHolder) holder).mIvDelete.setOnClickListener(new OnDeletePhotoClickListener(position, mXCallbackListener));

            //查看预览图
            ((ContentViewHolder) holder).mIvPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PreviewPhotoActivity.class);
                    intent.putExtra(PreviewPhotoActivity.PHOTO_LIST, (ArrayList) getDatas());
                    intent.putExtra(PreviewPhotoActivity.PHOTO_INDEX, position);
                    mContext.startActivity(intent);
                }
            });

        } else if (holder instanceof FootViewHolder) {//显示添加按钮
//            if (position == getDatas().size() && position == NUM_ITEM) {
//                ((FootViewHolder) holder).mImageAdd.setVisibility(View.GONE);
//            } else {
//                ((FootViewHolder) holder).mImageAdd.setVisibility(View.VISIBLE);
            ((FootViewHolder) holder).mImageAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) mContext).openChoiceDialog(mContext,fragmentManager);
                }
            });
//            }
        }
    }

    //包含一个添加按钮
    @Override
    public int getItemCount() {
        int num = mPhotoInfoList.size();
        int total = 0;
        if (num < NUM_ITEM) {
            total = ++num;
        } else {
            total = num;
        }
        return total;
    }


    //底部ViewHolder
    public static class FootViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageAdd;

        public FootViewHolder(View footView) {
            super(footView);
            mImageAdd = (ImageView) itemView.findViewById(R.id.imageviewadd);
        }

    }

    //内容viewHolder
    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        GFImageView mIvPhoto;
        ImageView mIvDelete;

        public ContentViewHolder(View itemView) {
            super(itemView);

            mIvPhoto = (GFImageView) itemView.findViewById(R.id.iv_photo);
            mIvDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
        }
    }

    /**
     * 返回列表数据
     *
     * @return
     */
    public List<PhotoInfo> getDatas() {
        return this.mPhotoInfoList;
    }


    //图片删除操作
    private class OnDeletePhotoClickListener implements View.OnClickListener {

        private int position;
        private XCallbackListener mXCallbackListener;

        public OnDeletePhotoClickListener(int position, XCallbackListener mXCallbackListener) {
            this.position = position;
            this.mXCallbackListener = mXCallbackListener;
        }

        @Override
        public void onClick(View view) {
            PhotoInfo photoInfo = null;
            try {
                photoInfo = getDatas().remove(position);
                mXCallbackListener.call(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
            notifyDataSetChanged();
        }
    }
}
