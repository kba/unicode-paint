package kba.unicodeart.gui;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import kba.unicodeart.gui.window.DialogWindow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.GUIScreen.Position;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.Panel.Orientation;
import com.googlecode.lanterna.gui.layout.LinearLayout;
import com.googlecode.lanterna.gui.layout.VerticalLayout;
import com.googlecode.lanterna.input.Key;

class TMEditorWindow extends Window {

	Logger log = LoggerFactory.getLogger(getClass().getName());
	private TMEditor applicationState;
	private TMEditorToolPanel toolPanel;
	public TMEditorToolPanel getToolPanel() { return toolPanel; }
	private TMMapEditorComponent mapEditComponent;
	public TMMapEditorComponent getMapEditComponent() { return mapEditComponent; }
	private TMStatusBarPanel statusBarPanel;
	public TMStatusBarPanel getStatusBarPanel() { return statusBarPanel; }

	public TMEditor getApplicationState() {
		return applicationState;
	}
	private TMEditorWindow(String title) {
		super(title);
	}
	public TMEditorWindow(TMEditor editor) {
		this("TMEditor");
		this.applicationState = editor;
		setupGUI();
	}

	private void setupGUI() {

        this.setBorder(new Border.Invisible());
        final Panel mainPanel = new Panel("BorderLayout", Orientation.VERTICAL);
        mainPanel.setBorder(new Border.Invisible());
        mainPanel.setLayoutManager(new VerticalLayout());

        Panel topPanel = new Panel(Orientation.HORIZONTAL);

		mapEditComponent = new TMMapEditorComponent(this);

        Panel mapEditComponentPanel = new Panel();
        mapEditComponentPanel.addComponent(mapEditComponent);
		mapEditComponentPanel.setBorder(new Border.Standard());
		topPanel.addComponent(mapEditComponentPanel, LinearLayout.GROWS_HORIZONTALLY);

		toolPanel = new TMEditorToolPanel(this, Orientation.VERTICAL);
        topPanel.addComponent(toolPanel, LinearLayout.GROWS_HORIZONTALLY);

        mainPanel.addComponent(topPanel, LinearLayout.GROWS_HORIZONTALLY);

        statusBarPanel = new TMStatusBarPanel(this);
        mainPanel.addComponent(statusBarPanel, LinearLayout.GROWS_HORIZONTALLY);
        this.addComponent(mainPanel, LinearLayout.MAXIMIZES_HORIZONTALLY, LinearLayout.MAXIMIZES_VERTICALLY);
        this.setFocus(mapEditComponent);
	}
	public void drawMsg(String text) {
		getStatusBarPanel().getMsgLabel().setText(text);
	}

	@Override
	public void onKeyPressed(Key key) {
		if (key.equalsString("q")) {
			this.close();
		} else {
			if (key.equalsString("<escape>")) {
				setFocus(mapEditComponent);
			} else if (key.equalsString("<S-a>")) {
				DialogWindow dialogWindow = new DialogWindow("New Layer", "");
				getOwner().showWindow(dialogWindow, Position.CENTER);
				if (dialogWindow.ok()) {
					String layerName = dialogWindow.getValue();
					applicationState.getCurrentMap().addLayer(layerName);
					toolPanel.getLayerBoxList().addItem(applicationState.getCurrentMap().getLayerByName(layerName));
				}
			} else if (key.equalsString("1")) {
				applicationState.cycleModes();
			} else if (key.equalsString("2")) {
				applicationState.cycleLayers();
			} else if (key.equalsString("3")) {
				applicationState.cycleBrushes();
			} else if (key.equalsString("<S-s>")) {
				long t0 = System.nanoTime();
				String s = applicationState.getCurrentMap().exportToString();
				log.debug("export took " + ((System.nanoTime() - t0) / 1000000) + " milliseconds.");
				try {
					File tmpFile = File.createTempFile("tmeditor_", ".txt");
					FileWriter fw = new FileWriter(tmpFile);
					fw.write(s);
					fw.close();
					log.debug("written to " + tmpFile);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
//		else if (key.equalsString("r")) {
//			getOwner().invalidate();
//			getOwner().getScreen().refresh();
//		}
		super.onKeyPressed(key);
	}
	
	
}