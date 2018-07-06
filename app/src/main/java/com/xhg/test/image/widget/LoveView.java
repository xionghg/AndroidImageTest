package com.xhg.test.image.widget;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class LoveView extends View {

    private static final float CENTER_TOP_Y_RATE = 0.3f;    // 中间顶部比例
    private static final float MOST_WIDTH_RATE = 0.49f;     // 心形一半most宽度
    private static final float LINE_WIDTH_RATE = 0.35f;     // 左右边线宽度比例
    private static final float K_1 = 1.14f;                 // 左右边线斜率
    private static final float K_2 = 0.80f;                 // 顶部圆球曲率

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path path = new Path();

    private float loveCenterX = 0;     // 中心x坐标
    private float loveCenterTopY;      // 中心最高点y坐标
    private float loveCenterBottomY;   // 中心最低点y坐标
    private float leftmostX;
    private float rightmostX;
    private float lineLeftX;
    private float lineRightX;
    private float lineTopY;
    private float quadY1;
    private float quadY2;

    public LoveView(Context context) {
        super(context);
    }

    public LoveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLove(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            left = left + getPaddingLeft();
            top = top + getPaddingTop();
            right = right - getPaddingRight();
            bottom = bottom - getPaddingBottom();

            if (left < right && top < bottom) {
                final float width = right - left;
                final float height = bottom - top;

                loveCenterX = left + width * 0.5f;
                loveCenterTopY = top + height * CENTER_TOP_Y_RATE;
                loveCenterBottomY = top + height * 0.99f;
                leftmostX = loveCenterX - width * MOST_WIDTH_RATE;
                rightmostX = loveCenterX + width * MOST_WIDTH_RATE;
                lineLeftX = loveCenterX - width * LINE_WIDTH_RATE;
                lineRightX = loveCenterX + width * LINE_WIDTH_RATE;
                lineTopY = loveCenterBottomY - K_1 * LINE_WIDTH_RATE * height;
                quadY1 = loveCenterBottomY - K_1 * MOST_WIDTH_RATE * height;
                quadY2 = loveCenterTopY - K_2 * MOST_WIDTH_RATE * height;
            } else {
                loveCenterX = 0;
            }
        }
    }

    private void drawLove(Canvas canvas) {
        if (loveCenterX <= 0) {
            return;
        }
        path.reset();

        path.moveTo(loveCenterX, loveCenterBottomY);
        path.lineTo(lineLeftX, lineTopY);
        path.quadTo(leftmostX, quadY1, leftmostX, loveCenterTopY);

        path.cubicTo(leftmostX, quadY2, loveCenterX, quadY2, loveCenterX, loveCenterTopY);
        path.cubicTo(loveCenterX, quadY2, rightmostX, quadY2, rightmostX, loveCenterTopY);

        path.quadTo(rightmostX, quadY1, lineRightX, lineTopY);
        path.lineTo(loveCenterX, loveCenterBottomY);

        canvas.drawPath(path, paint);
    }

}
