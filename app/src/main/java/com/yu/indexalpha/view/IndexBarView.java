package com.yu.indexalpha.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.yu.indexalpha.util.UnitConverter;

public class IndexBarView extends View {

    //索引字母颜色
    private static final int LETTER_COLOR = 0xFF333333;

    //索引字母背景圆圈颜色
    private static final int BG_COLOR = 0xFF18ad84;

    //索引字母数组
    public String[] indexs = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};

    //控件的宽高
    private int mWidth;

    private int mHeight;

    //单元格的高度
    private float mCellHeight;

    //顶部间距
    private float mMarginTop;

    //手指按下的字母索引
    private int touchIndex = 0;

    private Paint mPaint;

    private Paint bgPaint;

    public IndexBarView(Context context) {
        super(context);
        init();
    }

    public IndexBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setBackgroundColor(0xffffffff);
        mPaint = new Paint();
        mPaint.setColor(LETTER_COLOR);
        mPaint.setTextSize(UnitConverter.dip2px(getContext(), 9));
        mPaint.setAntiAlias(true); // 去掉锯齿，让字体边缘变得平滑

        bgPaint = new Paint();
        bgPaint.setColor(BG_COLOR);
        bgPaint.setAntiAlias(true);
    }

    public void setIndexs(String[] indexs) {
        this.indexs = indexs;
        mMarginTop = (mHeight - mCellHeight * indexs.length) / 2;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //字母的坐标点：(x,y)
        if (indexs.length <= 0) {
            return;
        }
        for (int i = 0; i < indexs.length; i++) {
            String letter = indexs[i];
            //判断是不是我们按下的当前字母
            if (touchIndex == i) {
                //绘制文字圆形背景
                canvas.drawCircle(mWidth / 2, getTextHeight(letter) + mCellHeight * i + mMarginTop + UnitConverter.dip2px(getContext(), 2), UnitConverter.dip2px(getContext(), 7), bgPaint);
                mPaint.setColor(Color.WHITE);
            } else {
                mPaint.setColor(LETTER_COLOR);
            }
            float x = mWidth / 2 - getTextWidth(letter) / 2;
            float y = mCellHeight / 2 + getTextHeight(letter) / 2 + mCellHeight * i + mMarginTop;
            canvas.drawText(letter, x, y, mPaint);
        }
    }

    /**
     * 获取字符的宽度
     *
     * @param text 需要测量的字母
     * @return 对应字母的高度
     */
    public float getTextWidth(String text) {
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }

    /**
     * 获取字符的高度
     *
     * @param text 需要测量的字母
     * @return 对应字母的高度
     */
    public float getTextHeight(String text) {
        Rect bounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.height();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mCellHeight = (mHeight * 1f / 27);    //26个字母加上“#”
        mMarginTop = (mHeight - mCellHeight * indexs.length) / 2;
    }

    // 处理Touch事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // 按下字母的下标
                int letterIndex = (int) ((event.getY() - mMarginTop) / mCellHeight);
                if (letterIndex != touchIndex) {
                    if (letterIndex > indexs.length - 1) {
                        touchIndex = indexs.length - 1;
                    } else if (letterIndex < 0) {
                        touchIndex = 0;
                    } else {
                        touchIndex = letterIndex;
                    }
                }
                // 判断是否越界
                if (letterIndex >= 0 && letterIndex < indexs.length) {
                    // 显示按下的字母
                    if (textView != null) {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText(indexs[letterIndex]);
                    }
                    //通过回调方法通知列表定位
                    if (mOnIndexChangedListener != null) {
                        mOnIndexChangedListener.onIndexChanged(indexs[letterIndex]);
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (textView != null) {
                    textView.setVisibility(View.GONE);
                }
                break;
        }

        return true;
    }

    public void setTouchIndex(String word) {
        for (int i = 0; i < indexs.length; i++) {
            if (indexs[i].equals(word)) {
                touchIndex = i;
                invalidate();
                return;
            }
        }
    }

    public interface OnIndexChangedListener {
        /**
         * 按下字母改变了
         *
         * @param index 按下的字母
         */
        void onIndexChanged(String index);
    }

    private OnIndexChangedListener mOnIndexChangedListener;

    private TextView textView;

    public void setOnIndexChangedListener(OnIndexChangedListener onIndexChangedListener) {
        this.mOnIndexChangedListener = onIndexChangedListener;
    }

    /**
     * 设置显示按下首字母的TextView
     */
    public void setSelectedIndexTextView(TextView textView) {
        this.textView = textView;
    }
}
