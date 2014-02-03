package kba.unicodeart.gui;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import kba.unicodeart.CompassDir;
import kba.unicodeart.format.LanternaAdapter;
import kba.unicodeart.format.TMColoredCharacter;
import kba.unicodeart.format.TMEditFormatLayer;
import kba.unicodeart.gui.action.CharacterDrawAction;
import kba.unicodeart.gui.action.DirectedDrawAction;
import kba.unicodeart.gui.action.DrawAction;
import kba.unicodeart.gui.window.FillDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.TextGraphics;
import com.googlecode.lanterna.gui.GUIScreen.Position;
import com.googlecode.lanterna.gui.component.AbstractInteractableComponent;
import com.googlecode.lanterna.gui.dialog.MessageBox;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.ACS;
import com.googlecode.lanterna.terminal.TerminalPosition;
import com.googlecode.lanterna.terminal.TerminalSize;
import com.googlecode.lanterna.terminal.TextColor;

public class TMMapEditorComponent extends AbstractInteractableComponent {
	
	private static final char SCROLLBAR_BACKGROUND = ACS.BLOCK_SPARSE;
	private static final char SCROLLBAR_HANDLE = ACS.BLOCK_SOLID;

	Logger log = LoggerFactory.getLogger(getClass().getName());
	
	private int mapOffsetX = 0;
	public int getMapOffsetX() { return mapOffsetX; }
	public void setMapOffsetX(int mapOffsetX) { this.mapOffsetX = mapOffsetX; }
	public void incMapOffsetX(int delta) { 
		int newOffsetX = this.mapOffsetX + mapOffsetX;
		if (newOffsetX > applicationState.getCurrentMap().getWidth() - 10)
			newOffsetX = applicationState.getCurrentMap().getWidth() - 10;
		else if (newOffsetX < 0) newOffsetX = 0;
		this.mapOffsetX = newOffsetX;
	}
	private int mapOffsetY = 0;
	public int getMapOffsetY() { return mapOffsetY; }
	public void setMapOffsetY(int mapOffsetY) { this.mapOffsetY = mapOffsetY; }
	public void incMapOffsetY(int delta) { 
		int newOffsetY = this.mapOffsetY + mapOffsetY;
		if (newOffsetY > applicationState.getCurrentMap().getHeight() - 10)
			newOffsetY = applicationState.getCurrentMap().getHeight() - 10;
		else if (newOffsetY < 0) newOffsetY = 0;
		this.mapOffsetY = newOffsetY;
	}
	private static final int MAX_WIDTH = 80;
	private static final int MAX_HEIGHT = 30;

	private TerminalPosition cursorPosition = new TerminalPosition(mapOffsetX, mapOffsetY);
	public TerminalPosition getCursorPosition() { return cursorPosition; }
	private TMEditor applicationState;
	private TMEditorWindow mainWindow;

	private int mapViewWidth;
	private int mapViewHeight;
	
