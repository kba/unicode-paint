package kba.gui.editor;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import com.google.common.io.Resources;
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.Border;
import com.googlecode.lanterna.gui.DefaultBackgroundRenderer;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.Panel.Orientation;
import com.googlecode.lanterna.gui.layout.LinearLayout;
import com.googlecode.lanterna.gui.layout.VerticalLayout;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import com.googlecode.lanterna.terminal.swing.TerminalAppearance;
import com.googlecode.lanterna.terminal.swing.TerminalPalette;


public class TMEditor{
	
	public static void main(String[] args) throws FontFormatException, IOException {
		Font font;
        Font unifontBase;
		font= new Font("PragmataPro", Font.PLAIN, 20);
//		font= new Font("Monaco", Font.PLAIN, 20);
//		font = new Font("Monospaced", Font.PLAIN, 20);
//		font = new Font("Monospace", Font.PLAIN, 20);
//		Font cambria = new Font("Cambria", Font.PLAIN, 20);
		Font mplusonem = new Font("m+ 1m", Font.PLAIN, 20);
		Font pragmatapro = Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("PragmataPro.ttf").openStream());
//		font = new Font("DejaVu Sans Mono", Font.BOLD, 20);
		unifontBase = Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("unifont.ttf").openStream());
//        createFont = Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("Quivira.otf").openStream());
//        createFont = Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("Symbola.ttf").openStream());
//        createFont = Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("monaco.ttf").openStream());
//		font = createFont.deriveFont(Font.BOLD, 24);
		unifontBase = Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("unifont.ttf").openStream());
		Font arialBase = Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("arial.ttf").openStream());
		Font arial = arialBase.deriveFont(Font.BOLD, 20);
		Font unifont = unifontBase.deriveFont(Font.BOLD, 20);
		Font unifontHalfWidth = unifont.deriveFont(AffineTransform.getScaleInstance(.6, 1));
		unifontHalfWidth = unifontHalfWidth.deriveFont(Font.PLAIN, 20);
		SwingTerminal terminal = new SwingTerminal(
				TerminalAppearance.DEFAULT_APPEARANCE
				.withFont(font)
				.withPalette(TerminalPalette.MAC_OS_X_TERMINAL_APP)
				.withUseBrightColors(false)
				.withUseAntiAliasing(true)
				.withWideFont(unifontHalfWidth)
				.withFallbackFonts(unifont, arial)
				, 110, 30
				);
		final GUIScreen guiScreen = TerminalFacade.createGUIScreen(terminal);
		guiScreen.getScreen().startScreen();
//        ((SwingTerminal)guiScreen.getScreen().getTerminal()).getJFrame().setSize(800, 600);
//        ((SwingTerminal)guiScreen.getScreen().getTerminal()).getJFrame().setResizable(false);
//        guiScreen.getScreen().refresh();
        guiScreen.setBackgroundRenderer(new DefaultBackgroundRenderer("GUI Test"));
		guiScreen.setTheme(new KbaEditorTheme());

        final TMEditorWindow editorWindow = new TMEditorWindow("Window with BorderLayout");
        editorWindow.setBorder(new Border.Invisible());
        final Panel mainPanel = new Panel("BorderLayout", Orientation.VERTICAL);
        mainPanel.setBorder(new Border.Invisible());
        mainPanel.setLayoutManager(new VerticalLayout());

        Panel topPanel = new Panel(Orientation.HORIZONTAL);

        final Panel statusPanel = new Panel(Panel.Orientation.HORIZONTAL);
        statusPanel.setBorder(new Border.Standard());

        final TMMapEditorComponent mapEditComponent = new TMMapEditorComponent(editorWindow);

        Panel mapEditComponentPanel = new Panel();
        mapEditComponentPanel.addComponent(mapEditComponent);
		mapEditComponentPanel.setBorder(new Border.Standard());
		topPanel.addComponent(mapEditComponentPanel, LinearLayout.GROWS_HORIZONTALLY);

        topPanel.addComponent(editorWindow.toolPanel, LinearLayout.GROWS_HORIZONTALLY);

        mainPanel.addComponent(topPanel, LinearLayout.GROWS_HORIZONTALLY);
        editorWindow.addComponent(mainPanel, LinearLayout.MAXIMIZES_HORIZONTALLY, LinearLayout.MAXIMIZES_VERTICALLY);
        editorWindow.setFocus(mapEditComponent);

        guiScreen.showWindow(editorWindow, GUIScreen.Position.FULL_SCREEN);
        guiScreen.getScreen().stopScreen();
    }
}
    
