package org.catrobat.catroid.wrapper;

import android.content.Context;
import android.util.Log;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.common.BrickValues;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.AddItemToUserListBrick;
import org.catrobat.catroid.content.bricks.AskBrick;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.BroadcastBrick;
import org.catrobat.catroid.content.bricks.BroadcastWaitBrick;
import org.catrobat.catroid.content.bricks.CameraBrick;
import org.catrobat.catroid.content.bricks.ChangeBrightnessByNBrick;
import org.catrobat.catroid.content.bricks.ChangeSizeByNBrick;
import org.catrobat.catroid.content.bricks.ChangeTransparencyByNBrick;
import org.catrobat.catroid.content.bricks.ChangeVariableBrick;
import org.catrobat.catroid.content.bricks.ChangeVolumeByNBrick;
import org.catrobat.catroid.content.bricks.ChangeXByNBrick;
import org.catrobat.catroid.content.bricks.ChangeYByNBrick;
import org.catrobat.catroid.content.bricks.ChooseCameraBrick;
import org.catrobat.catroid.content.bricks.ClearGraphicEffectBrick;
import org.catrobat.catroid.content.bricks.ComeToFrontBrick;
import org.catrobat.catroid.content.bricks.DeleteItemOfUserListBrick;
import org.catrobat.catroid.content.bricks.FlashBrick;
import org.catrobat.catroid.content.bricks.ForeverBrick;
import org.catrobat.catroid.content.bricks.GlideToBrick;
import org.catrobat.catroid.content.bricks.GoNStepsBackBrick;
import org.catrobat.catroid.content.bricks.GoToBrick;
import org.catrobat.catroid.content.bricks.HideBrick;
import org.catrobat.catroid.content.bricks.HideTextBrick;
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
import org.catrobat.catroid.content.bricks.PlaySoundAndWaitBrick;
import org.catrobat.catroid.content.bricks.PlaySoundBrick;
import org.catrobat.catroid.content.bricks.PointInDirectionBrick;
import org.catrobat.catroid.content.bricks.PointToBrick;
import org.catrobat.catroid.content.bricks.PreviousLookBrick;
import org.catrobat.catroid.content.bricks.RepeatBrick;
import org.catrobat.catroid.content.bricks.ReplaceItemInUserListBrick;
import org.catrobat.catroid.content.bricks.SetBrightnessBrick;
import org.catrobat.catroid.content.bricks.SetColorBrick;
import org.catrobat.catroid.content.bricks.SetLookBrick;
import org.catrobat.catroid.content.bricks.SetLookByIndexBrick;
import org.catrobat.catroid.content.bricks.SetRotationStyleBrick;
import org.catrobat.catroid.content.bricks.SetSizeToBrick;
import org.catrobat.catroid.content.bricks.SetTransparencyBrick;
import org.catrobat.catroid.content.bricks.SetVariableBrick;
import org.catrobat.catroid.content.bricks.SetVolumeToBrick;
import org.catrobat.catroid.content.bricks.SetXBrick;
import org.catrobat.catroid.content.bricks.SetYBrick;
import org.catrobat.catroid.content.bricks.ShowBrick;
import org.catrobat.catroid.content.bricks.ShowTextBrick;
import org.catrobat.catroid.content.bricks.SpeakAndWaitBrick;
import org.catrobat.catroid.content.bricks.SpeakBrick;
import org.catrobat.catroid.content.bricks.StopAllSoundsBrick;
import org.catrobat.catroid.content.bricks.TurnLeftBrick;
import org.catrobat.catroid.content.bricks.TurnRightBrick;
import org.catrobat.catroid.content.bricks.VibrationBrick;
import org.catrobat.catroid.content.bricks.WaitBrick;
import org.catrobat.catroid.content.bricks.WaitUntilBrick;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.UserList;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.formulaeditor.datacontainer.DataContainer;
import org.catrobat.catroid.physics.PhysicsObject;
import org.catrobat.catroid.physics.content.bricks.SetBounceBrick;
import org.catrobat.catroid.physics.content.bricks.SetFrictionBrick;
import org.catrobat.catroid.physics.content.bricks.SetGravityBrick;
import org.catrobat.catroid.physics.content.bricks.SetMassBrick;
import org.catrobat.catroid.physics.content.bricks.SetPhysicsObjectTypeBrick;
import org.catrobat.catroid.physics.content.bricks.TurnLeftSpeedBrick;

