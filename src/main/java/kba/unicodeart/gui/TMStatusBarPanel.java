package kba.unicodeart.gui;

import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.layout.LinearLayout;
import com.googlecode.lanterna.terminal.TerminalSize;

public class TMStatusBarPanel extends Panel {
	
	private TMEditor applicationState;
	private TMEditorWindow mainWindow;

	private Label mapNameLabel;
	private Label mapSizeLabel;
	private Label curPosLabel;
	private Label msgLabel;
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
		mapNameLabel = new Label("Map: " + applicationState.getCurrentMap().getName());
        mapSizeLabel = new Label("Size: " + applicationState.getCurrentMap().getWidth() + "x" + applicationState.getCurrentMap().getHeight());
        curPosLabel = new Label("Cursor: " + mainWindow.getMapEditComponent().getCursorPosition());
        Label msgLabelLabel = new Label("Msg: ");
        msgLabel = new Label("---");
        addComponent(mapNameLabel, LinearLayout.GROWS_HORIZONTALLY);
        addComponent(mapSizeLabel, LinearLayout.GROWS_HORIZONTALLY);
        addComponent(curPosLabel, LinearLayout.GROWS_HORIZONTALLY);
        addComponent(msgLabelLabel, LinearLayout.GROWS_HORIZONTALLY);
        addComponent(msgLabel, LinearLayout.GROWS_HORIZONTALLY);
	}
	
	@Override
	protected TerminalSize calculatePreferredSize() {
		return new TerminalSize(80, 3);
	}


}
