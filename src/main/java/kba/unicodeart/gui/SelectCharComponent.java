package kba.unicodeart.gui;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.lanterna.gui.TextGraphics;
import com.googlecode.lanterna.gui.Theme;
import com.googlecode.lanterna.gui.component.AbstractInteractableComponent;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.ScreenCharacter;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.terminal.ACS;
import com.googlecode.lanterna.terminal.TerminalSize;
import com.googlecode.lanterna.terminal.TextColor;

public class SelectCharComponent extends AbstractInteractableComponent  {
	private static final char SCROLLBAR_BACKGROUND = ACS.BLOCK_SPARSE;
	private static final char SCROLLBAR_HANDLE = ACS.BLOCK_SOLID;

	private List<ScreenCharacter> chars = new ArrayList<>();
	private int selectedIndex;
	private int cursorIndex;
	private int actualWidth;
	private int actualHeight;
	private final int maxWidth;
	private final int maxHeight;
	private int lineOffset = 0;
	private boolean disableHighlight = false;
	private ScreenCharacterStyle selectedStyle = ScreenCharacterStyle.Blinking;
	private ScreenCharacterStyle cursorStyle = ScreenCharacterStyle.Bordered;

	public int getCursorIndex() { return cursorIndex; }
	public void setCursorIndex(int cursorIndex) { this.cursorIndex = cursorIndex; }
	public void incCursorIndex(int delta) {
		cursorIndex = cursorIndex + delta;
		if (cursorIndex < 0) cursorIndex = 0;
		if (cursorIndex > chars.size() -1) cursorIndex = chars.size() -1;
		// TODO smelly smelly smelly
		if (cursorIndex < ((lineOffset + getActualHeight() -1) * actualWidth) + cursorIndex % actualWidth) { incLineOffset(-1); }
		else if (cursorIndex > ((lineOffset + getActualHeight() - 1) * actualWidth) + cursorIndex % actualWidth) { incLineOffset(1); }
	}
	public List<ScreenCharacter> getChars() { return chars; }
	public void setChars(List<ScreenCharacter> chars) {
		this.chars = chars;
	}
	public void setChars(char[] chars) { 
		this.chars.clear();
		for (char ch : chars) {
			ScreenCharacter screenCh = new ScreenCharacter(ch);
			addChar(screenCh);
		}
	}
	public void addChar(ScreenCharacter screenCh) {
		this.chars.add(screenCh);
		invalidate();
	}

	public int getSelectedIndex() { return selectedIndex; }
	public void setSelectedIndex(int selectedIndex) { this.selectedIndex = selectedIndex; }
	public void incSelectedIndex(int delta) {
		selectedIndex = selectedIndex + delta;
		if (selectedIndex < 0) selectedIndex = 0;
		if (selectedIndex > chars.size() -1) selectedIndex = chars.size() -1;
//		if (selectedIndex < lineOffset * actualWidth) { incLineOffset(-1); }
//		if (selectedIndex > lineOffset * actualWidth) { incLineOffset(1); }
	}
	public int getMaxWidth() { return maxWidth; }
	public int getActualWidth() { return actualWidth; }
	public void setActualWidth(int actualWidth) { this.actualWidth = actualWidth; }
	public int getActualHeight() { return actualHeight; }
	public void setActualHeight(int actualHeight) { this.actualHeight = actualHeight; }
	public int getLineOffset() { return lineOffset; }
	public void setLineOffset(int lineOffset) { this.lineOffset = lineOffset; }
	public void incLineOffset(int delta) {
		this.lineOffset += delta;
		if (this.lineOffset < 0) this.lineOffset = 0;
	}

	public SelectCharComponent(int maxWidth, int maxHeight, char[] chars) {
		this(maxWidth, maxHeight, false);
		if (null == chars || chars.length == 0) {
			throw new IllegalArgumentException("SelectCharComponent must have at least one char.");
		}
		this.setChars(chars);
		this.setSelectedIndex(0);
		cursorIndex = selectedIndex;
	}
	