import java.util.List;

public class ScriptWrapper {
	private static final String TAG = ScriptWrapper.class.getSimpleName();

	private final Context context;
	private final SpriteWrapper spriteWrapper;
	private final Script script;

	ScriptWrapper(Context context, SpriteWrapper spriteWrapper, Script script) {
		this.context = context;
		this.spriteWrapper = spriteWrapper;
		this.script = script;

		spriteWrapper.sprite.addScript(script);
	}

	public SpriteWrapper done() {
		return spriteWrapper;
	}

	private LoopBeginBrick getLoopBeginBrick() {
		List<Brick> bricks = script.getBrickList();

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
		List<Brick> bricks = script.getBrickList();

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
		List<Brick> bricks = script.getBrickList();

		for (int index = bricks.size() - 1; index >= 0; index--) {
			Brick currentBrick = bricks.get(index);
			if (currentBrick instanceof IfLogicElseBrick && ((IfLogicElseBrick) currentBrick).getIfEndBrick() == null) {
				return (IfLogicElseBrick) currentBrick;
			}
		}

		Log.e(TAG, "Missing IfLogicElseBrick");
		return null;
	}

	public ScriptWrapper wait(double seconds) {
		return wait(new Formula(seconds));
	}

	public ScriptWrapper wait(String seconds) {
		return wait(FormulaParser.parse(seconds));
	}

	public ScriptWrapper wait(Formula seconds) {
		script.addBrick(new WaitBrick(seconds));
		return this;
	}

	public ScriptWrapper broadcast(String message) {
		script.addBrick(new BroadcastBrick(message));
		return this;
	}

	public ScriptWrapper broadcastAndWait(String message) {
		script.addBrick(new BroadcastWaitBrick(message));
		return this;
	}

	public ScriptWrapper note(String note) {
		script.addBrick(new NoteBrick(note));
		return this;
	}

	public ScriptWrapper forever() {
		script.addBrick(new ForeverBrick());
		return this;
	}

	public ScriptWrapper endForever() {
		script.addBrick(new LoopEndlessBrick(getLoopBeginBrick()));
		return this;
	}

	public ScriptWrapper ifCondition(double condition) {
		return ifCondition(new Formula(condition));
	}

	public ScriptWrapper ifCondition(String condition) {
		return ifCondition(FormulaParser.parse(condition));
	}

	public ScriptWrapper ifCondition(Formula condition) {
		script.addBrick(new IfLogicBeginBrick(condition));
		return this;
	}

	public ScriptWrapper elseCondition() {
		script.addBrick(new IfLogicElseBrick(getIfLogicBeginBrick()));
		return this;
	}

	public ScriptWrapper endWithElseIfCondition() {
		elseCondition();
		return endIfCondition();
	}

	public ScriptWrapper endIfCondition() {
		IfLogicElseBrick ifLogicElseBrick = getIfLogicElseBrick();
		script.addBrick(new IfLogicEndBrick(ifLogicElseBrick, ifLogicElseBrick.getIfBeginBrick()));
		return this;
	}

	public ScriptWrapper waitUntil(String formula) {
		return waitUntil(FormulaParser.parse(formula));
	}

	public ScriptWrapper waitUntil(Formula formula) {
		script.addBrick(new WaitUntilBrick(formula));
		return this;
	}

	public ScriptWrapper repeat(double timesToRepeat) {
		return repeat(new Formula(timesToRepeat));
	}

