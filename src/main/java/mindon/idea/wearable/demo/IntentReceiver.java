package mindon.idea.wearable.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import android.app.PendingIntent;

/**
 * Broadcast receiver to post toast messages in response to notification intents firing.
 */
public class IntentReceiver extends BroadcastReceiver {
    public static final String ACTION_DEMO =
            "mindon.idea.wearable.demo.ACTION_DEMO";
    public static final String ACTION_ENABLE =
            "mindon.idea.wearable.demo.ACTION_ENABLE";
    public static final String ACTION_DISABLE =
            "mindon.idea.wearable.demo.ACTION_DISABLE";


    public static final String EXTRA_MESSAGE =
            "mindon.idea.wearable.demo.EXTRA_MESSAGE";
    public static final String EXTRA_REPLY =
            "mindon.idea.wearable.demo.EXTRA_REPLY";


    private boolean mEnableMessages = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_DEMO)) {
            if (mEnableMessages) {
                String message = intent.getStringExtra(EXTRA_MESSAGE);
                String replyMessage = intent.getStringExtra(EXTRA_REPLY);
                if (replyMessage != null) {
                    message = message + ": \"" + replyMessage + "\"";
                }
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        } else if (intent.getAction().equals(ACTION_ENABLE)) {
            mEnableMessages = true;
        } else if (intent.getAction().equals(ACTION_DISABLE)) {
            mEnableMessages = false;
        }
    }
}
