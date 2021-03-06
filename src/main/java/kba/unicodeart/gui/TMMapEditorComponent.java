package kba.unicodeart.gui;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import kba.unicodeart.CompassDir;
import kba.unicodeart.algo.BresenhamLine;
import kba.unicodeart.format.TMLanternaAdapter;
import kba.unicodeart.format.TMEditFormatLayer;
import kba.unicodeart.format.colored_char.TMColoredCharacter;
import kba.unicodeart.format.colored_char.TMColoredCharacterFactory;
import kba.unicodeart.gui.action.CharacterDrawAction;
import kba.unicodeart.gui.action.DirectedDrawAction;
import kba.unicodeart.gui.action.DrawAction;
import kba.unicodeart.gui.window.FillDialog;
import kba.unicodeart.gui.window.TMEditorWindow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.GUIScreen.Position;
import com.googlecode.lanterna.gui.TextGraphics;
import com.googlecode.lanterna.gui.Theme.Category;
import com.googlecode.lanterna.gui.component.AbstractInteractableComponent;
import com.googlecode.lanterna.gui.dialog.MessageBox;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.ACS;
import com.googlecode.lanterna.terminal.TerminalPosition;
import com.googlecode.lanterna.terminal.TerminalSize;
import com.googlecode.lanterna.terminal.TextColor;

/**
 * The map edit component
 * @author kba
 *
 */
public class TMMapEditorComponent extends AbstractInteractableComponent {

	private static final Logger log = LoggerFactory.getLogger(TMMapEditorComponent.class);

	private static final int MAX_WIDTH = 80;
	private static final int MAX_HEIGHT = 30;
	private static final char SCROLLBAR_BACKGROUND = ACS.BLOCK_SPARSE;
	private static final char SCROLLBAR_HANDLE = ACS.BLOCK_SOLID;


	private TMEditor applicationState;
	private TMEditorWindow mainWindow;
	private int mapOffsetX = 0;
	private int mapOffsetY = 0;
	private TerminalPosition cursorPosition = new TerminalPosition(mapOffsetX, mapOffsetY);
	private int mapViewWidth;
	private int mapViewHeight;
	public TMMapEditorComponent(TMEditorWindow tmEditorWindow) {
		this.mainWindow = tmEditorWindow;
		this.applicationState = mainWindow.getApplicationState();
		// TODO
		// this.setHotspot(new TerminalPosition(3, 3));
	}

