package mindon.idea.wearable.demo;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class WearUtil {

	public static PendingIntent getPendingIntent(Context context, String message) {
        Intent intent = new Intent(IntentReceiver.ACTION_DEMO)
                .setClass(context, IntentReceiver.class);
        intent.putExtra(IntentReceiver.EXTRA_MESSAGE, message);
        return PendingIntent.getBroadcast(context, message.length() /* requestCode */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
	}
}
