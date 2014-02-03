package kba.unicodeart.gui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.LinkedList;

import kba.unicodeart.CharPalette;
import kba.unicodeart.CharacterBoxBrush;
import kba.unicodeart.format.TMEditFormat;
import kba.unicodeart.format.TMEditFormatLayer;
import kba.unicodeart.gui.action.DrawAction;

import com.google.common.io.Resources;
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.DefaultBackgroundRenderer;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.terminal.TextColor;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import com.googlecode.lanterna.terminal.swing.TerminalAppearance;
import com.googlecode.lanterna.terminal.swing.TerminalPalette;


public class TMEditor implements Runnable{

	public static void main(String[] args) throws FontFormatException, IOException {
		TMEditor tmEditor = new TMEditor();
		tmEditor.run();
	}

	private Font mainFont;
	private Font[] fallbackFonts;
	private Font narrowFont;
	
	public TMEditor() throws FontFormatException, IOException {
		// TODO Auto-generated constructor stub
        Font unifontBase;
		mainFont= new Font("PragmataPro", Font.PLAIN, 20);
//		font= new Font("Monaco", Font.PLAIN, 20);
//		font = new Font("Monospaced", Font.PLAIN, 20);
//		font = new Font("Monospace", Font.PLAIN, 20);
//		Font cambria = new Font("Cambria", Font.PLAIN, 20);
//		Font mplusonem = new Font("m+ 1m", Font.PLAIN, 20);
//		Font pragmatapro = Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("PragmataPro.ttf").openStream());
//		font = new Font("DejaVu Sans Mono", Font.BOLD, 20);
		unifontBase = Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("fonts/unifont.ttf").openStream());
//        createFont = Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("Quivira.otf").openStream());
//        createFont = Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("Symbola.ttf").openStream());
//        createFont = Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("monaco.ttf").openStream());
//		font = createFont.deriveFont(Font.BOLD, 24);
		unifontBase = Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("fonts/unifont.ttf").openStream());
		Font arialBase = Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("fonts/arial.ttf").openStream());
		Font arial = arialBase.deriveFont(Font.BOLD, 20);
		Font unifont = unifontBase.deriveFont(Font.BOLD, 20);
		narrowFont = unifont.deriveFont(AffineTransform.getScaleInstance(.6, 1));
		narrowFont = narrowFont.deriveFont(Font.PLAIN, 20);
		fallbackFonts = new Font[2];
		fallbackFonts[0] = unifont;
		fallbackFonts[1] = arial;
	}
	
	
	public enum MapEditorMode {
		DIRECTIONAL_DRAW,
		CHAR_DRAW,
//		SHOW_LEGEND,
//		SELECT,
//		MULTISELECT,
		;
		public MapEditorMode getNext() { return values()[(ordinal()+1) % values().length]; }
	}
    // Application state
	private LinkedList<DrawAction> drawHistory = new LinkedList<>();
	public LinkedList<DrawAction> getDrawHistory() { return drawHistory; }
	private TMEditFormat currentMap = new TMEditFormat("test2", 20, 20);
	private MapEditorMode currentMode = MapEditorMode.DIRECTIONAL_DRAW;
	private CharacterBoxBrush currentCharacterBoxBrush = CharacterBoxBrush.ASCII_SIMPLE;
	public CharacterBoxBrush getCurrentCharacterBoxBrush() { return currentCharacterBoxBrush; }
	public void setCurrentCharacterBoxBrush(CharacterBoxBrush currentCharacterBoxBrush) { this.currentCharacterBoxBrush = currentCharacterBoxBrush; }
	private int curPaletteIndex = 0;
	private CharPalette currentCharPalette = CharPalette.CARD_SUITS;
	// TODO handle new map without layers
	private TMEditFormatLayer currentLayer = currentMap.getLayerIndex(0);
//	private String currentLayerName;
//	public String getCurrentLayerName() { return currentLayerName; }
//	public void setCurrentLayerName(String currentLayerType) { this.currentLayerName = currentLayerType; }
	public void setDrawHistory(LinkedList<DrawAction> drawHistory) { this.drawHistory = drawHistory; }
	public TMEditFormat getCurrentMap() { return currentMap; }
	public void cycleLayers() {
		TMEditFormatLayer nextLayer = this.currentMap.getNextLayer(this.currentLayer);
		if (null == nextLayer) {
			nextLayer = this.currentMap.getLayerIndex(0);
		}
		currentLayer = nextLayer;

	}
	public MapEditorMode getCurrentMode() { return currentMode; }
	public void cycleModes() { this.currentMode = this.currentMode.getNext(); }
	public CharacterBoxBrush getCurBrush() { return currentCharacterBoxBrush; }
	public void cycleBrushes() { currentCharacterBoxBrush = this.currentCharacterBoxBrush.getNext(); }
	public CharPalette getCurPalette() { return this.getCurrentCharPalette(); }
	public void cyclePalettes() { setCurrentCharPalette(this.getCurrentCharPalette().getNext()); }

	
	public void run() {
		SwingTerminal terminal = new SwingTerminal(
				TerminalAppearance.DEFAULT_APPEARANCE
				.withFont(mainFont)
				.withPalette(TerminalPalette.MAC_OS_X_TERMINAL_APP)
				.withUseBrightColors(false)
				.withUseAntiAliasing(true)
				.withWideFont(narrowFont)
				.withFallbackFonts(fallbackFonts)
				, 110, 30
				);
		final GUIScreen guiScreen = TerminalFacade.createGUIScreen(terminal);
		guiScreen.getScreen().startScreen();
//        ((SwingTerminal)guiScreen.getScreen().getTerminal()).getJFrame().setSize(800, 600);
//        ((SwingTerminal)guiScreen.getScreen().getTerminal()).getJFrame().setResizable(false);
//        guiScreen.getScreen().refresh();
        guiScreen.setBackgroundRenderer(new DefaultBackgroundRenderer("GUI Test"));
		guiScreen.setTheme(new KbaEditorTheme());
        final TMEditorWindow editorWindow = new TMEditorWindow(this);
        guiScreen.showWindow(editorWindow, GUIScreen.Position.FULL_SCREEN);
        guiScreen.getScreen().stopScreen();
    }
	public TMEditFormatLayer getCurrentLayer() {
		return currentLayer;
	}
	public char getCurPaletteChar() {
		return getCurPalette().getChars()[curPaletteIndex];
	}
	public void setCurrentLayer(TMEditFormatLayer layer) {
		this.currentLayer = layer;
	}

	// TODO wire to palette
	private TextColor currentBg = TextColor.ANSI.BLACK;
	public TextColor getCurrentBg() { return currentBg; }
	public void setCurrentBg(TextColor currentBg) { this.currentBg = currentBg; }

	// TODO wire to palette
	private TextColor currentFg = TextColor.ANSI.WHITE; 
	public TextColor getCurrentFg() { return currentFg; }
	public void setCurrentFg(TextColor currentFg) { this.currentFg = currentFg; }
	public CharPalette getCurrentCharPalette() {
		return currentCharPalette;
	}
	public void setCurrentCharPalette(CharPalette currentCharPalette) {
		this.currentCharPalette = currentCharPalette;
	}
}
    
