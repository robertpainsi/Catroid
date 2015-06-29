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

public class DangerousCrossingProject extends CreateAndPlayProject {

	@Override
	protected void createProject() {
		new ProjectWrapper(getActivity(), "Dangerous crossing", 1080, 1920) {
			@Override
			protected void createSprites() {
				//@formatter:off
				addProjectVariable("ANIMATE");
				addProjectVariable("MOVE_NUMBER");
				addProjectVariable("REMAINING_DWARFS");
				addProjectVariable("SPEED");
				addProjectVariable("TIME");
				addProjectVariable("TORCH_HOLDER");
				addProjectVariable("TORCH_POSITION");
				addProjectList("SELECTED");

				getBackground()
					.addLook(R.drawable.bat_background, "Background")
				;

				new SpriteWrapper("Game logic")
					.whenProgramStarted()
						.setVariable("ANIMATE", "TRUE")

					.whenIReceive("reset")
						.setVariable("ANIMATE", "FALSE")
						.setVariable("MOVE_NUMBER", 0)
						.setVariable("REMAINING_DWARFS", 4)
						.setVariable("TIME", 0)
						.setVariable("TORCH_HOLDER", 0)
						.setVariable("TORCH_POSITION", "'right'")
						.broadcast("clear_selected_list")
						.broadcast("start")

					.whenIReceive("init_go")
						.setVariable("ANIMATE", "TRUE")
						.setVariable("SPEED", "max(element(1, [SELECTED]), max(1, element(2, [SELECTED])))")
						.changeVariable("TIME", "{SPEED}")
						.changeVariable("MOVE_NUMBER", 1)

						.broadcast("go")

						.wait("{SPEED}")
						.ifCondition("{TIME} <= 15")
							.ifCondition("{REMAINING_DWARFS} == 0")
								.broadcast("win")
								.note("lose is handled by object [Time units position] because it's a timer")
							.elseCondition()
								.setVariable("ANIMATE", "FALSE")
							.endIfCondition()
						.endWithElseIfCondition()

						.broadcastAndWait("clear_selected_list")
						.broadcastAndWait("switch_torch_position")
						.broadcast("update")


					.whenIReceive("clear_selected_list")
						.repeat("number_of_items([SELECTED])")
							.deleteItemFormList("SELECTED", 1)
						.endRepeat()

					.whenIReceive("switch_torch_position")
						.ifCondition("{TORCH_POSITION} == 'left'")
							.setVariable("TORCH_POSITION", "'right'")
						.elseCondition()
							.setVariable("TORCH_POSITION", "'left'")
						.endIfCondition()
				;

				new SpriteWrapper("Foreground Layer")
					.addLook(R.drawable.bat_foreground, "Black layer")
					.addSpriteVariable("darken_time")

					.whenProgramStarted()
						.setTransparency(100)
						.setSize(10000)
						.forever()
							.ifCondition("{TIME} == 0")
								.setTransparency(100)
								.setVariable("darken_time", 0)
							.endWithElseIfCondition()
							.ifCondition("{darken_time} < {TIME}")
								.repeat(50)
									.changeTransparencyBy(-0.1)
									.wait(0.02)
								.endRepeat()
								.changeVariable("darken_time", 1.0)
							.endWithElseIfCondition()
						.endForever()

					.whenIReceive("lose")
						.repeat(50)
							.changeTransparencyBy(-0.5)
						.endRepeat()
				;

				new SpriteWrapper("Torch")
					.addLook(R.drawable.bat_torch, "Torch1")
					.addLook(R.drawable.bat_torch2, "Torch2")

					.whenProgramStarted()
						.placeAt(128, -380)

					.whenIReceive("start")
						.goToFront()
						.placeAt(128, -380)
						.show()
						.forever()
							.nextLook()
							.wait(0.5)
						.endForever()

					.whenIReceive("go")
						.glideTo("-position_x - (2 * 64)", "position_y", "{SPEED}")

					.whenIReceive("update")
						.goToFront()
						.goBack("7 + (3 - {TORCH_HOLDER})")
						.ifCondition("{TORCH_POSITION} == 'right'")
							.setX("190 + ({TORCH_HOLDER} * 85) - 64")
						.elseCondition()
							.setX("-(190 + ({TORCH_HOLDER} * 85)) - 64")
						.endIfCondition()

					.whenIReceive("lose")
						.hide()
				;

				new SpriteWrapper("Info box")
					.whenProgramStarted()
						.hide()

					.whenIReceive("go")
						.hide()

					.whenIReceive("start")
						.broadcast("show_info_box_1")

					.whenIReceive("update")
						.ifCondition("{MOVE_NUMBER} == 0")
							.broadcast("show_info_box_1")
						.elseCondition()
							.ifCondition("{MOVE_NUMBER} == 1")
								.broadcast("show_info_box_2")
							.elseCondition()
								.hide()
							.endIfCondition()
						.endIfCondition()

					.whenIReceive("show_info_box_1")
						.placeAt(240, 120)
						.setLook(R.drawable.bat_info_box1)
						.show()

					.whenIReceive("show_info_box_2")
						.placeAt(-240, 120)
						.setLook(R.drawable.bat_info_box2)
						.show()
				;

				createDwarf(0, "Pinky", 8, R.drawable.bat_dwarf_pinky, R.drawable.bat_dwarf_pinky_selected, 190, -380);
				createDwarf(1, "Greeny", 5, R.drawable.bat_dwarf_greeny, R.drawable.bat_dwarf_greeny_selected, 275, -400);
				createDwarf(2, "Bluey", 2, R.drawable.bat_dwarf_bluey, R.drawable.bat_dwarf_bluey_selected, 360, -410);
				createDwarf(3, "Redy", 1, R.drawable.bat_dwarf_redy, R.drawable.bat_dwarf_redy_selected, 445, -440);

				SpriteWrapper timeUnits = new SpriteWrapper("Time units position");
				fillWithNumbers(timeUnits)
					.addSpriteVariable("current_time")
					.addSpriteVariable("old_time")
					.whenProgramStarted()
						.placeAt(-105, 764)

					.whenIReceive("start")
						.switchToLook(R.drawable.bat_0)
						.setVariable("old_time", 0)

					.whenIReceive("go")
						.setVariable("current_time", "{old_time}")
						.repeat("{TIME} - {old_time}")
							.wait(0.5)
							.nextLook()
							.ifCondition("mod({current_time} + 1, 10) == 0")
								.broadcast("carry")
							.endWithElseIfCondition()
							.ifCondition("{current_time} == 15")
								.broadcast("lose")
							.endWithElseIfCondition()
							.changeVariable("current_time", 1)
							.wait(0.5)
						.endRepeat()
						.setVariable("old_time", "{TIME}")
				;

				SpriteWrapper timeTens = new SpriteWrapper("Time tens position");
				fillWithNumbers(timeTens)
					.whenProgramStarted()
						.placeAt(-245, 764)

					.whenIReceive("start")
						.switchToLook(R.drawable.bat_0)

					.whenIReceive("carry")
						.nextLook()
				;

				new SpriteWrapper("Clock")
					.addLook(R.drawable.bat_seconds, "Clock")

					.whenProgramStarted()
						.placeAt(-414, 764)
				;

				new SpriteWrapper("Intro")
					.addLook(R.drawable.bat_intro, "Intro")

					.whenIReceive("start")
						.hide()
				;

				new SpriteWrapper("Start button")
					.addLook(R.drawable.bat_start, "Start")
					.addLook(R.drawable.bat_restart, "Restart")

					.whenProgramStarted()
						.placeAt(300, -775)

					.whenIReceive("start")
						.show()

					.whenTapped()
						.switchToLook(R.drawable.bat_restart)
						.placeAt(350, 760)
						.broadcast("reset")

					.whenIReceive("update")
						.show()

					.whenIReceive("go")
						.hide()
				;

				new SpriteWrapper("Go button")
					.addLook(R.drawable.bat_go_left_button, "Go left")
					.addLook(R.drawable.bat_go_right_button, "Go right")

					.whenProgramStarted()
						.hide()

					.whenIReceive("start")
						.placeAt(330, -730)
						.hide()
						.switchToLook(R.drawable.bat_go_left_button)

					.whenTapped()
						.hide()
						.setX("-position_x")
						.nextLook()
						.broadcast("init_go")

					.whenIReceive("update")
						.ifCondition("({TORCH_POSITION} == 'right' && number_of_items([SELECTED]) == 2)" +
								"|| ({TORCH_POSITION} == 'left' && number_of_items([SELECTED]) == 1)")
							.show()
						.elseCondition()
							.hide()
						.endIfCondition()
				;

				new SpriteWrapper("Result")
					.addLook(R.drawable.bat_win, "Win")
					.addLook(R.drawable.bat_lose, "Lose")
					.addSound(R.raw.bat_tada, "Ta da")
					.addSound(R.raw.bat_trumpet, "Trumpet")

					.whenProgramStarted()
						.hide()

					.whenIReceive("start")
						.hide()

					.whenIReceive("win")
						.switchToLook(R.drawable.bat_win)
						.show()
						.startSound(R.raw.bat_tada)

					.whenIReceive("lose")
						.switchToLook(R.drawable.bat_lose)
						.show()
						.startSound(R.raw.bat_trumpet)
				;
				//@formatter:on
			}

			private void createDwarf(int id, String name, int speed, int imageId, int imageIdSelected, double x, double y) {
				//@formatter:off
				new SpriteWrapper(name)
					.addLook(imageId, name)
					.addLook(imageIdSelected, name + " selected")
					.addSpriteVariable("position")
					.addSpriteVariable("speed")

					.whenProgramStarted()
						.placeAt(x, y)
						.setVariable("speed", speed)

					.whenIReceive("start")
						.placeAt(x, y)
						.setVariable("position", "'right'")
						.switchToLook(imageId)
						.show()

					.whenTapped()
						.ifCondition("{position} == {TORCH_POSITION} && {ANIMATE} == FALSE")
							.ifCondition("element(1, [SELECTED]) == {speed}")
								.deleteItemFormList("SELECTED", 1)
							.elseCondition()
								.ifCondition("element(2, [SELECTED]) == {speed}")
									.deleteItemFormList("SELECTED", 2)
								.elseCondition()
									.ifCondition("({TORCH_POSITION} == 'left' && number_of_items([SELECTED]) == 1)" +
											"|| ({TORCH_POSITION} == 'right' && number_of_items([SELECTED]) == 2)")
										.deleteItemFormList("SELECTED", 1)
									.endWithElseIfCondition()
									.addItemToList("SELECTED", "{speed}")
									.setVariable("TORCH_HOLDER", id)
								.endIfCondition()
							.endIfCondition()
							.broadcast("update")
						.endWithElseIfCondition()

					.whenIReceive("update")
						.ifCondition("contains([SELECTED], {speed})")
							.switchToLook(imageIdSelected)
						.elseCondition()
							.switchToLook(imageId)
						.endIfCondition()

					.whenIReceive("go")
						.switchToLook(imageId)
						.ifCondition("contains([SELECTED], {speed})")
							.ifCondition("{position} == 'right'")
								.setVariable("position", "'left'")
								.changeVariable("REMAINING_DWARFS", "-1")
							.elseCondition()
								.setVariable("position", "'right'")
								.changeVariable("REMAINING_DWARFS", "1")
							.endIfCondition()
						.glideTo("-position_x", "position_y", "{SPEED}")
						.endWithElseIfCondition()

					.whenIReceive("lose")
						.hide()
				;
				//@formatter:on
			}

			private SpriteWrapper fillWithNumbers(SpriteWrapper sprite) {
				//@formatter:off
				return sprite
					.addLook(R.drawable.bat_0, "0")
					.addLook(R.drawable.bat_1, "1")
					.addLook(R.drawable.bat_2, "2")
					.addLook(R.drawable.bat_3, "3")
					.addLook(R.drawable.bat_4, "4")
					.addLook(R.drawable.bat_5, "5")
					.addLook(R.drawable.bat_6, "6")
					.addLook(R.drawable.bat_7, "7")
					.addLook(R.drawable.bat_8, "8")
					.addLook(R.drawable.bat_9, "9")
				;
				//@formatter:on
			}
		}
				.setDescription("Four dwarfs, Redy, Bluey, Greeny and Pinky have to cross a bridge at night.\n" +
						"The bridge is narrow and not more than 2 dwarfs can cross it at a time. Because of the" +
						" darkness they have to use a torch to light the way. The torch only lasts up to 15 seconds"
						+ ".\n" +
						"Redy is the fastest of the dwarfs and can cross the bridge in 1 second. Bluey needs 2," +
						" Greeny 5 and Pinky 8 seconds.\n" +
						"When walking together the slowest of the 2 dwarfs sets the pace.\n\n" +
						"Can you guide them across the bridge before the torch goes out?")
				.build();
	}
}
