package com.ameron32.apps.projectbanditv3.manager;

import android.os.Bundle;
import android.support.v4.app.Fragment;

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
    //
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
    addSubscription();
  }

  private void addSubscription() {

  }

  private void example(List<ParseObject> objects) {
    // putPusher()

    final Observer<ParseObject> observer = _getPushWatcher();

    for (ParseObject obj : objects) {
      final Observable<ParseObject> observable = _getPush(obj);
      _subscription =
          AndroidObservable.bindFragment(this, observable)
          .subscribeOn(Schedulers.computation())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(observer);
    }
  }


  private Observable<Boolean> _getObservable() {
    return Observable.create(new Observable.OnSubscribe<Boolean>() {

      @Override
      public void call(Subscriber<? super Boolean> subscriber) {
//        _log("Within Observable");
//        _doSomeLongOperation_thatBlocksCurrentThread();
        subscriber.onNext(true);
        subscriber.onCompleted();
      }
    });
  }

  private Observer<Boolean> _getObserver() {
    return new Observer<Boolean>() {

      @Override
      public void onCompleted() {
//        _log("OnCompleted");
//        _progress.setVisibility(View.INVISIBLE);
      }

      @Override
      public void onError(Throwable e) {
        e.printStackTrace();
//        _log(String.format("BooError %s", e.getMessage()));
//        _progress.setVisibility(View.INVISIBLE);
      }

      @Override
      public void onNext(Boolean aBoolean) {
//        _log(String.format("onNext with return value \"%b\"", aBoolean));
      }
    };
  }

  private Observable<ParseObject> _getPush(final ParseObject object) {
    return Observable.create(new Observable.OnSubscribe<ParseObject>() {

      @Override
      public void call(Subscriber<? super ParseObject> subscriber) {
//        _log("Within Observable");
//        _doSomeLongOperation_thatBlocksCurrentThread();
        try {
          object.save();
          subscriber.onNext(object);
          subscriber.onCompleted();
        } catch (ParseException e) {
          e.printStackTrace();
          subscriber.onError(e);
        }
      }
    });
  }

  private Observer<ParseObject> _getPushWatcher() {
    return new Observer<ParseObject>() {

      @Override
      public void onNext(ParseObject parseObject) {

      }

      @Override
      public void onCompleted() {

      }

      @Override
      public void onError(Throwable e) {

      }
    };
  }
}
