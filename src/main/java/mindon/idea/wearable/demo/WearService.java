package mindon.idea.wearable.demo;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;

import android.os.Bundle;
import android.os.Parcelable;

import android.preview.support.v4.app.NotificationManagerCompat;
import android.preview.support.wearable.notifications.WearableNotifications;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;

public class WearService extends Service {
    public static final String NOTIFY =
            "mindon.idea.wearable.demo.NOTIFY";

    public static final String EXTRA = "wear.notify";
    public static final String STEPS = "steps";
	private static final int _NOTIFICATION_ID = 0;
	private static final int _IMAGE_WIDTH = 280;
	private static final int _IMAGE_HEIGHT = 280;

	private NotificationManagerCompat mNotificationManager;
    private Binder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        WearService getService() {
            return WearService.this;
        }
    }

    @Override
    public void onCreate() {
        mNotificationManager = NotificationManagerCompat.from(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(NOTIFY)) {
            createNotification(intent);
            return START_STICKY;
        }
        return START_NOT_STICKY;
    }

    private void createNotification(Intent intent) {
        Bundle bundle = intent.getBundleExtra(EXTRA);
        ArrayList<Notification> notificationPages = new ArrayList<Notification>();

		ArrayList<Parcelable> stepBundles = bundle.getParcelableArrayList(STEPS);
        if (stepBundles != null) {
			Bundle step = null;
			int i = 0;
	        int stepCount = stepBundles.size();

			String stepTitle = bundle.containsKey("step-title") ? bundle.getString("step-title"): getResources().getString(R.string.step_title);

            for (Parcelable stepBundle : stepBundles) {
				i += 1;
				step = (Bundle) stepBundle;
				if(step == null || !step.containsKey("title"))
					continue;

				NotificationCompat.Builder child = new NotificationCompat.Builder(this);
				//child.setSmallIcon(R.mipmap.notification); // icon

				if( step.containsKey("src") ) {
					Bitmap image = Bitmap.createScaledBitmap(
						AssetUtils.loadBitmapAsset(this, step.getString("src")),
							_IMAGE_WIDTH, _IMAGE_HEIGHT, false);

					NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle().bigPicture(image);

					if( step.containsKey("summary") ) {
						style.setBigContentTitle( step.getString("title") );
						style.setSummaryText( step.getString("summary"));
					} else {
						style.setBigContentTitle(String.format(stepTitle, i, stepCount) +":"+ step.getCharSequence("title"));
					}
					child.setStyle(style);

				} else {
					NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();

					style.bigText( step.getString("title") );
					style.setBigContentTitle(String.format(stepTitle, i, stepCount));

					if( step.containsKey("summary") )
						style.setSummaryText(step.getString("summary"));
					child.setStyle(style);
				}
				notificationPages.add(child.build());
			}
		}

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        if (bundle.containsKey("src")) {
            NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();

            Bitmap image = Bitmap.createScaledBitmap(
                    AssetUtils.loadBitmapAsset(this, bundle.getString("src")),
						_IMAGE_WIDTH, _IMAGE_HEIGHT, false);
            style.bigPicture(image);
			builder.setStyle(style);
        }
        builder.setContentTitle(bundle.getString("title"));
		if(bundle.containsKey("summary")) {
			builder.setContentText(bundle.getString("summary"));
		}
        builder.setSmallIcon(R.mipmap.notification); // icon
		
		if(bundle.containsKey("deleted")) {
			builder.setDeleteIntent(WearUtil.getPendingIntent(this, bundle.getString("deleted")));
		}
		if(bundle.containsKey("content")) {
			builder.setContentIntent(WearUtil.getPendingIntent(this, bundle.getString("content")));
		}
		
		if(bundle.containsKey("ongoing")) {
			builder.setOngoing(true);
		}
		if(bundle.containsKey("once")) {
			builder.setOnlyAlertOnce(true);
		}
		if(bundle.containsKey("num")) {
			builder.setNumber(bundle.getInt("num"));
		}
		if(bundle.containsKey("priority")) {
			builder.setPriority(bundle.getInt("priority"));
		}
		if(bundle.containsKey("when")) {
			builder.setWhen(bundle.getLong("when"));
		}
		if(bundle.containsKey("action-full")) {
			builder.addAction(
					R.drawable.ic_reload,
					bundle.getString("action-full"),
					WearUtil.getPendingIntent(this,
							bundle.containsKey("action-full-message") ? bundle.getString("action-full-message") : getString(R.string.action_full_message)));
		}
		if(bundle.containsKey("action-reply")) {
			builder.addAction(
					R.drawable.ic_reply,
					bundle.getString("action-reply"),
					WearUtil.getPendingIntent(this,
							bundle.containsKey("action-reply-message") ? bundle.getString("action-reply-message") : getString(R.string.action_reply_message)));
		}

        Notification notification = new WearableNotifications.Builder(builder)
                .addPages(notificationPages)
                .build();
        mNotificationManager.notify(_NOTIFICATION_ID, notification);
    }
}
