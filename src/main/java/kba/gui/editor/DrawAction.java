package kba.gui.editor;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.terminal.TerminalPosition;

abstract class DrawAction {
	public TerminalPosition oldPos;
	public char oldChar;
	public TerminalPosition newPos;
	public TMEditFormatLayer layer;
	protected Logger log = LoggerFactory.getLogger(getClass().getName());
	public void execute(LinkedList<DrawAction> history) {
		this.oldChar = layer.get(oldPos.getColumn(), oldPos.getRow());
	}
	public void undo() {
		log.debug("Undoing, setting {} back to {}", newPos, oldChar);
		layer.set(oldPos.getColumn(), oldPos.getRow(), oldChar);
	}
}