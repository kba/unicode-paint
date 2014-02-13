package kba.unicodeart.gui.action;

import java.util.HashMap;
import java.util.Map;

import kba.unicodeart.CharacterBoxBrush;
import kba.unicodeart.CompassDir;
import kba.unicodeart.gui.TMEditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.googlecode.lanterna.terminal.TerminalPosition;

/**
 * Draw action for directional (box) drawing.
 * 
 * @author kba
 *
 */
public class DirectedDrawAction extends DrawAction {

	private static final Logger log = LoggerFactory.getLogger(DirectedDrawAction.class);

	private CharacterBoxBrush brush = CharacterBoxBrush.BOX_LIGHT;
	private CompassDir selectedDir;
	CompassDir moveDir;
	private static Map<CompassDir,Map<CompassDir,CompassDir>> transitions = new HashMap<>();

	/*
	 * Sets up the transitions that define what character is drawn depending on
	 * the previous drawing direction
	 */
	static {
		for (CompassDir curDir : CompassDir.values()) {
			transitions.put(curDir, new HashMap<CompassDir,CompassDir>());
		}
		transitions.get(CompassDir.NORTH).put(CompassDir.NORTH, CompassDir.EAST);
		transitions.get(CompassDir.NORTH).put(CompassDir.EAST, CompassDir.NORTHWEST);
		transitions.get(CompassDir.NORTH).put(CompassDir.SOUTH, CompassDir.EAST);
		transitions.get(CompassDir.NORTH).put(CompassDir.WEST, CompassDir.NORTHEAST);
		transitions.get(CompassDir.EAST).put(CompassDir.NORTH, CompassDir.SOUTHEAST);
		transitions.get(CompassDir.EAST).put(CompassDir.EAST, CompassDir.SOUTH);
		transitions.get(CompassDir.EAST).put(CompassDir.SOUTH, CompassDir.NORTHEAST);
		transitions.get(CompassDir.EAST).put(CompassDir.WEST, CompassDir.SOUTH);
		transitions.get(CompassDir.SOUTH).put(CompassDir.NORTH, CompassDir.EAST);
		transitions.get(CompassDir.SOUTH).put(CompassDir.EAST, CompassDir.SOUTHWEST);
		transitions.get(CompassDir.SOUTH).put(CompassDir.SOUTH, CompassDir.EAST);
		transitions.get(CompassDir.SOUTH).put(CompassDir.WEST, CompassDir.SOUTHEAST);
		transitions.get(CompassDir.WEST).put(CompassDir.NORTH, CompassDir.SOUTHWEST);
		transitions.get(CompassDir.WEST).put(CompassDir.EAST, CompassDir.SOUTH);
		transitions.get(CompassDir.WEST).put(CompassDir.SOUTH, CompassDir.NORTHWEST);
		transitions.get(CompassDir.WEST).put(CompassDir.WEST, CompassDir.SOUTH);

	}

	public void execute(TMEditor applicationState) {
		super.execute(applicationState);
		Preconditions.checkNotNull(getSelectedDir());
		this.moveDir = getSelectedDir();
		CompassDir lastMoveDir = getLastMoveDir(applicationState);
		if (null == lastMoveDir) {
			lastMoveDir = getSelectedDir();
		}
		boolean fail = false;
		switch(getSelectedDir()) {
		case NORTHEAST: case NORTHWEST: case SOUTHEAST: case SOUTHWEST:
			log.error("Not yet(?) supported.");
			fail = true;
			break;
		default:
			CompassDir drawDir = transitions.get(lastMoveDir).get(getSelectedDir());
			getDrawChar().setCharacter(getBrush().get(drawDir));
			if (isTransparent())
				getDrawChar().setTransparent();
			break;
		}
		if (! fail) {
			// TODO
			//				log.debug("Old Char: " + this.getOldChar());
			log.debug("Draw Char: " + this.getDrawChar());
			getLayer().set(this.getOldPos().getColumn(), this.getOldPos().getRow(), this.getDrawChar());
			this.setNewPos(new TerminalPosition(
					Math.min(Math.max(getOldPos().getColumn() + moveDir.getDeltaX(), 0), getLayer().getWidth() - 1),
					Math.min(Math.max(getOldPos().getRow() + moveDir.getDeltaY(), 0), getLayer().getHeight() - 1)));
			applicationState.getDrawHistory().addFirst(this);
		}
	}

	public CharacterBoxBrush getBrush() {
		return brush;
	}

	public void setBrush(CharacterBoxBrush brush) {
		this.brush = brush;
	}

	public CompassDir getSelectedDir() {
		return selectedDir;
	}

	public void setSelectedDir(CompassDir selectedDir) {
		this.selectedDir = selectedDir;
	}
}