	public SelectCharComponent(int maxWidth, int maxHeight, boolean disableHighlight) {
		this.disableHighlight = disableHighlight;
		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
	}
	
	@Override
	public Result keyboardInteraction(Key key) {
		if (key.equalsString("<Tab>")) {
			return Result.NEXT_INTERACTABLE_DOWN;
		} else if (key.equalsString("<S-Tab>")) {
			return Result.PREVIOUS_INTERACTABLE_UP;
		} else if (key.equalsString("<Left>", "h")) {
			incCursorIndex(-1);
		} else if (key.equalsString("<Up>", "k")) {
			incCursorIndex(-1 * getActualWidth());
		} else if (key.equalsString("<Down>", "j")) {
			incCursorIndex(getActualWidth());
		} else if (key.equalsString("<Right>", "l")) {
			incCursorIndex(+1);
		} else if (key.equalsString("<Space>", "<cr>")) {
			selectedIndex = cursorIndex;
			valueChanged();
		}
		return Result.EVENT_HANDLED;
	}
	@Override
	public void repaint(TextGraphics graphics) {
		if (chars.isEmpty()) return;
		TerminalSize actualSize = graphics.getSize();
		int maxLength = chars.size();
		setActualWidth(actualSize.getColumns());
		setActualHeight(actualSize.getRows());
		boolean showVerticalScrollbar = false;
		if (actualSize.getRows() < chars.size() / getActualWidth()) {
			showVerticalScrollbar = true;
		}
		if (showVerticalScrollbar) {
			setActualWidth(getActualWidth() -1);
			maxLength -= lineOffset * getActualWidth();
		}
		for (int i = lineOffset * getActualWidth(); i <lineOffset * getActualWidth() + maxLength ; i++) {
			ScreenCharacter curChar = chars.get(i);
			List<ScreenCharacterStyle> styles = new ArrayList<>();
			if (i == selectedIndex) styles.add(selectedStyle);
			if (i == cursorIndex && hasFocus()) styles.add(cursorStyle);

			int col = i % getActualWidth();
			int row = (i - lineOffset*getActualWidth()) / getActualWidth();
				graphics.setBackgroundColor(curChar.getBackgroundColor());
				graphics.setForegroundColor(curChar.getForegroundColor());
			if (! disableHighlight) {
				if (hasFocus()) graphics.applyTheme(
						graphics.getTheme().getDefinition(Theme.Category.TEXTBOX_FOCUSED));
			}
			graphics.drawString(col, row, String.valueOf(curChar), styles.toArray(new ScreenCharacterStyle[styles.size()]));
		}
		
//		// Vertical scrollbar
		if (showVerticalScrollbar) {
			int linesTotal = chars.size() / getMaxWidth();
			int rowsAboveHandle = (int) (actualHeight * (lineOffset / (float)linesTotal));
			// TODO wrong
			int rowsVerticalHandle = Math.max(1, (int) (actualHeight * ((actualHeight-1) / (float)linesTotal)));
			graphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
			graphics.setForegroundColor(TextColor.ANSI.DEFAULT);
			for (int j = 0; j < rowsAboveHandle ; j++) 
				graphics.drawString(actualWidth, j, String.valueOf(SCROLLBAR_BACKGROUND));
			for (int j = rowsAboveHandle ; j < rowsAboveHandle + rowsVerticalHandle; j++)
				graphics.drawString(actualWidth, j, String.valueOf(SCROLLBAR_HANDLE));
			for (int j = rowsAboveHandle + rowsVerticalHandle ; j < actualHeight; j++)
				graphics.drawString(actualWidth, j, String.valueOf(SCROLLBAR_BACKGROUND));
		}
	}

	@Override
	protected TerminalSize calculatePreferredSize() {
		int height = Math.max(chars.size() / getMaxWidth(), 1);
		if (maxHeight > 0) {
			height = Math.min(height,  maxHeight);
		}
		return new TerminalSize(maxWidth, maxHeight);
	}



}
