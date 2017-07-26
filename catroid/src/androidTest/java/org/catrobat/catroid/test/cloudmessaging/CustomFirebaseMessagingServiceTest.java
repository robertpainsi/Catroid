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

package org.catrobat.catroid.test.cloudmessaging;

import android.app.NotificationManager;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.catrobat.catroid.cloudmessaging.CloudMessage;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class CustomFirebaseMessagingServiceTest {
	private NotificationManager mockNotificationManager;

	private CloudMessage mockCloudMessage;

	@Before
	public void setUp() {
		mockCloudMessage = Mockito.mock(CloudMessage.class);
		mockNotificationManager = Mockito.mock(NotificationManager.class);
	}

	@Test
	public void myTest() {
		HashMap<String, String> data = new HashMap<>();
		data.put(CloudMessage.TITLE, "MyTitle");
		data.put(CloudMessage.MESSAGE, "MyMessage");
		data.put(CloudMessage.WEB_PAGE_URL, "http://mylink");

		Context context = InstrumentationRegistry.getContext();

		NotificationsWrapper notificationWrapper = new NotificationsWrapper(mockNotificationManager, mockCloudMessage);
		notificationWrapper.showNotification(0, data, context);

		verify(mockNotificationManager).notify(anyInt(), eq(mockCloudMessage.notification));
	}
}
