package kba.unicodeart.gui.action;

import kba.unicodeart.gui.TMEditor;

/**
 * Action that draws a single character to the map
 *
 */
public class CharacterDrawAction extends DrawAction {
	
	@Override
	public void execute(TMEditor applicationState) {
		super.execute(applicationState);
		getLayer().set(this.getOldPos().getColumn(), this.getOldPos().getRow(), this.getDrawChar());
		this.setNewPos(getOldPos());
		applicationState.getDrawHistory().addFirst(this);
	}


}
