package com.ameron32.apps.projectbanditv3.view;

import android.content.Context;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.widget.EditText;

public class TitledEditText extends EditText implements TextWatcher{

  private final String LINE_SEPERTAOR = "\n";

  public TitledEditText(Context context) {
    super(context);
    initialize();
  }

  public TitledEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    initialize();
  }

  public TitledEditText(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initialize();
  }

  private void initialize() {
    addTextChangedListener(this);
  }

  private void makeSpannableTitle(Editable editable) {
    RelativeSizeSpan[] relativeSizeSpans = editable.getSpans(0, editable.length(), RelativeSizeSpan.class);
    for (int i = 0; i < relativeSizeSpans.length; i++) {
      editable.removeSpan(relativeSizeSpans[i]);
    }

    int lineSeperatorIndex = findFirstLineSeperatorIndex(editable);
    int spanEndIndex = (lineSeperatorIndex == -1 ? editable.length() : lineSeperatorIndex);

    if (spanEndIndex != 0) {
      RelativeSizeSpan localRelativeSizeSpan = new RelativeSizeSpan(1.3F);
      editable.setSpan(localRelativeSizeSpan, 0, spanEndIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    }
  }

  private int findFirstLineSeperatorIndex(Editable editable){
    if(editable != null) {
      return editable.toString().indexOf(LINE_SEPERTAOR);
    } else {
      return getText().toString().indexOf(LINE_SEPERTAOR);
    }
  }

  @Override
  public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

  }

  @Override
  public void afterTextChanged(Editable editable) {
    makeSpannableTitle(editable);
  }

  @Override
  public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
    super.onTextChanged(text, start, lengthBefore, lengthAfter);
  }

  public String getTitle(){
    int lineSeperatorIndex = findFirstLineSeperatorIndex(getText());
    if(lineSeperatorIndex != -1){
      return getText().subSequence(0, lineSeperatorIndex).toString();
    } else {
      return "";
    }
  }

  public String getContent(){
    int lineSeperatorIndex = findFirstLineSeperatorIndex(getText());
    if(lineSeperatorIndex == -1) {
      return "";
    } else {
      return getText().subSequence(lineSeperatorIndex + 1, length() - 1).toString();
    }
  }
}
