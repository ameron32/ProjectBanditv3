package com.ameron32.apps.projectbanditv3.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.manager.CharacterManager;
import com.ameron32.apps.projectbanditv3.manager.GameManager;
import com.ameron32.apps.projectbanditv3.object.CAction;
import com.ameron32.apps.projectbanditv3.object.Character;
import com.ameron32.apps.projectbanditv3.object.Message;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import org.json.JSONObject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.observables.AndroidObservable;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by klemeilleur on 1/20/2015.
 */
public class DEMORxJavaFragment extends SectionContainerTestFragment {

  private Subscription _subscription;

  public DEMORxJavaFragment() {
    // Required empty public constructor
  }

  @InjectView(R.id.progress)
  ProgressBar _progress;

  @InjectView(R.id.srv1)
  SuperRecyclerView _logsList;

  @Override
  public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    _setupLogger();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.reset(this);
    if (_subscription != null) {
      _subscription.unsubscribe();
    }
  }

  @OnClick(R.id.start_button)
  void start() {
    _progress.setVisibility(View.VISIBLE);
    _log("ButtonClicked");

    _subscription = AndroidObservable.bindFragment(this, _getObservable())
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(_getObserver());


  }

  private Observable<Boolean> _getObservable() {
    return Observable.create(new Observable.OnSubscribe<Boolean>() {

      @Override
      public void call(Subscriber<? super Boolean> subscriber) {
        _log("Within Observable");
        _doSomeLongOperation_thatBlocksCurrentThread();
        subscriber.onNext(true);
        subscriber.onCompleted();
      }
    });
  }

  private Observer<Boolean> _getObserver() {
    return new Observer<Boolean>() {

      @Override
      public void onCompleted() {
        _log("OnCompleted");
        _progress.setVisibility(View.INVISIBLE);
      }

      @Override
      public void onError(Throwable e) {
        e.printStackTrace();
        _log(String.format("BooError %s", e.getMessage()));
        _progress.setVisibility(View.INVISIBLE);
      }

      @Override
      public void onNext(Boolean aBoolean) {
        _log(String.format("onNext with return value \"%b\"", aBoolean));
      }
    };
  }

  @Override
  protected int onReplaceFragmentLayout(int storedLayoutResource) {
    return R.layout.demorxjavafragment;
  }

  private void _doSomeLongOperation_thatBlocksCurrentThread() {
    _log("performing long operation");

    try {
      Message secretMessage =
      Message.create().setType(Message.MessageType.SECRET)
        .setMessage("testing Secret message")
          .setType(Message.MessageType.SECRET)
        .setCharacter(CharacterManager.get().getChatCharacter())
        .setAction(ParseQuery.getQuery(CAction.class).get("rc7xowNm58"));
      ParseRelation<Character> relation = secretMessage.getRelation("secretToCharacter");
      relation.add(ParseQuery.getQuery(Character.class).get("OEcYqjmqo6"));
      relation.add(CharacterManager.get().getChatCharacter());
      secretMessage.send();

      Thread.sleep(3000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }








  public List<String> _logs;
  private LogAdapter _adapter;

  private void _log(String logMsg) {

    if (_isCurrentlyOnMainThread()) {
      _logs.add(logMsg + " (main thread) ");
      _adapter.notifyDataSetChanged();
    } else {
      _logs.add(logMsg + " (NOT main thread) ");

      // You can only do below stuff on main thread.
      new Handler(Looper.getMainLooper()).post(new Runnable() {

        @Override
        public void run() {
          _adapter.notifyDataSetChanged();
        }
      });
    }
  }

  private void _setupLogger() {
    _logs = new ArrayList<>();
    _logsList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    _adapter = new LogAdapter();
    _logsList.setAdapter(_adapter);
  }

  private boolean _isCurrentlyOnMainThread() {
    return Looper.myLooper() == Looper.getMainLooper();
  }

  class LogAdapter
      extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    public LogAdapter() {

    }

    @Override
    public LogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_simple_textview, parent, false);
      return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LogAdapter.ViewHolder holder, int position) {
      final String item = _logs.get(position);
      holder.textView.setText(item);
    }

    @Override
    public int getItemCount() {
      return _logs.size();
    }

    public void addAll(List<String> logs) {
      _logs.addAll(logs);
      notifyDataSetChanged();
    }

    public void clear() {
      _logs.clear();
      notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      @InjectView(android.R.id.text1)
      public TextView textView;
      public ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.inject(this, itemView);
      }
    }
  }
}
