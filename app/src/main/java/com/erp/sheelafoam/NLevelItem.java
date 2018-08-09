package com.erp.sheelafoam;


import android.view.View;

public class NLevelItem implements NLevelListItem {
	
	private Object wrappedObject;
	private NLevelItem parent;
	private NLevelView nLevelView;
	private boolean isExpanded = false;
	private boolean SetExpanded = false;
	
	public NLevelItem(Object wrappedObject, NLevelItem parent, NLevelView nLevelView) {
		this.wrappedObject = wrappedObject;
		this.parent = parent;
		this.nLevelView = nLevelView;
	}
	
	public Object getWrappedObject() {
		return wrappedObject;
	}
// For Dealer type user, set nav drawer parent menu item Expand
	@Override
	public boolean setExpand() {
		return SetExpanded;
	}

	@Override
	//return of isExpand() can be change in future if any other menu also have child menu, currently Business Process has child menu
	public boolean isExpanded() {
		return isExpanded;
	}
	@Override
	public NLevelListItem getParent() {
		return parent;
	}
	@Override
	public View getView() {
		return nLevelView.getView(this);
	}
	@Override
	public void toggle() {
		isExpanded = !isExpanded;
	}
}
