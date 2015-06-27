package org.catrobat.catroid.uitest.wrapper;

import org.catrobat.catroid.R;
import org.catrobat.catroid.wrapper.ProjectWrapper;

public class CreateSimpleProject extends CreateAndPlayProject {

	@Override
	protected void createProject() {
		new ProjectWrapper(getActivity(), "Muku_" + System.currentTimeMillis() / 1000, 480, 800) {

			@Override
			protected void createSprites() {
				//@formatter:off
				getBackground()
					.addSpriteList("list")
					.addSpriteVariable("var")
					.addLook(R.drawable.default_project_mole_digged_out, "Mole")

					.whenProgramStarted()
						.addItemToList("list", 100)
						.addItemToList("list", 200)
						.addItemToList("list", 300)
						.addItemToList("list", 400)
						.setVariable("var", "'HelloWorld'")

						.ifCondition("number_of_items([list]) == 4")
							.setX("200")
						.elseCondition().endIfCondition()
						.ifCondition("element(3, [list]) == 300")
						.	setY("200")
						.elseCondition().endIfCondition()
						.ifCondition("contains([list], 200) == 1.0")
						.	setSize("10")
						.elseCondition().endIfCondition()
						.ifCondition("{var} == 'HelloWorld'")
							.pointInDirection("45")
						.elseCondition().endIfCondition()
				;
				//@formatter:on
			}
		}.build();
	}

}
