package net.trevize.gui.cbtree;

import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * CBCellRenderer.java - Jun 11, 2009
 */
public class CBTreeCellRenderer extends JPanel implements TreeCellRenderer {

	private CBCellPanel cbcp;

	private CBNodeData current;

	public CBTreeCellRenderer() {
		super();

		cbcp = new CBCellPanel();
	}

	/***************************************************************************
	 * implementation of TreeCellRenderer
	 **************************************************************************/

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expand, boolean leaf, int row,
			boolean hasfocus) {

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		current = (CBNodeData) node.getUserObject();

		cbcp.getLabel().setText(current.getName());

		if (node == tree.getModel().getRoot()) {
			cbcp.displayCheckbox(false);
		} else {
			cbcp.displayCheckbox(true);
			cbcp.getCb().setSelected(current.isSelected());
		}

		return cbcp;

	}

}