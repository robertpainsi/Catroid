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
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

public class CustomFirebaseMessagingService extends FirebaseMessagingService {

	public static final String TAG = CustomFirebaseMessagingService.class.getSimpleName();

	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		CloudMessaging cloudMessaging = new CloudMessaging(this,
				new NotificationBuilderProvider(this), (NotificationManager) getSystemService(NOTIFICATION_SERVICE));
		cloudMessaging.showNotification(new CloudMessage(remoteMessage));
	}
}
