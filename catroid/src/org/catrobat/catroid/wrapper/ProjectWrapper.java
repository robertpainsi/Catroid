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

package org.catrobat.catroid.wrapper;

import android.content.Context;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.io.StorageHandler;

public abstract class ProjectWrapper {
	private final Project project;
	private final SpriteWrapper background;

	public ProjectWrapper(Context context, String projectName) {
		this(context, projectName, 0, 0);
	}

	public ProjectWrapper(Context context, String projectName, int width, int height) {
		project = new Project(context, projectName);
		project.setDeviceData(context);

		if (width > 0 && height > 0) {
			setSize(width, height);
		}

		SpriteWrapper.init(context, project);

		background = new SpriteWrapper(project.getSpriteList().get(0), false);
	}

	public void addProjectVariable(String variable) {
		project.getDataContainer().addProjectUserVariable(variable);
	}

	public void addProjectList(String list) {
		project.getDataContainer().addProjectUserList(list);
	}

	public ProjectWrapper setSize(int width, int height) {
		project.getXmlHeader().virtualScreenWidth = width;
		project.getXmlHeader().virtualScreenHeight = height;
		return this;
	}

	public ProjectWrapper setDescription(String description) {
		project.setDescription(description);
		return this;
	}

	public SpriteWrapper getBackground() {
		return background;
	}

	public Project build() {
		StorageHandler.getInstance().saveProject(project);
		ProjectManager.getInstance().setProject(project);

		createSprites();

		StorageHandler.getInstance().saveProject(project);
		return project;
	}

	protected abstract void createSprites();
}
