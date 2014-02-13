package kba.unicodeart.gui;

import kba.unicodeart.CharPalette;
import kba.unicodeart.CharacterBoxBrush;
import kba.unicodeart.format.TMEditFormatLayer;
import kba.unicodeart.format.TMLanternaAdapter;
import kba.unicodeart.gui.components.SelectCharComponent;
import kba.unicodeart.gui.window.TMEditorWindow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.Component;
import com.googlecode.lanterna.gui.TextGraphics;
import com.googlecode.lanterna.gui.component.InteractableComponent;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.PopupCheckBoxList;
import com.googlecode.lanterna.gui.layout.LinearLayout;
import com.googlecode.lanterna.gui.listener.ComponentAdapter;
import com.googlecode.lanterna.screen.ScreenCharacter;
import com.googlecode.lanterna.terminal.TextColor;

/**
 * @author kba
 *
 */
public class TMEditorToolPanel extends Panel {

	Logger log = LoggerFactory.getLogger(getClass().getName());
	
    final PopupCheckBoxList characterBoxBrushList;
    final PopupCheckBoxList layerBoxList;
    final PopupCheckBoxList characterPaletteList;
    final SelectCharComponent characterPaletteSelect;
    final SelectCharComponent fgPaletteList;
    final SelectCharComponent bgPaletteList;
    final Label modeLabel = new Label();

    public PopupCheckBoxList getCharacterBoxBrushList() { return characterBoxBrushList; }
    public PopupCheckBoxList getLayerBoxList() { return layerBoxList; }
    public SelectCharComponent getCharacterPaletteSelect() { return characterPaletteSelect; }
    public SelectCharComponent getFgPaletteList() { return fgPaletteList; }
    public SelectCharComponent getBgPaletteList() { return bgPaletteList; }
    public Label getModeLabel() { return modeLabel; }

	private TMEditor applicationState;
	
