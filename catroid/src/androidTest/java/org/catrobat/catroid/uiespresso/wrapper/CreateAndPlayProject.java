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
import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Look;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.actions.SetRotationStyleAction;
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
		ProjectManager.getInstance().setProject(project);

		MainMenuActivity mainMenuActivity = (MainMenuActivity) UiTestUtils.getCurrentActivity();
		IdlingResource idlingResource = mainMenuActivity.getIdlingResource();
		IdlingRegistry.getInstance().register(idlingResource);

		onView(withId(R.id.main_menu_button_programs))
				.perform(click());
		onView(withText(project.getName()))
				.perform(click());
		onView(withId(R.id.button_play))
				.perform(click());

		IdlingRegistry.getInstance().register(new ElapsedTimeIdlingResource(60 * 60 * 1000));

		pressBack();
	}

	public Project createProject() {
		// @formatter:off
		return
			new ProjectWrapper(CatroidApplication.getAppContext(),
					"Math Jungle",
					"Solve 10 tricky calculations and become the king of the math jungle.",
					480, 800)
				.firstScene()
					.getBackground()
						.addLook(R.drawable.background, "Jungle")

						.whenProgramStarted()
							.setY(360)
						.done()

						.whenIReceive("correct")
							.glideTo("0", "position_y - 72", "0.7")
						.done()

						.whenIReceive("wrong")
							.glideTo(0, 360, 1)
						.done()
					.done()

					.createSprite("Logic")
						.addSound(R.raw.fail_trumpet, "Wrong answer")
						.addSound(R.raw.game_show_bell_ding, "Right answer")

						.addProjectVariable("wait")

						.addProjectList("answers")
						.addProjectVariable("left_summand")
						.addProjectVariable("right_summand")
						.addProjectVariable("sum")
						.addProjectVariable("temp")
						.addProjectVariable("selected_answer")
						.addProjectVariable("score")

						.whenProgramStarted()
							.setVariable("wait", "TRUE")
							.broadcast("init_game")
						.done()

						.whenIReceive("init_game")
							.setVariable("score", "0")
							.broadcast("init_calculation")
						.done()

						.whenIReceive("init_calculation")
							.setVariable("left_summand", "random(1, 9)")
							.setVariable("right_summand", "random(1, 10 - {left_summand})")
							.setVariable("sum", "{left_summand} + {right_summand}")
							.setVariable("temp", "{sum} - random(0, 2)")
							.ifCondition("{temp} < 2")
								.setVariable("temp", 2)
							.elseCondition()
								.ifCondition("{temp} > 8")
									.setVariable("temp", 8)
								.endWithElseIfCondition()
							.endIfCondition()
							.deleteItemFormList("answers", 1)
							.deleteItemFormList("answers", 1)
							.deleteItemFormList("answers", 1)
							.addItemToList("answers", "{temp}")
							.addItemToList("answers", "{temp} + 1")
							.addItemToList("answers", "{temp} + 2")

							.broadcastAndWait("update")
							.setVariable("wait", "FALSE")
						.done()

						.whenIReceive("answer_selected")
							.ifCondition("{selected_answer} == {sum}")
								.changeVariable("score", 1)
								.broadcast("correct")
								.ifCondition("{score} == 10")
									.broadcast("win")
								.elseCondition()
									.startSound(R.raw.game_show_bell_ding)
									.broadcast("init_calculation")
								.endIfCondition()
							.elseCondition()
								.startSound(R.raw.fail_trumpet)
								.broadcast("wrong")
								.broadcast("init_game")
							.endIfCondition()
						.done()
					.done()

					.createSprite("Left summand")
						.addLook(R.drawable.n1, "Number 1")
						.addLook(R.drawable.n2, "Number 2")
						.addLook(R.drawable.n3, "Number 3")
						.addLook(R.drawable.n4, "Number 4")
						.addLook(R.drawable.n5, "Number 5")
						.addLook(R.drawable.n6, "Number 6")
						.addLook(R.drawable.n7, "Number 7")
						.addLook(R.drawable.n8, "Number 8")
						.addLook(R.drawable.n9, "Number 9")
						.addLook(R.drawable.n10, "Number 10")

						.whenProgramStarted()
							.setX(-100)
						.done()

						.whenIReceive("update")
							.switchToLookWithNumber("{left_summand}")
						.done()

						.whenIReceive("win")
							.hide()
						.done()
					.done()

					.createSprite("Plus")
						.addLook(R.drawable.plus, "Plus")

						.whenIReceive("win")
							.hide()
						.done()
					.done()

					.createSprite("Right summand")
						.addLook(R.drawable.n1, "Number 1")
						.addLook(R.drawable.n2, "Number 2")
						.addLook(R.drawable.n3, "Number 3")
						.addLook(R.drawable.n4, "Number 4")
						.addLook(R.drawable.n5, "Number 5")
						.addLook(R.drawable.n6, "Number 6")
						.addLook(R.drawable.n7, "Number 7")
						.addLook(R.drawable.n8, "Number 8")
						.addLook(R.drawable.n9, "Number 9")
						.addLook(R.drawable.n10, "Number 10")

						.whenProgramStarted()
							.setX(100)
						.done()

						.whenIReceive("update")
							.switchToLookWithNumber("{right_summand}")
						.done()

						.whenIReceive("win")
							.hide()
						.done()
					.done()

					.createSprite("Equals")
						.addLook(R.drawable.equals, "Equals")

						.whenProgramStarted()
							.setX(185)
						.done()

						.whenIReceive("win")
							.hide()
						.done()
					.done()

					.createSprite("Result 1")
						.addLook(R.drawable.n1, "Number 1")
						.addLook(R.drawable.n2, "Number 2")
						.addLook(R.drawable.n3, "Number 3")
						.addLook(R.drawable.n4, "Number 4")
						.addLook(R.drawable.n5, "Number 5")
						.addLook(R.drawable.n6, "Number 6")
						.addLook(R.drawable.n7, "Number 7")
						.addLook(R.drawable.n8, "Number 8")
						.addLook(R.drawable.n9, "Number 9")
						.addLook(R.drawable.n10, "Number 10")

						.addSpriteVariable("answer_index")
						.addSpriteVariable("answer_value")

						.whenProgramStarted()
							.placeAt(-150, -200)
							.setVariable("answer_index", 1)
						.done()

						.whenIReceive("update")
							.setVariable("answer_value", "element({answer_index}, [answers])")
							.switchToLookWithNumber("{answer_value}")
						.done()

						.whenIReceive("win")
							.hide()
						.done()

						.whenTapped()
							.ifCondition("!{wait}")
								.setVariable("wait", "TRUE")
								.setVariable("selected_answer", "{answer_value}")
								.broadcast("answer_selected")
							.endWithElseIfCondition()
						.done()
					.done()

					.createSprite("Result 2")
						.addLook(R.drawable.n1, "Number 1")
						.addLook(R.drawable.n2, "Number 2")
						.addLook(R.drawable.n3, "Number 3")
						.addLook(R.drawable.n4, "Number 4")
						.addLook(R.drawable.n5, "Number 5")
						.addLook(R.drawable.n6, "Number 6")
						.addLook(R.drawable.n7, "Number 7")
						.addLook(R.drawable.n8, "Number 8")
						.addLook(R.drawable.n9, "Number 9")
						.addLook(R.drawable.n10, "Number 10")

						.addSpriteVariable("answer_index")
						.addSpriteVariable("answer_value")

						.whenProgramStarted()
							.placeAt(0, -200)
							.setVariable("answer_index", 2)
						.done()

						.whenIReceive("update")
							.setVariable("answer_value", "element({answer_index}, [answers])")
							.switchToLookWithNumber("{answer_value}")
						.done()

						.whenIReceive("win")
							.hide()
						.done()

						.whenTapped()
							.ifCondition("!{wait}")
								.setVariable("wait", "TRUE")
								.setVariable("selected_answer", "{answer_value}")
								.broadcast("answer_selected")
							.endWithElseIfCondition()
						.done()
					.done()

					.createSprite("Result 3")
						.addLook(R.drawable.n1, "Number 1")
						.addLook(R.drawable.n2, "Number 2")
						.addLook(R.drawable.n3, "Number 3")
						.addLook(R.drawable.n4, "Number 4")
						.addLook(R.drawable.n5, "Number 5")
						.addLook(R.drawable.n6, "Number 6")
						.addLook(R.drawable.n7, "Number 7")
						.addLook(R.drawable.n8, "Number 8")
						.addLook(R.drawable.n9, "Number 9")
						.addLook(R.drawable.n10, "Number 10")

						.addSpriteVariable("answer_index")
						.addSpriteVariable("answer_value")

						.whenProgramStarted()
							.placeAt(150, -200)
							.setVariable("answer_index", 3)
						.done()

						.whenIReceive("update")
							.setVariable("answer_value", "element({answer_index}, [answers])")
							.switchToLookWithNumber("{answer_value}")
						.done()

						.whenIReceive("win")
							.hide()
						.done()

						.whenTapped()
							.ifCondition("!{wait}")
								.setVariable("wait", "TRUE")
								.setVariable("selected_answer", "{answer_value}")
								.broadcast("answer_selected")
							.endWithElseIfCondition()
						.done()
					.done()

					.createSprite("Score")
						.addLook(R.drawable.score0, "Score 0")
						.addLook(R.drawable.score1, "Score 1")
						.addLook(R.drawable.score2, "Score 2")
						.addLook(R.drawable.score3, "Score 3")
						.addLook(R.drawable.score4, "Score 4")
						.addLook(R.drawable.score5, "Score 5")
						.addLook(R.drawable.score6, "Score 6")
						.addLook(R.drawable.score7, "Score 7")
						.addLook(R.drawable.score8, "Score 8")
						.addLook(R.drawable.score9, "Score 9")
						.addLook(R.drawable.score10, "Score 10")

						.whenProgramStarted()
							.placeAt(120, 360)
						.done()

						.whenIReceive("wrong")
							.switchToLook(R.drawable.score0)
						.done()

						.whenIReceive("correct")
							.nextLook()
						.done()
					.done()

					.createSprite("Great Job!")
						.addLook(R.drawable.great_job, "Great Job")
						.addSound(R.raw.ta_da, "Win game")

						.whenProgramStarted()
							.hide()
						.done()

						.whenIReceive("win")
							.show()
							.startSound(R.raw.ta_da)
						.done()
					.done()

					.createSprite("Animal")
						.addLook(R.drawable.tiger, "Tiger")
						.addLook(R.drawable.elephant, "Elephant")
						.addLook(R.drawable.zebra, "Zebra")
						.addLook(R.drawable.monkey, "Monkey")

						.whenProgramStarted()
							.placeAt(300, 1000)
							.wait(2.0)
							.forever()
								.setY("random(115, 350)")
								.glideTo("-position_x", "random(115, 350)", "random(1, 4)")
								.nextLook()
								.wait("random(2, 5)")
							.endForever()
						.done()
					.done()
				.done()
			.done();
		// @formatter:on
	}
}
