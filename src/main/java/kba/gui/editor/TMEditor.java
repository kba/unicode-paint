package kba.gui.editor;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.net.URL;

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
		font= new Font("PragmataPro", Font.PLAIN, 20);
//		font = new Font("Monospaced", Font.BOLD, 20);
//		font = new Font("DejaVu Sans Mono", Font.BOLD, 20);
        Font createFont = Font.createFont(Font.TRUETYPE_FONT, Resources.getResource("unifont.ttf").openStream());
//		font = createFont.deriveFont(Font.BOLD, 20);
		SwingTerminal terminal = new SwingTerminal(
				TerminalAppearance.DEFAULT_APPEARANCE
				.withFont(font)
				.withPalette(TerminalPalette.MAC_OS_X_TERMINAL_APP)
				.withUseBrightColors(false)
				.withUseAntiAliasing(true),
				110, 30
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
    
