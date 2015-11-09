package com.ameron32.apps.projectbanditv3.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.adapter.AbsMessageAdapter;
import com.ameron32.apps.projectbanditv3.manager.MessageManager;
import com.ameron32.apps.projectbanditv3.manager.UserManager;
import com.ameron32.apps.projectbanditv3.object.Message;

import java.util.List;
import java.util.Locale;

public abstract class AbsChatView extends
    AbsFrameRecyclerView implements
    MessageManager.MessageListener,
    AbsMessageAdapter.OnQueryLoadListener<Message> {

  private static final String TAG = AbsChatView.class.getSimpleName();
  private static final boolean TOAST = true;
  private static final boolean LOG = true;

  private static final String ID = "Chat View";

  protected final Context context;

  public AbsChatView(Context context,
      AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    this.context = context;
  }

  public AbsChatView(Context context,
      AttributeSet attrs) {
    super(context, attrs);
    this.context = context;
  }

  public AbsChatView(Context context) {
    super(context);
    this.context = context;
  }

  @Override public void create() {
    super.create();
    registerAsListener();
    prepareFilters();
  }

  private void prepareFilters() {
    final View filterButton = this.findViewById(R.id.button_filter_menu_toggle);
    filterButton.setOnClickListener(new OnClickListener() {

      @Override public void onClick(
          View v) {
        // TODO implement filters menu
        if (TOAST) {
          Toast.makeText(context, "Filters not yet implemented", Toast.LENGTH_SHORT).show();
        }
      }
    });

    final TextView filtersTextView = (TextView) this.findViewById(R.id.textview_chat_filters);
    filtersTextView.setText(("filter: none").toUpperCase(Locale.US));
  }

  public void requeryMessagesFromServer() {
    AbsMessageAdapter adapter = getAdapter();
    adapter.loadObjects();
  }

  private void markMessagesAsReceived(
      List<Message> messagesToSave) {

    for (int i = 0; i < messagesToSave.size(); i++) {
      Message message = messagesToSave.get(i);
      message.markAsReceived(UserManager.get().getCurrentUser());
    }
    MessageManager.get().markMessagesAsReceived(messagesToSave);
  }

  public String getChatViewId() {
    return ID;
  }

  private void registerAsListener() {
    AbsMessageAdapter adapter = getAdapter();
    adapter.addOnQueryLoadListener(this);
    MessageManager.get().addMessageListener(this);
  }

  private void unregisterAsListener() {
    AbsMessageAdapter adapter = getAdapter();
    adapter.removeOnQueryLoadListener(this);
    MessageManager.get().removeMessageListener(this);
  }

  @Override public void onMessageReceived() {
    requeryMessagesFromServer();
  }

  @Override public void destroy() {
    unregisterAsListener();
    super.destroy();
  }

  @Override public void onLoaded(
      List<Message> messages,
      Exception e) {
    if (e == null) {
        // TODO: messages marked as received
//      markMessagesAsReceived(messages);
    }
  }

  @Override public void onLoading() {
    // TODO Auto-generated method stub

  }

  @Override protected AbsMessageAdapter createAdapter(
      Context context) {
    // TODO Auto-generated method stub
    return null;
  }

}
