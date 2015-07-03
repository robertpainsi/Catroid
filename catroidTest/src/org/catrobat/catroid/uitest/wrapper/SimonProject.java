/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2015 The Catrobat Team
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

package org.catrobat.catroid.uitest.wrapper;

import org.catrobat.catroid.R;
import org.catrobat.catroid.wrapper.ProjectWrapper;
import org.catrobat.catroid.wrapper.SpriteWrapper;

public class SimonProject extends CreateAndPlayProject {
	@Override
	protected void createProject() {
		new ProjectWrapper(getActivity(), "Simon", 800, 1280) {
			@Override
			protected void createSprites() {
				addProjectVariable("ANIMATE");
				addProjectVariable("LOST");
				addProjectVariable("PLAY_COLOR");
				addProjectVariable("SCORE");
				addProjectVariable("SEQUENCE_INDEX");
				addProjectList("COLORS");
				addProjectList("SEQUENCE");

				//@formatter:off
				getBackground()
						.addLook(R.drawable.background, "Background")
				;

				double position = 180;
				createButton("Green ", R.drawable.green, R.drawable.green_light, R.raw.beep1, -position, position);
				createButton("Red", R.drawable.red, R.drawable.red_light, R.raw.beep2, position, position);
				createButton("Yellow", R.drawable.yellow, R.drawable.yellow_light, R.raw.beep3, -position, -position);
				createButton("Blue", R.drawable.blue, R.drawable.blue_light, R.raw.beep4, position, -position);

				new SpriteWrapper("Simon")
					.addLook(R.drawable.simon, "Simon")
					.addSound(R.raw.wrong, "Wrong")

					.whenProgramStarted()
						.setVariable("ANIMATE", "TRUE")
						.setVariable("LOST", "FALSE")
						.addItemToList("COLORS", "'green'")
						.addItemToList("COLORS", "'red'")
						.addItemToList("COLORS", "'yellow'")
						.addItemToList("COLORS", "'blue'")
						.broadcast("restart")

					.whenIReceive("restart")
						.setVariable("SCORE", 0)
						.setVariable("LOST", "FALSE")
						.repeat("number_of_items([SEQUENCE])")
							.deleteItemFormList("SEQUENCE", 1)
						.endRepeat()
						.broadcast("simon")

					.whenIReceive("simon")
						.setVariable("ANIMATE", "TRUE")
						.addItemToList("SEQUENCE", "element(random(1, 4), [COLORS])")
						.setVariable("SEQUENCE_INDEX", 1)
						.wait(1.0)
						.repeat("number_of_items([SEQUENCE])")
							.ifCondition("{LOST} == FALSE")
								.setVariable("PLAY_COLOR", "element({SEQUENCE_INDEX}, [SEQUENCE])")
								.broadcastAndWait("play_color")
								.ifCondition("{SEQUENCE_INDEX} == 1")
									.wait(0.6)
								.endWithElseIfCondition()
								.changeVariable("SEQUENCE_INDEX", 1)
								.wait(0.1)
							.endWithElseIfCondition()
						.endRepeat()
						.setVariable("SEQUENCE_INDEX", 1)
						.setVariable("ANIMATE", "FALSE")

					.whenIReceive("wrong")
						.startSound(R.raw.wrong)
						.wait(2.0)
						.broadcast("restart")
				;

				SpriteWrapper scoreUnits = new SpriteWrapper("Score units position");
				fillWithNumbers(scoreUnits)
					.whenProgramStarted()
						.placeAt(100, -57)

					.whenIReceive("restart")
						.switchToLook(R.drawable.n0)

					.whenIReceive("correct")
						.nextLook()
						.ifCondition("mod({SCORE}, 10) == 0")
							.broadcast("carry")
						.endWithElseIfCondition()
				;

				SpriteWrapper scoreTens = new SpriteWrapper("Score tens position");
				fillWithNumbers(scoreTens)
					.whenProgramStarted()
						.placeAt(60, -57)

					.whenIReceive("restart")
						.switchToLook(R.drawable.n0)

					.whenIReceive("carry")
						.nextLook()
				;
				//@formatter:on
			}

			private void createButton(String name, int imageId, int imageLightId, int soundId, double x, double y) {
				String lowerCaseName = name.toLowerCase();
				//@formatter:off
				new SpriteWrapper(name)
					.addLook(imageId, name)
					.addLook(imageLightId, name + " light")

					.whenProgramStarted()
						.placeAt(x, y)

					.whenIReceive("restart")
						.switchToLook(imageId)

					.whenTapped()
						.ifCondition("{ANIMATE} == FALSE && {LOST} == FALSE")
							.ifCondition("element({SEQUENCE_INDEX}, [SEQUENCE]) == '" + lowerCaseName + "'")
								.changeVariable("SEQUENCE_INDEX", 1)
								.setVariable("PLAY_COLOR", "'" + lowerCaseName + "'")
								.broadcast("play_color")
								.ifCondition("{SEQUENCE_INDEX} > number_of_items([SEQUENCE])")
									.changeVariable("SCORE", 1)
									.broadcast("correct")
									.broadcast("simon")
								.endWithElseIfCondition()
							.elseCondition()
								.setVariable("LOST", "TRUE")
								.broadcast("wrong")
							.endIfCondition()
						.endWithElseIfCondition()

					.whenIReceive("play_color")
						.ifCondition("{PLAY_COLOR} == '" + lowerCaseName + "'")
							.switchToLook(imageId)
							.wait(0.05)
							.switchToLook(imageLightId)
							.startSound(soundId)
							.wait(0.5)
						.endWithElseIfCondition()
						.switchToLook(imageId)
				;
				//@formatter:on
			}

			private SpriteWrapper fillWithNumbers(SpriteWrapper sprite) {
				//@formatter:off
				sprite
					.addLook(R.drawable.n0, "0")
					.addLook(R.drawable.n1, "1")
					.addLook(R.drawable.n2, "2")
					.addLook(R.drawable.n3, "3")
					.addLook(R.drawable.n4, "4")
					.addLook(R.drawable.n5, "5")
					.addLook(R.drawable.n6, "6")
					.addLook(R.drawable.n7, "7")
					.addLook(R.drawable.n8, "8")
					.addLook(R.drawable.n9, "9")
				;
				//@formatter:on
				return sprite;
			}
		}
				.setDescription("")
				.build();
	}
}
