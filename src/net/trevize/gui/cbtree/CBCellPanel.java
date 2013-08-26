package net.trevize.gui.cbtree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * CBCellPanel.java - Jun 26, 2009
 */

public class CBCellPanel extends JPanel {

	public static final String ACTION_CBCELLPANEL_CHECKBOX = "ACTION_CBCELLPANEL_CHECKBOX";

	private JCheckBox cb;

	private boolean cbdisplayed;

	private JLabel label;

	private Component box;

	public CBCellPanel() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);

		cb = new JCheckBox();
		cb.setOpaque(true);
		cb.setBackground(Color.WHITE);
		cb.setRolloverEnabled(false);
		cb.setFocusPainted(false);
		cb.setFocusable(false);
		cbdisplayed = true;
		add(cb, BorderLayout.WEST);
		cb.setActionCommand(ACTION_CBCELLPANEL_CHECKBOX);

		label = new JLabel();
		label.setOpaque(true);
		label.setBackground(Color.WHITE);
		add(label, BorderLayout.CENTER);

		box = Box.createHorizontalStrut(3);
		add(box, BorderLayout.EAST);
	}

	public void displayCheckbox(boolean displaycb) {
		if (displaycb && !cbdisplayed) {
			add(cb, BorderLayout.WEST);
			cbdisplayed = true;
		} else if (!displaycb && cbdisplayed) {
			remove(cb);
			cbdisplayed = false;
		}
	}

	public void setSelected(boolean selected) {
		if (selected) {
			cb.setBackground(UIManager.getColor("Tree.selectionBackground"));
			cb.setForeground(UIManager.getColor("Tree.selectionForeground"));
			label.setBackground(UIManager.getColor("Tree.selectionBackground"));
			label.setForeground(UIManager.getColor("Tree.selectionForeground"));
			setBackground(UIManager.getColor("Tree.selectionBackground"));
		} else {
			cb.setBackground(Color.WHITE);
			cb.setForeground(Color.BLACK);
			label.setBackground(Color.WHITE);
			label.setForeground(Color.BLACK);
			setBackground(Color.WHITE);
		}
	}

	public JCheckBox getCb() {
		return cb;
	}

	public void setCb(JCheckBox cb) {
		this.cb = cb;
	}

	public JLabel getLabel() {
		return label;
	}

	public void setLabel(JLabel label) {
		this.label = label;
	}

}
