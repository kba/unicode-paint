package kba.unicodeart.gui.action;

import java.util.LinkedList;

import kba.unicodeart.CompassDir;
import kba.unicodeart.format.TMEditFormatLayer;
import kba.unicodeart.format.colored_char.TMColoredCharacter;
import kba.unicodeart.gui.TMEditor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.googlecode.lanterna.terminal.TerminalPosition;

/**
 * Abstract base class of all draw actions
 * @author kba
 *
 */
abstract public class DrawAction {
	
	private static final Logger log = LoggerFactory.getLogger(DrawAction.class);

	private TMColoredCharacter drawChar;
	private TerminalPosition oldPos;
	private TerminalPosition newPos;
	private TMEditFormatLayer layer;
	private TMColoredCharacter oldChar;
	private boolean transparent = false;

	/**
	 * @return the colored character to draw
	 */
	public TMColoredCharacter getDrawChar() {
		return drawChar;
	}

	/**
	 * @param drawChar the colored character to draw
	 */
	public void setDrawChar(TMColoredCharacter drawChar) {
		this.drawChar = drawChar;
	}

	/**
	 * @return the position where to draw
	 */
	public TerminalPosition getOldPos() {
		return oldPos;
	}

	/**
	 * @param oldPos the position where to draw
	 */
	public void setOldPos(TerminalPosition oldPos) {
		this.oldPos = oldPos;
	}

	/**
	 * @return the new cursor position
	 */
	public TerminalPosition getNewPos() {
		return newPos;
	}

	/**
	 * @param newPos the new cursor position
	 */
	public void setNewPos(TerminalPosition newPos) {
		this.newPos = newPos;
	}

	/**
	 * @return the layer to draw to
	 */
	public TMEditFormatLayer getLayer() {
		return layer;
	}

	/**
	 * @param layer the layer to draw to
	 */
	public void setLayer(TMEditFormatLayer layer) {
		this.layer = layer;
	}

	/**
	 * Executes this action
	 * 
	 * @param applicationState the applicationState (used for its' color character factory)
	 * @throws IllegalArgumentException if either {@link #getLayer()} or {@link #getOldPos()} is not set.
	 */
	public void execute(TMEditor applicationState) {
		Preconditions.checkNotNull(getLayer());
		Preconditions.checkNotNull(getOldPos());
		Preconditions.checkNotNull(isTransparent());
		this.setOldChar(getLayer().get(getOldPos().getColumn(), getOldPos().getRow()));
		this.newPos = oldPos;
	}

	/**
	 * Undoes this action.
	 */
	public void undo() {
		log.debug("Undoing, setting {} back to {}", getNewPos(), getOldChar());
		getLayer().set(getOldPos().getColumn(), getOldPos().getRow(), getOldChar());
	}

	/**
	 * Searches the last direction an action in the history has moved towards.
	 * 
	 * @param applicationState the {@link TMEditor} that contains the history to search
	 * @return the last direction an action drawed towards
	 */
	protected CompassDir getLastMoveDir(TMEditor applicationState) {
		CompassDir retDir = null;
		LinkedList<DrawAction> history = applicationState.getDrawHistory();
		// for (int i = history.size() - 1; i >= 0; i--) {
		for (int i = 0; i < history.size(); i++) {
			DrawAction lastAct;
			lastAct = history.get(i);
			if (lastAct instanceof DirectedDrawAction) {
				retDir = DirectedDrawAction.class.cast(lastAct).moveDir;
				break;
			}
		}
		return retDir;
	}
	
	/**
	 * @return whether the background should be transparent
	 */
	public boolean isTransparent() {
		return transparent;
	}

	/**
	 * @param transparent whether the background should be transparent
	 */
	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}

	/**
	 * @return the colored character at the position to draw to
	 */
	public TMColoredCharacter getOldChar() {
		return oldChar;
	}

	/**
	 * @param oldChar the colored character at the position to draw to
	 */
	public void setOldChar(TMColoredCharacter oldChar) {
		this.oldChar = oldChar;
	}
}