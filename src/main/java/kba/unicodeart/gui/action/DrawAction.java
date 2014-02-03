package kba.unicodeart.gui.action;

import java.util.LinkedList;

import kba.unicodeart.CompassDir;
import kba.unicodeart.format.TMColoredCharacter;
import kba.unicodeart.format.LanternaAdapter;
import kba.unicodeart.format.TMEditFormatLayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.terminal.TerminalPosition;
import com.googlecode.lanterna.terminal.TextColor;

abstract public class DrawAction {
	private char oldChar;
	public char getOldChar() { return oldChar; }

	private char drawChar;
	public char getDrawChar() { return drawChar; }
	public void setDrawChar(char drawChar) { this.drawChar = drawChar; }

	private TerminalPosition oldPos;
	public TerminalPosition getOldPos() { return oldPos; }
	public void setOldPos(TerminalPosition oldPos) { this.oldPos = oldPos; }

	private TerminalPosition newPos;
	public TerminalPosition getNewPos() { return newPos; }
	public void setNewPos(TerminalPosition newPos) { this.newPos = newPos; }

	private TMEditFormatLayer layer;
	public TMEditFormatLayer getLayer() { return layer; }
	public void setLayer(TMEditFormatLayer layer) { this.layer = layer; }

	private TextColor oldFg;
	public TextColor getOldFg() { return oldFg; }
	public void setOldFg(TextColor oldFg) { this.oldFg = oldFg; }

	private TextColor oldBg;
	public TextColor getOldBg() { return oldBg; }
	public void setOldBg(TextColor oldBg) { this.oldBg = oldBg; }

	private TextColor newFg;
	public TextColor getNewFg() { return newFg; }
	public void setNewFg(TextColor newFg) { this.newFg = newFg; }

	private TextColor newBg;
	public TextColor getNewBg() { return newBg; }
	public void setNewBg(TextColor newBg) { this.newBg = newBg; }

	protected Logger log = LoggerFactory.getLogger(getClass().getName());

	public void execute(LinkedList<DrawAction> history) {
		this.oldChar = getLayer().get(getOldPos().getColumn(), getOldPos().getRow()).getCharacter();
		TMColoredCharacter coloredCharacter = getLayer().get(getOldPos().getColumn(), getOldPos().getRow());
		setOldFg(LanternaAdapter.toTextColor(coloredCharacter.getFg()));
		setOldBg(LanternaAdapter.toTextColor(coloredCharacter.getBg()));
		this.newPos = oldPos;
	}
	public void undo() {
		log.debug("Undoing, setting {} back to {}", getNewPos(), oldChar);
		getLayer().get(getOldPos().getColumn(), getOldPos().getRow()).setCharacter(oldChar);
		getLayer().get(getOldPos().getColumn(), getOldPos().getRow()).setFg(LanternaAdapter.toAwtColor(getOldFg()));
		getLayer().get(getOldPos().getColumn(), getOldPos().getRow()).setBg(LanternaAdapter.toAwtColor(getOldBg()));
	}
	protected CompassDir getLastMoveDir(LinkedList<DrawAction> history) {
		CompassDir retDir = null;
//		for (int i = history.size() - 1; i >= 0;  i--) {
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
}