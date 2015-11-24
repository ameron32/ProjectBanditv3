package com.ameron32.apps.projectbanditv3.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.ameron32.apps.projectbanditv3.adapter.ChatCActionSpinnerAdapter;
import com.ameron32.apps.projectbanditv3.adapter.ChatCharacterSpinnerAdapter;
import com.ameron32.apps.projectbanditv3.ChatInputWatcher;
import com.ameron32.apps.projectbanditv3.adapter.ChatViewPagerAdapter;
import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.SaveObjectSerialExecutor;
import com.ameron32.apps.projectbanditv3.manager.CharacterManager;
import com.ameron32.apps.projectbanditv3.manager.CharacterManager.OnCharacterChangeListener;
import com.ameron32.apps.projectbanditv3.manager.MessageManager;
import com.ameron32.apps.projectbanditv3.object.*;
import com.ameron32.apps.projectbanditv3.object.Character;
import com.google.android.exoplayer.ExoPlaybackException;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.upstream.TransferListener;
import com.google.android.exoplayer.util.PlayerControl;
import com.google.android.exoplayer.util.Util;
import com.parse.ParseObject;
import com.ameron32.apps.projectbanditv3.parseui.ParseQueryAdapter;
import com.ameron32.apps.projectbanditv3.parseui.ParseQueryAdapter.OnQueryLoadListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.viewpagerindicator.LinePageIndicator;

