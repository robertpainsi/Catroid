/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2017 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.cloudmessaging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.catrobat.catroid.R;

import java.util.ArrayList;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {

	public static final String TAG = CustomFirebaseMessagingService.class.getSimpleName();

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {

		CloudMessaging cloudMessaging = new CloudMessaging(this, remoteMessage);

		String title = cloudMessaging.getTitle();
		String message = cloudMessaging.getMessage();
		String imageUrl = cloudMessaging.getImageUrl();
		String webPageUrl = cloudMessaging.getWebPageUrl();

		ArrayList<String> notificationData = new ArrayList<>();
		notificationData.add(title);
		notificationData.add(message);
		notificationData.add(imageUrl);
		notificationData.add(webPageUrl);

		if (!cloudMessaging.isValidData(notificationData)) {
			return;
		}

		Bitmap imageBitmap = cloudMessaging.getBitmap(imageUrl);

		sendNotification(title, message, imageBitmap, webPageUrl);
	}

	private void sendNotification(String title, String message, Bitmap image, String link) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

		NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setLargeIcon(image)
				.setContentTitle(title)
				.setContentText(message)
				.setAutoCancel(true)
				.setSound(defaultSoundUri)
				.setContentIntent(pendingIntent);

		NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(0, notification.build());
	}
}
