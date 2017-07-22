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

import android.app.Notification;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;

import org.catrobat.catroid.cloudmessaging.NotificationBuilderProvider;
import org.catrobat.catroid.cloudmessaging.NotificationsWrapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CloudMessagingTest {
	private NotificationBuilderProvider mockNotificationBuilderProvider;
	private NotificationCompat.Builder mockNotificationBuilder;
	private Notification mockNotification;
	private NotificationManager mockNotificationManager;

	private NotificationsWrapper notificationWrapper;

	@Before
	public void setUp() {
		mockNotificationBuilder = Mockito.mock(NotificationCompat.Builder.class, Mockito.RETURNS_SELF);
		mockNotification = Mockito.mock(Notification.class);
		mockNotificationBuilderProvider = Mockito.mock(NotificationBuilderProvider.class);
		when(mockNotificationBuilderProvider.get()).thenReturn(mockNotificationBuilder);
		when(mockNotificationBuilder.build()).thenReturn(mockNotification);
		mockNotificationManager = Mockito.mock(NotificationManager.class);
	}

	@Test
	public void givenTitleInData_whenPost_thenNotificationHasContentTitle() {
		Map<String, String> data = new HashMap<>();
		data.put("Title", "MyTitle");

		notificationWrapper = new NotificationsWrapper(mockNotificationBuilderProvider, mockNotificationManager);
		notificationWrapper.postNotification(0, data);

		verify(mockNotificationBuilder).setContentTitle(eq("MyTitle"));
		verify(mockNotificationManager).notify(anyInt(), eq(mockNotification));
	}
}
