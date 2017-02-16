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
import android.view.MotionEvent;
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
        //canvas.drawColor(Color.RED);
        //canvas.drawLine(0, 0, 200, 100, paint);

        //botó1

        r1.top=(menu.getHeight()*122)/1000; // a una pantalla de amplada 1000 la cantonada estaria al pixel 131
        r1.left=(menu.getWidth()*1272)/1000;
        r1.right=(menu.getWidth()*1816)/1000;
        r1.bottom=(menu.getHeight()*277)/1000;

        //r1.top=(menu.getHeight()-122)/menu.getHeight(); //percentatge sobre el tamany original
       // r1.left=(menu.getWidth()-1272)/menu.getWidth();
       // r1.right=(menu.getWidth()-1816)/menu.getWidth();
       // r1.bottom=(menu.getHeight()-277)/menu.getHeight();

        r2.top=getHeight();
        r2.left=getWidth();
        r2.right=getWidth();
        r2.bottom=getHeight();

        //canvas.drawBitmap(menu, r1, r2, paint); //origen, rectacngle original, rectangle destí
        //decidir una amplada per ex 1000, i despres multiplicar pel tamany real i dividir per mil
        //utilitzar touch events, que defineixen una àrea de la pantalla

        //menu 1272 x 122 superior esquerre. inferior dret 1816 x 227
        //original 1920 x 1078

    }






}
