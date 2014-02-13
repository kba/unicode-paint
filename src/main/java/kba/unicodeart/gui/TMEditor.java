
package kba.unicodeart.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import kba.unicodeart.CharPalette;
import kba.unicodeart.CharacterBoxBrush;
import kba.unicodeart.format.TMEditFormat;
import kba.unicodeart.format.TMEditFormatLayer;
import kba.unicodeart.format.TMLanternaAdapter;
import kba.unicodeart.format.colored_char.TMColoredCharacterFactory;
import kba.unicodeart.gui.action.DrawAction;
import kba.unicodeart.gui.window.TMEditorWindow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Resources;
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.DefaultBackgroundRenderer;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.terminal.TextColor;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import com.googlecode.lanterna.terminal.swing.TerminalAppearance;
import com.googlecode.lanterna.terminal.swing.TerminalPalette;

/**
 * The application state of the editor.
 * 
 * @author kba
 * 
 */
public class TMEditor implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(TMEditor.class);

	public static void main(String[] args) throws FontFormatException, IOException {
		TMEditor tmEditor = new TMEditor();
		tmEditor.run();
	}

	private List<CharacterBoxBrush> characterBoxBrushList = new ArrayList<>();
	// Fonts
	private Font mainFont;
	private Font[] fallbackFonts;
	private Font narrowFont;

	// Application state
	private String fileName;
	private LinkedList<DrawAction> drawHistory = new LinkedList<>();
	private TMEditFormat currentMap;
	private TMColoredCharacterFactory colorCharFactory;
	private TMEditFormatLayer currentLayer;
	private MapEditorMode currentMode = MapEditorMode.CHAR_DRAW;
	private CharPalette currentCharPalette = CharPalette.CARD_SUITS; 	// TODO a enum isn't flexible enough
	private char currentChar;

	private int backgroundColorIndex;
	private int foregroundColorIndex;
	private boolean transparent = false;
	private int currentCharacterBoxBrushIndex;

	public TMEditor() throws FontFormatException, IOException {
		setupFonts();
		characterBoxBrushList.add(CharacterBoxBrush.BOX_ROUND);
		characterBoxBrushList.add(CharacterBoxBrush.BOX_LIGHT);
		characterBoxBrushList.add(CharacterBoxBrush.BOX_FAT);
		characterBoxBrushList.add(CharacterBoxBrush.BOX_DOUBLE);
		characterBoxBrushList.add(CharacterBoxBrush.ASCII_SIMPLE);
		currentCharacterBoxBrushIndex = 0;
		currentChar = currentCharPalette.toValueString().charAt(0);
	}

	public void setupFonts() throws FontFormatException, IOException {
		// TODO Auto-generated constructor stub
		Font unifontBase;
		mainFont = new Font("PragmataPro", Font.PLAIN, 20);
		// font= new Font("Monaco", Font.PLAIN, 20);
		// font = new Font("Monospaced", Font.PLAIN, 20);
		// font = new Font("Monospace", Font.PLAIN, 20);
		// Font cambria = new Font("Cambria", Font.PLAIN, 20);
		// Font mplusonem = new Font("m+ 1m", Font.PLAIN, 20);
		// Font pragmatapro = Font.createFont(Font.TRUETYPE_FONT,
		// Resources.getResource("PragmataPro.ttf").openStream());
		// font = new Font("DejaVu Sans Mono", Font.BOLD, 20);
		unifontBase = Font.createFont(Font.TRUETYPE_FONT, Resources
				.getResource("fonts/unifont.ttf").openStream());
		// createFont = Font.createFont(Font.TRUETYPE_FONT,
		// Resources.getResource("Quivira.otf").openStream());
		// createFont = Font.createFont(Font.TRUETYPE_FONT,
		// Resources.getResource("Symbola.ttf").openStream());
		// createFont = Font.createFont(Font.TRUETYPE_FONT,
		// Resources.getResource("monaco.ttf").openStream());
		// font = createFont.deriveFont(Font.BOLD, 24);
		unifontBase = Font.createFont(Font.TRUETYPE_FONT, Resources
				.getResource("fonts/unifont.ttf").openStream());
		Font arialBase = Font.createFont(Font.TRUETYPE_FONT,
				Resources.getResource("fonts/arial.ttf").openStream());
		Font arial = arialBase.deriveFont(Font.BOLD, 20);
		Font unifont = unifontBase.deriveFont(Font.BOLD, 20);
		narrowFont = unifont.deriveFont(AffineTransform.getScaleInstance(.6, 1));
		narrowFont = narrowFont.deriveFont(Font.PLAIN, 20);
		fallbackFonts = new Font[2];
		fallbackFonts[0] = unifont;
		fallbackFonts[1] = arial;
	}

	public enum MapEditorMode {
		DIRECTIONAL_DRAW, CHAR_DRAW,
		// SHOW_LEGEND,
		// SELECT,
		// MULTISELECT,
		;
		public MapEditorMode getNext() {
			return values()[(ordinal() + 1) % values().length];
		}
	}

	public CharacterBoxBrush getCurrentCharacterBoxBrush() {
		return getCharacterBoxBrushList().get(currentCharacterBoxBrushIndex);
	}

	public TMColoredCharacterFactory getColorCharFactory() {
		return colorCharFactory;
	}


	public LinkedList<DrawAction> getDrawHistory() { return drawHistory; }

	public void setDrawHistory(LinkedList<DrawAction> drawHistory) {
		this.drawHistory = drawHistory;
	}

	public TMEditFormat getCurrentMap() { return currentMap; }

	public MapEditorMode getCurrentMode() { return currentMode; }

	public CharPalette getCurPalette() { return this.getCurrentCharPalette(); }

	/**
	 * @return the current map layer
	 */
	public TMEditFormatLayer getCurrentLayer() { return currentLayer; }

	/**
	 * @param layer
	 *            the current map layer
	 */
	public void setCurrentLayer(TMEditFormatLayer layer) { this.currentLayer = layer; }

	/**
	 * @param currentChar
	 *            the currenly selected char
	 */
	public void setCurrentChar(char currentChar) { this.currentChar = currentChar; }

	/**
	 * @return the currently selected char
	 */
	public char getCurrentChar() { return currentChar; }

	/**
	 * @return Index of currently selected background color TODO wire to palette
	 */
	public int getBackgroundColorIndex() { return backgroundColorIndex; }

	/**
	 * @param backgroundColorIndex
	 *            the index of the currently selected background color
	 */
	public void setBackgroundColorIndex(int backgroundColorIndex) { this.backgroundColorIndex = backgroundColorIndex; }

	public void setBackgroundColorIndex(TextColor textColor) {
		Color awtColor = TMLanternaAdapter.toAwtColor(textColor, getCurrentMap().getPalette());
		setBackgroundColorIndex(getCurrentMap().getPalette().getIndexByColor(awtColor));
	}

	/**
	 * @return the index of currently selected foreground color
	 */
	public int getForegroundColorIndex() { return foregroundColorIndex; }

	/**
	 * @param foregroundColorIndex
	 *            the index of the currently selected foreground color
	 */
	public void setForegroundColorIndex(int foregroundColorIndex) { this.foregroundColorIndex = foregroundColorIndex; }

	public CharPalette getCurrentCharPalette() { return currentCharPalette; }

	public void setCurrentCharPalette(CharPalette currentCharPalette) { this.currentCharPalette = currentCharPalette; }

	public void setTransparent(boolean yesno) { this.transparent = yesno; }

	public boolean isTransparent() { return transparent; }

	public void toggleTransparent() { transparent = !transparent; }

	public int getCurrentCharacterBoxBrushIndex() { return currentCharacterBoxBrushIndex; }

	public void setCurrentCharacterBoxBrushIndex(int currentCharacterBoxBrushIndex) { this.currentCharacterBoxBrushIndex = currentCharacterBoxBrushIndex; }

	public List<CharacterBoxBrush> getCharacterBoxBrushList() { return characterBoxBrushList; }

	public void setCharacterBoxBrushList(List<CharacterBoxBrush> characterBoxBrushList) { this.characterBoxBrushList = characterBoxBrushList; }

	public void setCurrentMap(TMEditFormat currentMap) { this.currentMap = currentMap; }


	/**
	 * @return the file name of the current map
	 */
	public String getFileName() { return fileName; }


	/**
	 * @param fileName
	 *            the file name of the current map
	 */
	public void setFileName(String fileName) { this.fileName = fileName; }
	public void cycleLayers() {
		TMEditFormatLayer nextLayer = this.getCurrentMap().getNextLayer(this.currentLayer);
		if (null == nextLayer) {
			nextLayer = this.getCurrentMap().getLayerIndex(0);
		}
		currentLayer = nextLayer;

	}

	public void cycleModes() {
		this.currentMode = this.currentMode.getNext();
	}

	public void cycleBrushes() {
		if (++currentCharacterBoxBrushIndex == getCharacterBoxBrushList().size()) {
			currentCharacterBoxBrushIndex = 0;
		}
	}

	public void cyclePalettes() {
		setCurrentCharPalette(this.getCurrentCharPalette().getNext());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		final GUIScreen guiScreen = setupTerminal();

		// Load the map
		loadMap();

		final TMEditorWindow editorWindow = new TMEditorWindow(this);
		guiScreen.showWindow(editorWindow, GUIScreen.Position.FULL_SCREEN);
		guiScreen.getScreen().stopScreen();
	}

	public GUIScreen setupTerminal() {
		// @noformat
		SwingTerminal terminal = new SwingTerminal(TerminalAppearance.DEFAULT_APPEARANCE
				.withFont(mainFont)
				.withPalette(TerminalPalette.MAC_OS_X_TERMINAL_APP)
				.withUseBrightColors(false)
				.withUseAntiAliasing(true)
				.withWideFont(narrowFont)
				.withFallbackFonts(fallbackFonts),
				110,
				30);
		// @format
		final GUIScreen guiScreen = TerminalFacade.createGUIScreen(terminal);
		guiScreen.getScreen().startScreen();
		// ((SwingTerminal)guiScreen.getScreen().getTerminal()).getJFrame().setSize(800,
		// 600);
		// ((SwingTerminal)guiScreen.getScreen().getTerminal()).getJFrame().setResizable(false);
		// guiScreen.getScreen().refresh();
		guiScreen.setBackgroundRenderer(new DefaultBackgroundRenderer("GUI Test"));
		guiScreen.setTheme(new TMLanternaGuiTheme());
		return guiScreen;
	}

	/**
	 * Load a new map
	 */
	public void loadMap() {
		TMEditFormat map = new TMEditFormat("test2", 40, 20);
		this.setFileName("/tmp/newTMMap_" + System.currentTimeMillis() + ".xml");
		loadMap(map);
	}

	public void loadMap(TMEditFormat map) {
		setCurrentMap(map);
		colorCharFactory = new TMColoredCharacterFactory(getCurrentMap().getPalette());
		setCurrentLayer(getCurrentMap().getLayerIndex(0));
		setForegroundColorIndex(7);
		setBackgroundColorIndex(0);
	}

	public void saveMap() {
		try {
			long t0 = System.nanoTime();
			File tmpFile = new File(getFileName());
			FileOutputStream fw = new FileOutputStream(tmpFile);
			XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(fw);
			getCurrentMap().writeXML(writer);
			fw.close();
			log.debug("Saving map to {} took {} ms", fileName, ((System.nanoTime() - t0) / 1000000));
		} catch (IOException | XMLStreamException e) {
			e.printStackTrace();
		}
	}

	public void addLayer(String layerName) {
		getCurrentMap().addLayer(layerName);
		setCurrentLayer(getCurrentMap().getLayerByName(layerName));
	}
}
