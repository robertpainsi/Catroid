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

import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Map;

public class CloudMessaging {

	private static final String TAG = CloudMessaging.class.getSimpleName();

	private RemoteMessage remoteMessage;
	private Map<String, String> dataPayload;
	public static final String WEB_PAGE_URL = "link";

	public CloudMessaging(RemoteMessage remoteMessage) {
		this.remoteMessage = remoteMessage;
		dataPayload = remoteMessage.getData();
	}

	public String getTitle() {
		return remoteMessage.getNotification().getTitle();
	}

	public String getMessage() {
		return remoteMessage.getNotification().getBody();
	}

	public String getWebPageUrl() {
		return dataPayload.get(WEB_PAGE_URL);
	}

	public boolean isValidData(ArrayList<String> notificationData) {

		for (String data : notificationData) {
			if (isStringNullOrEmpty(data)) {
				return false;
			}
		}
		return true;
	}

	private boolean isStringNullOrEmpty(String string) {
		return string == null || string.isEmpty();
	}
}
