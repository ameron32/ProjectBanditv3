package com.ameron32.apps.projectbanditv3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.ameron32.apps.projectbanditv3.Query;
import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.manager.CharacterManager;
import com.ameron32.apps.projectbanditv3.object.Character;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.ameron32.apps.projectbanditv3.parseui.ParseQueryAdapter.QueryFactory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class CharacterSelectorAdapter
    extends RecyclerView.Adapter<CharacterSelectorAdapter.ViewHolder>
{

  class ViewHolder extends RecyclerView.ViewHolder {

    @InjectView(R.id.imageview) ImageView imageView;
    @InjectView(R.id.outline) FrameLayout outline;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.inject(this, itemView);
    }
  }

  private Context context;
  private List<com.ameron32.apps.projectbanditv3.object.Character> mDataset;
  private QueryFactory<Character> factory;

  public CharacterSelectorAdapter(Context context) {
    super();
    this.context = context;
    factory = makeQueryFactory();
    loadObjects();
  }

  public void loadObjects() {
    factory.create().findInBackground(new FindCallback<Character>() {

      @Override public void done(
          List<Character> characters,
          ParseException e) {
        if (e == null) {
          mDataset = characters;
          notifyDataSetChanged();
        }
      }
    });
  }

//  private TwoWayGridView gridView;

  public static QueryFactory<Character> makeQueryFactory() {
    QueryFactory<Character> factory = new QueryFactory<Character>() {

      @Override public ParseQuery<Character> create() {
        return Query._Character.getPlayableCharacters();
      }};
    return factory;
  }

//  public CharacterSelectorAdapter_v2(
//      Context context) {
//    super(context, makeQueryFactory());
//    this.context = context;
//    this.gridView = gridView;
//  }

//  @Override public View getItemView(
//      Character character, View v,
//      ViewGroup container) {
//    if (v == null) {
//      v = LayoutInflater.from(context).inflate(R.layout.row_character_icon, container, false);
//    }
//    v = super.getItemView(character, v, container);
//    ButterKnife.inject(this, v);

  private void onBind(Context context, ViewHolder holder, Character character) {
    final String picUrl = character.getString("profilePicUrl");
    Picasso.with(context).load(picUrl)
      .placeholder(R.drawable.ic_launcher)
      .error(R.drawable.ic_bandit_clear)
      .resizeDimen(R.dimen.image_character_length_squared_large, R.dimen.image_character_length_squared_large)
      .into(holder.imageView);
    characters.add(character);
    holder.imageView.setTag(character);

//    return v;
  }

  private final List<Character> characters = new ArrayList<Character>();
  private int mCurrentSelectedPosition = 0;

  @OnClick(R.id.imageview)
  void onClick(View v) {
    Character character = (Character) v.getTag();
    int position = getPositionFromCharacter(character);
    if (position != -1) {
      setSelection(position);
      // gridView.setSelection(position);
    } else {
      Toast.makeText(context, "No character for " + position, Toast.LENGTH_SHORT).show();
    }
  }

  public void setSelection(int position) {
    Character character = mDataset.get(position);
    Toast.makeText(context, "Switched to: " + character.getString("name"), Toast.LENGTH_SHORT).show();
    CharacterManager.get().setCurrentCharacter(character);

    mCurrentSelectedPosition = position;

    notifyDataSetChanged();
  }

  private int getPositionFromCharacter(Character character) {
    for (int i = 0; i < characters.size(); i++) {
      if (characters.get(i).equals(character)) { return i; }
    }
    return -1;
  }

  @Override public int getItemCount() {
    if (mDataset != null) {
      return mDataset.size();
    } else {
      return 0;
    }
  }

  @Override public void onBindViewHolder(
      ViewHolder holder, int position) {
    Character character = mDataset.get(position);
    onBind(context, holder, character);
    displaySelected(holder, position);
  }

  private void displaySelected(ViewHolder holder, int position) {
    boolean isSelected = isSelected(position);

    View child = holder.itemView;
    View outline = child.findViewById(R.id.outline);
    int bgColor = 0;

    // if not selected, reset color to clear
    if (!isSelected) {
      bgColor = context.getResources().getColor(android.R.color.transparent);
    }

    // if selected, set color
    if (isSelected) {
      bgColor = context.getResources().getColor(R.color.character_toolbar_selected_outline);
    }

    outline.setBackgroundColor(bgColor);
  }

  private boolean isSelected(int position) {
    return position == mCurrentSelectedPosition;
  }

  @Override public ViewHolder onCreateViewHolder(
      ViewGroup parent, int viewType) {
    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_character_icon, parent, false);
    ViewHolder holder = new ViewHolder(v);
    return holder;
  }
}
