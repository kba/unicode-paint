package kba.gui.editor;

import java.util.NoSuchElementException;

import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.TextGraphics;
import com.googlecode.lanterna.gui.component.AbstractInteractableComponent;
import com.googlecode.lanterna.gui.dialog.MessageBox;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.TerminalPosition;
import com.googlecode.lanterna.terminal.TerminalSize;

public class TMMapEditorComponent extends AbstractInteractableComponent {
	
	private static final int MAX_WIDTH = 80;
	private static final int MAX_HEIGHT = 40;
	private TMEditorWindow editorWindow;
	private TerminalPosition cursorPosition = new TerminalPosition(3, 3);
	
	public TMMapEditorComponent(TMEditorWindow tmEditorWindow) {
		this.editorWindow = tmEditorWindow;
//		this.modeLabel = modeLabel;
//		modeLabel.setText(mode.toString());
//		this.layerBoxList = layerBoxList;
//		layerBoxList.setCheckedItemIndex(currentLayerType.ordinal());
//		this.characterBoxBrushList = characterBoxBrushList;
//		characterBoxBrushList.setCheckedItemIndex(0);

//		this.currentMap.getLayer(LayerType.ASCII_CHAR).set(3, 3, '#');
		// TODO
//		this.setHotspot(new TerminalPosition(3, 3));
	}

	
	@Override public void repaint(TextGraphics graphics) {
        graphics.fillArea('x');
        TMEditFormatLayer layer = editorWindow.getCurrentMapLayer();
        for (int x = 0 ; x < editorWindow.getCurrentMap().getWidth() ; x ++) {
        	for (int y = 0 ; y < editorWindow.getCurrentMap().getHeight() ; y ++) {
        		graphics.drawString(x, y, String.valueOf(layer.get(x, y)));
        	}
        }
        if(hasFocus()) {
        	setHotspot(graphics.translateToGlobalCoordinates(cursorPosition));
        }
        // TODO this should be in the repaint of the toolpanel
	}

	@Override protected TerminalSize calculatePreferredSize() {
		return new TerminalSize(
				Math.min(editorWindow.getCurrentMap().getWidth(), MAX_WIDTH),
				Math.min(editorWindow.getCurrentMap().getHeight(), MAX_HEIGHT));
	}

	@Override public Result keyboardInteraction(Key key) {
		if (key.equalsString("<f1>")) {
			GUIScreen gui = getParent().getWindow().getOwner();
			MessageBox.showMessageBox(gui, "HAHU", "You presssed F1");
			return Result.NEXT_INTERACTABLE_DOWN;
		} else if (key.equalsString("q")) {
			getGUIScreen().getActiveWindow().close();
		} else if (key.equalsString("<c-z>")) {
			undo();
		} else if (key.equalsString("1")) {
			editorWindow.cycleLayers();
		} else if (key.equalsString("2")) {
			editorWindow.cycleModes();
		} else if (key.equalsString("3")) {
			editorWindow.cycleBrushes();
		}
		switch (editorWindow.getCurrentMode()) {
		case DIRECTIONAL_DRAW:
			keyboardInteraction_DIRECTIONAL_DRAW(key);
			break;
		case PIXEL_DRAW:
			keyboardInteraction_PIXEL_DRAW(key);
			break;
		case SHOW_LEGEND:
			break;
		default:
			break;
		}
		
			return Result.EVENT_HANDLED;
//		return Result.NEXT_INTERACTABLE_DOWN;
	}

	private void undo() {
		DrawAction lastAct;
		try {
			lastAct = editorWindow.getDrawHistory().pop();
			lastAct.undo();
			cursorPosition = lastAct.oldPos;
		} catch (NoSuchElementException e) { }
	}


	private void deleteChar() {
		editorWindow.getCurrentMapLayer().set(cursorPosition, '.');
	}
	
	private void moveCursor(CompassDir moveDir) {
		TMEditFormatLayer layer = editorWindow.getCurrentMapLayer();
		cursorPosition = new TerminalPosition(
					Math.min(Math.max(cursorPosition.getColumn() + moveDir.getDeltaX(), 0), layer.getWidth() - 1),
					Math.min(Math.max(cursorPosition.getRow() + moveDir.getDeltaY(), 0), layer.getHeight() - 1));
	}


	public void drawDir(CompassDir selectedDir) {
		DirectedDrawAction act = new DirectedDrawAction();
		act.brush = editorWindow.getCurBrush();
		act.oldPos = new TerminalPosition(cursorPosition.getColumn(), cursorPosition.getRow());
		act.selectedDir = selectedDir;
		act.layer = editorWindow.getCurrentMapLayer();
		act.execute(editorWindow.getDrawHistory());
		cursorPosition = act.newPos;
	}

	private void keyboardInteraction_PIXEL_DRAW(Key key) {
		if (key.equalsString("x")) {
			deleteChar();
		} else if (key.equalsString("j")) {
			moveCursor(CompassDir.SOUTH);
		} else if (key.equalsString("k")) {
			moveCursor(CompassDir.NORTH);
		} else if (key.equalsString("h")) {
			moveCursor(CompassDir.WEST);
		} else if (key.equalsString("l")) {
			moveCursor(CompassDir.EAST);
		} else if (key.equalsString("n")) {
			moveCursor(CompassDir.SOUTHWEST);
		} else if (key.equalsString("m")) {
			moveCursor(CompassDir.SOUTHEAST);
		} else if (key.equalsString("u")) {
			moveCursor(CompassDir.NORTHWEST);
		} else if (key.equalsString("i")) {
			moveCursor(CompassDir.NORTHEAST);
		}
	}

	private void keyboardInteraction_DIRECTIONAL_DRAW(Key key) {
		if (key.equalsString("x")) {
			deleteChar();
		} else if (key.equalsString("j")) {
			drawDir(CompassDir.SOUTH);
		} else if (key.equalsString("k")) {
			drawDir(CompassDir.NORTH);
		} else if (key.equalsString("h")) {
			drawDir(CompassDir.WEST);
		} else if (key.equalsString("l")) {
			drawDir(CompassDir.EAST);
		} else if (key.equalsString("n")) {
			drawDir(CompassDir.SOUTHWEST);
		} else if (key.equalsString("m")) {
			drawDir(CompassDir.SOUTHEAST);
		} else if (key.equalsString("u")) {
			drawDir(CompassDir.NORTHWEST);
		} else if (key.equalsString("i")) {
			drawDir(CompassDir.NORTHEAST);
		}
//		else if (key.equalsString("<cr>")) {
//			this.setMode(MapEditorMode.SELECT);
//		}
	}

}