	public ScriptWrapper repeat(String timesToRepeat) {
		return repeat(FormulaParser.parse(timesToRepeat));
	}

	public ScriptWrapper repeat(Formula timesToRepeat) {
		script.addBrick(new RepeatBrick(timesToRepeat));
		return this;
	}

	public ScriptWrapper endRepeat() {
		script.addBrick(new LoopEndBrick(getLoopBeginBrick()));
		return this;
	}

	public ScriptWrapper placeAt(double x, double y) {
		return placeAt(new Formula(x), new Formula(y));
	}

	public ScriptWrapper placeAt(String x, String y) {
		return placeAt(FormulaParser.parse(x), FormulaParser.parse(y));
	}

	public ScriptWrapper placeAt(Formula x, Formula y) {
		script.addBrick(new PlaceAtBrick(x, y));
		return this;
	}

	public ScriptWrapper setX(double x) {
		return setX(new Formula(x));
	}

	public ScriptWrapper setX(String x) {
		return setX(FormulaParser.parse(x));
	}

	public ScriptWrapper setX(Formula x) {
		script.addBrick(new SetXBrick(x));
		return this;
	}

	public ScriptWrapper setY(double y) {
		return setY(new Formula(y));
	}

	public ScriptWrapper setY(String y) {
		return setY(FormulaParser.parse(y));
	}

	public ScriptWrapper setY(Formula y) {
		script.addBrick(new SetYBrick(y));
		return this;
	}

	public ScriptWrapper changeXBy(double x) {
		return changeXBy(new Formula(x));
	}

	public ScriptWrapper changeXBy(String x) {
		return changeXBy(FormulaParser.parse(x));
	}

	public ScriptWrapper changeXBy(Formula x) {
		script.addBrick(new ChangeXByNBrick(x));
		return this;
	}

	public ScriptWrapper changeYBy(double y) {
		return changeYBy(new Formula(y));
	}

	public ScriptWrapper changeYBy(String y) {
		return changeYBy(FormulaParser.parse(y));
	}

	public ScriptWrapper changeYBy(Formula y) {
		script.addBrick(new ChangeYByNBrick(y));
		return this;
	}

	public ScriptWrapper goTo(Sprite sprite) {
		GoToBrick brick = new GoToBrick();
		brick.setDestinationSprite(sprite);
		brick.setSpinnerSelection(BrickValues.GO_TO_OTHER_SPRITE_POSITION);
		script.addBrick(brick);
		return this;
	}

	public ScriptWrapper goTo(int i) {
		GoToBrick brick = new GoToBrick();
		brick.setSpinnerSelection(i);
		script.addBrick(brick);
		return this;
	}

	public ScriptWrapper ifOnEdgeBounce() {
		script.addBrick(new IfOnEdgeBounceBrick());
		return this;
	}

	public ScriptWrapper moveNSteps(double steps) {
		return moveNSteps(new Formula(steps));
	}

	public ScriptWrapper moveNSteps(String steps) {
		return moveNSteps(FormulaParser.parse(steps));
	}

	public ScriptWrapper moveNSteps(Formula steps) {
		script.addBrick(new MoveNStepsBrick(steps));
		return this;
	}

	public ScriptWrapper turnLeft(double degrees) {
		return turnLeft(new Formula(degrees));
	}

	public ScriptWrapper turnLeft(String degrees) {
		return turnLeft(FormulaParser.parse(degrees));
	}

	public ScriptWrapper turnLeft(Formula degrees) {
		script.addBrick(new TurnLeftBrick(degrees));
		return this;
	}

	public ScriptWrapper turnRight(double degrees) {
		return turnRight(new Formula(degrees));
	}

	public ScriptWrapper turnRight(String degrees) {
		return turnRight(FormulaParser.parse(degrees));
	}

	public ScriptWrapper turnRight(Formula degrees) {
		script.addBrick(new TurnRightBrick(degrees));
		return this;
	}

	public ScriptWrapper pointInDirection(double direction) {
		return pointInDirection(new Formula(direction));
	}

