package kba.gui.editor;

import java.util.LinkedList;

import kba.gui.editor.TMEditFormat.LayerType;

import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Panel.Orientation;

class TMEditorWindow extends Window {
	public TMEditorWindow(String title) {
		super(title);
		setUpComponents();
	}
	void setUpComponents() {
	}

	public enum MapEditorMode {
		DIRECTIONAL_DRAW,
		PIXEL_DRAW,
		SHOW_LEGEND,
		//		SELECT,
		//		MULTISELECT,
		;
		public TMEditorWindow.MapEditorMode getNext() { return values()[(ordinal()+1) % values().length]; }
	}

	// GUI components
    final TMEditorToolPanel toolPanel = new TMEditorToolPanel(this, Orientation.VERTICAL);

    // Application state
	private LinkedList<DrawAction> drawHistory = new LinkedList<>();
	public LinkedList<DrawAction> getDrawHistory() { return drawHistory; }
	private TMEditFormat currentMap = new TMEditFormat("test2", 40, 20);
	private LayerType currentLayerType = LayerType.ASCII_CHAR;
	private TMEditorWindow.MapEditorMode currentMode = MapEditorMode.DIRECTIONAL_DRAW;
	private CharacterBoxBrush curBrush = CharacterBoxBrush.ASCII_SIMPLE;
	private CharPalette curPal = CharPalette.CARD_SUITS;
	
	public void setDrawHistory(LinkedList<DrawAction> drawHistory) { this.drawHistory = drawHistory; }
	public TMEditFormat getCurrentMap() { return currentMap; }
	public LayerType getCurrentLayerType() { return currentLayerType; }
	
	public TMEditFormatLayer getCurrentMapLayer() { return getCurrentMap().getLayer(currentLayerType); }
	public void cycleLayers() { this.currentLayerType = this.currentLayerType.getNext(); }
	public TMEditorWindow.MapEditorMode getCurrentMode() { return currentMode; }
	public void cycleModes() { this.currentMode = this.currentMode.getNext(); }
	public CharacterBoxBrush getCurBrush() { return curBrush; }
	public void cycleBrushes() { curBrush = this.curBrush.getNext(); }
	public CharPalette getCurPalette() { return this.curPal; }
	public void cyclePalettes() { curPal = this.curPal.getNext(); }

	
}