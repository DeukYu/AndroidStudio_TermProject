package kr.ac.kpu.game.deukyu.termproject.ui.view;

import android.app.Service;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import kr.ac.kpu.game.deukyu.termproject.game.framework.GameWorld;
import kr.ac.kpu.game.deukyu.termproject.res.bitmap.SharedBitmap;
import kr.ac.kpu.game.deukyu.termproject.util.IndexTimer;

public class GameView extends SurfaceView {
    private static final String TAG = GameView.class.getSimpleName();
    public static final int FRAME_RATE_SECONDS = 10;
    private Rect mainRect;
    private Paint mainPaint;

    private GameWorld gameWorld;
    private IndexTimer timer;
    private boolean paused;
    public GameView(Context context) {
        super(context);
        initResources();
    }

    public GameView(Context context, @Nullable AttributeSet attrs){
        super(context, attrs);
        initResources();
    }

    private void initResources(){
        WindowManager wm = (WindowManager) getContext().getSystemService(Service.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        mainRect = new Rect(0,0, size.x, size.y);

        SharedBitmap.setResources((getResources()));
        mainPaint = new Paint();
        mainPaint.setColor(0xFFFFEEEE);

        gameWorld = GameWorld.get();
        gameWorld.setRect(mainRect);
        gameWorld.initResources(this);

        timer = new IndexTimer(FRAME_RATE_SECONDS, 1);
        postFrameCallBack();
    }

    private void postFrameCallBack(){
        if(paused){
            return;
        }
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                update(frameTimeNanos);
                invalidate();
                postFrameCallBack();
            }
        });
    }

    @Override
    protected void draw(Canvas canvas) {
        canvas.drawRect(mainRect, mainPaint);
        gameWorld.draw(canvas);
    }

    private int count;
    public void update(long frameTimeNanos){
        gameWorld.update(frameTimeNanos);
        count++;
        if(timer.done()){
            Log.d(TAG, "Frame Per Second = " + ((float)count / FRAME_RATE_SECONDS));
            count = 0;
            timer.reset();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gameWorld.onTouchEvent(event);
    }

    public void pause() {
        this.paused = true;
    }

    public void resume() {
        this.paused = false;
    }
}