	public ScriptWrapper pointInDirection(String direction) {
		return pointInDirection(FormulaParser.parse(direction));
	}

	public ScriptWrapper pointInDirection(Formula direction) {
		script.addBrick(new PointInDirectionBrick(direction));
		return this;
	}

	public ScriptWrapper pointTowards(Sprite pointedSprite) {
		script.addBrick(new PointToBrick(pointedSprite));
		return this;
	}

	public ScriptWrapper pointTowards(SpriteWrapper pointedSprite) {
		script.addBrick(new PointToBrick(pointedSprite.sprite));
		return this;
	}

	public ScriptWrapper setRotationStyle(int style) {
		SetRotationStyleBrick brick = new SetRotationStyleBrick();
		brick.setSelection(style);
		script.addBrick(brick);
		return this;
	}

	public ScriptWrapper glideTo(double x, double y, double seconds) {
		return glideTo(new Formula(x), new Formula(y), new Formula(seconds));
	}

	public ScriptWrapper glideTo(String x, String y, String seconds) {
		return glideTo(FormulaParser.parse(x), FormulaParser.parse(y), FormulaParser.parse(seconds));
	}

	public ScriptWrapper glideTo(Formula x, Formula y, Formula seconds) {
		script.addBrick(new GlideToBrick(x, y, seconds));
		return this;
	}

	public ScriptWrapper vibrate(double seconds) {
		return vibrate(new Formula(seconds));
	}

	public ScriptWrapper vibrate(String seconds) {
		return vibrate(FormulaParser.parse(seconds));
	}

	public ScriptWrapper vibrate(Formula seconds) {
		script.addBrick(new VibrationBrick(seconds));
		return this;
	}

	public ScriptWrapper setMotionType(PhysicsObject.Type type) {
		script.addBrick(new SetPhysicsObjectTypeBrick(type));
		return this;
	}

	public ScriptWrapper turnLeftSpeed(double degreesPerSecond) {
		return turnLeftSpeed(new Formula(degreesPerSecond));
	}

	public ScriptWrapper turnLeftSpeed(String degreesPerSecond) {
		return turnLeftSpeed(FormulaParser.parse(degreesPerSecond));
	}

	public ScriptWrapper turnLeftSpeed(Formula degreesPerSecond) {
		script.addBrick(new TurnLeftSpeedBrick(degreesPerSecond));
		return this;
	}

	public ScriptWrapper turnRightSpeed(double degreesPerSecond) {
		return turnRightSpeed(new Formula(degreesPerSecond));
	}

	public ScriptWrapper turnRightSpeed(String degreesPerSecond) {
		return turnRightSpeed(FormulaParser.parse(degreesPerSecond));
	}

	public ScriptWrapper turnRightSpeed(Formula degreesPerSecond) {
		script.addBrick(new TurnRightBrick(degreesPerSecond));
		return this;
	}

	public ScriptWrapper setGravity(double x, double y) {
		return setGravity(new Formula(x), new Formula(y));
	}

	public ScriptWrapper setGravity(String x, String y) {
		return setGravity(FormulaParser.parse(x), FormulaParser.parse(y));
	}

	public ScriptWrapper setGravity(Formula x, Formula y) {
		script.addBrick(new SetGravityBrick(x, y));
		return this;
	}

	public ScriptWrapper setMass(double mass) {
		return setMass(new Formula(mass));
	}

	public ScriptWrapper setMass(String mass) {
		return setMass(FormulaParser.parse(mass));
	}

	public ScriptWrapper setMass(Formula mass) {
		script.addBrick(new SetMassBrick(mass));
		return this;
	}

	public ScriptWrapper setBounceFactor(double bounceFactor) {
		return setBounceFactor(new Formula(bounceFactor));
	}

	public ScriptWrapper setBounceFactor(String bounceFactor) {
		return setBounceFactor(FormulaParser.parse(bounceFactor));
	}

