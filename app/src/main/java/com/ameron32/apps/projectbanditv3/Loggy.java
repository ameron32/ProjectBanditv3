package com.ameron32.apps.projectbanditv3;

import android.util.Log;
import android.util.SparseArray;

import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * TODO remove this when not needed
 * 
 * @author LeMeilleur
 *
 */
public class Loggy {
  
  private static SparseArray<Record> records = new SparseArray<Record>();
  private static Random r = new Random();
  
  private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(false);
  
  public static int start(
      final String name) {
    final int id = r.nextInt(Integer.MAX_VALUE);
    final Record record = new Record(name, System.nanoTime());
    
    lock.writeLock().lock();
    try {
      records.append(id, record);
    } finally {
      lock.writeLock().unlock();
    }
    return id;
  }
  
  public static void stop(final int id) {
    long time = System.nanoTime();
    
    lock.readLock().lock();
    try {
      final Record record = records.get(id);
      time = time - record.startTime;
      Log.d("LOGGY", record.name + id
          + ": time[" + time + "]");
    } finally {
      lock.readLock().unlock();
    }
  }
  
  public static class Record {
    
    public String name;
    public long startTime;
    
    public Record(String name,
        long startTime) {
      this.name = name;
      this.startTime = startTime;
    }
  }
  
}
