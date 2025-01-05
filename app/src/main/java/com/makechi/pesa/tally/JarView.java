package com.makechi.pesa.tally;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class JarView extends View {

    private Paint jarPaint;
    private Paint liquidPaint;
    private float progress = 0.5f;
    private float waveOffset = 0f;
    private ValueAnimator waveAnimator;

    public JarView(Context context) {
        super(context);
        init();
    }

    public JarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        jarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        jarPaint.setStyle(Paint.Style.STROKE);
        jarPaint.setColor(0xFF90A4AE); // Jar color
        jarPaint.setStrokeWidth(8);

        liquidPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        liquidPaint.setStyle(Paint.Style.FILL);

        waveAnimator = ValueAnimator.ofFloat(0, 1);
        waveAnimator.setDuration(4000);
        waveAnimator.setRepeatCount(ValueAnimator.INFINITE);
        waveAnimator.setRepeatMode(ValueAnimator.REVERSE);
        waveAnimator.addUpdateListener(animation -> {
            waveOffset = (float) animation.getAnimatedValue();
            invalidate();
        });
        waveAnimator.start();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        float jarLeft = width * 0.2f;
        float jarRight = width * 0.8f;
        float jarTop = height * 0.1f;
        float jarBottom = height * 0.9f;
        float cornerRadius = 50f;

        // Draw the jar
        Path jarPath = new Path();
        RectF jarRect = new RectF(jarLeft, jarTop, jarRight, jarBottom);
        jarPath.addRoundRect(jarRect, cornerRadius, cornerRadius, Path.Direction.CW);
        canvas.drawPath(jarPath, jarPaint);

        // Clip the canvas to keep the liquid inside the jar
        canvas.save();
        canvas.clipPath(jarPath);

        // Calculate liquid height
        float liquidHeight = jarBottom - (jarBottom - jarTop) * progress;

        // Draw the liquid with improved animation
        Path liquidPath = new Path();
        float waveHeight = 10; // Height of the wave
        float waveLength = width / 2; // Length of the wave

        liquidPath.moveTo(jarLeft, liquidHeight);
        for (float x = jarLeft; x <= jarRight; x += 1) {
            // Combine two sine waves for smoother animation
            float y = (float) (liquidHeight
                    + waveHeight * Math.sin((x / waveLength + waveOffset) * 2 * Math.PI)
                    + waveHeight * 0.5 * Math.sin((x / waveLength * 0.5 + waveOffset * 1.5) * 2 * Math.PI));
            liquidPath.lineTo(x, y);
        }
        liquidPath.lineTo(jarRight, jarBottom); // Bottom-right corner
        liquidPath.lineTo(jarLeft, jarBottom); // Bottom-left corner
        liquidPath.close();

        liquidPaint.setColor(progress < 0.3 ? 0xFF64B5F6 : progress < 0.7 ? 0xFFFFD54F : 0xFF81C784);
        canvas.drawPath(liquidPath, liquidPaint);

        // Restore the canvas
        canvas.restore();
    }

    public void setProgress(float progress) {
        this.progress = Math.max(0, Math.min(progress, 1)); // Clamp progress
        invalidate();
    }

    public void stopWaveAnimation() {
        if (waveAnimator != null) {
            waveAnimator.cancel();
        }
    }

    public void startWaveAnimation() {
        if (waveAnimator != null && !waveAnimator.isRunning()) {
            waveAnimator.start();
        }
    }
}
