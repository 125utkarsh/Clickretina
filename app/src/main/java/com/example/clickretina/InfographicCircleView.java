package com.example.clickretina;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class InfographicCircleView extends View {

    private Paint backgroundPaint;
    private Paint foregroundPaint;
    private Paint textPaint;
    private int progress = 0;
    private int max = 100;

    public InfographicCircleView(Context context) {
        super(context);
        init();
    }

    public InfographicCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InfographicCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(0xFFD3D3D3); // Light gray color
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(20);
        backgroundPaint.setAntiAlias(true);

        foregroundPaint = new Paint();
        foregroundPaint.setColor(0xFFFFA500); // Orange color
        foregroundPaint.setStyle(Paint.Style.STROKE);
        foregroundPaint.setStrokeWidth(20);
        foregroundPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(0xFFFFA500); // Orange color
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2 - 20;

        canvas.drawCircle(width / 2, height / 2, radius, backgroundPaint);

        float angle = 360 * progress / (float) max;
        canvas.drawArc(20, 20, width - 20, height - 20, -90, angle, false, foregroundPaint);

        String progressText = progress + " / " + max;
        canvas.drawText(progressText, width / 2, height / 2 + 20, textPaint);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    public void setMax(int max) {
        this.max = max;
        invalidate();
    }
}
