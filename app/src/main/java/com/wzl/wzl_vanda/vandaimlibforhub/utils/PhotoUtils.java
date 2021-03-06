package com.wzl.wzl_vanda.vandaimlibforhub.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lzw on 15/4/24.
 */
public class PhotoUtils {
//  public static DisplayImageOptions avatarImageOptions = new DisplayImageOptions.Builder()
//      .showImageOnLoading(R.drawable.chat_default_user_avatar)
//      .showImageForEmptyUri(R.drawable.chat_default_user_avatar)
//      .showImageOnFail(R.drawable.chat_default_user_avatar)
//      .cacheInMemory(true)
//      .cacheOnDisc(true)
//      .considerExifParams(true)
//      .imageScaleType(ImageScaleType.EXACTLY)
//      .bitmapConfig(Bitmap.Config.RGB_565)
//      .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
//          //.displayer(new RoundedBitmapDisplayer(20))
//          //.displayer(new FadeInBitmapDisplayer(100))// 淡入
//      .build();
//
//  private static DisplayImageOptions normalImageOptions = new DisplayImageOptions.Builder()
//      .showImageOnLoading(R.drawable.chat_common_empty_photo)
//      .showImageForEmptyUri(R.drawable.chat_common_empty_photo)
//      .showImageOnFail(R.drawable.chat_common_image_load_fail)
//      .cacheInMemory(true)
//      .cacheOnDisc(true)
//      .considerExifParams(true)
//      .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
//      .bitmapConfig(Bitmap.Config.RGB_565)
//      .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
//          //.displayer(new RoundedBitmapDisplayer(20))
//      .displayer(new FadeInBitmapDisplayer(100))// 淡入
//      .build();
//
//  public static void displayImageCacheElseNetwork(ImageView imageView,
//                                                  String path, String url) {
//    ImageLoader imageLoader = ImageLoader.getInstance();
//    if (path != null) {
//      File file = new File(path);
//      if (file.exists()) {
//        imageLoader.displayImage("file://" + path, imageView, normalImageOptions);
//        return;
//      }
//    }
//    imageLoader.displayImage(url, imageView, normalImageOptions);
//  }

  public static String compressImage(String path, String newPath) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(path, options);
    int inSampleSize = 1;
    int maxSize = 3000;
    if (options.outWidth > maxSize || options.outHeight > maxSize) {
      int widthScale = (int) Math.ceil(options.outWidth * 1.0 / maxSize);
      int heightScale = (int) Math.ceil(options.outHeight * 1.0 / maxSize);
      inSampleSize = Math.max(widthScale, heightScale);
    }
    options.inJustDecodeBounds = false;
    options.inSampleSize = inSampleSize;
    Bitmap bitmap = BitmapFactory.decodeFile(path, options);
    int w = bitmap.getWidth();
    int h = bitmap.getHeight();
    int newW = w;
    int newH = h;
    if (w > maxSize || h > maxSize) {
      if (w > h) {
        newW = maxSize;
        newH = (int) (newW * h * 1.0 / w);
      } else {
        newH = maxSize;
        newW = (int) (newH * w * 1.0 / h);
      }
    }
    Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, newW, newH, false);
    FileOutputStream outputStream = null;
    try {
      outputStream = new FileOutputStream(newPath);
      newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } finally {
      try {
        if (outputStream != null) {
          outputStream.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }

    }
    recycle(newBitmap);
    recycle(bitmap);
    return newPath;
  }


    /**
     * 将图片变为圆角
     *
     * @param bitmap 原Bitmap图片
     * @param pixels 图片圆角的弧度(单位:像素(px))
     * @return 带有圆角的图片(Bitmap 类型)
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 将图片转化为圆形头像
     *
     * @throws
     * @Title: toRoundBitmap
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;

            left = 0;
            top = 0;
            right = width;
            bottom = width;

            height = width;

            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;

            float clip = (width - height) / 2;

            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;

            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// 设置画笔无锯齿

        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

        // 以下有两种方法画圆,drawRounRect和drawCircle
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        // canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

        return output;
    }

  public static void recycle(Bitmap bitmap) {
    // 先判断是否已经回收
    if (bitmap != null && !bitmap.isRecycled()) {
      // 回收并且置为null
      bitmap.recycle();
    }
    System.gc();
  }
}
