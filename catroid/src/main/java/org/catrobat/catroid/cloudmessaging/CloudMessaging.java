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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import org.catrobat.catroid.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class CloudMessaging {

	private static final String TAG = CloudMessaging.class.getSimpleName();

	private Context context;
	private RemoteMessage remoteMessage;
	private Map<String, String> dataPayload;
	private static final String IMAGE_URL = "image";
	public static final String WEB_PAGE_URL = "link";

	public CloudMessaging(Context context, RemoteMessage remoteMessage) {
		this.context = context;
		this.remoteMessage = remoteMessage;
		dataPayload = remoteMessage.getData();
	}

	public Bitmap getBitmap(String link) {
		try {
			URL url = new URL(link);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			return BitmapFactory.decodeStream(input);
		} catch (IOException e) {
			Log.w(TAG, "Failed to download image. Using default icon", e);
			return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
		}
	}

	public String getTitle() {
		return remoteMessage.getNotification().getTitle();
	}

	public String getMessage() {
		return remoteMessage.getNotification().getBody();
	}

	public String getImageUrl() {
		return dataPayload.get(IMAGE_URL);
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