	public TMMapEditorComponent(TMEditorWindow tmEditorWindow) {
		this.mainWindow = tmEditorWindow;
		this.applicationState = mainWindow.getApplicationState();
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
//
	
	@Override public void repaint(TextGraphics graphics) {
        graphics.fillArea('0');
        this.mapViewWidth = graphics.getWidth() -1;
        this.mapViewHeight = graphics.getHeight() -1;
        TMEditFormatLayer layer = applicationState.getCurrentLayer();
        if (null != layer) {
        	// draw map
        	for (int mapX = mapOffsetX ;
        			(mapX < mapOffsetX + graphics.getWidth() -1) // within the view's width
        			&& (mapX < layer.getWidth() -1) // within the map's width
        			;
        			mapX ++) {
        		for (int mapY = mapOffsetY ;
        				(mapY < mapOffsetY + graphics.getHeight() - 1) // within the view's height
        				&& (mapY < layer.getHeight() - 1) // within the map's height
        				;
        				mapY ++) {
        			TMColoredCharacter colChar = layer.get(mapX, mapY);
        			graphics.setForegroundColor(LanternaAdapter.toTextColor(colChar.getFg()));
        			graphics.setBackgroundColor(LanternaAdapter.toTextColor(colChar.getBg()));
					graphics.drawString(mapX - mapOffsetX, mapY - mapOffsetY, String.valueOf(colChar.getCharacter()));
        		}
        	}
        	
        	{
        		/*
        		 * Vertical scollbar 
        		 * draw vertical scrollbar 
        		 * a) part above scroll handle 
        		 * b) scroll handle =~ view height 
        		 * c) part below scroll handle
        		 */
        		float percentageAboveHandle = getMapOffsetY() / (float)layer.getHeight();
        		float percentageVerticalHandle = (graphics.getHeight()-1) / (float)layer.getHeight();
        		int rowsAboveHandle = (int) (percentageAboveHandle * graphics.getHeight());
        		int rowsHandle = (int) (percentageVerticalHandle * graphics.getHeight());
        		for (int i = 0 ; i < rowsAboveHandle ; i ++ )
        			graphics.drawString(graphics.getWidth()-1, i, String.valueOf(SCROLLBAR_BACKGROUND));
        		for (int i = rowsAboveHandle ; i < rowsAboveHandle + rowsHandle ; i ++ )
        			graphics.drawString(graphics.getWidth()-1, i, String.valueOf(SCROLLBAR_HANDLE));
        		for (int i = rowsAboveHandle + rowsHandle ; i < graphics.getHeight() ; i ++ )
        			graphics.drawString(graphics.getWidth()-1, i, String.valueOf(SCROLLBAR_BACKGROUND));;
        	}
        		
        	{
        		/*
        		 * Horizontal scollbar 
        		 * draw vertical scrollbar 
        		 * a) part left of scroll handle 
        		 * b) scroll handle =~ view width 
        		 * c) part right of scroll handle
        		 */
        		float percentageLeftOfHandle = getMapOffsetX() / (float)layer.getWidth();
        		float percentageHorizontalHandle = (graphics.getWidth()-1) / (float)layer.getWidth();
        		int colsLeftOfHandle = (int) (percentageLeftOfHandle * graphics.getWidth());
        		int colsHandle = (int) (percentageHorizontalHandle * graphics.getWidth());
        		for (int i = 0 ; i < colsLeftOfHandle ; i ++ )
        			graphics.drawString(i, graphics.getHeight() -1, String.valueOf(SCROLLBAR_BACKGROUND));
        		for (int i = colsLeftOfHandle ; i < colsLeftOfHandle + colsHandle ; i ++ )
        			graphics.drawString(i, graphics.getHeight() -1, String.valueOf(SCROLLBAR_HANDLE));
        		for (int i = colsLeftOfHandle + colsHandle ; i < graphics.getWidth() ; i ++ )
        			graphics.drawString(i, graphics.getHeight() -1, String.valueOf(SCROLLBAR_BACKGROUND));
        	}
        		
        }
        if(hasFocus()) {
        	log.debug("mapOffsetY:  " + mapOffsetY);
        	TerminalPosition drawPosition = new TerminalPosition(cursorPosition.getColumn()-mapOffsetX, cursorPosition.getRow()-mapOffsetY);
        	setHotspot(graphics.translateToGlobalCoordinates(drawPosition));
        }
        // TODO this should be in the repaint of the toolpanel
	}

	@Override protected TerminalSize calculatePreferredSize() {
		return new TerminalSize(
				Math.min(applicationState.getCurrentMap().getWidth(), MAX_WIDTH),
				Math.min(applicationState.getCurrentMap().getHeight(), MAX_HEIGHT));
	}

	@Override public Result keyboardInteraction(Key key) {
		if (key.equalsString("<tab>")) {
			return Result.NEXT_INTERACTABLE_DOWN;
		} else if (key.equalsString("<f1>")) {
			GUIScreen gui = getParent().getWindow().getOwner();
			MessageBox.showMessageBox(gui, "HAHU", "You presssed F1");
			return Result.NEXT_INTERACTABLE_DOWN;
		} else if (key.equalsString("<c-z>")) {
			undo();
		}
		switch (applicationState.getCurrentMode()) {
		case DIRECTIONAL_DRAW:
			keyboardInteraction_DIRECTIONAL_DRAW(key);
			break;
		case CHAR_DRAW:
			keyboardInteraction_CHAR_DRAW(key);
			break;
//		case SHOW_LEGEND:
//			break;
		default:
			break;
		}
		mainWindow.getStatusBarPanel().getCurPosLabel().setText(cursorPosition.toString());
		
			return Result.EVENT_HANDLED;
//		return Result.NEXT_INTERACTABLE_DOWN;
	}

	private void undo() {
		DrawAction lastAct;
		try {
			lastAct = applicationState.getDrawHistory().pop();
			lastAct.undo();
			cursorPosition = lastAct.getOldPos();
		} catch (NoSuchElementException e) { }
	}

	private void deleteChar() {
		drawChar(applicationState.getCurrentLayer().getTransparentChar());
	}
	
	private void moveCursor(CompassDir moveDir) {
		TMEditFormatLayer layer = applicationState.getCurrentLayer();
		TerminalPosition newPos = new TerminalPosition(
					Math.min(Math.max(cursorPosition.getColumn() + moveDir.getDeltaX(), mapOffsetX), layer.getWidth() - 1),
					Math.min(Math.max(cursorPosition.getRow() + moveDir.getDeltaY(), mapOffsetY), layer.getHeight() - 1));
		if (positionIsInView(newPos)) {
			cursorPosition = newPos;
		}
	}
	private void drawChar(char ch, TextColor fg, TextColor bg) {
		CharacterDrawAction act = new CharacterDrawAction();
		act.setDrawChar(ch);
		act.setNewFg(fg);
		act.setNewBg(bg);
		act.setOldPos(cursorPosition);
		act.setLayer(applicationState.getCurrentLayer());
		act.execute(applicationState.getDrawHistory());
		cursorPosition = act.getNewPos();
	}
	private void drawChar(TMColoredCharacter transparentChar) {
		TextColor fg = LanternaAdapter.toTextColor(transparentChar.getFg());
		TextColor bg = LanternaAdapter.toTextColor(transparentChar.getBg());
		drawChar(transparentChar.getCharacter(), fg, bg);
	}
	
	private void drawChar(char ch) {
		drawChar(ch, applicationState.getCurrentFg(), applicationState.getCurrentBg());
	}

	public void drawDir(CompassDir selectedDir) {
		DirectedDrawAction act = new DirectedDrawAction();
		act.setBrush(applicationState.getCurBrush());
		act.setOldPos(new TerminalPosition(cursorPosition.getColumn(), cursorPosition.getRow()));
		act.setNewFg(applicationState.getCurrentFg());
		act.setNewBg(applicationState.getCurrentBg());
		act.setSelectedDir(selectedDir);
		act.setLayer(applicationState.getCurrentLayer());
		act.execute(applicationState.getDrawHistory());
		TerminalPosition newPos = act.getNewPos();
		if (! positionIsInView(newPos)) {
			act.undo();
		} else {
			cursorPosition = newPos;
		}

	}

	private void keyboardInteraction_CHAR_DRAW(Key key) {
		if (key.equalsString("x")) {
			deleteChar();
		} else if (key.equalsString("F")) {
			FillDialog fcw = new FillDialog(applicationState);
			this.getGUIScreen().showWindow(fcw, Position.CENTER);
			if (fcw.ok)
				fill(new TMColoredCharacter( fcw.fillChar,
						LanternaAdapter.toAwtColor(applicationState.getCurrentFg()),
						LanternaAdapter.toAwtColor(applicationState.getCurrentBg())),
						fcw.doFillChar, fcw.doFillFg, fcw.doFillBg);
		} else if (key.equalsString("<space>")) {
			drawChar(applicationState.getCurPaletteChar());
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
		} else if (key.equalsString("F")) {
			fill(new TMColoredCharacter(applicationState.getCurPaletteChar(),
                        LanternaAdapter.toAwtColor(applicationState.getCurrentFg()),
                        LanternaAdapter.toAwtColor(applicationState.getCurrentBg())),
					true, true, true);
		} else if (key.equalsString("+")) {
			drawChar(applicationState.getCurBrush().get(null));
		} else if (key.equalsString("<s-h>")) {
			panMap(-5, 0);
		} else if (key.equalsString("<s-l>")) {
			panMap(+5, 0);
		} else if (key.equalsString("<s-j>")) {
			panMap(0, +5);
		} else if (key.equalsString("<s-k>")) {
			panMap(0, -5);
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
	
	public boolean positionIsInView(TerminalPosition newPos) {
		if (newPos.getColumn() - mapOffsetX < 0 || newPos.getRow() - mapOffsetY < 0) {
			return false;
		} else if (newPos.getColumn() - mapOffsetX >= mapViewWidth || newPos.getRow() - mapOffsetY >= mapViewHeight) {
			return false;
		}
		return true;
	}
	private void ensureValidCursorPosition() {
		if (! positionIsInView(cursorPosition)) {
			// TODO buggy
//			int newX = (cursorPosition.getColumn() > mapViewWidth+mapOffsetX) ? mapOffsetX + mapViewWidth : mapOffsetX + 1;
//			int newY = (cursorPosition.getRow() > mapViewHeight+mapOffsetY) ? mapOffsetY + mapViewHeight : mapOffsetY + 1;
//			cursorPosition =  new TerminalPosition(newY, newX);
			cursorPosition =  new TerminalPosition(mapOffsetX + 1, mapOffsetY + 1);
		}
		
	}
	public void panMap(int deltaX, int deltaY) {
		int newOffsetY = mapOffsetY+deltaY;
		if (newOffsetY >= 0 && newOffsetY < applicationState.getCurrentLayer().getHeight() -1) {
			mapOffsetY = newOffsetY;
		}
		int newOffsetX = mapOffsetX+deltaX;
		if (newOffsetX >= 0 && newOffsetX < applicationState.getCurrentLayer().getWidth() -1) {
			mapOffsetX = newOffsetX;
		}
		ensureValidCursorPosition();
		invalidate();
	}

	public void fill(TMColoredCharacter fillChar, boolean doFillChar, boolean doFillFg, boolean doFillBg) {
		TMColoredCharacter template = applicationState.getCurrentLayer().get(cursorPosition.getColumn(), cursorPosition.getRow());
		TMColoredCharacter match = new TMColoredCharacter(template.getCharacter(),template.getFg(),template.getBg());
		floodFill(cursorPosition, match, fillChar, doFillChar, doFillFg, doFillBg);
	}
	private void floodFill(TerminalPosition startPos, TMColoredCharacter target, TMColoredCharacter replacement, boolean doFillChar, boolean doFillFg, boolean doFillBg) {
		TMEditFormatLayer layer = applicationState.getCurrentLayer();
//		try {
//			System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		LinkedList<TerminalPosition> fillQueue = new LinkedList<>();
		fillQueue.addLast(startPos);
		while (! fillQueue.isEmpty()) {
			TerminalPosition pos = fillQueue.removeFirst();
			int posX = pos.getColumn();
			int posY = pos.getRow();
			TMColoredCharacter thisChar = layer.get(posX, posY);
			if (! thisChar.equals(target)) {
				continue;
			}
			if (doFillChar) thisChar.setCharacter(replacement.getCharacter());
			if (doFillBg) thisChar.setBg(replacement.getBg());
			if (doFillFg) thisChar.setFg(replacement.getFg());
			if (posX>0)
				fillQueue.addLast(new TerminalPosition(posX-1, posY));
			if (posX < layer.getWidth() -1)
				fillQueue.addLast(new TerminalPosition(posX+1, posY));
			if (posY>0)
				fillQueue.addLast(new TerminalPosition(posX, posY-1));
			if (posY < layer.getHeight() -1)
				fillQueue.addLast(new TerminalPosition(posX, posY+1));
		}
	}
}
