package net.trevize.gui.cbtree;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

/**
 * This class provides an extension of a JTree where the cell renderer is a
 * CBTreeCellRenderer and the cell editor is a CBTreeCellEditor. These classes 
 * add a checkbox to the cell.
 * 
 * Contextual menus are available on the nodes.
 * To display the appropriate contextual menu considering nodes, you can use
 * either the level of the node either different derivations of CBNodeData 
 * and instanceof. 
 * 
 * @author nicolas james <nicolas.james@gmail.com> 06.06.13
 */

public class CBTree extends JTree {

	public CBTree() {
		super();
		getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		setDragEnabled(false);
	}

	/**
	 * select or unselect a branch.
	 * @param n
	 * @param value
	 */
	public void setBranchSelected(DefaultMutableTreeNode n, boolean value) {

		DefaultTreeModel model = (DefaultTreeModel) getModel();
		CBNodeData data = (CBNodeData) n.getUserObject();
		data.setSelected(value);

		int nb_child = model.getChildCount(n);
		for (int i = 0; i < nb_child; ++i) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) model
					.getChild(n, i);
			data = (CBNodeData) node.getUserObject();
			data.setSelected(value);
			int nb_little_child = model.getChildCount(node);
			if (nb_little_child != 0) {
				setBranchSelected(node, value);
			}
		}

		repaint();

	}

	/**
	 * return true if all children of the node is selected, false if not.
	 * @param node
	 * @param value
	 */
	public boolean areAllChildrenSelected(DefaultMutableTreeNode node) {

		boolean res = true;

		DefaultTreeModel model = (DefaultTreeModel) getModel();

		int nb_child = model.getChildCount(node);

		for (int i = 0; i < nb_child; ++i) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode) model
					.getChild(node, i);

			CBNodeData nodedata = (CBNodeData) child.getUserObject();

			if (nodedata.isSelected() == false) {
				res = false;
				return res;
			}

		}

		return res;

	}

}
