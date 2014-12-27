package com.ameron32.apps.projectbanditv3;

import android.os.Handler;
import android.os.Looper;

import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SaveObjectSerialExecutor extends ThreadPoolExecutor {

    private static final int START_THREAD_POOL_SIZE = 1;
    private static final int MAX_THREAD_POOL_SIZE = 1;
    private static final long KEEP_ALIVE_TIME = 1;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private static final int MAX_PENDING_SAVE_REQUESTS = 100;

    public static SaveObjectSerialExecutor executor;

    public static SaveObjectSerialExecutor get() {
        if (executor == null) {
            executor = new SaveObjectSerialExecutor();
        }
        return executor;
    }

    private final Handler handler;
    final Queue tasks = new ArrayDeque();
    //    final Executor executor;
    Runnable active;
    Thread thread;

    private SaveObjectSerialExecutor() {
        super(START_THREAD_POOL_SIZE,
                MAX_THREAD_POOL_SIZE,
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                new LinkedBlockingQueue<Runnable>(MAX_PENDING_SAVE_REQUESTS));

        handler = new Handler(Looper.getMainLooper());
    }

    public boolean sendMessage(
            ParseObject object, OnSaveCallbacks callbacks) {
        enqueue(new SaveRunnable(object), callbacks);
        return true;
    }

    public synchronized void enqueue(final Runnable r, final OnSaveCallbacks callbacks) {
        execute(new QueueRunnable(r, callbacks));
    }

//    public synchronized void execute(final Runnable r) {
//        if (thread == null) {
//            thread = new Thread();
//            thread.start();
//        }
//
//        tasks.offer(r);
//        if (active == null) {
//            scheduleNext();
//        }
//    }

//    protected synchronized void scheduleNext() {
//        final Object poll = tasks.poll();
//        if ((active = (Runnable) poll) != null) {
//            executor.execute(active);
//        }
//    }

    public interface OnSaveCallbacks {

        public void onBegin();

        public void onComplete();
    }

    private class SaveRunnable implements Runnable {

        private static final boolean LOGGY = true;

        private final ParseObject object;

        SaveRunnable(ParseObject object) {
            this.object = object;
        }

        @Override
        public void run() {
            int saveObject = 0;
            if (LOGGY) {
                saveObject = Loggy.start("saveObject");
            }

            try {
                object.save();
                if (LOGGY) {
                    Loggy.stop(saveObject);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                if (LOGGY) {
                    Loggy.stop(saveObject);
                }
            }
        }
    }

    private class QueueRunnable implements Runnable {

        private final Runnable r;
        private final OnSaveCallbacks callbacks;

        public QueueRunnable(final Runnable r, final OnSaveCallbacks callbacks) {
            this.r = r;
            this.callbacks = callbacks;
        }

        @Override
        public void run() {
            try {
                if (callbacks != null) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            callbacks.onBegin();
                        }
                    });

                }
                r.run();
            } finally {
                if (callbacks != null) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            callbacks.onComplete();
                        }
                    });

                }
//                scheduleNext();
            }
        }
    }
}
