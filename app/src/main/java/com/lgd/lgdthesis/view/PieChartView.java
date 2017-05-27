package com.lgd.lgdthesis.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.lgd.lgdthesis.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 蜗牛 on 2017-05-27.
 */

public class PieChartView extends View {
    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    /**
     * ��ͼ�뾶
     */
    private float pieChartCircleRadius = 100;

    private float textBottom;
    /**
     * ��¼���ִ�С
     */
    private float mTextSize = 14;

    /**
     * ��ͼ��ռ�������򣨲��������֣�
     */
    private RectF pieChartCircleRectF = new RectF();

    /**
     * ��״ͼ��Ϣ�б�
     */
    private List<PieceDataHolder> pieceDataHolders = new ArrayList<>();


    /**
     * �����߳���
     */
    private float markerLineLength = 30f;

    public PieChartView(Context context) {
        super(context);
        init(null, 0);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PieChartView, defStyle, 0);

        pieChartCircleRadius = a.getDimension(
                R.styleable.PieChartView_circleRadius,
                pieChartCircleRadius);

        mTextSize = a.getDimension(R.styleable.PieChartView_textSize, mTextSize) / getResources().getDisplayMetrics().density;

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);


        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    private void invalidateTextPaintAndMeasurements() {

        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, getContext().getResources().getDisplayMetrics()));

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.descent - fontMetrics.ascent;
        textBottom = fontMetrics.bottom;
    }

    /**
     * ���ñ�״ͼ�İ뾶
     *
     * @param pieChartCircleRadius ��״ͼ�İ뾶��px��
     */
    public void setPieChartCircleRadius(int pieChartCircleRadius) {

        this.pieChartCircleRadius = pieChartCircleRadius;

        invalidate();
    }

    /**
     * ���ñ����ߵĳ���
     *
     * @param markerLineLength �����ߵĳ��ȣ�px��
     */
    public void setMarkerLineLength(int markerLineLength) {
        this.markerLineLength = markerLineLength;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        initPieChartCircleRectF();

        drawAllSectors(canvas);

    }

    private void drawAllSectors(Canvas canvas) {
        float sum = 0f;
        for (PieceDataHolder pieceDataHolder : pieceDataHolders) {
            sum += pieceDataHolder.value;
        }

        float sum2 = 0f;
        for (PieceDataHolder pieceDataHolder : pieceDataHolders) {
            float startAngel = sum2 / sum * 360;
            sum2 += pieceDataHolder.value;
            float sweepAngel = pieceDataHolder.value / sum * 360;

            drawSector(canvas, pieceDataHolder.color, startAngel, sweepAngel);
            drawMarkerLineAndText(canvas, pieceDataHolder.color, startAngel + sweepAngel / 2, pieceDataHolder.marker);
        }
    }

    private void initPieChartCircleRectF() {
        pieChartCircleRectF.left = getWidth() / 2 - pieChartCircleRadius;
        pieChartCircleRectF.top = getHeight() / 2 - pieChartCircleRadius;
        pieChartCircleRectF.right = pieChartCircleRectF.left + pieChartCircleRadius * 2;
        pieChartCircleRectF.bottom = pieChartCircleRectF.top + pieChartCircleRadius * 2;
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.(sp)
     */
    public float getTextSize() {
        return mTextSize;
    }

    /**
     * Sets the view's text dimension attribute value. In the PieChartView view, this dimension
     * is the font size.
     *
     * @param textSize The text dimension attribute value to use.(sp)
     */
    public void setTextSize(float textSize) {
        mTextSize = textSize;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * ���ñ�״ͼҪ��ʾ������
     *
     * @param data �б�����
     */
    public void setData(List<PieceDataHolder> data) {

        if (data != null) {
            pieceDataHolders.clear();
            pieceDataHolders.addAll(data);
        }

        invalidate();
    }

    /**
     * ��������
     *
     * @param canvas     ����
     * @param color      Ҫ�������ε���ɫ
     * @param startAngle ��ʼ�Ƕ�
     * @param sweepAngle �����Ƕ�
     */
    protected void drawSector(Canvas canvas, int color, float startAngle, float sweepAngle) {

        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);

        canvas.drawArc(pieChartCircleRectF, startAngle, sweepAngle, true, paint);

    }

    /**
     * ���Ʊ�ע�ߺͱ�������
     *
     * @param canvas      ����
     * @param color       ���ǵ���ɫ
     * @param rotateAngel �����ߺ�ˮƽ������ת�ĽǶ�
     */
    protected void drawMarkerLineAndText(Canvas canvas, int color, float rotateAngel, String text) {
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(color);

        Path path = new Path();
        path.close();
        path.moveTo(getWidth() / 2, getHeight() / 2);
        final float x = (float) (getWidth() / 2 + (markerLineLength + pieChartCircleRadius) * Math.cos(Math.toRadians(rotateAngel)));
        final float y = (float) (getHeight() / 2 + (markerLineLength + pieChartCircleRadius) * Math.sin(Math.toRadians(rotateAngel)));
        path.lineTo(x, y);
        float landLineX;
        if (270f > rotateAngel && rotateAngel > 90f) {
            landLineX = x - 20;
        } else {
            landLineX = x + 20;
        }
        path.lineTo(landLineX, y);
        canvas.drawPath(path, paint);

        mTextPaint.setColor(color);
        if (270f > rotateAngel && rotateAngel > 90f) {
            float textWidth = mTextPaint.measureText(text);
            canvas.drawText(text, landLineX - textWidth, y + mTextHeight / 2 - textBottom, mTextPaint);

        } else {
            canvas.drawText(text, landLineX, y + mTextHeight / 2 - textBottom, mTextPaint);
        }

    }

    /**
     * ��״ͼÿ������Ϣ������
     */
    public static final class PieceDataHolder {

        /**
         * ÿ�����ε�ֵ�Ĵ�С
         */
        private float value;

        /**
         * ���ε���ɫ
         */
        private int color;

        /**
         * ÿ���ı���
         */
        private String marker;


        public PieceDataHolder(float value, int color, String marker) {
            this.value = value;
            this.color = color;
            this.marker = marker;
        }
    }

}
