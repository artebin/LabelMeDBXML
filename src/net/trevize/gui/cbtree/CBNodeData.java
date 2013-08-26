package net.trevize.gui.cbtree;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * CBNodeData.java - Jun 26, 2009
 */

public class CBNodeData {

	private String name;

	private boolean selected;

	public CBNodeData(String name, boolean selected) {
		this.name = name;
		this.selected = selected;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
