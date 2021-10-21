package com.example.a2048;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

public class BorderedTextView extends AppCompatTextView {
    private Paint paint = new Paint();
    private int color;

    public BorderedTextView(Context context) {
        super(context);
        init();
    }

    public BorderedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        paint.setStyle(Paint.Style.FILL);
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
        Log.i("COOLOOR",Integer.toString(color));
    }

    public int getColor() {
        return color;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        canvas.drawRoundRect(0,0,getWidth(),getHeight(),10,10,paint);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        canvas.drawRoundRect(0,0,getWidth(),getHeight(),10,10,paint);
        super.onDraw(canvas);
        /**
        canvas.drawLine(0, 0, getWidth(), 0, paint);
        canvas.drawLine(getWidth(), 0, getWidth(), getHeight(), paint);
        canvas.drawLine(0, getHeight(), getWidth(), getHeight(), paint);
        canvas.drawLine(0, 0, 0, getHeight(), paint);**/
        }
    }


