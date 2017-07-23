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

import android.test.AndroidTestCase;

import org.catrobat.catroid.cloudmessaging.CloudMessaging;

public class CloudMessagingTest extends AndroidTestCase {

	private CloudMessaging cloudMessaging;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		cloudMessaging = new CloudMessaging();
	}

	public void testAllRequiredFieldsAvailable() {

		assertFalse(cloudMessaging.isValidData());

		cloudMessaging.setNotificationTitle("Test Notification Title");
		cloudMessaging.setNotificationMessage("Test Notification Message");
		cloudMessaging.setNotificationUrl("https://www.catrobat.org/");

		assertTrue(cloudMessaging.isValidData());
	}

	public void testRequiredFieldIsMissing() {

		assertFalse(cloudMessaging.isValidData());

		cloudMessaging.setNotificationTitle("Test Notification Title");

		assertFalse(cloudMessaging.isValidData());

		cloudMessaging.setNotificationMessage("Test Notification Message");
		cloudMessaging.setNotificationUrl("https://www.catrobat.org/");

		assertTrue(cloudMessaging.isValidData());
	}

	public void testRequiredFieldIsEmpty() {

		assertFalse(cloudMessaging.isValidData());

		cloudMessaging.setNotificationTitle("");

		assertFalse(cloudMessaging.isValidData());

		cloudMessaging.setNotificationTitle("Test Notification Title");
		cloudMessaging.setNotificationMessage("Test Notification Message");
		cloudMessaging.setNotificationUrl("https://www.catrobat.org/");

		assertTrue(cloudMessaging.isValidData());
	}

	public void testRequiredFieldIsInValid() {

		assertFalse(cloudMessaging.isValidData());

		cloudMessaging.setNotificationTitle("Test Notification Title");
		cloudMessaging.setNotificationMessage("Test Notification Message");
		cloudMessaging.setNotificationUrl("htt0s://www.catrobat.org/");

		assertFalse(cloudMessaging.isValidData());

		cloudMessaging.setNotificationUrl("http://www.catrobat.org/");

		assertTrue(cloudMessaging.isValidData());
	}
}
