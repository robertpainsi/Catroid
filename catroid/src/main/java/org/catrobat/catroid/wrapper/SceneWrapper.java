package org.catrobat.catroid.wrapper;

import android.content.Context;

import org.catrobat.catroid.content.Scene;

public class SceneWrapper {

	private final Context context;
	final ProjectWrapper projectWrapper;
	final Scene scene;
	private final SpriteWrapper background;

	SceneWrapper(Context context, ProjectWrapper projectWrapper, String name) {
		this(context, projectWrapper, new Scene(context, name, projectWrapper.project));
		projectWrapper.project.addScene(scene);
	}

	SceneWrapper(Context context, ProjectWrapper projectWrapper, Scene scene) {
		this.context = context;
		this.projectWrapper = projectWrapper;
		this.scene = scene;
		this.background = new SpriteWrapper(context, this, scene.getSpriteList().get(0));
	}

	public SpriteWrapper getBackground() {
		return background;
	}

	public SpriteWrapper createSprite(String name) {
		return new SpriteWrapper(context, this, name);
	}

	public ProjectWrapper done() {
		return projectWrapper;
	}
}
