package com.ameron32.apps.projectbanditv3.manager;

import com.ameron32.apps.projectbanditv3.SaveObjectSerialExecutor;
import com.ameron32.apps.projectbanditv3.object.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageManager extends AbsManager {

    private static MessageManager messageManager;

    private MessageManager() {
    }

    public static MessageManager get() {
        if (messageManager == null) {
            messageManager = new MessageManager();
        }
        return messageManager;
    }

    private List<MessageListener> listeners;

    public void initialize() {
        setInitialized(true);
    }

    public void addMessageListener(MessageListener listener) {
        if (listeners == null) {
            listeners = new ArrayList<MessageListener>();
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeMessageListener(MessageListener listener) {
        if (listeners != null) {
            if (listeners.contains(listener)) {
                listeners.remove(listener);
            }
        }
    }

    public void notifyMessageReceived() {
        if (listeners != null) {
            for (MessageListener listener : listeners) {
                if (listener != null) {
                    listener.onMessageReceived();
                }
            }
        }
    }

    public interface MessageListener {

        public void onMessageReceived();
    }

    public void markMessagesAsReceived(List<Message> messagesToSave) {
        Message[] save = messagesToSave.toArray(new Message[messagesToSave.size()]);
        for (int i = 0; i < save.length; i++) {
            Message m = save[i];
            SaveObjectSerialExecutor.get().sendMessage(m, null);
        }
    }

    public static void destroy() {
        messageManager = null;
    }
}
