package kba.unicodeart.gui.window;

import kba.unicodeart.gui.TMEditor;
import kba.unicodeart.gui.TMEditorToolPanel;
import kba.unicodeart.gui.TMMapEditorComponent;
import kba.unicodeart.gui.TMStatusBarPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.GUIScreen.Position;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.Panel.Orientation;
import com.googlecode.lanterna.gui.layout.LinearLayout;
import com.googlecode.lanterna.gui.layout.VerticalLayout;
import com.googlecode.lanterna.input.Key;

/**
 * The main window of the application
 */
public class TMEditorWindow extends Window {

	private static final Logger log = LoggerFactory.getLogger(TMEditorWindow.class);

	private TMEditor applicationState;

	//
	// The widgets
	//

	/**
	 * Panel on the right side with all the tools as subpanels
	 */
	private TMEditorToolPanel toolPanel;

	/**
	 * The map display and edit component
	 */
	private TMMapEditorComponent mapEditComponent;

	/**
	 * The status bar
	 */
	private TMStatusBarPanel statusBarPanel;

	/**
	 * Sets up all the GUI components inside the window.
	 */
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
		topPanel.addComponent(mapEditComponentPanel,
				LinearLayout.GROWS_HORIZONTALLY);

		toolPanel = new TMEditorToolPanel(this, Orientation.VERTICAL);
		topPanel.addComponent(toolPanel, LinearLayout.GROWS_HORIZONTALLY);

		mainPanel.addComponent(topPanel, LinearLayout.GROWS_HORIZONTALLY);

		statusBarPanel = new TMStatusBarPanel(this);
		mainPanel.addComponent(statusBarPanel, LinearLayout.GROWS_HORIZONTALLY);
		this.addComponent(mainPanel, LinearLayout.MAXIMIZES_HORIZONTALLY, LinearLayout.MAXIMIZES_VERTICALLY);
		this.setFocus(mapEditComponent);
	}

	/**
	 * @return the state of the application
	 */
	public TMEditor getApplicationState() {
		return applicationState;
	}


	/**
	 * @return the tool panel
	 */
	public TMEditorToolPanel getToolPanel() {
		return toolPanel;
	}


	/**
	 * @return the map display and edit component 
	 */
	public TMMapEditorComponent getMapEditComponent() {
		return mapEditComponent;
	}


	/**
	 * @return the status bar panel
	 */
	public TMStatusBarPanel getStatusBarPanel() {
		return statusBarPanel;
	}

	//
	// Constructors
	//

	/**
	 * The inherited constructor is hidden.
	 */
	private TMEditorWindow(String title) {
		super(title);
	}

	/**
	 * @param editor the initial state of the application
	 */
	public TMEditorWindow(TMEditor editor) {
		this("TMEditor");
		this.applicationState = editor;
		setupGUI();
	}

	//
	// Keyboard Input
	//
	/* (non-Javadoc)
	 * @see com.googlecode.lanterna.gui.Window#onKeyPressed(com.googlecode.lanterna.input.Key)
	 */
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
					applicationState.addLayer(layerName);
				}
			} else if (key.equalsString("1")) {
				applicationState.cycleModes();
			} else if (key.equalsString("2")) {
				applicationState.cycleLayers();
			} else if (key.equalsString("3")) {
				applicationState.cycleBrushes();
			} else if (key.equalsString("<S-s>")) {
				applicationState.saveMap();
			}
		}
		// else if (key.equalsString("r")) {
		// getOwner().invalidate();
		// getOwner().getScreen().refresh();
		// }
		super.onKeyPressed(key);
	}
	
	
	//
	// Helpers
	//



	/**
	 * Draw a message by writing it to the status bar
	 * @param text the text to draw
	 */
	public void drawMsg(String text) {
		getStatusBarPanel().getMsgLabel().setText(text);
	}


}