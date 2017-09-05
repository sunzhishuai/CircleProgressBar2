package com.newborntown.circleprogressbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Graceful Sun on 17/9/5.
 * E-mail itzhishuaisun@sina.com
 */

public class CircleProgressBar extends View {

    private Paint mProgressPaint;
    private float lineWidth = 50; //进度线宽
    private float lineSpan = 5; //进度线间距
    private Path dtPath = new Path();
    private Path progressDtPath = new Path();
    private int mArcRange = 240; //进度条总计所占弧度
    private Paint mBgPaint;
    private int mProgress = 70;  //进度
    private Paint mCirClePaint;
    private int mCircleRadius = 20; //进度条末尾圆形半径
    private float mCircleSpan = 10; //圆形距离 进度条的距离
    private Paint mStarAndEndPaint;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mProgressPaint = new Paint();
        mProgressPaint.setColor(Color.RED);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(lineWidth);

        mBgPaint = new Paint();
        mBgPaint.setColor(Color.GRAY);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setStrokeWidth(lineWidth);

        mCirClePaint = new Paint();
        mCirClePaint.setAntiAlias(true);

        mStarAndEndPaint = new Paint();
        mStarAndEndPaint.setAntiAlias(true);
        mStarAndEndPaint.setStrokeWidth(lineSpan);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = Math.max((int) dpToPx(100), getMinimumWidth());
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = Math.max((int) dpToPx(100), getMinimumHeight());
        }
        setMeasuredDimension(widthSize, heightSize);
        setPaintShader();
    }

    private void setPaintShader() {
        LinearGradient shader = new LinearGradient(0, 0, getMeasuredWidth(),
                0, Color.RED, Color.YELLOW, Shader.TileMode.MIRROR);
        mProgressPaint.setShader(shader);
        mCirClePaint.setShader(shader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int measuredHeight = getMeasuredHeight();
        int measuredWidth = getMeasuredWidth();
        drawBgLine(canvas, Math.min(measuredWidth, measuredHeight));
        drawProgress(canvas, Math.min(measuredWidth, measuredHeight));
    }

    private void drawProgress(Canvas canvas, int progressSize) {
        Path path = new Path();
        RectF rectF = new RectF(lineWidth, lineWidth, progressSize - lineWidth, progressSize - lineWidth);
        path.addArc(rectF, -210, mArcRange);
        PathMeasure pathMeasure = new PathMeasure(path, false);
        for (int i = 0; i * lineSpan < pathMeasure.getLength() * (mProgress / 100.f); i = i + 2) {
            pathMeasure.getSegment(i * lineSpan, (i + 1) * lineSpan, dtPath, true);
            canvas.drawPath(dtPath, mProgressPaint);
        }

    }

    /**
     * 绘制背景
     *
     * @param canvas   画板
     * @param drawSize 绘制区域
     */
    private void drawBgLine(Canvas canvas, int drawSize) {
        //绘制背景先
        Path path = new Path();
        RectF rectF = new RectF(lineWidth, lineWidth, drawSize - lineWidth, drawSize - lineWidth);
        path.addArc(rectF, -210, mArcRange);
        PathMeasure pathMeasure = new PathMeasure(path, false);
        for (int i = 0; i * lineSpan < pathMeasure.getLength(); i = i + 2) {
            pathMeasure.getSegment(i * lineSpan, (i + 1) * lineSpan, progressDtPath, true);
            canvas.drawPath(progressDtPath, mBgPaint);
        }

        //绘制起始线
        canvas.save();
        int rotateDegree = mArcRange / 2;
        canvas.rotate(rotateDegree, canvas.getWidth()/2, canvas.getHeight() / 2);
        canvas.drawLine(canvas.getWidth() / 2, lineWidth / 2, canvas.getWidth() / 2, lineWidth / 2 + lineWidth * 1.3f, mStarAndEndPaint);
        canvas.rotate(-rotateDegree * 2, canvas.getWidth() / 2, canvas.getHeight() / 2);
        canvas.drawLine(canvas.getWidth() / 2, lineWidth / 2, canvas.getWidth() / 2, lineWidth / 2 + lineWidth * 1.3f, mStarAndEndPaint);
        canvas.restore();

        //绘制 圆点
        float[] position = new float[2];
        pathMeasure.getPosTan(pathMeasure.getLength() * mProgress / 100.f + mCircleSpan + mCircleRadius, position, null);
        canvas.drawCircle(position[0], position[1], mCircleRadius, mCirClePaint);

    }


    /**
     * dp 转像素
     *
     * @param dp dp
     * @return 像素
     */
    private float dpToPx(int dp) {
        return Resources.getSystem().getDisplayMetrics().density * dp;
    }

    /**
     * 设置进度
     *
     * @param progress 进度
     */
    public void setProgress(int progress) {
        this.mProgress = progress;
        invalidate();
    }
}
