package kba.unicodeart.gui.action;

import java.util.LinkedList;

import kba.unicodeart.CompassDir;
import kba.unicodeart.format.TMColoredCharacter;
import kba.unicodeart.format.LanternaAdapter;

import com.googlecode.lanterna.terminal.TerminalPosition;

public class CharacterDrawAction extends DrawAction {
	
	@Override
	public void execute(LinkedList<DrawAction> history) {
		super.execute(history);
		TMColoredCharacter layerChar = getLayer().get(this.getOldPos().getColumn(), this.getOldPos().getRow());
		layerChar.setCharacter(getDrawChar());
		layerChar.setFg(LanternaAdapter.toAwtColor(getNewFg()));
		layerChar.setBg(LanternaAdapter.toAwtColor(getNewBg()));
		this.setNewPos(getOldPos());
//		CompassDir moveDir = getLastMoveDir(history);
//		if (null != moveDir)
//			this.setNewPos(new TerminalPosition(
//					Math.min(Math.max(getOldPos().getColumn() + moveDir.getDeltaX(), 0), getLayer().getWidth() - 1),
//					Math.min(Math.max(getOldPos().getRow() + moveDir.getDeltaY(), 0), getLayer().getHeight() - 1)));
		history.addFirst(this);
	}

}
