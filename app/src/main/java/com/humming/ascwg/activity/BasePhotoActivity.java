package com.humming.ascwg.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.humming.ascwg.Application;
import com.humming.ascwg.utils.FileUtils;
import com.humming.ascwg.R;
import com.humming.ascwg.activity.AbstractActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Elvira on 2016/5/19.
 * 图片和照相的公用类
 */
public class BasePhotoActivity extends AbstractActivity {

    /* 请求识别码 */
    protected static final int CODE_CAMERA_REQUEST_ONE = 0x01;
    protected static final int CODE_CAMERA_REQUEST_TWO = 0x02;
    public static final int CODE_GALLERY_REQUEST_ONE = 0x03;
    public static final int CODE_GALLERY_REQUEST_TWO = 0x04;

    //版本比较：是否是4.4及以上版本
    public static boolean mIsKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    /**
     * 存放拍照上传的照片路径
     */
    public static String mPublishPhotoPath;

    public class PopupWindows extends PopupWindow {

        public PopupWindows(Context mContext, View parent) {

            View view = View
                    .inflate(mContext, R.layout.popupwindows_photo, null);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_in));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_out));

            setWidth(ViewGroup.LayoutParams.FILL_PARENT);
            setHeight(ViewGroup.LayoutParams.FILL_PARENT);
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            setAnimationStyle(R.style.mypopwindow_anim_style);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();

            TextView camera = (TextView) view
                    .findViewById(R.id.item_popupwindows_camera);
            TextView photo = (TextView) view
                    .findViewById(R.id.item_popupwindows_Photo);
            TextView cancel = (TextView) view
                    .findViewById(R.id.item_popupwindows_cancel);
            camera.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getCamerePhoto();
                    dismiss();
                }
            });
            photo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // 调用图片浏览器选择图片
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    if (mIsKitKat) {
                        Application.getInstance().getCurrentActivity().startActivityForResult(intent, CODE_GALLERY_REQUEST_ONE);
                    } else {
                        Application.getInstance().getCurrentActivity().startActivityForResult(Intent.createChooser(intent, "选择图片"), CODE_GALLERY_REQUEST_TWO);
                    }
                    dismiss();
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                }
            });

        }
    }


    public static void getCamerePhoto() {

        // 根目录
        String rootDir = FileUtils.getRootDir(Application.getInstance().getCurrentActivity());
        // 创建应用缓存目录
        String appCacheDir = rootDir + File.separator + FileUtils.LBTWYYJS;
        File dir = new File(appCacheDir);
        if (!dir.exists())
            dir.mkdirs();
        // 创建应用缓存文件
        mPublishPhotoPath = appCacheDir + File.separator + UUID.randomUUID() + ".jpg";
        File file = new File(mPublishPhotoPath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        // 调用相机拍照
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

        try {
            if (mIsKitKat)
                Application.getInstance().getCurrentActivity().startActivityForResult(intent, CODE_CAMERA_REQUEST_ONE);
            else
                Application.getInstance().getCurrentActivity().startActivityForResult(intent, CODE_CAMERA_REQUEST_TWO);

        } catch (Exception e) {
            e.printStackTrace();
            new AlertDialog.Builder(Application.getInstance().getCurrentActivity()).setMessage("没有合适的相机应用程序").setPositiveButton("OK", null).show();
        }
    }


    //获取图片路径 4.4以下
    protected String getPhotoName(Uri uri, boolean flag, String path) {

        int degree = 0;
        if (flag) {
            ContentResolver cr = Application.getInstance().getCurrentActivity().getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);
            cursor.moveToFirst();
            degree = readPictureDegree(cursor.getString(1));
            cursor.close();
        } else
            degree = readPictureDegree(path);

        Bitmap bitmap = FileUtils.getBitmapUseUri(uri, Application.getInstance().getCurrentActivity().getContentResolver());
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        // 根目录
        String rootDir = FileUtils.getRootDir(this);

        // 创建应用缓存目录(一级)
        String appCacheDir = rootDir + File.separator + FileUtils.LBTWYYJS;
        File dir = new File(appCacheDir);
        if (!dir.exists())
            dir.mkdirs();

        // 创建应用缓存目录(二级)
        String appCacheDir2 = appCacheDir + File.separator + FileUtils.LBTWYYJS_SMALL;
        File dir2 = new File(appCacheDir2);
        if (!dir2.exists())
            dir2.mkdirs();

        // 创建应用缓存文件
        String filePath = appCacheDir2 + File.separator + UUID.randomUUID() + ".jpg";
        File fileFile = new File(filePath);
        if (!fileFile.exists())
            try {
                fileFile.createNewFile();
                // 写入文件
                FileOutputStream fos = new FileOutputStream(fileFile);
                if (null != fos)
                    resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 60, fos);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        if (resizedBitmap != null) {
            resizedBitmap.recycle();
            bitmap.recycle();
        }
        return filePath;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    protected int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}
