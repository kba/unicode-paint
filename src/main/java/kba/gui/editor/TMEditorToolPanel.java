package kba.gui.editor;

import kba.gui.editor.TMEditFormat.LayerType;

import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.TextGraphics;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.RadioCheckBoxList;
import com.googlecode.lanterna.gui.layout.LinearLayout;

class TMEditorToolPanel extends Panel {
	
	private TMEditorWindow editorWindow;

    final RadioCheckBoxList characterBoxBrushList = new RadioCheckBoxList();
    final RadioCheckBoxList layerBoxList = new RadioCheckBoxList();
    final RadioCheckBoxList characterPaletteList = new RadioCheckBoxList();
    final Label modeLabel = new Label();

	public TMEditorToolPanel(TMEditorWindow editorWindow, Orientation orientation) {
		super(orientation);
		this.editorWindow = editorWindow;

		for (LayerType layer : LayerType.values()) {
			this.layerBoxList.addItem(layer);
		}
		for (CharacterBoxBrush brush : CharacterBoxBrush.values()) {
			this.characterBoxBrushList.addItem(brush.toString());
		}
		for (CharPalette palette : CharPalette.values()) {
			this.characterPaletteList.addItem(palette.toString());
		}

		this.setBorder(new Border.Standard());
		final Panel editModePanel = new Panel(Panel.Orientation.HORIZONTAL);
		editModePanel.addComponent(new Label("Mode: "));
		editModePanel.addComponent(modeLabel);

		final Panel layerPanel = new Panel(Panel.Orientation.HORIZONTAL);
		layerPanel.addComponent(new Label("Layer: "));
		layerPanel.addComponent(layerBoxList);

		final Panel brushPanel = new Panel(Panel.Orientation.HORIZONTAL);
		brushPanel.addComponent(new Label("Box Brush: "));
		brushPanel.addComponent(characterBoxBrushList);

		final Panel palettePanel = new Panel(Panel.Orientation.HORIZONTAL);
		palettePanel.addComponent(new Label("Box Brush: "));
		palettePanel.addComponent(characterPaletteList);

		this.addComponent(editModePanel);
		this.addComponent(layerPanel, LinearLayout.GROWS_HORIZONTALLY);
		this.addComponent(brushPanel, LinearLayout.GROWS_HORIZONTALLY);
		this.addComponent(palettePanel, LinearLayout.GROWS_HORIZONTALLY);
	}
	
	@Override
	public void repaint(TextGraphics graphics) {
		this.layerBoxList.setCheckedItemIndex(editorWindow.getCurrentLayerType().ordinal());
		this.modeLabel.setText(editorWindow.getCurrentMode().toString());
		this.characterBoxBrushList.setCheckedItemIndex(editorWindow.getCurBrush().ordinal());
		this.characterPaletteList.setCheckedItemIndex(editorWindow.getCurPalette().ordinal());
		super.repaint(graphics);
	}
	
}