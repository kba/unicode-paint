package kba.unicodeart.gui;

import kba.unicodeart.format.TMLanternaAdapter;
import kba.unicodeart.gui.window.TMEditorWindow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.TextGraphics;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.layout.LinearLayout;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.terminal.TerminalSize;
import com.googlecode.lanterna.terminal.TextColor;

/**
 * Status bar panel that contains current information
 *
 */
public class TMStatusBarPanel extends Panel {
	
	private static final Logger log = LoggerFactory.getLogger(TMStatusBarPanel.class);
	
	private TMEditor applicationState;
	private TMEditorWindow mainWindow;

	private Label mapNameLabel;
	private Label mapSizeLabel;
	private Label curPosLabel;
	private Label fgLabel;
	private Label msgLabel;
	private Label bgLabel;
	private Label charLabel;
	private Panel cfbtPanel;

	private Label alphaLabel;
	public Label getMsgLabel() { return msgLabel; }
	public Label getCurPosLabel() { return curPosLabel; }

	public TMStatusBarPanel(TMEditorWindow mainWindow) {
		super(Orientation.HORIZONTAL);
		setBorder(new Border.Standard());
		applicationState = mainWindow.getApplicationState();
		this.mainWindow = mainWindow;
		setupGUI();
	}

	private void setupGUI() {
		mapNameLabel = new Label("Map: " + applicationState.getFileName());
        mapSizeLabel = new Label("Size: " + applicationState.getCurrentMap().getWidth() + "x" + applicationState.getCurrentMap().getHeight());
        alphaLabel = new Label("Transp: " + applicationState.isTransparent());
        curPosLabel = new Label("Cursor: " + mainWindow.getMapEditComponent().getCursorPosition());

        cfbtPanel = new Panel(Orientation.HORIZONTAL) {
        	@Override
        	protected TerminalSize calculatePreferredSize() {
        		return new TerminalSize(13, 1);
        	}
        };
        fgLabel = new Label("X");
        bgLabel = new Label("X");
        charLabel = new Label("");
        alphaLabel = new Label();
        bgLabel.setTextColor(new TextColor.RGB(44,44,44));
        bgLabel.setCharStyle(ScreenCharacterStyle.Underline);
        fgLabel.setCharStyle(ScreenCharacterStyle.Underline);
        charLabel.setCharStyle(ScreenCharacterStyle.Underline);
        cfbtPanel.addComponent(new Label("C/F/B: "));
        cfbtPanel.addComponent(charLabel, LinearLayout.GROWS_HORIZONTALLY);
        cfbtPanel.addComponent(fgLabel, LinearLayout.GROWS_HORIZONTALLY);
        cfbtPanel.addComponent(bgLabel, LinearLayout.GROWS_HORIZONTALLY);
        cfbtPanel.addComponent(alphaLabel, LinearLayout.GROWS_HORIZONTALLY);

        msgLabel = new Label("---");

        addComponent(mapNameLabel, LinearLayout.GROWS_HORIZONTALLY);
        addComponent(mapSizeLabel, LinearLayout.GROWS_HORIZONTALLY);
        addComponent(curPosLabel, LinearLayout.GROWS_HORIZONTALLY);

        addComponent(cfbtPanel, LinearLayout.GROWS_HORIZONTALLY);

        addComponent(new Label("Msg: "), LinearLayout.GROWS_HORIZONTALLY);
        addComponent(msgLabel, LinearLayout.GROWS_HORIZONTALLY);
	}
	
	@Override
	protected TerminalSize calculatePreferredSize() {
		return new TerminalSize(80, 3);
	}
	
	@Override
	public void repaint(TextGraphics graphics) {
		updateFilenameLabel();
        updateMapSizeLabel();
        updateCurrentPositionLabel();
        updateAlphaLabel();
        updateCharLabel();
        updateBgLabel();
        updateFgLabel();
		super.repaint(graphics);
	}
	public void updateCurrentPositionLabel() {
		curPosLabel.setText(String.valueOf(mainWindow.getMapEditComponent().getCursorPosition()));
	}
	public void updateMapSizeLabel() {
		mapSizeLabel.setText("Size: " + applicationState.getCurrentMap().getWidth() + "x" + applicationState.getCurrentMap().getHeight());
	}
	public void updateCharLabel() {
		char currentChar  = applicationState.getCurrentChar();
        charLabel.setText(String.valueOf(currentChar));
	}
	public void updateBgLabel() {
		TextColor backgroundColor = TMLanternaAdapter.toTextColor(applicationState.getCurrentMap()
        		.getPalette().getColorByIndex(applicationState.getBackgroundColorIndex()));
        bgLabel.setBackgroundColor(backgroundColor);
	}
	public void updateFgLabel() {
		TextColor textColor = TMLanternaAdapter.toTextColor(applicationState.getCurrentMap()
        		.getPalette().getColorByIndex(applicationState.getForegroundColorIndex()));
        fgLabel.setTextColor(textColor);
	}
	public void updateAlphaLabel() {
		alphaLabel.setText(String.valueOf(applicationState.isTransparent() ? '✔' : '✗'));
        alphaLabel.setTextColor(applicationState.isTransparent() ? TextColor.ANSI.GREEN : TextColor.ANSI.RED);
	}
	public void updateFilenameLabel() {
		String fileName = applicationState.getFileName();
		int maxLength = 10;
		String drawString = fileName;
		if (maxLength < fileName.length()) {
			int length = Math.min(maxLength, fileName.length());
			int startIndex = Math.max(0, fileName.length() - length);
			drawString = ".." + fileName.substring(startIndex, startIndex + length);
		}
		mapNameLabel.setText("Map: " + drawString);
	}


}
