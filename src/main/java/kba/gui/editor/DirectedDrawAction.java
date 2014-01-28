package kba.gui.editor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;

import com.googlecode.lanterna.terminal.TerminalPosition;

class DirectedDrawAction extends DrawAction {
		
		public CharacterBoxBrush brush = CharacterBoxBrush.BOX_LIGHT;
		public CompassDir selectedDir;
		public CompassDir drawDir;
		public CompassDir moveDir;
		
//		public DrawAction(TerminalPosition pos, CompassDir dir, char ch) {
//			this.ch = ch;
//			this.drawDir = dir;
//			this.curPos = pos;
//		}
		public DirectedDrawAction() {
			super();
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
		
		private CompassDir getLastMoveDir(LinkedList<DrawAction> history) {
			CompassDir retDir = null;
			try {
				DrawAction lastAct = history.getFirst();
				if (lastAct instanceof DirectedDrawAction) {
					retDir = ((DirectedDrawAction) lastAct).moveDir;
				}
			} catch (NoSuchElementException e) { }
			return retDir;
		}
		
		private Map<CompassDir,Map<CompassDir,CompassDir>> transitions = new HashMap<>();
		
		public void execute(LinkedList<DrawAction> history) {
			super.execute(history);
			this.moveDir = selectedDir;
			CompassDir lastMoveDir = getLastMoveDir(history);
			if (null == lastMoveDir) {
				lastMoveDir = selectedDir;
			}
			boolean fail = false;
			switch(selectedDir) {
			case NORTHEAST: case NORTHWEST: case SOUTHEAST: case SOUTHWEST:
				fail = true;
				break;
			default:
				drawDir = transitions.get(lastMoveDir).get(selectedDir);
			}
			log.debug("Draw Direction" + drawDir);
			if (fail) {
				return;
			}
			layer.set(this.oldPos, brush.get(drawDir));
			this.newPos = new TerminalPosition(
					Math.min(Math.max(oldPos.getColumn() + moveDir.getDeltaX(), 0), layer.getWidth() - 1),
					Math.min(Math.max(oldPos.getRow() + moveDir.getDeltaY(), 0), layer.getHeight() - 1));
			history.addFirst(this);
		}
	}