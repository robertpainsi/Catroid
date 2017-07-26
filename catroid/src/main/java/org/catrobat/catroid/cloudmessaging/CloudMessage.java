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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import org.catrobat.catroid.R;
import org.catrobat.catroid.utils.StatusBarNotificationManager;

import java.util.HashMap;

public class CloudMessage {

	public static final String TITLE = "title";
	public static final String MESSAGE = "message";
	public static final String WEB_PAGE_URL = "link";

	public Notification notification;

	private String title;
	private String message;
	private String url;

	@VisibleForTesting
	public CloudMessage() {

	}

	public CloudMessage(RemoteMessage remoteMessage) {
		setNotificationTitle(remoteMessage.getNotification().getTitle());
		setNotificationMessage(remoteMessage.getNotification().getBody());
		setNotificationUrl(remoteMessage.getData().get(WEB_PAGE_URL));
	}

	public String getTitle() {
		return title;
	}

	public String getMessage() {
		return message;
	}

	public String getWebPageUrl() {
		return url;
	}

	public void setNotificationTitle(String title) {
		this.title = title;
	}

	public void setNotificationMessage(String message) {
		this.message = message;
	}

	public void setNotificationUrl(String url) {
		if ((url.startsWith("http://") || url.startsWith("https://")) && url.contains(".")) {
			this.url = url;
		}
	}

	public boolean isValidData() {
		return !(isStringNullOrEmpty(getTitle()) || isStringNullOrEmpty(getMessage()) || isStringNullOrEmpty(getWebPageUrl()));
	}

	public void showNotification(HashMap<String, String> data, Context context) {

		StatusBarNotificationManager manager = StatusBarNotificationManager.getInstance();
		int notificationId = manager.getUniqueNotificationId();

		Notification notification = getNotification(data, context);

		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(notificationId, notification);
	}

	public Notification getNotification(HashMap<String, String> data, Context context) {

		String title = data.get(TITLE);
		String message = data.get(MESSAGE);
		String link = data.get(WEB_PAGE_URL);

		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(title)
				.setContentText(message)
				.setAutoCancel(true)
				.setSound(defaultSoundUri)
				.setContentIntent(pendingIntent);

		this.notification = notification.build();

		return notification.build();
	}

	private boolean isStringNullOrEmpty(String string) {
		return string == null || string.isEmpty();
	}
}
