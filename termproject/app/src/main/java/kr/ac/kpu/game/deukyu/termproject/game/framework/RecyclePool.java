package kr.ac.kpu.game.deukyu.termproject.game.framework;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclePool {
    private HashMap<Class, ArrayList<Object>> map = new HashMap<>();

    public RecyclePool() {
    }
    public void add(Object obj){
        Class claszz = obj.getClass();
        ArrayList<Object> list = map.get(claszz);
        if(list == null){
            list = new ArrayList<>();
            map.put(claszz, list);
        }
        list.add(obj);
    }

    public Object get(Class clazz){
        ArrayList<Object> list = map.get(clazz);
        if(list == null){
            return null;
        }
        if(list.size() == 0){
            return null;
        }

        return list.remove(0);
    }
}
