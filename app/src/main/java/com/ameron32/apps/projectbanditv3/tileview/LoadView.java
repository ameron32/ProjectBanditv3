package com.ameron32.apps.projectbanditv3.tileview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.ameron32.apps.projectbanditv3.R;


public class LoadView extends RelativeLayout {

    public LoadView(Context context) {
        super(context);
    }

    public LoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    View progress;
    private void init() {
        progress = LayoutInflater.from(this.getContext()).inflate(R.layout.misc_loadview, this, false);
        this.addView(progress);
        progress.setVisibility(View.INVISIBLE);
    }

    Action action;
    public void performAction(final Runnable r) {
        action = new Action(r);
        action.execute();
    }

    private class Action extends AsyncTask<Void, Void, Void> {

        Runnable mRunnable;

        private Action(final Runnable r) {
            mRunnable = r;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            mRunnable.run();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress.setVisibility(View.INVISIBLE);
        }
    }
}