// TODO: ADD viewpagerindicator LinePageIndicator
public class ChatManagerFragment
    extends
      AbsContentFragment
    implements
      MessageManager.MessageListener,
    SaveObjectSerialExecutor.OnSaveCallbacks,
      OnCharacterChangeListener
{

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated to
   * the activity and potentially other fragments contained in that activity.
   * <p>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnChatManagerListener {

    // TODO: Update argument type and name
    public void onChatViewPositionChange(
        int position);
  }

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String CURRENT_VIEW_KEY = "CurrentView";
  private static final String ARG_PARAM2 = "param2";

  private static final boolean TOAST = false;
  private static final boolean LOG = true;
  private static final String TAG = ChatManagerFragment.class.getSimpleName();


  /**
   * Use this factory method to create a new instance of this fragment using
   * the provided parameters.
   *
   * @param param1
   *          Parameter 1.
   * @param param2
   *          Parameter 2.
   * @return A new instance of fragment ChatManager.
   */
  // TODO: Rename and change types and number of parameters
  public static ChatManagerFragment newInstance(
      int param1, String param2) {
    final ChatManagerFragment fragment = new ChatManagerFragment();
    final Bundle args = new Bundle();
    args.putInt(CURRENT_VIEW_KEY, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @InjectView(R.id.imageview_character_spinner_image) ImageView spinnerImage;
  @InjectView(R.id.spinner_action) Spinner actionSpinner;
  @InjectView(R.id.spinner_character) Spinner characterSpinner;
  @InjectView(R.id.edittext_message_to_send) EditText chatInputEditText;
  @InjectView(R.id.viewpager) ViewPager mViewPager;
  @InjectView(R.id.bSend) View sendButton;
  @InjectView(R.id.progress_send) ProgressBar sendProgress;
  @InjectView(R.id.titles) LinePageIndicator titleIndicator;

//  private OnClickListener clickListener;
//  private OnLongClickListener longClickListener;
  private ParseObject currentAction;
  // private Character currentCharacter;
  // private String currentChatViewId;
  private String currentHintMessage;

  private ChatViewPagerAdapter mChatViewPagerAdapter;
  private OnChatManagerListener mListener;

  // TODO: Rename and change types of parameters
  private int mCurrentChatViewPosition;
  private String mParam2;

  private View mRootView;


  public ChatManagerFragment() {
    // Required empty public constructor
  }

  @OnClick(R.id.bSend)
  void sendMessage() {
    final EditText edittext = ButterKnife.findById(mRootView, R.id.edittext_message_to_send);
    final String message = edittext.getText().toString();

    final Message chatMessage = makeMessage(message, CharacterManager.get().getCurrentCharacter().getCurrentChannel());
    chatMessage.send(ChatManagerFragment.this);
  }

  private void closeSpinnerListener() {
    currentAction = null;
  }

  private void initChatViewsWithPager() {
    mChatViewPagerAdapter = new ChatViewPagerAdapter(getActivity(), mViewPager);
    mViewPager.setOffscreenPageLimit(4);
    mViewPager.setAdapter(mChatViewPagerAdapter);

    // TODO: why isn't onPageSelected() firing?
    mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

      @Override public void onPageSelected(
          int position) {
        super.onPageSelected(position);
        mListener.onChatViewPositionChange(position);
      }

    });

    titleIndicator.setViewPager(mViewPager);

    changeChatView(mCurrentChatViewPosition);
  }

  private void changeChatView(
      final int position) {
    mCurrentChatViewPosition = position;
    mListener.onChatViewPositionChange(position);
    mViewPager.setCurrentItem(position);
  }

  private void initListeners() {
    ChatInputWatcher hw = new ChatInputWatcher(chatInputEditText, this);
    chatInputEditText.addTextChangedListener(hw);
    chatInputEditText.setOnKeyListener(new OnKeyListener() {

      @Override
      public boolean onKey(
          View v, int keyCode,
          KeyEvent event) {
        if (event != null && v.getId() == R.id.edittext_message_to_send) {
          // if shift key is down, then we want to insert the '\n' char in the
          // TextView;
          // otherwise, the default action is to send the message.
          if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
              if (!event.isShiftPressed()) {
                sendMessage();
                return true;
              }
            }
          }
        }
        return false;
      }
    });
  }

  private void initSpinner() {

    final ParseQueryAdapter<com.ameron32.apps.projectbanditv3.object.Character> characterAdapter
        = new ChatCharacterSpinnerAdapter(getActivity());
    characterAdapter.setTextKey("name");
    characterAdapter.setPaginationEnabled(false);
    characterSpinner.setAdapter(characterAdapter);
    characterAdapter.addOnQueryLoadListener(new OnQueryLoadListener<Character>() {

      @Override public void onLoaded(
          List<Character> character,
          Exception e) {
        if (e == null) {
          int selection = CharacterManager.get().getChatCharacterPosition();
          if (characterSpinner != null) {
            characterSpinner.setSelection(selection, false);
          }
        }
      }

      @Override public void onLoading() {
        // none
      }});;
    characterSpinner.setSelection(1);


    final ParseQueryAdapter<CAction> actionAdapter = new ChatCActionSpinnerAdapter(getActivity());
    actionAdapter.setTextKey("action");
    actionAdapter.setPaginationEnabled(false);
    actionSpinner.setAdapter(actionAdapter);
    actionSpinner.setSelection(1);
  }

  private void initSpinnerListener() {

    characterSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

      @Override
      public void onItemSelected(
          AdapterView<?> parent,
          View view, int position,
          long id) {
        final Object characterObject = parent.getAdapter().getItem(position);
        final Character character = (Character) characterObject;
        // currentCharacter = Character
        // .getFromParseObject(characterParseObject);
        CharacterManager.get().setChatCharacter(character, position);
        updateChatImage();
        updateEditTextHint();
      }

      @Override
      public void onNothingSelected(
          AdapterView<?> parent) {
        // do nothing
      }
    });

    actionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

      @Override
      public void onItemSelected(
          AdapterView<?> parent,
          View view, int position,
          long id) {
        final Object actionObject = parent.getAdapter().getItem(position);
        currentAction = (ParseObject) actionObject;
        updateEditTextHint();
      }

      @Override
      public void onNothingSelected(
          AdapterView<?> parent) {
        // do nothing
      }
    });
  }

  private Message makeMessage(
      final String message, final String channel) {
    final Character character = CharacterManager.get().getChatCharacter();
    final ParseObject action = currentAction;
    return Message.create().setMessage(message)
        .setChannel(channel).setCharacter(character).setAction(action);
  }

  @Override public void onAttach(
      Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (OnChatManagerListener) activity;
    } catch (final ClassCastException e) {
      throw new ClassCastException(activity.toString()
          + " must implement OnChatManagerListener");
    }

