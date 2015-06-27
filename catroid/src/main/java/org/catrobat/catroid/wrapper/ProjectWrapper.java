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

public class ProjectWrapper {

	private final Context context;
	final Project project;
	private final SceneWrapper firstScene;

	public ProjectWrapper(Context context, String projectName) {
		this(context, projectName, 0, 0);
	}

	public ProjectWrapper(Context context, String name, int width, int height) {
		this.context = context;
		project = new Project(context, name);
		project.setDeviceData(context);

		if (width > 0 && height > 0) {
			setSize(width, height);
		}

		firstScene = new SceneWrapper(context, this, project.getDefaultScene());
		done();
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

	public SceneWrapper firstScene() {
		return firstScene;
	}

	public SceneWrapper createScene(String name) {
		return new SceneWrapper(context, this, name);
	}

	public Project done() {
		ProjectManager.getInstance().setProject(project);
		StorageHandler.getInstance().saveProject(project);
		return project;
	}
}