	//
	// Repaint
	//
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.lanterna.gui.Component#repaint(com.googlecode.lanterna
	 * .gui.TextGraphics)
	 */
	@Override
	public void repaint(TextGraphics graphics) {
		graphics.fillArea('0');
		this.mapViewWidth = graphics.getWidth() - 1;
		this.mapViewHeight = graphics.getHeight() - 1;
		TMEditFormatLayer layer = applicationState.getCurrentLayer();
		if (null != layer) {
			// draw map
			// @noformat
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
        			// @format

					// TODO probably have to deep copy here
					// TMColoredCharacter colChar =
					// applicationState.getColorCharFactory().create(layer.get(mapX,
					// mapY));
					TMColoredCharacter colChar = layer.get(mapX, mapY);
					boolean overrideChar = false;
					if (null != applicationState.getFirstPoint()) {
						if (mapX == applicationState.getFirstPoint().getColumn() && mapY == applicationState.getFirstPoint().getRow()) {
							graphics.setForegroundColor(TextColor.ANSI.BLUE);
							overrideChar = true;
						}
					}
					if (applicationState.getSecondPoint() != null) {
						if (mapX == applicationState.getSecondPoint().getColumn() && mapY == applicationState.getSecondPoint().getRow()) {
							graphics.setForegroundColor(TextColor.ANSI.BLUE);
							overrideChar = true;
						}
					}
					if (! overrideChar) {
						//					log.debug("colChar: " + colChar);
						graphics.setForegroundColor(TMLanternaAdapter.toTextColor(colChar.getFg()));
						graphics.setBackgroundColor(TMLanternaAdapter.toTextColor(colChar.getBg()));
					}
					graphics.drawString(mapX - mapOffsetX, mapY - mapOffsetY,
							String.valueOf(colChar.getCharacter()));
				}
			}

			{
				graphics.applyTheme(Category.DIALOG_AREA);
				/*
				 * Vertical scollbar draw vertical scrollbar a) part above
				 * scroll handle b) scroll handle =~ view height c) part below
				 * scroll handle
				 */
				float percentageAboveHandle = getMapOffsetY() / (float) layer.getHeight();
				float percentageVerticalHandle = (graphics.getHeight() - 1)
						/ (float) layer.getHeight();
				int rowsAboveHandle = (int) (percentageAboveHandle * graphics.getHeight());
				int rowsHandle = (int) (percentageVerticalHandle * graphics.getHeight());
				for (int i = 0; i < rowsAboveHandle; i++)
					graphics.drawString(graphics.getWidth() - 1, i,
							String.valueOf(SCROLLBAR_BACKGROUND));
				for (int i = rowsAboveHandle; i < rowsAboveHandle + rowsHandle; i++)
					graphics.drawString(graphics.getWidth() - 1, i,
							String.valueOf(SCROLLBAR_HANDLE));
				for (int i = rowsAboveHandle + rowsHandle; i < graphics.getHeight(); i++)
					graphics.drawString(graphics.getWidth() - 1, i,
							String.valueOf(SCROLLBAR_BACKGROUND));
				;
			}

			{
				/*
				 * Horizontal scollbar draw vertical scrollbar a) part left of
				 * scroll handle b) scroll handle =~ view width c) part right of
				 * scroll handle
				 */
				float percentageLeftOfHandle = mapOffsetX / (float) layer.getWidth();
				float percentageHorizontalHandle = (graphics.getWidth() - 1)
						/ (float) layer.getWidth();
				int colsLeftOfHandle = (int) (percentageLeftOfHandle * graphics.getWidth());
				int colsHandle = (int) (percentageHorizontalHandle * graphics.getWidth());
				for (int i = 0; i < colsLeftOfHandle; i++)
					graphics.drawString(i, graphics.getHeight() - 1,
							String.valueOf(SCROLLBAR_BACKGROUND));
				for (int i = colsLeftOfHandle; i < colsLeftOfHandle + colsHandle; i++)
					graphics.drawString(i, graphics.getHeight() - 1,
							String.valueOf(SCROLLBAR_HANDLE));
				for (int i = colsLeftOfHandle + colsHandle; i < graphics.getWidth(); i++)
					graphics.drawString(i, graphics.getHeight() - 1,
							String.valueOf(SCROLLBAR_BACKGROUND));
			}

		}
		if (hasFocus()) {
			TerminalPosition drawPosition = new TerminalPosition(cursorPosition.getColumn()
					- mapOffsetX, cursorPosition.getRow() - mapOffsetY);
			setHotspot(graphics.translateToGlobalCoordinates(drawPosition));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.googlecode.lanterna.gui.component.AbstractComponent#
	 * calculatePreferredSize()
	 */
	@Override
	protected TerminalSize calculatePreferredSize() {
		return new TerminalSize(Math.min(applicationState.getCurrentMap().getWidth(), MAX_WIDTH),
				Math.min(applicationState.getCurrentMap().getHeight(), MAX_HEIGHT));
	}

	//
	// Keyboard Input
	//
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.lanterna.gui.Interactable#keyboardInteraction(com.googlecode
	 * .lanterna.input.Key)
	 */
	@Override
	public Result keyboardInteraction(Key key) {
		if (key.equalsString("<tab>")) {
			return Result.NEXT_INTERACTABLE_DOWN;
		} else if (key.equalsString("<S-Tab>")) {
			return Result.PREVIOUS_INTERACTABLE_UP;
		} else if (key.equalsString("<f1>")) {
			GUIScreen gui = getParent().getWindow().getOwner();
			MessageBox.showMessageBox(gui, "HAHU", "You presssed F1");
			return Result.NEXT_INTERACTABLE_DOWN;
		} else if (key.equalsString("<c-z>")) {
			undo();
		} else if (key.equalsString("<s-b>")) {
			applicationState.toggleTransparent();
			mainWindow.getToolPanel().getBgPaletteList().setForceMonochrome(applicationState.isTransparent());
		}
		switch (applicationState.getCurrentMode()) {
		case DIRECTIONAL_DRAW:
			keyboardInteraction_DIRECTIONAL_DRAW(key);
			break;
		case CHAR_DRAW:
			keyboardInteraction_CHAR_DRAW(key);
			break;
		case TWO_POINT_SELECT:
			keyboardInteraction_TWO_POINT_SELECT(key);
			break;
		// case SHOW_LEGEND:
		// break;
		default:
			break;
		}
		mainWindow.getStatusBarPanel().getCurPosLabel().setText(cursorPosition.toString());

		return Result.EVENT_HANDLED;
		// return Result.NEXT_INTERACTABLE_DOWN;
	}

	private void keyboardInteraction_TWO_POINT_SELECT(Key key) {
		if (key.equalsString("x")) {
			deleteChar();
		} else if (key.equalsString("<space>")) {
			if (applicationState.getFirstPoint() == null) {
				applicationState.setFirstPoint(cursorPosition);
			} else if (applicationState.getSecondPoint() == null) {
				applicationState.setSecondPoint(cursorPosition);
			} else {
				applicationState.resetPoints();
			}
		} else if (key.equalsString("<s-l>")) {
			if (null != applicationState.getFirstPoint() && null != applicationState.getSecondPoint()) {
				drawLine(applicationState.getFirstPoint(), applicationState.getSecondPoint());
			}
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

	private void keyboardInteraction_CHAR_DRAW(Key key) {
		if (key.equalsString("x")) {
			deleteChar();
		} else if (key.equalsString("F")) {
			FillDialog fcw = new FillDialog(applicationState);
			this.getGUIScreen().showWindow(fcw, Position.CENTER);
			if (fcw.ok) {
				TMColoredCharacter fillChar = applicationState.getColorCharFactory().create(
						fcw.fillChar, applicationState.getForegroundColorIndex(),
						applicationState.getForegroundColorIndex());
				fill(fillChar, fcw.doFillChar, fcw.doFillFg, fcw.doFillBg);
			}
		} else if (key.equalsString("<space>")) {
			drawChar(applicationState.getCurrentChar());
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
			// fill(new TMColoredCharacter(applicationState.getCurrentChar(),
			// LanternaAdapter.toAwtColor(applicationState.getCurrentFg()),
			// LanternaAdapter.toAwtColor(applicationState.getCurrentBg())),
			// true, true, true);
		} else if (key.equalsString("+")) {
			drawChar(applicationState.getCurrentCharacterBoxBrush().get(null));
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
		// else if (key.equalsString("<cr>")) {
		// this.setMode(MapEditorMode.SELECT);
		// }
	}
	
	/**
	 * @return the current cursor position within the map view (not the map!)
	 */
	public TerminalPosition getCursorPosition() {
		return cursorPosition;
	}


	//
	// Map offset
	//

	private void setMapOffsetX(int mapOffsetX) {
		this.mapOffsetX = mapOffsetX;
	}

	private void incMapOffsetX(int delta) {
		int newOffsetX = this.mapOffsetX + mapOffsetX;
		if (newOffsetX > applicationState.getCurrentMap().getWidth() - 10)
			newOffsetX = applicationState.getCurrentMap().getWidth() - 10;
		else if (newOffsetX < 0)
			newOffsetX = 0;
		this.mapOffsetX = newOffsetX;
	}

	private int getMapOffsetY() {
		return mapOffsetY;
	}

	private void setMapOffsetY(int mapOffsetY) {
		this.mapOffsetY = mapOffsetY;
	}

	private void incMapOffsetY(int delta) {
		int newOffsetY = this.mapOffsetY + mapOffsetY;
		if (newOffsetY > applicationState.getCurrentMap().getHeight() - 10)
			newOffsetY = applicationState.getCurrentMap().getHeight() - 10;
		else if (newOffsetY < 0)
			newOffsetY = 0;
		this.mapOffsetY = newOffsetY;
	}

	/**
	 * Undoes the last draw action.
	 */
	public void undo() {
		DrawAction lastAct;
		try {
			lastAct = applicationState.getDrawHistory().pop();
			lastAct.undo();
			cursorPosition = lastAct.getOldPos();
		} catch (NoSuchElementException e) {
		}
	}

	/**
	 * Replace the char at cursor position with the transparent char of this layer
	 */
	public void deleteChar() {
		 doDrawChar(applicationState.getCurrentLayer().getTransparentChar());
	}

	/**
	 * Moves the cursor by one step towards the specified {@code CompassDir}
	 * @param moveDir the direction to move to
	 */
	private void moveCursor(CompassDir moveDir) {
		TMEditFormatLayer layer = applicationState.getCurrentLayer();
		TerminalPosition newPos = new TerminalPosition(Math.min(
				Math.max(cursorPosition.getColumn() + moveDir.getDeltaX(), mapOffsetX),
				layer.getWidth() - 1), Math.min(
				Math.max(cursorPosition.getRow() + moveDir.getDeltaY(), mapOffsetY),
				layer.getHeight() - 1));
		if (positionIsInView(newPos)) {
			cursorPosition = newPos;
		}
	}

	/**
	 * Draw a char at the cursor Position including colors
	 * @param ch the char to draw
	 * @param fgIndex index of the foreground color in this map's palette
	 * @param bgIndex index of the background color in this map's palette
	 */
	private void drawChar(char ch, int fgIndex, int bgIndex) {
		TMColoredCharacterFactory tmColoredCharacterFactory = applicationState.getColorCharFactory();
		TMColoredCharacter coloredCharacter = tmColoredCharacterFactory.create(ch, fgIndex, bgIndex);
		doDrawChar(coloredCharacter);
	}

	/**
	 * Draw a character with the currently selected color to the cursor position
	 * @param ch the character to draw
	 */
	public void drawChar(char ch) {
		log.debug("Draw as " +  applicationState.getForegroundColorIndex());
		drawChar(ch, applicationState.getForegroundColorIndex(), applicationState.getBackgroundColorIndex());
	}

	/**
	 * Draw a box drawing character towards the selection {@code CompassDir}.
	 * @param selectedDir the direction to draw towards
	 */
	public void drawDir(CompassDir selectedDir) {
		DirectedDrawAction act = new DirectedDrawAction();
		act.setDrawChar(applicationState.getColorCharFactory().create((char)0, applicationState.getForegroundColorIndex(), applicationState.getBackgroundColorIndex()));
		act.setBrush(applicationState.getCurrentCharacterBoxBrush());
		act.setOldPos(new TerminalPosition(cursorPosition.getColumn(), cursorPosition.getRow()));
		act.setSelectedDir(selectedDir);
		act.setLayer(applicationState.getCurrentLayer());
		act.setTransparent(applicationState.isTransparent());
		act.execute(applicationState);
		TerminalPosition newPos = act.getNewPos();
		// HACK
		if (!positionIsInView(newPos)) {
			act.undo();
		} else {
			cursorPosition = newPos;
		}

	}

	/**
	 * Draws a {@link TMColoredCharacter} to the cursor position.
	 * 
	 * <p>NOTE: The coloredCharacter is not not copied by value.</p>
	 * 
	 * @param coloredCharacter the colored character to draw
	 */
	private void doDrawChar(TMColoredCharacter coloredCharacter) {
		CharacterDrawAction act = new CharacterDrawAction();
		if (applicationState.isTransparent()) {
			coloredCharacter.setTransparent();
		}
		act.setDrawChar(coloredCharacter);
		log.debug(coloredCharacter.toString());
		act.setOldPos(cursorPosition);
		act.setLayer(applicationState.getCurrentLayer());
		act.execute(applicationState);
		cursorPosition = act.getNewPos();
	}
	
	/**
	 * @param newPos
	 * @return true if the position is within the boundaries of the view
	 */
	public boolean positionIsInView(TerminalPosition newPos) {
		if (newPos.getColumn() - mapOffsetX < 0 || newPos.getRow() - mapOffsetY < 0) {
			return false;
		} else if (newPos.getColumn() - mapOffsetX >= mapViewWidth
				|| newPos.getRow() - mapOffsetY >= mapViewHeight) {
			return false;
		}
		return true;
	}
	
	/**
	 * make sure the cursor is within the map and the view.
	 * 
	 * @deprecated
	 */
	private void ensureValidCursorPosition() {
		if (!positionIsInView(cursorPosition)) {
			// TODO buggy
			// int newX = (cursorPosition.getColumn() > mapViewWidth+mapOffsetX)
			// ? mapOffsetX + mapViewWidth : mapOffsetX + 1;
			// int newY = (cursorPosition.getRow() > mapViewHeight+mapOffsetY) ?
			// mapOffsetY + mapViewHeight : mapOffsetY + 1;
			// cursorPosition = new TerminalPosition(newY, newX);
			cursorPosition = new TerminalPosition(mapOffsetX + 1, mapOffsetY + 1);
		}

	}

	/**
	 * Pans the map view over the map towards deltaX/deltaY direction.
	 * 
	 * @param deltaX
	 * @param deltaY
	 */
	public void panMap(int deltaX, int deltaY) {
		int newOffsetY = mapOffsetY + deltaY;
		if (newOffsetY >= 0 && newOffsetY < applicationState.getCurrentLayer().getHeight() - 1) {
			mapOffsetY = newOffsetY;
		}
		int newOffsetX = mapOffsetX + deltaX;
		if (newOffsetX >= 0 && newOffsetX < applicationState.getCurrentLayer().getWidth() - 1) {
			mapOffsetX = newOffsetX;
		}
		ensureValidCursorPosition();
		invalidate();
	}

	/**
	 * Fills an area starting at the cursor position with a colored character.
	 * @param fillChar the colored character to fill with
	 * @param doFillChar whether to draw the char
	 * @param doFillFg whether to draw the foreground color
	 * @param doFillBg whether to draw the background color
	 */
	public void fill(TMColoredCharacter fillChar, boolean doFillChar, boolean doFillFg, boolean doFillBg) {
		TMColoredCharacter template = applicationState.getCurrentLayer().get(
				cursorPosition.getColumn(), cursorPosition.getRow());
		TMColoredCharacter match = applicationState.getColorCharFactory().create(template);
		fill(cursorPosition, match, fillChar, doFillChar, doFillFg, doFillBg);
	}

	/**
	 * Internal flood fill algorithm.
	 * 
	 * <p>
	 * Uses a queue instead of recursion.
	 * </p>
	 * 
	 * @param startPos the position at which the fill starts
	 * @param target the colored character to be replace
	 * @param replacement the colored character to replace with
	 * @param doFillChar whether to draw the char
	 * @param doFillFg whether to draw the foreground color
	 * @param doFillBg whether to draw the background color
	 */
	private void fill(TerminalPosition startPos, TMColoredCharacter target,
			TMColoredCharacter replacement, boolean doFillChar, boolean doFillFg, boolean doFillBg) {
		TMEditFormatLayer layer = applicationState.getCurrentLayer();
		// try {
		// System.in.read();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		LinkedList<TerminalPosition> fillQueue = new LinkedList<>();
		fillQueue.addLast(startPos);
		while (!fillQueue.isEmpty()) {
			TerminalPosition pos = fillQueue.removeFirst();
			int posX = pos.getColumn();
			int posY = pos.getRow();
			TMColoredCharacter thisChar = layer.get(posX, posY);
			if (!thisChar.equals(target)) {
				continue;
			}
			if (doFillChar)
				thisChar.setCharacter(replacement.getCharacter());
			if (doFillBg)
				thisChar.setBg(replacement.getBg());
			if (doFillFg)
				thisChar.setFg(replacement.getFg());
			if (posX > 0)
				fillQueue.addLast(new TerminalPosition(posX - 1, posY));
			if (posX < layer.getWidth() - 1)
				fillQueue.addLast(new TerminalPosition(posX + 1, posY));
			if (posY > 0)
				fillQueue.addLast(new TerminalPosition(posX, posY - 1));
			if (posY < layer.getHeight() - 1)
				fillQueue.addLast(new TerminalPosition(posX, posY + 1));
		}
	}

	
	public void drawLine(TerminalPosition p0, TerminalPosition p1) {
		List<Point> points = BresenhamLine.drawLine(p0.getColumn(), p0.getRow(), p1.getColumn(), p1.getRow());
		for (Point p : points) {
			TerminalPosition pos = new TerminalPosition(p.x, p.y);
			cursorPosition = pos;
			drawChar('X');
		}
	}

}
