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

package org.catrobat.catroid.uiespresso.util.idling;

import android.support.test.espresso.IdlingResource;

public class ElapsedTimeIdlingResource implements IdlingResource {
	private final long startTime;
	private final long waitingTime;
	private ResourceCallback resourceCallback;

	public ElapsedTimeIdlingResource(long millis) {
		this.startTime = System.currentTimeMillis();
		this.waitingTime = millis;
	}

	@Override
	public String getName() {
		return ElapsedTimeIdlingResource.class.getName() + ":" + waitingTime;
	}

	@Override
	public boolean isIdleNow() {
		long elapsed = System.currentTimeMillis() - startTime;
		boolean idle = elapsed >= waitingTime;
		if (idle) {
			resourceCallback.onTransitionToIdle();
		}
		return idle;
	}

	@Override
	public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
		this.resourceCallback = resourceCallback;
	}
}