//    if (mParam1 != null) {
//      ((ActionBarActivity) getActivity()).getSupportActionBar().setSubtitle(mParam1);
//    }
  }

  @Override public void onBegin() {
    chatInputEditText.getText().clear();
    sendProgress.setVisibility(View.VISIBLE);
  }

  @Override public void onComplete() {
    sendProgress.setVisibility(View.INVISIBLE);
  }

  @Override public void onCreate(
      Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    restoreFromBundle(savedInstanceState);
    restoreFromFragmentArguments();
  }

  @Override protected int getCustomLayoutResource() {
    return R.layout.fragment_chat_manager;
  }

  @Override public void onViewCreated(
      View view,
      Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mRootView = view;
    ButterKnife.inject(this, mRootView);
    setPadding(false);
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    ButterKnife.reset(this);
  }

  @Override public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Override public void onMessageReceived() {
    if (LOG) {
      Log.i(TAG, "onMessageReceived()");
    }
    requeryMessagesFromServer();
  }

  @Override public void onPause() {
//    stopRadio();
    super.onPause();
    unregisterAsListener();
  }

  @Override public void onResume() {
    registerAsListener();
    requeryMessagesFromServer();
    super.onResume();
//    startRadio();
  }

  @Override public void onSaveInstanceState(
      Bundle outState) {
    super.onSaveInstanceState(outState);
    storeToBundle(outState);
  }

  @Override public void onStart() {
    super.onStart();
    initChatViewsWithPager();
    initListeners();
    initSpinner();
    initSpinnerListener();
  }

  @Override public void onStop() {
    super.onStop();
    closeSpinnerListener();
  }

  private void registerAsListener() {
    MessageManager.get().addMessageListener(this);
  }

  private void requeryMessagesFromServer() {
    // moved to Fragment
    mChatViewPagerAdapter.notifyDataSetChanged();
  }

  private void restoreFromBundle(
      Bundle savedInstanceState) {
    if (savedInstanceState != null) {
      // currentChatViewId = savedInstanceState.getString(CURRENT_VIEW_KEY);
    }
  }

  private void restoreFromFragmentArguments() {
    if (getArguments() != null) {
      mCurrentChatViewPosition = getArguments().getInt(CURRENT_VIEW_KEY);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  private void storeToBundle(
      Bundle outState) {
    // outState.putString(CURRENT_VIEW_KEY, currentChatViewId);
  }

  private void unregisterAsListener() {
    MessageManager.get().addMessageListener(null);
  }

  public void updateChatImage() {
    String profilePicUrl = CharacterManager.get().getChatCharacter().getProfilePicUrl();
    Picasso.with(getActivity()).load(profilePicUrl).into(spinnerImage);
  }

  public void updateEditTextHint() {
    final EditText inputbox = ButterKnife.findById(mRootView, R.id.edittext_message_to_send);
    Character currentChatCharacter = CharacterManager.get().getChatCharacter();
    if (currentAction != null
        && currentChatCharacter != null) {
      currentHintMessage = currentChatCharacter.getString("name")
          + " "
          + currentAction.getString("action")
          + "...";
    } else {
      currentHintMessage = "Message as...";
    }
    inputbox.setHint(currentHintMessage);
  }

  @Override public void onCharacterChange(
      CharacterManager manager,
      Character newCharacter) {
    // nothing yet
  }

  @Override public void onChatCharacterChange(
      CharacterManager manager,
      Character newCharacter) {
    characterSpinner.setSelection(CharacterManager.get().getChatCharacterPosition(), false);
//    updateEditTextHint();
  }

  private ExoPlayer exoPlayer;
  private int RENDERER_COUNT = 1; //since you want to render simple audio
//  int minBufferMs = 1000;
//  int minRebufferMs = 5000;
//  int requestedBufferSize = 5000;
//  private int bufferSegmentSize = 1;
//  private int bufferSegmentCount;
  private void startRadio() {
    Log.d("RADIO", "readying!");
    exoPlayer = ExoPlayer.Factory.newInstance(RENDERER_COUNT, 1000, 5000);
    exoPlayer.addListener(new ExoPlayer.Listener() {
      @Override
      public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        Log.d("RADIO", "state: playWhenReady[" + playWhenReady + "] playbackState[" + playbackState + "]");
      }

      @Override
      public void onPlayWhenReadyCommitted() {
        Log.d("RADIO", "onPlayWhenReadyCommitted()");
      }

      @Override
      public void onPlayerError(ExoPlaybackException error) {
        Log.d("RADIO", "error: " + error.getLocalizedMessage());
        error.printStackTrace();
      }
    });
    final PlayerControl playerControl = new PlayerControl(exoPlayer);
    String userAgent = Util.getUserAgent(getActivity(), "ProjectBanditv3");
    Allocator allocator = new DefaultAllocator(1024);
    DataSource dataSource = new DefaultUriDataSource(getActivity(), null, userAgent);
//    bufferSegmentCount = 1;
    ExtractorSampleSource sampleSource = new ExtractorSampleSource(
        Uri.parse("http://download.jw.org/files/media_magazines/ee/ws_E_201601_01.mp3"),
        dataSource, allocator, 1024, new Mp3Extractor());
    MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource);
    exoPlayer.prepare(audioRenderer);
    exoPlayer.setPlayWhenReady(true);
  }

  private void stopRadio() {
    exoPlayer.stop();
    exoPlayer.release();
    Log.d("RADIO", "released!");
  }
}
