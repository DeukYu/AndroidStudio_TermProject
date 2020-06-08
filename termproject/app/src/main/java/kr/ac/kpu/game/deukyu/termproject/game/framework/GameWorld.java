package kr.ac.kpu.game.deukyu.termproject.game.framework;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import kr.ac.kpu.game.deukyu.termproject.game.iface.GameObject;
import kr.ac.kpu.game.deukyu.termproject.game.iface.Recyclable;

public abstract class GameWorld {
    private static final String TAG = GameWorld.class.getSimpleName();
    protected View view;
    protected long frameTimeNanos;
    protected long timeDiffNanos;
    protected RecyclePool recyclePool = new RecyclePool();


    public static GameWorld get(){
        if(singleton == null){
            Log.e(TAG, "GameWorld subclass not created");
        }
        return singleton;
    }
    protected static GameWorld singleton;

    protected Rect rect;

    protected GameWorld(){
    }

    public RecyclePool getRecyclePool(){
        return this.recyclePool;
    }

    public ArrayList<GameObject> objectsAt(int index) {
        return layers.get(index);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    public void initResources(View view) {
        this.view = view;
        initLayers();
        initObjects();
    }

    protected void initLayers() {
        layers = new ArrayList<>();
        int layerCount = getLayerCount();
        for(int i = 0; i < layerCount; i++){
            ArrayList<GameObject> layer = new ArrayList<>();
            layers.add(layer);
        }
    }

    abstract protected int getLayerCount();

    public void initObjects(){
    }

    protected ArrayList<ArrayList<GameObject>> layers;

    public void draw(Canvas canvas) {
        for(ArrayList<GameObject> objects : layers){
            for(GameObject o : objects){
                o.draw(canvas);
            }
        }
    }

    public long getTimeDiffNanos() {
        return timeDiffNanos;
    }
    public float getTimeDiffInSecond(){
        return (float)(timeDiffNanos / 1_000_000_000.0);
    }

    public long getCurrentTimeNanos() {
        return frameTimeNanos;
    }

    public void update(long frameTimeNanos) {
        this.timeDiffNanos = frameTimeNanos - this.frameTimeNanos;
        this.frameTimeNanos = frameTimeNanos;

        if(rect == null){
            return;
        }

        for(ArrayList<GameObject> objects : layers){
            for(GameObject o : objects) {
                o.update();
            }
        }
        if(trash.size() > 0 ){
            removeTrashObjects();
        }
        trash.clear();
    }

    protected void removeTrashObjects() {
        for (int tIndex = trash.size() - 1; tIndex >= 0; tIndex--) {
            GameObject tobj = trash.get(tIndex);
            for (ArrayList<GameObject> objects : layers) {
                int index = objects.indexOf(tobj);
                if(index >= 0){
                    objects.remove(index);
                    break;
                }
            }
            trash.remove(tIndex);
            if(tobj instanceof Recyclable){
                ((Recyclable) tobj).recycle();
                getRecyclePool().add(tobj);
            }
        }
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }
    public int getLeft(){
        return rect.left;
    }
    public int getTop(){
        return rect.top;
    }
    public int getRight(){
        return rect.right;
    }
    public int getBottom(){
        return rect.bottom;
    }

    public Resources getResources() {
        return view.getResources();
    }

    public void add(final int index, final GameObject obj) {
        view.post(new Runnable() {
            @Override
            public void run() {
                ArrayList<GameObject> objects = layers.get(index);
                objects.add(obj);
            }
        });
    }

    protected ArrayList<GameObject> trash = new ArrayList<>();
    public void remove(GameObject obj) {
        trash.add(obj);
    }

    public Context getContext() {
        return view.getContext();
    }

    public void pause() {

    }

    public void resume() {

    }
}
