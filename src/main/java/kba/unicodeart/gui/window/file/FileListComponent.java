package kba.unicodeart.gui.window.file;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.lanterna.gui.TextGraphics;
import com.googlecode.lanterna.gui.Theme;
import com.googlecode.lanterna.gui.Theme.Category;
import com.googlecode.lanterna.gui.Theme.Definition;
import com.googlecode.lanterna.gui.component.RadioCheckBoxList;

public class FileListComponent extends RadioCheckBoxList {

	private static final Logger log = LoggerFactory.getLogger(FileListComponent.class);

	private final boolean showPrefixes;
	private final boolean showHiddenFiles;
	private final boolean directoriesFirst;

	private Path currentDir;


	public FileListComponent(
			Path currentDir,
			boolean showHiddenFiles,
			boolean showPrefixes,
			boolean directoriesFirst)
	{
		this.currentDir = currentDir;
		this.showHiddenFiles = showHiddenFiles;
		this.showPrefixes = showPrefixes;
		this.directoriesFirst = directoriesFirst;
		setCurrentDir(currentDir);
	}

	@Override
	public void clearItems()
	{
		// TODO Auto-generated method stub
		super.clearItems();
	}

	public void setCurrentDir(
			Path currentDir)
	{
		if (null == currentDir) {
			return;
		}
		if (!Files.isDirectory(currentDir)) {
			throw new IllegalArgumentException("currentDir must be a directory.");
		}

		clearItems();
		if (null != currentDir.getParent()) {
			addItem(currentDir.getParent());
		}
		this.currentDir = currentDir;
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(currentDir)) {
			if (! directoriesFirst) {
				for (Path path : directoryStream) {
					if (Files.isHidden(path) && !showHiddenFiles) {
						continue;
					}
					addItem(path);
				}
			} else {
				List<Path> dirs = new ArrayList<>();
				List<Path> files = new ArrayList<>();
				for (Path path : directoryStream) {
					if (Files.isHidden(path) && !showHiddenFiles) {
						continue;
					} else if (Files.isDirectory(path)) {
						dirs.add(path);
					} else {
						files.add(path);
					}
				}
				dirs.addAll(files);
				for (Path path : dirs) {
					addItem(path);
				}
			}
		} catch (IOException ex) {
			log.error("IOException!", ex);
		}
		invalidate();
	}

	public Path getCurrentDir()
	{
		return currentDir;
	}

	@Override
    protected void printItem(TextGraphics graphics, int x, int y, int index) {
		Path path = (Path) getItemAt(index);
		Path relativePath = currentDir.relativize(path);
		String asText = relativePath.toString();
		if (showPrefixes)
			asText = getPrefix(path) + asText;
        if(asText.length() > graphics.getWidth())
            asText = asText.substring(0, graphics.getWidth());
//        for (int fillX = 0; fillX < graphics.getWidth() ; fillX ++) {
//        	graphics.drawString(fillX, y, " ");
//        }
        graphics.drawString(x, y, asText);
    }

	private char getPrefix(
			Path path)
	{
		char prefix;
		if (Files.isDirectory(path)) {
			prefix = '/';
		} else if (Files.isSymbolicLink(path)) {
			prefix = '@';
		} else {
			prefix = ' ';
		}
		return prefix;
	}
	
	@Override
	protected Definition getListItemThemeDefinition(
			Theme theme,
			int index)
	{
		if (index > 0 ) {
			Path pathAtIndex = (Path)getItemAt(index);
			if (Files.isDirectory(pathAtIndex)) {
				return theme.getDefinition(Category.FILE_LIST_DIRECTORY);
			} else if (Files.isSymbolicLink(pathAtIndex)) {
				return theme.getDefinition(Category.FILE_LIST_SYMLINK);
			} else {
				return theme.getDefinition(Category.FILE_LIST_NORMAL_FILE);
			}
		} 
		return super.getListItemThemeDefinition(theme, index);
	}

}