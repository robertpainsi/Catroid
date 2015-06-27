/**
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2013 The Catrobat Team
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
package org.catrobat.catroid.uiespresso.wrapper;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.runner.AndroidJUnit4;

import org.catrobat.catroid.CatroidApplication;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.io.StorageHandler;
import org.catrobat.catroid.ui.MainMenuActivity;
import org.catrobat.catroid.uiespresso.util.UiTestUtils;
import org.catrobat.catroid.uiespresso.util.idling.ElapsedTimeIdlingResource;
import org.catrobat.catroid.uiespresso.util.rules.BaseActivityInstrumentationRule;
import org.catrobat.catroid.wrapper.ProjectWrapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class CreateAndPlayProject {

	@Rule
	public BaseActivityInstrumentationRule<MainMenuActivity> mainMenuActivity =
			new BaseActivityInstrumentationRule<>(MainMenuActivity.class, true, true);

	@Before
	public void setUp() {
		mainMenuActivity.launchActivity(null);
	}

	@Test
	public void testCreateProject() {
		Project project = createProject();
		StorageHandler.getInstance().saveProject(project);

		MainMenuActivity mainMenuActivity = (MainMenuActivity) UiTestUtils.getCurrentActivity();
		IdlingResource idlingResource = mainMenuActivity.getIdlingResource();
		IdlingRegistry.getInstance().register(idlingResource);

		onView(withId(R.id.main_menu_button_programs))
				.perform(click());
		onView(withText(project.getName()))
				.perform(click());
		onView(withId(R.id.button_play))
				.perform(click());

		IdlingRegistry.getInstance().register(new ElapsedTimeIdlingResource(Long.MAX_VALUE));

		pressBack();
	}

	public Project createProject() {
		String projectName = "Muku_" + System.currentTimeMillis() / 1000;
		// @formatter:off
		return
			new ProjectWrapper(CatroidApplication.getAppContext(), projectName, 1080, 1920)
				.firstScene()
					.getBackground()
						.addLook(R.drawable.brick_blue_1h, "Brick blue")
						.whenProgramStarted()
							.setX(123)
						.done()
					.done()
					.createSprite("Sprite1")
						.whenProgramStarted()
							.setX(123)
						.done()
					.done()
				.done()
			.done();
		// @formatter:on
	}
}
