package com.ameron32.apps.projectbanditv3.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ameron32.apps.projectbanditv3.Loggy;
import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.object.*;
import com.ameron32.apps.projectbanditv3.object.Character;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQueryAdapter.QueryFactory;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;



public abstract class AbsMessageAdapter
    extends
    AbsParseSuperRecyclerQueryAdapter<Message, AbsMessageAdapter.ViewHolder>
//    RecyclerView.Adapter<AbsMessageAdapter.ViewHolder> 
{
  
  
  
//  private List<Message> items = new ArrayList<Message>();
//  private QueryFactory<Message> factory;
  private int itemLayout;
  private int systemLayout;
  private int gameLayout;

  
  public AbsMessageAdapter(
      Context context,
      QueryFactory<Message> factory,
      int itemLayout, int systemLayout, int gameLayout) {
	  super(factory);
    this.context = context;
//    this.factory = factory;
    this.itemLayout = itemLayout;
    this.systemLayout = systemLayout;
    this.gameLayout = gameLayout;
//    mListeners = new ArrayList<OnDataSetChangedListener>();
    
//    loadObjects();
  }
  
//  private List<OnDataSetChangedListener> mListeners;
//  
//  public void addOnDataSetChangedListener(OnDataSetChangedListener listener) {
//    mListeners.add(listener);
//  }
//  
//  public void removeOnDataSetChangedListener(OnDataSetChangedListener listener) {
//    if (mListeners.contains(listener)) {
//      mListeners.remove(listener);
//    }
//  }
//  
//  private void fireOnDataSetChanged() {
//    for (int i = 0; i < mListeners.size(); i++) {
//      mListeners.get(i).onDataSetChanged();
//    }
//  }
//  
//  public interface OnDataSetChangedListener {
//    public void onDataSetChanged();
//  }
  
  
  private static final int MESSAGE_TYPE_GAME = 2;
  private static final int MESSAGE_TYPE_SYSTEM = 1;
  private static final int MESSAGE_TYPE_STANDARD = 0;
  
  @Override public int getItemViewType(
      int position) {
    Message message = getItem(position);
    String messageType = message.getString("type");
    if (messageType != null && messageType.equals("System")) {
      return MESSAGE_TYPE_SYSTEM;
    } 
    else
    if (message.getCharacter().isPlayable()) {
      return MESSAGE_TYPE_GAME;
    }
    return MESSAGE_TYPE_STANDARD;
  }
  
  @Override public ViewHolder onCreateViewHolder(
      ViewGroup parent, int viewType) {
    
    View v = null;
    
    switch(viewType) {
    case MESSAGE_TYPE_SYSTEM:
      v = LayoutInflater.from(parent.getContext()).inflate(systemLayout, parent, false);
      return new ViewHolder(v);
//      break;
    case MESSAGE_TYPE_STANDARD:
      v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
      return new ViewHolder(v);
//      break;
    case MESSAGE_TYPE_GAME:
      v = LayoutInflater.from(parent.getContext()).inflate(gameLayout, parent, false);
      return new ViewHolder(v);
    }
    
    v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
    return new ViewHolder(v);
  }
  
  @Override public void onBindViewHolder(
      ViewHolder holder, int position) {
    
    switch(getItemViewType(position)) {
    case MESSAGE_TYPE_SYSTEM:
      getSystemItemView(getItem(position), holder.itemView);
      break;
    case MESSAGE_TYPE_GAME:
      bindItemView(getItem(position), holder);
      break;
    case MESSAGE_TYPE_STANDARD:
      bindItemView(getItem(position), holder);
      break;
    default:
      // none yet
    }
  }
  
//  @Override public int getItemCount() {
//    return items.size();
//  }
  
  public static class ViewHolder extends
      RecyclerView.ViewHolder {
    
    @InjectView(R.id.textview_message)
    public TextView messageText; 
    @InjectView(R.id.textview_channel)
    public TextView channelText; 
    @InjectView(R.id.textview_object_id)
    public TextView objectIdText; 
    @InjectView(R.id.textview_username)
    public TextView usernameText; 
    @InjectView(R.id.textview_character)
    public TextView characterText; 
    @InjectView(R.id.textview_action)
    public TextView actionText; 
    @InjectView(R.id.textview_created_time)
    public TextView timeText; 
    @Optional @InjectView(R.id.imageview_character)
    public ParseImageView characterImageView; 
    @Optional @InjectView(R.id.imageview_action)
    public ParseImageView actionImageView; 
    
    public ViewHolder(View v) {
      super(v);
      ButterKnife.inject(this, v);
    }
  }
  
  
  
  
  
  private static final String TAG = AbsMessageAdapter.class.getSimpleName();
  private static final boolean TOAST = false;
  private static final boolean LOG = false;
  private static final boolean ERROR = false;
  
  private final Context context;
  
//  public void loadObjects() {
//    factory.create().findInBackground(new FindCallback<Message>() {
//      
//      @Override public void done(
//          List<Message> messages,
//          ParseException e) {
//        if (e == null) {
//          items = messages;
//          notifyDataSetChanged();
////          fireOnDataSetChanged();
//        }
//      }
//    });
//  }

  
  // Customize the layout by overriding getItemView
  public void bindItemView(Message m,
      ViewHolder vh) {
    
    final String objectId = m.getObjectId();
    
    vh.usernameText.setText(".........");
    vh.actionText.setText(".........");
    vh.characterText.setText(".........");
    vh.characterImageView.setImageResource(R.drawable.ic_launcher);
    vh.actionImageView.setImageResource(R.drawable.ic_launcher);
    vh.usernameText.setTag(objectId);
    vh.characterText.setTag(objectId);
    vh.actionText.setTag(objectId);
    vh.characterImageView.setTag(objectId);
    vh.actionImageView.setTag(objectId);
    
    ParseObject messageObject = m;
    String objectStr = messageObject.getObjectId();
    String messageStr = messageObject.getString("message");
    String channel = messageObject.getString("channel");
    Date createdAt = messageObject.getCreatedAt();
    String time = new SimpleDateFormat("h:mm aa M/d/yyyy", Locale.US).format(createdAt);
    
    // ParseUser user;
//    int logID = 0;
//    if (LOG) {
//      logID = Loggy.start("getItemView()--fetch");
//    }
    
    pullAdditionalQueryData(objectId, 
        vh.usernameText, 
        vh.characterText, 
        vh.actionText, 
        vh.characterImageView, 
        vh.actionImageView, 
        messageObject);
    
//    if (LOG) Loggy.stop(logID);
    
    vh.messageText.setText(messageStr + "");
    vh.channelText.setText(channel + "");
    vh.objectIdText.setText(objectStr + "");
    vh.timeText.setText(time + "");
  }
  
  private void pullAdditionalQueryData(
      final String objectId,
      final TextView usernameText,
      final TextView characterText,
      final TextView actionText,
      final ParseImageView characterImageView,
      final ParseImageView actionImageView,
      final ParseObject messageObject) {
    ParseUser userObject = messageObject.getParseUser("user");
    if (userObject != null) {
      userObject.fetchIfNeededInBackground(new GetUserCallback(usernameText, objectId));
    } else {
      usernameText.setText("none");
    }
    
    ParseObject characterObject = messageObject.getParseObject("character");
    if (characterObject != null) {
      characterObject.fetchIfNeededInBackground(new GetCharacterCallback(characterText, objectId, characterImageView));
    } else {
      characterText.setText("none");
    }
    
    ParseObject actionObject = messageObject.getParseObject("actionO");
    if (actionObject != null) {
      actionObject.fetchIfNeededInBackground(new GetActionCallback(actionText, objectId, actionImageView));
    } else {
      if (actionImageView != null) {
        actionImageView.setImageResource(android.R.color.transparent);
      }
    }
  }
  
  //Customize the layout by overriding getItemView
   public View getSystemItemView(Message m,
       View v) {
     // if (v == null) {
     // /*
     // * DETERMINE THE PREFERENCE SIZE OF THE ROW
     // */
     // int res = context.getSharedPreferences("size",
     // Context.MODE_PRIVATE).getInt("message_row", R.layout.row_message);
     // v = View.inflate(context, res, null);
     // }
     
     // super.getItemView(object, v, parent);
     
     // Add and download the image
     // ParseImageView todoImage = (ParseImageView)
     // v.findViewById(R.id.icon);
     // ParseFile imageFile = object.getParseFile("image");
     // if (imageFile != null) {
     // todoImage.setParseFile(imageFile);
     // todoImage.loadInBackground();
     // }
     
     // Add the title view
     // TextView titleTextView = (TextView) v.findViewById(R.id.text1);
     // titleTextView.setText(object.getString("title"));
     
     // Add a reminder of how long this item has been outstanding
     // TextView timestampView = (TextView) v.findViewById(R.id.timestamp);
     // timestampView.setText(object.getCreatedAt().toString());
     
     final String objectId = m.getObjectId();
     final TextView messageText = (TextView) v.findViewById(R.id.textview_message);
     final TextView channelText = (TextView) v.findViewById(R.id.textview_channel);
     final TextView objectIdText = (TextView) v.findViewById(R.id.textview_object_id);
     final TextView usernameText = (TextView) v.findViewById(R.id.textview_username);
     final TextView characterText = (TextView) v.findViewById(R.id.textview_character);
     final TextView actionText = (TextView) v.findViewById(R.id.textview_action);
     final TextView timeText = (TextView) v.findViewById(R.id.textview_created_time);
     final ParseImageView characterImageView = (ParseImageView) v.findViewById(R.id.imageview_character);
     final ParseImageView actionImageView = (ParseImageView) v.findViewById(R.id.imageview_action);
     
     usernameText.setText(".........");
     actionText.setText(".........");
     characterText.setText(".........");
  //   characterImageView.setImageResource(R.drawable.ic_launcher);
  //   actionImageView.setImageResource(R.drawable.ic_launcher);
     usernameText.setTag(objectId);
     characterText.setTag(objectId);
     actionText.setTag(objectId);
  //   characterImageView.setTag(objectId);
  //   actionImageView.setTag(objectId);
     
     ParseObject messageObject = m;
     String objectStr = messageObject.getObjectId();
     String messageStr = messageObject.getString("message");
     String channel = messageObject.getString("channel");
     Date createdAt = messageObject.getCreatedAt();
     String time = new SimpleDateFormat("h:mm aa M/d/yyyy", Locale.US).format(createdAt);
     
     // ParseUser user;
     int logID = 0;
     if (LOG) {
       logID = Loggy.start("getItemView()--fetch");
     }
     
     pullAdditionalSystemQueryData(objectId, usernameText, characterText, actionText, characterImageView, actionImageView, messageObject);
     
     if (LOG) Loggy.stop(logID);
     
     messageText.setText(messageStr + "");
     channelText.setText(channel + "");
     objectIdText.setText(objectStr + "");
     timeText.setText(time + "");
     
     return v;
   }

  private void pullAdditionalSystemQueryData(
      final String objectId,
      final TextView usernameText,
      final TextView characterText,
      final TextView actionText,
      final ParseImageView characterImageView,
      final ParseImageView actionImageView,
      final ParseObject messageObject) {
    ParseUser userObject = messageObject.getParseUser("user");
    if (userObject != null) {
      userObject.fetchIfNeededInBackground(new GetUserCallback(usernameText, objectId));
    } else {
      usernameText.setText("none");
    }
    
    ParseObject characterObject = messageObject.getParseObject("character");
    if (characterObject != null) {
      characterObject.fetchIfNeededInBackground(new GetCharacterCallback(characterText, objectId, characterImageView));
    } else {
      characterText.setText("none");
    }
    
    ParseObject actionObject = messageObject.getParseObject("actionO");
    if (actionObject != null) {
      actionObject.fetchIfNeededInBackground(new GetActionCallback(actionText, objectId, actionImageView));
    } else {
      actionImageView.setImageResource(android.R.color.transparent);
    }
    
    // final String actionStr = messageObject.getString("action");
    // if (actionStr != null) {
    // ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("CAction");
    // query.whereContains("action", actionStr);
    // // query.findInBackground(new FindCallback<ParseObject>() {
    // //
    // // @Override
    // // public void done(List<ParseObject> objects, ParseException e) {
    // // if (e == null) {
    // // // ACTION PIC -- IMAGE
    // // ParseObject object = objects.get(0);
    // // ParseFile actionPic = object.getParseFile("actionPic");
    // // if (actionPic == null) {
    // // Log.d("MPQA", "actionPic is null");
    // // return;
    // // }
    // //
    // // // actionPic.getDataInBackground(new
    // // // GetActionPicCallback(actionImageView, objectId));
    // // Picasso.with(context).load(actionPic.getUrl()).into(actionImageView);
    // // }
    // // else {
    // // e.printStackTrace();
    // // }
    // // }
    // // });
    // query.getFirstInBackground(new GetCallback<ParseObject>() {
    //
    // @Override
    // public void done(ParseObject object, ParseException e) {
    // if (e == null) {
    // // ACTION PIC -- IMAGE
    // ParseFile actionPic = object.getParseFile("actionPic");
    // if (actionPic == null) {
    // Log.d("MPQA", "actionPic is null");
    // return;
    // }
    //
    // // actionPic.getDataInBackground(new
    // // GetActionPicCallback(actionImageView, objectId));
    // Picasso.with(context).load(actionPic.getUrl()).into(actionImageView);
    // }
    // else {
    // e.printStackTrace();
    // }
    // }
    // });
    // }
    // else {
    // // invisible the icon
    // actionImageView.setImageResource(android.R.color.transparent);
    // }
  }
  
  
  
//  public interface OnQueryLoadListener<T> {
//    
//    public void onLoaded(
//        List<T> objects, Exception e);
//    
//    public void onLoading();
//  }
//  
//  private List<OnQueryLoadListener> listeners = new ArrayList<OnQueryLoadListener>();
//  
//  public void addOnQueryLoadListener(
//      OnQueryLoadListener listener) {
//    if (!(listeners.contains(listener))) {
//      listeners.add(listener);
//    }
//  }
//  
//  public void removeOnQueryLoadListener(
//      OnQueryLoadListener listener) {
//    if (listeners.contains(listener)) {
//      listeners.remove(listener);
//    }
//  }
  
  
  
  
  public static class GetUserCallback
      extends GetCallback<ParseUser> {
    
    private final WeakReference<TextView> weakView;
    private final String objectId;
    
    public GetUserCallback(
        final TextView textView,
        final String objectId) {
      this.objectId = objectId;
      weakView = new WeakReference<TextView>(textView);
    }
    
    @Override public void done(
        ParseUser user, ParseException e) {
      if (e == null) {
        doneNoError(user);
      } else {
        if (ERROR) e.printStackTrace();
      }
    }
    
    private void doneNoError(
        ParseUser user) {
      final TextView textView = weakView.get();
      if (textView == null) {
        // listview has moved on
        if (LOG)
          Log.d(TAG, "textView is null: ["
              + textView + "]");
        return;
      }
      
      if (user == null) {
        textView.setText("none");
        return;
      }
      
      String userName = user.getString("username");
      if (userName == null) {
        textView.setText("none");
        return;
      }
      
      if (textView.getTag().equals(objectId)) {
        // the view STILL has the same objectId associated with it
        textView.setText(userName + "");
      } else {
        if (LOG)
          Log.d(TAG, "objectId does not match: ["
              + textView.getTag()
              + "],[" + objectId + "]");
      }
    }
  }
  
  public class GetCharacterCallback
      extends GetCallback<ParseObject> {
    
    private final WeakReference<TextView> weakView;
    private final WeakReference<ImageView> weakImageView;
    private final String objectId;
    
    public GetCharacterCallback(
        final TextView textView,
        final String objectId,
        final ImageView imageView) {
      this.objectId = objectId;
      weakView = new WeakReference<TextView>(textView);
      weakImageView = new WeakReference<ImageView>(imageView);
    }
    
    @Override public void done(
        ParseObject characterObject,
        ParseException e) {
      if (e == null) {
        doneNoError(characterObject);
      } else {
        if (ERROR) e.printStackTrace();
      }
    }
    
    private void doneNoError(
        ParseObject characterObject) {
      final TextView characterText = weakView.get();
      final ImageView imageView = weakImageView.get();
      if (characterText == null) {
        // listview has moved on
        if (LOG)
          Log.d(TAG, "characterText is null: ["
              + characterText
              + "]");
        return;
      }
      
      if (characterObject == null) {
        characterText.setText("none");
        return;
      }
      
      // CHARACTER NAME -- TEXT
      String characterName = characterObject.getString("name");
      if (characterName == null) {
        characterText.setText("none");
        return;
      }
      
      if (characterText.getTag().equals(objectId)) {
        // the view STILL has the same objectId associated with it
        characterText.setText(characterName
            + "");
      } else {
        if (LOG)
          Log.d(TAG, "objectId does not match: ["
              + characterText.getTag()
              + "],[" + objectId + "]");
        return;
      }
      
      // CHARACTER PIC -- IMAGE
      if (imageView == null) {
        return;
      }
      
      ParseFile characterPic = characterObject.getParseFile("profilePic");
      com.ameron32.apps.projectbanditv3.object.Character character = (Character) characterObject;
      String profilePicUrl = character.getProfilePicUrl();
      if (characterPic == null && profilePicUrl == null) {
        Log.d("MPQA", "characterPic & profilePicUrl are null");
        return;
      }
      
      if (characterPic == null) {
        Log.d("MPQA", "characterPic is null, using profilePicUrl");
        Picasso.with(context).load(profilePicUrl).into(imageView);
        return;
      }
      
      Picasso.with(context).load(characterPic.getUrl()).into(imageView);
      characterPic.getDataInBackground(new GetCharacterPicCallback(imageView, objectId));
    }
  }
  
  public class GetCharacterPicCallback
      extends GetDataCallback {
    
    private final WeakReference<ImageView> weakView;
    private final String objectId;
    
    public GetCharacterPicCallback(
        final ImageView imageView,
        String objectId) {
      this.objectId = objectId;
      weakView = new WeakReference<ImageView>(imageView);
    }
    
    @Override public void done(
        byte[] data, ParseException e) {
      if (e == null) {
        if (data != null) {
          doneNoError(data);
        } else {
          Log.d("MPQA", "data is null");
        }
      } else {
        if (ERROR) e.printStackTrace();
      }
    }
    
    private void doneNoError(byte[] data) {
      final ImageView imageView = weakView.get();
      if (imageView == null) {
        // listview has moved on
        if (LOG)
          Log.d(TAG, "imageView is null: ["
              + imageView + "]");
        return;
      }
      
      Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
      if (imageView.getTag().equals(objectId)) {
        imageView.setImageBitmap(bitmap);
      } else {
        if (LOG)
          Log.d(TAG, "objectId does not match: ["
              + imageView.getTag()
              + "],[" + objectId + "]");
      }
    }
  }
  
  public class GetActionCallback extends
      GetCallback<ParseObject> {
    
    private final WeakReference<TextView> weakView;
    private final WeakReference<ImageView> weakImageView;
    private final String objectId;
    
    public GetActionCallback(
        final TextView textView,
        final String objectId,
        final ImageView imageView) {
      this.objectId = objectId;
      weakView = new WeakReference<TextView>(textView);
      weakImageView = new WeakReference<ImageView>(imageView);
    }
    
    @Override public void done(
        ParseObject characterObject,
        ParseException e) {
      if (e == null) {
        doneNoError(characterObject);
      } else {
        if (ERROR) e.printStackTrace();
      }
    }
    
    private void doneNoError(
        ParseObject actionObject) {
      final TextView actionText = weakView.get();
      final ImageView imageView = weakImageView.get();
      if (actionText == null
          || imageView == null) {
        // listview has moved on
        if (LOG)
          Log.d(TAG, "actionText or imageView is null: ["
              + actionText
              + "],["
              + imageView + "]");
        return;
      }
      
      if (actionObject == null) {
        actionText.setText("none");
        return;
      }
      
      // ACTION TEXT
      String action = actionObject.getString("action");
      if (action == null) {
        actionText.setText("none");
        return;
      }

      if (actionText.getTag().equals(objectId)) {
        // the view STILL has the same objectId associated with it
        actionText.setText(action + "");
      } else {
        if (LOG)
          Log.d(TAG, "objectId does not match: ["
              + actionText.getTag()
              + "],[" + objectId + "]");
        return;
      }
      
      // CHARACTER PIC -- IMAGE
      ParseFile actionPic = actionObject.getParseFile("actionPic");
      if (actionPic == null) {
        Log.d("MPQA", "actionPic is null");
        return;
      }
      
      if (imageView.getTag() == null) {
        return;
      }
      
      Picasso.with(context).load(actionPic.getUrl()).into(imageView);
      actionPic.getDataInBackground(new GetActionPicCallback(imageView, objectId));
    }
  }
  
  public class GetActionPicCallback
      extends GetDataCallback {
    
    private final WeakReference<ImageView> weakView;
    private final String objectId;
    
    public GetActionPicCallback(
        final ImageView imageView,
        String objectId) {
      this.objectId = objectId;
      weakView = new WeakReference<ImageView>(imageView);
    }
    
    @Override public void done(
        byte[] data, ParseException e) {
      if (e == null) {
        if (data != null) {
          doneNoError(data);
        } else {
          Log.d("MPQA", "data is null");
        }
      } else {
        if (ERROR) e.printStackTrace();
      }
    }
    
    private void doneNoError(byte[] data) {
      final ImageView imageView = weakView.get();
      if (imageView == null) {
        // listview has moved on
        if (LOG)
          Log.d(TAG, "imageView is null: ["
              + imageView + "]");
        return;
      }
      
      Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
      if (imageView.getTag().equals(objectId)) {
        imageView.setImageBitmap(bitmap);
      } else {
        if (LOG)
          Log.d(TAG, "objectId does not match: ["
              + imageView.getTag()
              + "],[" + objectId + "]");
      }
    }
  }
}