	public ScriptWrapper setBounceFactor(Formula bounceFactor) {
		script.addBrick(new SetBounceBrick(bounceFactor));
		return this;
	}

	public ScriptWrapper setFriction(double friction) {
		return setFriction(new Formula(friction));
	}

	public ScriptWrapper setFriction(String friction) {
		return setFriction(FormulaParser.parse(friction));
	}

	public ScriptWrapper setFriction(Formula friction) {
		script.addBrick(new SetFrictionBrick(friction));
		return this;
	}

	public ScriptWrapper goBack(double layers) {
		return goBack(new Formula(layers));
	}

	public ScriptWrapper goBack(String layers) {
		return goBack(FormulaParser.parse(layers));
	}

	public ScriptWrapper goBack(Formula layers) {
		script.addBrick(new GoNStepsBackBrick(layers));
		return this;
	}

	public ScriptWrapper goToFront() {
		script.addBrick(new ComeToFrontBrick());
		return this;
	}

	public ScriptWrapper startSound(int id) {
		PlaySoundBrick playSoundBrick = new PlaySoundBrick();
		playSoundBrick.setSoundInfo(spriteWrapper.getSoundInfo(id));
		script.addBrick(playSoundBrick);
		return this;
	}

	public ScriptWrapper startSoundAndWait(int id) {
		PlaySoundAndWaitBrick playSoundAndWaitBrick = new PlaySoundAndWaitBrick();
		playSoundAndWaitBrick.setSoundInfo(spriteWrapper.getSoundInfo(id));
		script.addBrick(playSoundAndWaitBrick);
		return this;
	}

	public ScriptWrapper stopAllSounds() {
		script.addBrick(new StopAllSoundsBrick());
		return this;
	}

	public ScriptWrapper setVolume(double volume) {
		return setVolume(new Formula(volume));
	}

	public ScriptWrapper setVolume(String volume) {
		return setVolume(FormulaParser.parse(volume));
	}

	public ScriptWrapper setVolume(Formula volume) {
		script.addBrick(new SetVolumeToBrick(volume));
		return this;
	}

	public ScriptWrapper changeVolumeBy(double volume) {
		return changeVolumeBy(new Formula(volume));
	}

	public ScriptWrapper changeVolumeBy(String volume) {
		return changeVolumeBy(FormulaParser.parse(volume));
	}

	public ScriptWrapper changeVolumeBy(Formula volume) {
		script.addBrick(new ChangeVolumeByNBrick(volume));
		return this;
	}

	public ScriptWrapper speak(String text) {
		script.addBrick(new SpeakBrick(text));
		return this;
	}

	public ScriptWrapper speakAndWait(String text) {
		script.addBrick(new SpeakAndWaitBrick(text));
		return this;
	}

	public ScriptWrapper setLook(int id) {
		return switchToLook(id);
	}

	public ScriptWrapper switchToLook(int id) {
		SetLookBrick setLookBrick = new SetLookBrick();
		setLookBrick.setLook(spriteWrapper.getLookData(id));
		script.addBrick(setLookBrick);
		return this;
	}

//	TODO:
//	public ScriptWrapper setBackground(int id) {
//		return this;
//	}
//
//	public ScriptWrapper setBackgroundAndWait(int id) {
//		return this;
//	}

	public ScriptWrapper switchToLookWithNumber(double index) {
		return switchToLookWithNumber(new Formula(index));
	}

	public ScriptWrapper switchToLookWithNumber(String index) {
		return switchToLookWithNumber(FormulaParser.parse(index));
	}

	public ScriptWrapper switchToLookWithNumber(Formula index) {
		script.addBrick(new SetLookByIndexBrick(index));
		return this;
	}

	public ScriptWrapper nextLook() {
		script.addBrick(new NextLookBrick());
		return this;
	}

	public ScriptWrapper previousLook() {
		script.addBrick(new PreviousLookBrick());
		return this;
	}

