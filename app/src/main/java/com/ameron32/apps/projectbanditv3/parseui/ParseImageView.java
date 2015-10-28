package com.ameron32.apps.projectbanditv3.parseui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by klemeilleur on 10/27/2015.
 */
public class ParseImageView extends ImageView {
  private ParseFile file;
  private Drawable placeholder;
  private boolean isLoaded = false;

  public ParseImageView(Context context) {
    super(context);
  }

  public ParseImageView(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
  }

  public ParseImageView(Context context, AttributeSet attributeSet, int defStyle) {
    super(context, attributeSet, defStyle);
  }

  protected void onDetachedFromWindow() {
    if(this.file != null) {
      this.file.cancel();
    }
    super.onDetachedFromWindow();
  }

  public void setImageBitmap(Bitmap bitmap) {
    super.setImageBitmap(bitmap);
    this.isLoaded = true;
  }

  public void setPlaceholder(Drawable placeholder) {
    this.placeholder = placeholder;
    if(!this.isLoaded) {
      this.setImageDrawable(this.placeholder);
    }

  }

  public void setParseFile(ParseFile file) {
    if(this.file != null) {
      this.file.cancel();
    }

    this.isLoaded = false;
    this.file = file;
    this.setImageDrawable(this.placeholder);
  }

//  public Task<byte[]> loadInBackground() {
//    if(this.file == null) {
//      return Task.forResult(null);
//    } else {
//      final ParseFile loadingFile = this.file;
//      return this.file.getDataInBackground().onSuccessTask(new Continuation<byte[], Task<byte[]>>() {
//        @Override
//        public Task<byte[]> then(Task<byte[]> task) throws Exception {
//          byte[] data = (byte[])task.getResult();
//          if(ParseImageView.this.file != loadingFile) {
//            return Task.cancelled();
//          } else {
//            if(data != null) {
//              Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//              if(bitmap != null) {
//                ParseImageView.this.setImageBitmap(bitmap);
//              }
//            }
//
//            return task;
//          }
//        }
//      }, Task.UI_THREAD_EXECUTOR);
//    }
//  }

  public void loadInBackground(final GetDataCallback completionCallback) {
    if(this.file == null) {
      completionCallback.done(null, new ParseException(ParseException.OBJECT_NOT_FOUND, "ParseFile not found. Was setParseFile() used?"));
    } else {
      final ParseFile loadingFile = this.file;
      this.file.getDataInBackground(new GetDataCallback() {
        @Override
        public void done(byte[] data, ParseException e) {
          if(ParseImageView.this.file != loadingFile) {
            completionCallback.done(null, new ParseException(ParseException.VALIDATION_ERROR, "Wrong file was loaded."));
            return;
          } else {
            if (data != null) {
              Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
              if (bitmap != null) {
                ParseImageView.this.setImageBitmap(bitmap);
              }
            }
            completionCallback.done(data, null);
            return;
          }
        }
      });
    }
  }
}
