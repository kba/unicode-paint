package kba.unicodeart.gui.action;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import kba.unicodeart.CharacterBoxBrush;
import kba.unicodeart.CompassDir;
import kba.unicodeart.format.LanternaAdapter;

import com.googlecode.lanterna.terminal.TerminalPosition;

public class DirectedDrawAction extends DrawAction {
		
		private CharacterBoxBrush brush = CharacterBoxBrush.BOX_LIGHT;
		private CompassDir selectedDir;
		private CompassDir drawDir;
		CompassDir moveDir;
		
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
		
		private Map<CompassDir,Map<CompassDir,CompassDir>> transitions = new HashMap<>();
		
		public void execute(LinkedList<DrawAction> history) {
			super.execute(history);
			this.moveDir = getSelectedDir();
			CompassDir lastMoveDir = getLastMoveDir(history);
			if (null == lastMoveDir) {
				lastMoveDir = getSelectedDir();
			}
			boolean fail = false;
			switch(getSelectedDir()) {
			case NORTHEAST: case NORTHWEST: case SOUTHEAST: case SOUTHWEST:
				fail = true;
				break;
			default:
				drawDir = transitions.get(lastMoveDir).get(getSelectedDir());
			}
//			log.debug("Draw Direction" + drawDir);
			if (! fail) {
				getLayer().get(this.getOldPos().getColumn(), this.getOldPos().getRow()).setFg(LanternaAdapter.toAwtColor(getNewFg()));
				getLayer().get(this.getOldPos().getColumn(), this.getOldPos().getRow()).setBg(LanternaAdapter.toAwtColor(getNewBg()));
				getLayer().get(this.getOldPos().getColumn(), this.getOldPos().getRow()).setCharacter(getBrush().get(drawDir));
				this.setNewPos(new TerminalPosition(
						Math.min(Math.max(getOldPos().getColumn() + moveDir.getDeltaX(), 0), getLayer().getWidth() - 1),
						Math.min(Math.max(getOldPos().getRow() + moveDir.getDeltaY(), 0), getLayer().getHeight() - 1)));
				history.addFirst(this);
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