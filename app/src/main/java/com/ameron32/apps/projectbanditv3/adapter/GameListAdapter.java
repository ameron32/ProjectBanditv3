package com.ameron32.apps.projectbanditv3.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.object.Game;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;



public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.ViewHolder> {
  
  class ViewHolder extends RecyclerView.ViewHolder {

    @InjectView(android.R.id.button1) Button button;
    @InjectView(R.id.textview_description) TextView description;
    
    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.inject(this, itemView);
    }
  }

  public static class GameClickListener implements OnClickListener {
  
    private Game mGame;
    private GameChangeListener mListener;
  
    public GameClickListener(Game game, GameChangeListener listener) {
      mGame = game;
      mListener = listener;
    }
    
    @Override public void onClick(View v) {
      mListener.onGameChange(mGame);
    }
  }

  public interface GameChangeListener {
    public void onGameChange(Game game);
  }

  private List<Game> mGames;
  private GameChangeListener mListener;
  
  public GameListAdapter(List<Game> games, GameChangeListener listener) {
    mGames = games;
    mListener = listener;
  }

  @Override public int getItemCount() {
    return mGames.size();
  }
  
  @Override public ViewHolder onCreateViewHolder(
      ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_game_listitem_textview, parent, false);
    ViewHolder holder = new ViewHolder(v);
    return holder;
  }

  @Override public void onBindViewHolder(
      ViewHolder holder, int position) {
    final Game game = mGames.get(position);
    holder.button.setText(game.getName());
    holder.description.setText(game.getDescription());
    holder.itemView.setOnClickListener(new GameClickListener(game, mListener));
    holder.button.setOnClickListener(new GameClickListener(game, mListener));
  }
}
