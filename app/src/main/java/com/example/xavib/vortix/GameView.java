package com.example.xavib.vortix;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View {

    private Paint paint;
    private int level;
    private Bitmap menu;
    private Rect r1, r2;

    public GameView(Context context) { this(context, null, 0); }
    public GameView(Context context, AttributeSet attrs) { this(context, attrs, 0); }
    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(20);
        menu = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu);
        r1 = new Rect();
        r2 = new Rect();
    }

    @Override public void draw(Canvas canvas) {
        canvas.drawColor(Color.RED);
        canvas.drawLine(0, 0, 200, 100, paint);
        r1.top=0;
        r1.left=50;
        r1.right=menu.getWidth()-50;
        r1.bottom=menu.getHeight();
        r2.top=-50;
        r2.left=0;
        r2.right=getWidth();
        r2.bottom=getHeight();
        canvas.drawBitmap(menu, r1, r2, paint);
        //decidir una amplada per ex 1000, i despres multiplicar pel tamany real i dividir per mil
    }
}