	public TMEditorToolPanel(TMEditorWindow mainWindow, Orientation orientation) {
		super(orientation);
		this.applicationState = mainWindow.getApplicationState(); 
		
		// Set Border
		this.setBorder(new Border.Standard());
		
		fgPaletteList = new SelectCharComponent(20, 5, true);
		bgPaletteList = new SelectCharComponent(20, 5, true);
		for (int i =0 ; i < applicationState.getCurrentMap().getPalette().size() ; i++) {
			TextColor textColor = TMLanternaAdapter.toTextColor(applicationState.getCurrentMap().getPalette().getColorByIndex(i));
			char ch = applicationState.getCurrentMap().getPalette().getCharByIndex(i);
			fgPaletteList.addChar(new ScreenCharacter(ch, textColor, TextColor.ANSI.BLACK));
			bgPaletteList.addChar(new ScreenCharacter(ch, TextColor.ANSI.BLACK, textColor));
		}
		fgPaletteList.addComponentListener(new ComponentAdapter() {
			@Override
			public void onComponentValueChanged(InteractableComponent component) {
				SelectCharComponent comp = SelectCharComponent.class.cast(component);
				applicationState.setForegroundColorIndex(comp.getSelectedIndex());
			}
		});
		bgPaletteList.addComponentListener(new ComponentAdapter() {
			@Override
			public void onComponentValueChanged(InteractableComponent component) {
				SelectCharComponent comp = SelectCharComponent.class.cast(component);
				applicationState.setBackgroundColorIndex(comp.getSelectedIndex());
			}
		});

		layerBoxList = new PopupCheckBoxList();
		layerBoxList.addComponentListener(new ComponentAdapter() {
			@Override
			public void onComponentValueChanged(InteractableComponent component) {
				TMEditFormatLayer curL = (TMEditFormatLayer) PopupCheckBoxList.class.cast(component).getCheckedItem();
				if (null != curL) applicationState.setCurrentLayer(curL);
			}
		});

		characterBoxBrushList  = new PopupCheckBoxList();
		characterBoxBrushList.addComponentListener(new ComponentAdapter() {
			@Override public void onComponentInvalidated(Component comp) {
				int checkedItemIndex = ((PopupCheckBoxList) comp).getCheckedItemIndex();
				if (checkedItemIndex > -1) applicationState.setCurrentCharacterBoxBrushIndex(checkedItemIndex);
			}
		});
		for (CharacterBoxBrush brush : applicationState.getCharacterBoxBrushList()) {
			this.characterBoxBrushList.addItem(brush.toString());
		}
		
		characterPaletteList = new PopupCheckBoxList();
		for (CharPalette palette : CharPalette.values()) {
			this.characterPaletteList.addItem(palette);
		}
		characterPaletteList.addComponentListener(new ComponentAdapter() {
			@Override
			public void onComponentValueChanged(InteractableComponent component) {
				PopupCheckBoxList characterPaletteListInstance = PopupCheckBoxList.class.cast(component);
				// TODO NPE
				applicationState.setCurrentCharPalette((CharPalette) characterPaletteListInstance.getCheckedItem());
				characterPaletteSelect.setChars(applicationState.getCurPalette().getChars());
			}
		});
		characterPaletteSelect = new SelectCharComponent(10, 2, false);
		characterPaletteSelect.addComponentListener(new ComponentAdapter() {
			@Override
			public void onComponentValueChanged(InteractableComponent component) {
				SelectCharComponent characterPaletteSelectInstance = SelectCharComponent.class.cast(component);
				// TODO this is bad, we need an index or something to properly wire it all together
				applicationState.setCurrentChar(characterPaletteSelectInstance.getSelectedScreenCharacter().getCharacter());
			}
		});

		final Panel editModePanel = new Panel(Panel.Orientation.HORIZONTAL);
		editModePanel.addComponent(new Label("Mode: "));
		editModePanel.addComponent(modeLabel);

		final Panel layerPanel = new Panel(Panel.Orientation.HORIZONTAL); layerPanel.addComponent(new Label("Layer: "));
		layerPanel.addComponent(layerBoxList);
//		layerBoxList.setCheckedItemIndex(applicationState.getCurrentLayerType().ordinal());
		this.layerBoxList.clearItems();
		for (TMEditFormatLayer layer : applicationState.getCurrentMap().getLayers()) {
			this.layerBoxList.addItem(layer);
		}	
		int indexByLayer = applicationState.getCurrentMap().getIndexByLayer(applicationState.getCurrentLayer());
		this.layerBoxList.setCheckedItemIndex(indexByLayer);


		final Panel brushPanel = new Panel(Panel.Orientation.HORIZONTAL);
		brushPanel.addComponent(new Label("Box Brush: "));
		brushPanel.addComponent(characterBoxBrushList);

		final Panel characterPaletteSelectPanel = new Panel(Panel.Orientation.HORIZONTAL);
		characterPaletteSelectPanel.addComponent(new Label("Current Palette: "));
		characterPaletteSelectPanel.addComponent(characterPaletteList);

		final Panel characterPaletteListPanel = new Panel(Panel.Orientation.HORIZONTAL);
		characterPaletteListPanel.addComponent(new Label("Choose: "));
		characterPaletteListPanel.addComponent(characterPaletteSelect);

		final Panel fgPalettePanel = new Panel(Panel.Orientation.HORIZONTAL);
		fgPalettePanel.addComponent(new Label("Foreground: "));
		fgPalettePanel.addComponent(fgPaletteList);

		final Panel bgPalettePanel = new Panel(Panel.Orientation.HORIZONTAL);
		bgPalettePanel.addComponent(new Label("Background: "));
		bgPalettePanel.addComponent(bgPaletteList);

		this.addComponent(editModePanel);
		this.addComponent(layerPanel, LinearLayout.GROWS_HORIZONTALLY);
		this.addComponent(brushPanel, LinearLayout.GROWS_HORIZONTALLY);
		this.addComponent(characterPaletteListPanel, LinearLayout.GROWS_HORIZONTALLY);
		this.addComponent(characterPaletteSelectPanel, LinearLayout.GROWS_HORIZONTALLY);
		this.addComponent(fgPalettePanel, LinearLayout.GROWS_HORIZONTALLY);
		this.addComponent(bgPalettePanel, LinearLayout.GROWS_HORIZONTALLY);
	}
	
	@Override
	public void repaint(TextGraphics graphics) {

		updateLayerList();
		updateModeLabel();
		updateCharacterBoxBrushList();
		updateCharacterPaletteList();

		super.repaint(graphics);
	}
	public void updateCharacterBoxBrushList() {
		this.characterBoxBrushList.setCheckedItemIndex(applicationState.getCurrentCharacterBoxBrushIndex());
	}
	public void updateCharacterPaletteList() {
		this.characterPaletteList.setCheckedItemIndex(applicationState.getCurPalette().ordinal());
	}
	public void updateModeLabel() {
		this.modeLabel.setText(applicationState.getCurrentMode().toString());
	}
	public void updateLayerList() {
		int indexByLayer = applicationState.getCurrentMap().getIndexByLayer(applicationState.getCurrentLayer());
		layerBoxList.clearItems();
		for (TMEditFormatLayer layer : applicationState.getCurrentMap().getLayers()) {
			layerBoxList.addItem(layer);
		};
		this.layerBoxList.setCheckedItemIndex(indexByLayer);
	}
	
	
	
}