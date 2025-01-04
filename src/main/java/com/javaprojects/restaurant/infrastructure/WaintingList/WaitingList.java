package com.javaprojects.restaurant.infrastructure.WaintingList;
import java.util.LinkedList;
import java.util.Queue;

import com.javaprojects.restaurant.infrastructure.entity.ReservationEntity;

public class WaitingList {
    private Queue<String> waintingQueue = new LinkedList<>();

    public ReservationEntity addToQueue(String userEmail){
        waintingQueue.add(userEmail);
                return null;
    }

    public String removeFromQueue(){
        return waintingQueue.poll();
    }

    public boolean isQueueEmpty(){
        return waintingQueue.isEmpty();
    }

    public int getQueueSize(){
        return waintingQueue.size();
    }

    public String getFirstInQueue(){
        return waintingQueue.peek();
    }
}
