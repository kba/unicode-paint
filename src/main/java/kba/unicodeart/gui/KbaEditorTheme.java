package kba.unicodeart.gui;

import com.googlecode.lanterna.gui.Theme;
import com.googlecode.lanterna.terminal.TextColor;

public class KbaEditorTheme extends Theme {
    private static final Definition DEFAULT = new Definition(TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, false);
    private static final Definition SELECTED = new Definition(TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK, true);
	
	public KbaEditorTheme() {
        setDefinition(Category.DIALOG_AREA, DEFAULT);
        setDefinition(Category.SCREEN_BACKGROUND, new Definition(TextColor.ANSI.CYAN, TextColor.ANSI.BLUE, true));
        setDefinition(Category.SHADOW, new Definition(TextColor.ANSI.BLACK, TextColor.ANSI.BLACK, true));
        setDefinition(Category.BORDER, new Definition(TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, true));
        setDefinition(Category.RAISED_BORDER, new Definition(TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, true));
        setDefinition(Category.BUTTON_LABEL_ACTIVE, new Definition(TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK, true));
        setDefinition(Category.BUTTON_LABEL_INACTIVE, new Definition(TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, true));
        setDefinition(Category.BUTTON_ACTIVE, SELECTED);
        setDefinition(Category.BUTTON_INACTIVE, DEFAULT);
        setDefinition(Category.LIST_ITEM, DEFAULT);
        setDefinition(Category.LIST_ITEM_SELECTED, SELECTED);
        setDefinition(Category.CHECKBOX, DEFAULT);
        setDefinition(Category.CHECKBOX_SELECTED, SELECTED);
        setDefinition(Category.TEXTBOX, SELECTED);
        setDefinition(Category.TEXTBOX_FOCUSED, new Definition(TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK, true));
        setDefinition(Category.PROGRESS_BAR_COMPLETED, new Definition(TextColor.ANSI.GREEN, TextColor.ANSI.BLACK, false));
        setDefinition(Category.PROGRESS_BAR_REMAINING, new Definition(TextColor.ANSI.RED, TextColor.ANSI.BLACK, false));
	}

}
