package kba.unicodeart.gui.window;

import kba.unicodeart.gui.TMEditor;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.CheckBox;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.Panel.Orientation;
import com.googlecode.lanterna.gui.component.TextBox;
import com.googlecode.lanterna.gui.layout.LinearLayout;

/**
 * Dialog to fill an area
 *
 */
public class FillDialog extends Window {
	public boolean ok = false;
	
	/**
	 * The character to use for filling
	 */
	public char fillChar;
	/**
	 * Whether to use the #fillChar char to use for filling, defaults to <code>true</code>
	 */
	public boolean doFillChar = true;
	/**
	 * Whether to use the #fillChar foreground color to use for filling, defaults to <code>true</code>
	 */
	public boolean doFillFg = true;
	/**
	 * Whether to use the #fillChar foreground color to use for filling, defaults to <code>false</code>
	 */
	public boolean doFillBg = false;

	public FillDialog(TMEditor applicationState) {
		super("Fill options");
		
		final TextBox fillCharTextBox = new TextBox(String.valueOf(applicationState.getCurrentChar()), 1);
		final CheckBox doFillCharCheckbox = new CheckBox("Fill char", doFillChar);
		final CheckBox doFillFgCheckbox = new CheckBox("Fill Fg", doFillFg);
		final CheckBox doFillBgCheckbox = new CheckBox("Fill Bg", doFillBg);
		this.addComponent(fillCharTextBox);
		this.addComponent(doFillCharCheckbox);
		this.addComponent(doFillFgCheckbox);
		this.addComponent(doFillBgCheckbox);

		final FillDialog that = this;
		Button cancelButton = new Button("Cancel", new Action() {
			@Override
			public void doAction() {
				that.close();
			}
		});
		Button okButton = new Button("OK", new Action() {
			@Override
			public void doAction() {
				that.ok = true;
				that.fillChar = fillCharTextBox.getText().charAt(0);
				that.doFillChar = doFillCharCheckbox.isSelected();
				that.doFillBg = doFillBgCheckbox.isSelected();
				that.doFillFg = doFillFgCheckbox.isSelected();
				that.close();
			}
		});
		Panel buttonPanel = new Panel("", Orientation.HORIZONTAL);
		buttonPanel.addComponent(cancelButton);
		buttonPanel.addComponent(okButton);
		this.addComponent(buttonPanel, LinearLayout.GROWS_HORIZONTALLY);
	}

}
