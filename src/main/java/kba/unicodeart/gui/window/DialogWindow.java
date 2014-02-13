package kba.unicodeart.gui.window;

import com.googlecode.lanterna.gui.Action;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.Panel;
import com.googlecode.lanterna.gui.component.Panel.Orientation;
import com.googlecode.lanterna.gui.component.TextBox;
import com.googlecode.lanterna.gui.layout.LinearLayout;

public class DialogWindow extends Window {
	private TextBox textBox;
	private String value;
	private boolean ok = false;
	
	public String getValue() {
		return value;
	}
	
	public DialogWindow(String title, String preset) {
		super(title);
		textBox = new TextBox(preset, 30);
		final DialogWindow that = this;
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
				that.value = that.textBox.getText();
				that.close();
			}
		});
		Panel buttonPanel = new Panel("", Orientation.HORIZONTAL);
		buttonPanel.addComponent(cancelButton);
		buttonPanel.addComponent(okButton);
				
		this.addComponent(textBox, LinearLayout.GROWS_HORIZONTALLY);
		this.addComponent(buttonPanel, LinearLayout.GROWS_HORIZONTALLY);
	}

	public boolean ok() {
		return ok;
	}
	
}
