package com.szl.zhaozhao2.view.pulltorefresh;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;

public class PullDownBitmap {
    private Bitmap mBitmap;

    private int    mWidth;
    private int    mHeight;

    private Paint  mPaint;
    private RectF  mRect;
    private Matrix mMatrix;

    public PullDownBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        mWidth = bitmap.getWidth();
        mHeight = bitmap.getHeight();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mRect = new RectF();
        mMatrix = new Matrix();
    }

    public PullDownBitmap(BitmapDrawable drawable) {
        mBitmap = drawable.getBitmap();
        mWidth = drawable.getIntrinsicWidth();
        mHeight = drawable.getIntrinsicHeight();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mRect = new RectF();
        mMatrix = new Matrix();
    }

    public int getWidth() {
        return mWidth;
    }

    public int getHeight() {
        return mHeight;
    }

    public RectF getRect() {
        return mRect;
    }

    // public Matrix getMatrix() {
    // return mMatrix;
    // }

    public void drawBitmap( Canvas canvas, float x, float y ) {
        mMatrix.reset();
        mMatrix.postTranslate(x, y);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
    }

    public void drawBitmap( Canvas canvas, float scaleX, float scaleY, float translateX,
                            float translateY ) {
        mMatrix.reset();
        mMatrix.postScale(scaleX, scaleY);
        mMatrix.postTranslate(translateX, translateY);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
    }
}
