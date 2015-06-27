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
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.WhenScript;
import org.catrobat.catroid.content.bricks.AddItemToUserListBrick;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.BroadcastBrick;
import org.catrobat.catroid.content.bricks.BroadcastWaitBrick;
import org.catrobat.catroid.content.bricks.ChangeBrightnessByNBrick;
import org.catrobat.catroid.content.bricks.ChangeSizeByNBrick;
import org.catrobat.catroid.content.bricks.ChangeTransparencyByNBrick;
import org.catrobat.catroid.content.bricks.ChangeVariableBrick;
import org.catrobat.catroid.content.bricks.ChangeVolumeByNBrick;
import org.catrobat.catroid.content.bricks.ChangeXByNBrick;
import org.catrobat.catroid.content.bricks.ChangeYByNBrick;
import org.catrobat.catroid.content.bricks.ClearGraphicEffectBrick;
import org.catrobat.catroid.content.bricks.ComeToFrontBrick;
import org.catrobat.catroid.content.bricks.DeleteItemOfUserListBrick;
import org.catrobat.catroid.content.bricks.ForeverBrick;
import org.catrobat.catroid.content.bricks.GlideToBrick;
import org.catrobat.catroid.content.bricks.GoNStepsBackBrick;
import org.catrobat.catroid.content.bricks.HideBrick;
import org.catrobat.catroid.content.bricks.IfLogicBeginBrick;
import org.catrobat.catroid.content.bricks.IfLogicElseBrick;
import org.catrobat.catroid.content.bricks.IfLogicEndBrick;
import org.catrobat.catroid.content.bricks.IfOnEdgeBounceBrick;
import org.catrobat.catroid.content.bricks.InsertItemIntoUserListBrick;
import org.catrobat.catroid.content.bricks.LoopBeginBrick;
import org.catrobat.catroid.content.bricks.LoopEndBrick;
import org.catrobat.catroid.content.bricks.LoopEndlessBrick;
import org.catrobat.catroid.content.bricks.MoveNStepsBrick;
import org.catrobat.catroid.content.bricks.NextLookBrick;
import org.catrobat.catroid.content.bricks.NoteBrick;
import org.catrobat.catroid.content.bricks.PlaceAtBrick;
import org.catrobat.catroid.content.bricks.PlaySoundBrick;
import org.catrobat.catroid.content.bricks.PointInDirectionBrick;
import org.catrobat.catroid.content.bricks.PointToBrick;
import org.catrobat.catroid.content.bricks.RepeatBrick;
import org.catrobat.catroid.content.bricks.ReplaceItemInUserListBrick;
import org.catrobat.catroid.content.bricks.SetBrightnessBrick;
import org.catrobat.catroid.content.bricks.SetLookBrick;
import org.catrobat.catroid.content.bricks.SetSizeToBrick;
import org.catrobat.catroid.content.bricks.SetTransparencyBrick;
import org.catrobat.catroid.content.bricks.SetVariableBrick;
import org.catrobat.catroid.content.bricks.SetVolumeToBrick;
import org.catrobat.catroid.content.bricks.SetXBrick;
import org.catrobat.catroid.content.bricks.SetYBrick;
import org.catrobat.catroid.content.bricks.ShowBrick;
import org.catrobat.catroid.content.bricks.SpeakBrick;
import org.catrobat.catroid.content.bricks.StopAllSoundsBrick;
import org.catrobat.catroid.content.bricks.TurnLeftBrick;
import org.catrobat.catroid.content.bricks.TurnRightBrick;
import org.catrobat.catroid.content.bricks.WaitBrick;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.UserList;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.soundrecorder.SoundRecorder;
import org.catrobat.catroid.utils.UtilFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SpriteWrapper {
	private static final String TAG = SpriteWrapper.class.getSimpleName();

	private static Context context;
	private static Project project;

	public final Sprite sprite;
	public Script currentScript;

	private static final SparseArray<LookData> copiedLooks = new SparseArray<LookData>();
	private static final SparseArray<SoundInfo> copiedSounds = new SparseArray<SoundInfo>();

	private final SparseArray<LookData> lookDataMap = new SparseArray<LookData>();
	private final SparseArray<SoundInfo> soundInfoMap = new SparseArray<SoundInfo>();

	public SpriteWrapper(String name) {
		this(new Sprite(name));
	}

	public SpriteWrapper(Sprite sprite) {
		this(sprite, true);
	}

	SpriteWrapper(Sprite sprite, boolean addToProject) {
		this.sprite = sprite;

		if (addToProject) {
			project.addSprite(this.sprite);
		}
	}

	public static void init(Context context, Project project) {
		SpriteWrapper.context = context;
		SpriteWrapper.project = project;
	}

	public SpriteWrapper clone() {
		return new SpriteWrapper(sprite.clone());
	}

	private void add(Script script) {
		currentScript = script;
		sprite.addScript(script);
	}

	private void add(Brick brick) {
		if (currentScript == null) {
			whenProgramStarted();
		}
		currentScript.addBrick(brick);
	}

	public SpriteWrapper whenProgramStarted() {
		add(new StartScript());
		return this;
	}

	public SpriteWrapper whenTapped() {
		add(new WhenScript());
		return this;
	}

	public SpriteWrapper wait(double seconds) {
		return wait(new Formula(seconds));
	}

	public SpriteWrapper wait(String seconds) {
		return wait(FormulaParser.parse(seconds));
	}

	public SpriteWrapper wait(Formula seconds) {
		add(new WaitBrick(seconds));
		return this;
	}

	public SpriteWrapper whenIReceive(String message) {
		add(new BroadcastScript(message));
		return this;
	}

	public SpriteWrapper broadcast(String message) {
		add(new BroadcastBrick(message));
		return this;
	}

	public SpriteWrapper broadcastAndWait(String message) {
		add(new BroadcastWaitBrick(message));
		return this;
	}

	public SpriteWrapper note(String note) {
		add(new NoteBrick(note));
		return this;
	}

	public SpriteWrapper forever() {
		add(new ForeverBrick());
		return this;
	}

	public SpriteWrapper endForever() {
		add(new LoopEndlessBrick(getLoopBeginBrick()));
		return this;
	}

	public SpriteWrapper ifCondition(double condition) {
		return ifCondition(new Formula(condition));
	}

	public SpriteWrapper ifCondition(String condition) {
		return ifCondition(FormulaParser.parse(condition));
	}

	public SpriteWrapper ifCondition(Formula condition) {
		add(new IfLogicBeginBrick(condition));
		return this;
	}

	public SpriteWrapper elseCondition() {
		add(new IfLogicElseBrick(getIfLogicBeginBrick()));
		return this;
	}

	public SpriteWrapper endWithElseIfCondition() {
		elseCondition();
		return endIfCondition();
	}

	public SpriteWrapper endIfCondition() {
		IfLogicElseBrick ifLogicElseBrick = getIfLogicElseBrick();
		add(new IfLogicEndBrick(ifLogicElseBrick, ifLogicElseBrick.getIfBeginBrick()));
		return this;
	}

	public SpriteWrapper repeat(double timesToRepeat) {
		return repeat(new Formula(timesToRepeat));
	}

	public SpriteWrapper repeat(String timesToRepeat) {
		return repeat(FormulaParser.parse(timesToRepeat));
	}

	public SpriteWrapper repeat(Formula timesToRepeat) {
		add(new RepeatBrick(timesToRepeat));
		return this;
	}

	public SpriteWrapper endRepeat() {
		add(new LoopEndBrick(getLoopBeginBrick()));
		return this;
	}

	public SpriteWrapper placeAt(double x, double y) {
		return placeAt(new Formula(x), new Formula(y));
	}

	public SpriteWrapper placeAt(String x, String y) {
		return placeAt(FormulaParser.parse(x), FormulaParser.parse(y));
	}

	public SpriteWrapper placeAt(Formula x, Formula y) {
		add(new PlaceAtBrick(x, y));
		return this;
	}

	public SpriteWrapper setX(double x) {
		return setX(new Formula(x));
	}

	public SpriteWrapper setX(String x) {
		return setX(FormulaParser.parse(x));
	}

	public SpriteWrapper setX(Formula x) {
		add(new SetXBrick(x));
		return this;
	}

	public SpriteWrapper setY(double y) {
		return setY(new Formula(y));
	}

	public SpriteWrapper setY(String y) {
		return setY(FormulaParser.parse(y));
	}

	public SpriteWrapper setY(Formula y) {
		add(new SetYBrick(y));
		return this;
	}

	public SpriteWrapper changeXBy(double x) {
		return changeXBy(new Formula(x));
	}

	public SpriteWrapper changeXBy(String x) {
		return changeXBy(FormulaParser.parse(x));
	}

	public SpriteWrapper changeXBy(Formula x) {
		add(new ChangeXByNBrick(x));
		return this;
	}

	public SpriteWrapper changeYBy(double y) {
		return changeYBy(new Formula(y));
	}

	public SpriteWrapper changeYBy(String y) {
		return changeYBy(FormulaParser.parse(y));
	}

	public SpriteWrapper changeYBy(Formula y) {
		add(new ChangeYByNBrick(y));
		return this;
	}

	public SpriteWrapper ifOnEdgeBounce() {
		add(new IfOnEdgeBounceBrick());
		return this;
	}

	public SpriteWrapper moveNSteps(double steps) {
		return moveNSteps(new Formula(steps));
	}

	public SpriteWrapper moveNSteps(String steps) {
		return moveNSteps(FormulaParser.parse(steps));
	}

	public SpriteWrapper moveNSteps(Formula steps) {
		add(new MoveNStepsBrick(steps));
		return this;
	}

	public SpriteWrapper turnLeft(double degrees) {
		return turnLeft(new Formula(degrees));
	}

	public SpriteWrapper turnLeft(String degrees) {
		return turnLeft(FormulaParser.parse(degrees));
	}

	public SpriteWrapper turnLeft(Formula degrees) {
		add(new TurnLeftBrick(degrees));
		return this;
	}

	public SpriteWrapper turnRight(double degrees) {
		return turnRight(new Formula(degrees));
	}

	public SpriteWrapper turnRight(String degrees) {
		return turnRight(FormulaParser.parse(degrees));
	}

	public SpriteWrapper turnRight(Formula degrees) {
		add(new TurnRightBrick(degrees));
		return this;
	}

	public SpriteWrapper pointInDirection(double direction) {
		return pointInDirection(new Formula(direction));
	}

	public SpriteWrapper pointInDirection(String direction) {
		return pointInDirection(FormulaParser.parse(direction));
	}

	public SpriteWrapper pointInDirection(Formula direction) {
		add(new PointInDirectionBrick(direction));
		return this;
	}

	public SpriteWrapper pointTowards(Sprite pointedSprite) {
		add(new PointToBrick(pointedSprite));
		return this;
	}

	public SpriteWrapper pointTowards(SpriteWrapper pointedSprite) {
		add(new PointToBrick(pointedSprite.sprite));
		return this;
	}

	public SpriteWrapper glideTo(double x, double y, double seconds) {
		return glideTo(new Formula(x), new Formula(y), new Formula(seconds));
	}

	public SpriteWrapper glideTo(String x, String y, String seconds) {
		return glideTo(FormulaParser.parse(x), FormulaParser.parse(y), FormulaParser.parse(seconds));
	}

	public SpriteWrapper glideTo(Formula x, Formula y, Formula seconds) {
		add(new GlideToBrick(x, y, seconds));
		return this;
	}

	public SpriteWrapper goBack(double layers) {
		return goBack(new Formula(layers));
	}

	public SpriteWrapper goBack(String layers) {
		return goBack(FormulaParser.parse(layers));
	}

	public SpriteWrapper goBack(Formula layers) {
		add(new GoNStepsBackBrick(layers));
		return this;
	}

	public SpriteWrapper goToFront() {
		add(new ComeToFrontBrick());
		return this;
	}

	public SpriteWrapper startSound(int id) {
		PlaySoundBrick playSoundBrick = new PlaySoundBrick();
		playSoundBrick.setSoundInfo(getSoundInfo(id));
		add(playSoundBrick);
		return this;
	}

	public SpriteWrapper stopAllSounds() {
		add(new StopAllSoundsBrick());
		return this;
	}

	public SpriteWrapper setVolume(double volume) {
		return setVolume(new Formula(volume));
	}

	public SpriteWrapper setVolume(String volume) {
		return setVolume(FormulaParser.parse(volume));
	}

	public SpriteWrapper setVolume(Formula volume) {
		add(new SetVolumeToBrick(volume));
		return this;
	}

	public SpriteWrapper changeVolumeBy(double volume) {
		return changeVolumeBy(new Formula(volume));
	}

	public SpriteWrapper changeVolumeBy(String volume) {
		return changeVolumeBy(FormulaParser.parse(volume));
	}

	public SpriteWrapper changeVolumeBy(Formula volume) {
		add(new ChangeVolumeByNBrick(volume));
		return this;
	}

	public SpriteWrapper speak(String text) {
		add(new SpeakBrick(text));
		return this;
	}

	public SpriteWrapper setLook(int id) {
		return switchToLook(id);
	}

	public SpriteWrapper switchToLook(int id) {
		SetLookBrick setLookBrick = new SetLookBrick();
		setLookBrick.setLook(getLookData(id));
		add(setLookBrick);
		return this;
	}

	public SpriteWrapper nextLook() {
		add(new NextLookBrick());
		return this;
	}

	public SpriteWrapper setSize(double size) {
		return setSize(new Formula(size));
	}

	public SpriteWrapper setSize(String size) {
		return setSize(FormulaParser.parse(size));
	}

	public SpriteWrapper setSize(Formula size) {
		add(new SetSizeToBrick(size));
		return this;
	}

	public SpriteWrapper changeSizeBy(double size) {
		return changeSizeBy(new Formula(size));
	}

	public SpriteWrapper changeSizeBy(String size) {
		return changeSizeBy(FormulaParser.parse(size));
	}

	public SpriteWrapper changeSizeBy(Formula size) {
		add(new ChangeSizeByNBrick(size));
		return this;
	}

	public SpriteWrapper hide() {
		add(new HideBrick());
		return this;
	}

	public SpriteWrapper show() {
		add(new ShowBrick());
		return this;
	}

	public SpriteWrapper setTransparency(double transparency) {
		return setTransparency(new Formula(transparency));
	}

	public SpriteWrapper setTransparency(String transparency) {
		return setTransparency(FormulaParser.parse(transparency));
	}

	public SpriteWrapper setTransparency(Formula transparency) {
		add(new SetTransparencyBrick(transparency));
		return this;
	}

	public SpriteWrapper changeTransparencyBy(double transparency) {
		return changeTransparencyBy(new Formula(transparency));
	}

	public SpriteWrapper changeTransparencyBy(String transparency) {
		return changeTransparencyBy(FormulaParser.parse(transparency));
	}

	public SpriteWrapper changeTransparencyBy(Formula transparency) {
		add(new ChangeTransparencyByNBrick(transparency));
		return this;
	}

	public SpriteWrapper setBrightness(double brightness) {
		return setBrightness(new Formula(brightness));
	}

	public SpriteWrapper setBrightness(String brightness) {
		return setBrightness(FormulaParser.parse(brightness));
	}

	public SpriteWrapper setBrightness(Formula brightness) {
		add(new SetBrightnessBrick(brightness));
		return this;
	}

	public SpriteWrapper changeBrightnessBy(double brightness) {
		return changeBrightnessBy(new Formula(brightness));
	}

	public SpriteWrapper changeBrightnessBy(String brightness) {
		return changeBrightnessBy(FormulaParser.parse(brightness));
	}

	public SpriteWrapper changeBrightnessBy(Formula brightness) {
		add(new ChangeBrightnessByNBrick(brightness));
		return this;
	}

	public SpriteWrapper clearGraphicEffects() {
		add(new ClearGraphicEffectBrick());
		return this;
	}

	public SpriteWrapper setVariable(String variable, double value) {
		return setVariable(variable, new Formula(value));
	}

	public SpriteWrapper setVariable(String variable, String value) {
		return setVariable(variable, FormulaParser.parse(value));
	}

	public SpriteWrapper setVariable(String variable, Formula value) {
		add(new SetVariableBrick(value, getUserVariable(variable)));
		return this;
	}

	public SpriteWrapper changeVariable(String variable, double value) {
		return changeVariable(variable, new Formula(value));
	}

	public SpriteWrapper changeVariable(String variable, String value) {
		return changeVariable(variable, FormulaParser.parse(value));
	}

	public SpriteWrapper changeVariable(String variable, Formula value) {
		add(new ChangeVariableBrick(value, getUserVariable(variable)));
		return this;
	}

	private UserVariable getUserVariable(String variable) {
		return project.getDataContainer().getUserVariable(variable, sprite);
	}

	public SpriteWrapper addSpriteVariable(String variable) {
		ProjectManager.getInstance().setCurrentSprite(sprite);
		project.getDataContainer().addSpriteUserVariable(variable);
		return this;
	}

	public SpriteWrapper addProjectVariable(String variable) {
		project.getDataContainer().addProjectUserVariable(variable);
		return this;
	}

	public SpriteWrapper addItemToList(String list, int item) {
		return addItemToList(list, new Formula(item));
	}

	public SpriteWrapper addItemToList(String list, String item) {
		return addItemToList(list, FormulaParser.parse(item));
	}

	public SpriteWrapper addItemToList(String list, Formula item) {
		add(new AddItemToUserListBrick(item, getList(list)));
		return this;
	}

	public SpriteWrapper deleteItemFormList(String list, int position) {
		return deleteItemFormList(list, new Formula(position));
	}

	public SpriteWrapper deleteItemFormList(String list, String position) {
		return deleteItemFormList(list, FormulaParser.parse(position));
	}

	public SpriteWrapper deleteItemFormList(String list, Formula position) {
		add(new DeleteItemOfUserListBrick(position, getList(list)));
		return this;
	}

	public SpriteWrapper insertItemIntoList(String list, int position, int item) {
		return insertItemIntoList(list, new Formula(position), new Formula(item));
	}

	public SpriteWrapper insertItemIntoList(String list, String position, String item) {
		return insertItemIntoList(list, FormulaParser.parse(position), FormulaParser.parse(item));
	}

	public SpriteWrapper insertItemIntoList(String list, Formula position, Formula item) {
		add(new InsertItemIntoUserListBrick(position, item, getList(list)));
		return this;
	}

	public SpriteWrapper replaceItemInList(String list, int position, int item) {
		return replaceItemInList(list, new Formula(position), new Formula(item));
	}

	public SpriteWrapper replaceItemInList(String list, String position, String item) {
		return replaceItemInList(list, FormulaParser.parse(position), FormulaParser.parse(item));
	}

	public SpriteWrapper replaceItemInList(String list, Formula position, Formula item) {
		add(new ReplaceItemInUserListBrick(item, position, getList(list)));
		return this;
	}

	private UserList getList(String list) {
		ProjectManager.getInstance().setCurrentSprite(sprite);
		return project.getDataContainer().getUserList(list, sprite);
	}

	public SpriteWrapper addSpriteList(String list) {
		ProjectManager.getInstance().setCurrentSprite(sprite);
		project.getDataContainer().addSpriteUserList(list);
		return this;
	}

	public SpriteWrapper addProjectList(String list) {
		project.getDataContainer().addProjectUserList(list);
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
			File file = UtilFile.copyImageFromResourceIntoProject(project.getName(), name
					+ Constants.IMAGE_STANDARD_EXTENTION, id, context, true, 1.0);

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
			File file = UtilFile.copySoundFromResourceIntoProject(project.getName(), name
					+ SoundRecorder.RECORDING_EXTENSION, id, context, true);

			SoundInfo soundInfo = new SoundInfo();
			soundInfo.setTitle(name);
			soundInfo.setSoundFileName(file.getName());

			copiedSounds.put(id, soundInfo);
		} catch (IOException ioException) {
			Log.e(TAG, "Copying sound failed", ioException);
		}
	}

	private LoopBeginBrick getLoopBeginBrick() {
		List<Brick> bricks = currentScript.getBrickList();

		for (int index = bricks.size() - 1; index >= 0; index--) {
			Brick currentBrick = bricks.get(index);
			if (currentBrick instanceof LoopBeginBrick && ((LoopBeginBrick) currentBrick).getLoopEndBrick() == null) {
				return (LoopBeginBrick) currentBrick;
			}
		}

		Log.e(TAG, "Missing LoopBeginBrick");
		return null;
	}

	private IfLogicBeginBrick getIfLogicBeginBrick() {
		List<Brick> bricks = currentScript.getBrickList();

		for (int index = bricks.size() - 1; index >= 0; index--) {
			Brick currentBrick = bricks.get(index);
			if (currentBrick instanceof IfLogicBeginBrick && ((IfLogicBeginBrick) currentBrick).getIfElseBrick() == null) {
				return (IfLogicBeginBrick) currentBrick;
			}
		}

		Log.e(TAG, "Missing IfLogicBeginBrick");
		return null;
	}

	private IfLogicElseBrick getIfLogicElseBrick() {
		List<Brick> bricks = currentScript.getBrickList();

		for (int index = bricks.size() - 1; index >= 0; index--) {
			Brick currentBrick = bricks.get(index);
			if (currentBrick instanceof IfLogicElseBrick && ((IfLogicElseBrick) currentBrick).getIfEndBrick() == null) {
				return (IfLogicElseBrick) currentBrick;
			}
		}

		Log.e(TAG, "Missing IfLogicElseBrick");
		return null;
	}
}