	public ScriptWrapper setSize(double size) {
		return setSize(new Formula(size));
	}

	public ScriptWrapper setSize(String size) {
		return setSize(FormulaParser.parse(size));
	}

	public ScriptWrapper setSize(Formula size) {
		script.addBrick(new SetSizeToBrick(size));
		return this;
	}

	public ScriptWrapper changeSizeBy(double size) {
		return changeSizeBy(new Formula(size));
	}

	public ScriptWrapper changeSizeBy(String size) {
		return changeSizeBy(FormulaParser.parse(size));
	}

	public ScriptWrapper changeSizeBy(Formula size) {
		script.addBrick(new ChangeSizeByNBrick(size));
		return this;
	}

	public ScriptWrapper hide() {
		script.addBrick(new HideBrick());
		return this;
	}

	public ScriptWrapper show() {
		script.addBrick(new ShowBrick());
		return this;
	}

	public ScriptWrapper ask(String question, String answerVariable) {
		script.addBrick(new AskBrick(new Formula(question), getUserVariable(answerVariable)));
		return this;
	}

	public ScriptWrapper setTransparency(double transparency) {
		return setTransparency(new Formula(transparency));
	}

	public ScriptWrapper setTransparency(String transparency) {
		return setTransparency(FormulaParser.parse(transparency));
	}

	public ScriptWrapper setTransparency(Formula transparency) {
		script.addBrick(new SetTransparencyBrick(transparency));
		return this;
	}

	public ScriptWrapper changeTransparencyBy(double transparency) {
		return changeTransparencyBy(new Formula(transparency));
	}

	public ScriptWrapper changeTransparencyBy(String transparency) {
		return changeTransparencyBy(FormulaParser.parse(transparency));
	}

	public ScriptWrapper changeTransparencyBy(Formula transparency) {
		script.addBrick(new ChangeTransparencyByNBrick(transparency));
		return this;
	}

	public ScriptWrapper setBrightness(double brightness) {
		return setBrightness(new Formula(brightness));
	}

	public ScriptWrapper setBrightness(String brightness) {
		return setBrightness(FormulaParser.parse(brightness));
	}

	public ScriptWrapper setBrightness(Formula brightness) {
		script.addBrick(new SetBrightnessBrick(brightness));
		return this;
	}

	public ScriptWrapper changeBrightnessBy(double brightness) {
		return changeBrightnessBy(new Formula(brightness));
	}

	public ScriptWrapper changeBrightnessBy(String brightness) {
		return changeBrightnessBy(FormulaParser.parse(brightness));
	}

	public ScriptWrapper changeBrightnessBy(Formula brightness) {
		script.addBrick(new ChangeBrightnessByNBrick(brightness));
		return this;
	}

	public ScriptWrapper setColor(double color) {
		return setColor(new Formula(color));
	}

	public ScriptWrapper setColor(String color) {
		return setColor(FormulaParser.parse(color));
	}

	public ScriptWrapper setColor(Formula color) {
		script.addBrick(new SetColorBrick(color));
		return this;
	}

	public ScriptWrapper changeColorBy(double color) {
		return setColor(new Formula(color));
	}

	public ScriptWrapper changeColorBy(String color) {
		return setColor(FormulaParser.parse(color));
	}

	public ScriptWrapper changeColorBy(Formula color) {
		script.addBrick(new SetColorBrick(color));
		return this;
	}

	public ScriptWrapper clearGraphicEffects() {
		script.addBrick(new ClearGraphicEffectBrick());
		return this;
	}

	public ScriptWrapper turnCamera(boolean on) {
		script.addBrick(new CameraBrick((on) ? 1 : 0));
		return this;
	}

	public ScriptWrapper useCamera(boolean back) {
		script.addBrick(new ChooseCameraBrick((back) ? 0 : 1));
		return this;
	}

	public ScriptWrapper turnFlash(boolean on) {
		script.addBrick(new FlashBrick((on) ? 1 : 0));
		return this;
	}

