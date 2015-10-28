package com.ameron32.apps.projectbanditv3.adapter;

import android.content.Context;

import com.ameron32.apps.projectbanditv3.Query;
import com.ameron32.apps.projectbanditv3.R;
import com.ameron32.apps.projectbanditv3.object.Message;
import com.parse.ParseQuery;
import com.ameron32.apps.projectbanditv3.parseui.ParseQueryAdapter;
import com.ameron32.apps.projectbanditv3.parseui.ParseQueryAdapter.QueryFactory;

/**
 *
 *
 *
 */
public class ChatViewStoryPQAdapter extends AbsMessageAdapter {

	public ChatViewStoryPQAdapter(Context context) {
	  super(context, makeQuery(), R.layout.row_message_standard, R.layout.row_message_system, R.layout.row_message_game);
	}

	private static QueryFactory<Message> makeQuery() {
		return new ParseQueryAdapter.QueryFactory<Message>() {

			@Override
			public ParseQuery<Message> create() {
				return Query._Message.getStoryChatQuery();
			}
		};
	}


}
