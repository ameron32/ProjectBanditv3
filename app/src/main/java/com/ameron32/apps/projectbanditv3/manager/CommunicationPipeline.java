package com.ameron32.apps.projectbanditv3.manager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.fragment.AbsContentFragment;
import com.parse.ParseException;
import com.parse.ParseObject;

import org.jetbrains.annotations.Nullable;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by klemeilleur on 1/21/2015.
 * Intended as BackgroundOnly Fragment
 */
public class CommunicationPipeline extends Fragment {

  private Subscription _subscription;

  public CommunicationPipeline() {
    // required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setRetainInstance(true);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (_subscription != null) {
      _subscription.unsubscribe();
    }
  }

  /**
   * FROM PARSE RECEIVER
   */
  public void onReceive() {
//    enqueue();
  }

  public void example() {
    // putPusher()

    final Observer<ParseObject> observer = _getObserver();

    final Observable<ParseObject> observable = _getObservable();
//    final Observable<ParseObject> observable = Observable.from(objects.toArray(new ParseObject[objects.size()]));
    _subscription =
        AndroidObservable.bindFragment(this, observable)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);

  }


  private Observable<ParseObject> _getObservable() {
    return Observable.create(new Observable.OnSubscribe<ParseObject>() {

      @Override
      public void call(Subscriber<? super ParseObject> subscriber) {
        _log("Within Observable");
//        _doSomeLongOperation_thatBlocksCurrentThread();
        subscriber.onNext(null);
        subscriber.onCompleted();
      }
    });
  }

  private Observer<ParseObject> _getObserver() {
    return new Observer<ParseObject>() {

      @Override
      public void onCompleted() {
        _log("OnCompleted");
//        _progress.setVisibility(View.INVISIBLE);
      }

      @Override
      public void onError(Throwable e) {
        e.printStackTrace();
        _log(String.format("BooError %s", e.getMessage()));
//        _progress.setVisibility(View.INVISIBLE);
      }

      @Override
      public void onNext(ParseObject aBoolean) {
        _log(String.format("onNext with return value "+ aBoolean));
      }
    };
  }

  private void _log(String format) {
    Log.d("_log", format);
  }
}