	public ScriptWrapper setVariable(String variable, double value) {
		return setVariable(variable, new Formula(value));
	}

	public ScriptWrapper setVariable(String variable, String value) {
		return setVariable(variable, FormulaParser.parse(value));
	}

	public ScriptWrapper setVariable(String variable, Formula value) {
		script.addBrick(new SetVariableBrick(value, getUserVariable(variable)));
		return this;
	}

	public ScriptWrapper changeVariable(String variable, double value) {
		return changeVariable(variable, new Formula(value));
	}

	public ScriptWrapper changeVariable(String variable, String value) {
		return changeVariable(variable, FormulaParser.parse(value));
	}

	public ScriptWrapper changeVariable(String variable, Formula value) {
		script.addBrick(new ChangeVariableBrick(value, getUserVariable(variable)));
		return this;
	}

	private DataContainer getDataContainer() {
		return spriteWrapper.sceneWrapper.scene.getDataContainer();
	}

	private UserVariable getUserVariable(String variable) {
		return getDataContainer().getUserVariable(spriteWrapper.sprite, variable);
	}

	private UserList getList(String list) {
		ProjectManager.getInstance().setCurrentSprite(spriteWrapper.sprite);
		return getDataContainer().getUserList(spriteWrapper.sprite, list);
	}

	public ScriptWrapper showVariable(String variable, double x, double y) {
		return showVariable(variable, new Formula(x), new Formula(y));
	}

	public ScriptWrapper showVariable(String variable, String x, String y) {
		return showVariable(variable, FormulaParser.parse(x), FormulaParser.parse(y));
	}

	public ScriptWrapper showVariable(String variable, Formula x, Formula y) {
		ShowTextBrick brick = new ShowTextBrick(x, y);
		brick.setUserVariable(getUserVariable(variable));
		script.addBrick(brick);
		return this;
	}

	public ScriptWrapper hideVariable(String variable) {
		HideTextBrick brick = new HideTextBrick();
		brick.setUserVariable(getUserVariable(variable));
		script.addBrick(brick);
		return this;
	}

	public ScriptWrapper addItemToList(String list, int item) {
		return addItemToList(list, new Formula(item));
	}

	public ScriptWrapper addItemToList(String list, String item)  {
		return addItemToList(list, FormulaParser.parse(item));
	}

	public ScriptWrapper addItemToList(String list, Formula item) {
		script.addBrick(new AddItemToUserListBrick(item, getList(list)));
		return this;
	}

	public ScriptWrapper deleteItemFormList(String list, int position) {
		return deleteItemFormList(list, new Formula(position));
	}

	public ScriptWrapper deleteItemFormList(String list, String position) {
		return deleteItemFormList(list, FormulaParser.parse(position));
	}

	public ScriptWrapper deleteItemFormList(String list, Formula position) {
		script.addBrick(new DeleteItemOfUserListBrick(position, getList(list)));
		return this;
	}

	public ScriptWrapper insertItemIntoList(String list, int position, int item) {
		return insertItemIntoList(list, new Formula(position), new Formula(item));
	}

	public ScriptWrapper insertItemIntoList(String list, String position, String item) {
		return insertItemIntoList(list, FormulaParser.parse(position), FormulaParser.parse(item));
	}

	public ScriptWrapper insertItemIntoList(String list, Formula position, Formula item) {
		script.addBrick(new InsertItemIntoUserListBrick(position, item, getList(list)));
		return this;
	}

	public ScriptWrapper replaceItemInList(String list, int position, int item) {
		return replaceItemInList(list, new Formula(position), new Formula(item));
	}

	public ScriptWrapper replaceItemInList(String list, String position, String item) {
		return replaceItemInList(list, FormulaParser.parse(position), FormulaParser.parse(item));
	}

	public ScriptWrapper replaceItemInList(String list, Formula position, Formula item) {
		script.addBrick(new ReplaceItemInUserListBrick(item, position, getList(list)));
		return this;
	}
}
