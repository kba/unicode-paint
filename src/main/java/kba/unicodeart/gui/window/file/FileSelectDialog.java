package kba.unicodeart.gui.window.file;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.EmptySpace;
import com.googlecode.lanterna.gui.component.InteractableComponent;
import com.googlecode.lanterna.gui.component.Label;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.Panel.Orientation;
import com.googlecode.lanterna.gui.component.PopupCheckBoxList;
import com.googlecode.lanterna.gui.component.TextBox;
import com.googlecode.lanterna.gui.layout.LinearLayout;
import com.googlecode.lanterna.gui.listener.ComponentAdapter;
import com.googlecode.lanterna.input.Key;

public class FileSelectDialog extends Window {
	
	private static final Logger log = LoggerFactory.getLogger(FileSelectDialog.class);
	
	private final boolean allowChoosingDirectories;
	private boolean isOk = false;
	private Path chosenPath;
	private final PopupCheckBoxList directorySelect = new PopupCheckBoxList();
	private final TextBox fileTextBox = new TextBox();
	private final FileListComponent fileListComponent;
	private final Button okButton;
	private final Button cancelButton;
	
	public FileSelectDialog(
			Builder builder)
	{
		super(builder.title);

		this.allowChoosingDirectories = builder.allowChoosingDirectories;

		fileListComponent = new FileListComponent(builder.currentDir,
				builder.showHiddenFiles, builder.showPrefixes, builder.directoriesFirst);

		fileListComponent.addComponentListener(new ComponentAdapter() {
			@Override
			public void onComponentValueChanged(
					InteractableComponent component)
			{
				if (component instanceof FileListComponent) {
					chosenPath = (Path) ((FileListComponent) component).getCheckedItem();
					if (Files.isDirectory(chosenPath)) {
						fileListComponent.setCurrentDir(chosenPath);
						repopulateDirectorySelect();
					} else {
						fileTextBox.setText(chosenPath.getFileName().toString());
						setFocus(fileTextBox);
					}
				}
			}
		});
		directorySelect.addComponentListener(new ComponentAdapter() {
			@Override
			public void onComponentValueChanged(
					InteractableComponent component)
			{
				if (component instanceof PopupCheckBoxList) {
					Path selectedDir = (Path) ((PopupCheckBoxList) component).getCheckedItem();
					fileListComponent.setCurrentDir(selectedDir);
					repopulateDirectorySelect();
				}
			}
		});
		fileTextBox.addComponentListener(new ComponentAdapter() {
			@Override
			public void onComponentValueChanged(
					InteractableComponent component)
			{
				setFocus(okButton);
			}
		});
		okButton = new Button("OK", new Action() {
			@Override
			public void doAction()
			{
				ok();
			}
		});
		cancelButton = new Button("Cancel", new Action() {
			@Override
			public void doAction()
			{
				cancel();
			}
		});
		repopulateDirectorySelect();

		Panel directorySelectPanel = new Panel(Orientation.HORIZONTAL);
		directorySelectPanel.addComponent(new Label("Directory: "));
		directorySelectPanel.addComponent(directorySelect, LinearLayout.GROWS_HORIZONTALLY);
		Panel fileInputPanel = new Panel(Orientation.HORIZONTAL);
		fileInputPanel.addComponent(new Label("File: "));
		fileInputPanel.addComponent(fileTextBox, LinearLayout.MAXIMIZES_HORIZONTALLY);
		Panel buttonPanel = new Panel(Orientation.HORIZONTAL);
		buttonPanel.addComponent(new EmptySpace(), LinearLayout.MAXIMIZES_HORIZONTALLY);
		buttonPanel.addComponent(cancelButton);
		buttonPanel.addComponent(okButton);
		this.addComponent(directorySelectPanel, LinearLayout.GROWS_HORIZONTALLY);
		this.addComponent(fileInputPanel, LinearLayout.GROWS_HORIZONTALLY);
		this.addComponent(fileListComponent, LinearLayout.MAXIMIZES_VERTICALLY, LinearLayout.GROWS_HORIZONTALLY);
		this.addComponent(buttonPanel, LinearLayout.GROWS_HORIZONTALLY);
		setFocus(fileListComponent);
	}
	public Path getChosenPath() {
		if (! isOk) return null;
		return chosenPath;
	}
	
	private void repopulateDirectorySelect() {
		directorySelect.clearItems();
		directorySelect.addItem(fileListComponent.getCurrentDir());
		Path parent = fileListComponent.getCurrentDir().getParent();
		while (null != parent) {
			directorySelect.addItem(parent);
			parent = parent.getParent();
		}
	}

	@Override
	public void onKeyPressed(
			Key key)
	{
		if (key.equalsString("<esc>", "q")) {
			cancel();
		}
		super.onKeyPressed(key);
	}
	
	private void cancel()
	{
		isOk = false;
		this.close();
	}
	
	private void ok()
	{
		isOk = true;
		chosenPath = Paths.get(fileListComponent.getCurrentDir().toString(), fileTextBox.getText());
		this.close();
	}

	public static class Builder {
		private final String title;
		private Path currentDir = Paths.get(System.getProperty("user.dir"));
		private SortedMap<String, Path> bookmarks = new TreeMap<>();
		private boolean allowChoosingDirectories = false;
		private boolean directoriesFirst = false;
		private boolean showHiddenFiles = true;
		private boolean showPrefixes = true;
		
		public Builder(String title)
		{
			this.title = title;
		}
		
		public Builder allowChoosingDirectories( boolean allowChoosingDirectories) {
			this.allowChoosingDirectories = allowChoosingDirectories;
			return this;
		}
		
		public Builder directoriesFirst( boolean directoriesFirst) {
			this.directoriesFirst = directoriesFirst;
			return this;
		}

		public Builder showHiddenFiles( boolean showHiddenFiles) {
			this.showHiddenFiles = showHiddenFiles;
			return this;
		}

		public Builder showPrefixes( boolean directoriesFirst) {
			this.showPrefixes = directoriesFirst;
			return this;
		}
		public Builder currentDir( Path currentDir) {
			this.currentDir = currentDir;
			return this;
		}
		public Builder currentDir(String currentDir) {
			this.currentDir = Paths.get(currentDir);
			return this;
		}
		
		
		public FileSelectDialog build() {
			return new FileSelectDialog(this);
		}
		
	}

	public static void main(String[] args) throws Exception {
		final GUIScreen guiScreen = TerminalFacade.createGUIScreen();

		final FileSelectDialog fileDialog = 
				new FileSelectDialog.Builder("Choose File")
						.allowChoosingDirectories(true)
						.directoriesFirst(true)
                        .build();
		final Window mainWindow = new Window("Main Window");
		final Label chosenFileLabel = new Label("NONE");
		mainWindow.addComponent(new Label("File: "));
		mainWindow.addComponent(chosenFileLabel);
		mainWindow.addComponent(new Button("Choose", new Action() {
			@Override
			public void doAction()
			{
				guiScreen.showWindow(fileDialog);
				if (null != fileDialog.getChosenPath()) {
					chosenFileLabel.setText(fileDialog.getChosenPath().toString());
				}
			}
		}));
		mainWindow.addComponent(new Button("Quit", new Action() {
			@Override
			public void doAction()
			{
				mainWindow.close();
			}
		}));


		guiScreen.getScreen().startScreen();
		guiScreen.showWindow(mainWindow);
		guiScreen.getScreen().stopScreen();
	}

}
