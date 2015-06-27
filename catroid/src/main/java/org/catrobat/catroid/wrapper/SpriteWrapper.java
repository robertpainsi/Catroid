/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2014 The Catrobat Team
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
import android.util.Log;
import android.util.SparseArray;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.common.LookData;
import org.catrobat.catroid.common.SoundInfo;
import org.catrobat.catroid.content.BroadcastScript;
import org.catrobat.catroid.content.CollisionScript;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.SingleSprite;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.WhenBackgroundChangesScript;
import org.catrobat.catroid.content.WhenClonedScript;
import org.catrobat.catroid.content.WhenConditionScript;
import org.catrobat.catroid.content.WhenScript;
import org.catrobat.catroid.content.WhenTouchDownScript;
import org.catrobat.catroid.formulaeditor.datacontainer.DataContainer;
import org.catrobat.catroid.soundrecorder.SoundRecorder;
import org.catrobat.catroid.utils.UtilFile;

import java.io.File;
import java.io.IOException;

public class SpriteWrapper {
	private static final String TAG = SpriteWrapper.class.getSimpleName();

	private final SparseArray<LookData> copiedLooks = new SparseArray<LookData>();
	private final SparseArray<SoundInfo> copiedSounds = new SparseArray<SoundInfo>();

	private final SparseArray<LookData> lookDataMap = new SparseArray<LookData>();
	private final SparseArray<SoundInfo> soundInfoMap = new SparseArray<SoundInfo>();

	private final Context context;
	final SceneWrapper sceneWrapper;
	final Sprite sprite;

	SpriteWrapper(Context context, SceneWrapper sceneWrapper, String name) {
		this(context, sceneWrapper, new SingleSprite(name));
		sceneWrapper.scene.addSprite(sprite);
	}

	SpriteWrapper(Context context, SceneWrapper sceneWrapper, Sprite sprite) {
		this.context = context;
		this.sceneWrapper = sceneWrapper;
		this.sprite = sprite;
	}

	public SceneWrapper done() {
		return sceneWrapper;
	}

	private DataContainer getDataContainer() {
		return sceneWrapper.scene.getDataContainer();
	}

	private ScriptWrapper add(Script script) {
		return new ScriptWrapper(context, this, script);
	}

	public ScriptWrapper whenProgramStarted() {
		return add(new StartScript());
	}

	public ScriptWrapper whenTapped() {
		return add(new WhenScript());
	}

	public ScriptWrapper whenScreenIsTouched() {
		return add(new WhenTouchDownScript());
	}

	public ScriptWrapper whenIReceive(String message) {
		return add(new BroadcastScript(message));
	}

//	TODO
//	public ScriptWrapper whenFormulaBecomesTrue() {
//		return add(new WhenConditionScript());
//	}
//
//	public ScriptWrapper whenPhysicalCollisionWith() {
//		return add(new CollisionScript());
//	}

	public ScriptWrapper whenBackgroundChangesTo(int id) {
		WhenBackgroundChangesScript script = new WhenBackgroundChangesScript();
		script.setLook(getLookData(id));
		return add(script);
	}

	public ScriptWrapper whenIStartAsAClone() {
		return add(new WhenClonedScript());
	}

	public SpriteWrapper addSpriteList(String list) {
		ProjectManager.getInstance().setCurrentSprite(sprite);
		getDataContainer().addSpriteUserList(list);
		return this;
	}

	public SpriteWrapper addProjectList(String list) {
		getDataContainer().addProjectUserList(list);
		return this;
	}

	public SpriteWrapper addLook(int id, String name) {
		if (copiedLooks.indexOfKey(id) < 0) {
			copyLook(id);
		}

		LookData lookData = new LookData();
		lookData.setLookName(name);
		lookData.setLookFilename(copiedLooks.get(id).getLookFileName());
		sprite.getLookDataList().add(lookData);

		lookDataMap.put(id, lookData);

		return this;
	}

	public LookData getLookData(int id) {
		if (lookDataMap.indexOfKey(id) < 0) {
			addLook(id, String.valueOf(id));
		}
		return lookDataMap.get(id);
	}

	private void copyLook(int id) {
		String name = String.valueOf(id);
		try {
			File file = UtilFile.copyImageFromResourceIntoProject(sceneWrapper.projectWrapper.project.getName(),
					sceneWrapper.scene.getName(), name + Constants.IMAGE_STANDARD_EXTENSION, id, context, true, 1.0);

			LookData lookData = new LookData();
			lookData.setLookName(name);
			lookData.setLookFilename(file.getName());

			copiedLooks.put(id, lookData);
		} catch (IOException ioException) {
			Log.e(TAG, "Copying look failed", ioException);
		}
	}

	public SpriteWrapper addSound(int id, String name) {
		if (copiedSounds.indexOfKey(id) < 0) {
			copySounds(id);
		}

		SoundInfo soundInfo = new SoundInfo();
		soundInfo.setTitle(name);
		soundInfo.setSoundFileName(copiedSounds.get(id).getSoundFileName());
		sprite.getSoundList().add(soundInfo);

		soundInfoMap.put(id, soundInfo);

		return this;
	}

	public SoundInfo getSoundInfo(int id) {
		if (soundInfoMap.indexOfKey(id) < 0) {
			addSound(id, String.valueOf(id));
		}
		return soundInfoMap.get(id);
	}

	private void copySounds(int id) {
		String name = String.valueOf(id);
		try {
			File file = UtilFile.copySoundFromResourceIntoProject(sceneWrapper.projectWrapper.project.getName(),
					sceneWrapper.scene.getName(), name + SoundRecorder.RECORDING_EXTENSION, id, context, true);

			SoundInfo soundInfo = new SoundInfo();
			soundInfo.setTitle(name);
			soundInfo.setSoundFileName(file.getName());

			copiedSounds.put(id, soundInfo);
		} catch (IOException ioException) {
			Log.e(TAG, "Copying sound failed", ioException);
		}
	}

	public SpriteWrapper addSpriteVariable(String variable) {
		ProjectManager.getInstance().setCurrentSprite(sprite);
		getDataContainer().addSpriteUserVariable(variable);
		return this;
	}

	public SpriteWrapper addProjectVariable(String variable) {
		getDataContainer().addProjectUserVariable(variable);
		return this;
	}
}
