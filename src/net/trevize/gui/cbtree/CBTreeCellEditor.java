package net.trevize.gui.cbtree;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * CBTreeCellEditor.java - Jun 11, 2009
 */

public class CBTreeCellEditor implements TreeCellEditor, ActionListener {

	private CBTree tree;

	private CBCellPanel cbcp;

	private CBNodeData currentdata;

	private DefaultMutableTreeNode currentnode;

	public CBTreeCellEditor(CBTree tree) {
		super();

		this.tree = tree;

		cbcp = new CBCellPanel();
		cbcp.getCb().addActionListener(this);

		cbcp.setSelected(true);
	}

	/***************************************************************************
	 * implementation of TreeCellEditor
	 **************************************************************************/

	public Component getTreeCellEditorComponent(JTree tree, Object value,
			boolean selected, boolean expand, boolean leaf, int row) {

		currentnode = (DefaultMutableTreeNode) value;
		currentdata = (CBNodeData) currentnode.getUserObject();
		cbcp.getLabel().setText(currentdata.getName());
		
		if (value == tree.getModel().getRoot()) {
			cbcp.displayCheckbox(false);
		} else {
			cbcp.displayCheckbox(true);
			cbcp.getCb().setSelected(currentdata.isSelected());
		}

		return cbcp;
	}

	public void addCellEditorListener(CellEditorListener arg0) {
	}

	public void cancelCellEditing() {
	}

	public Object getCellEditorValue() {
		return cbcp.getCb().isSelected();
	}

	public boolean isCellEditable(EventObject arg0) {
		return true;
	}

	public void removeCellEditorListener(CellEditorListener arg0) {
	}

	public boolean shouldSelectCell(EventObject arg0) {
		return false;
	}

	public boolean stopCellEditing() {
		return false;
	}

	/***************************************************************************
	 * implementation of ActionListener
	 **************************************************************************/

	public void actionPerformed(ActionEvent arg0) {
		String command = arg0.getActionCommand();
		if (command.equals(CBCellPanel.ACTION_CBCELLPANEL_CHECKBOX)) {
			
			/*
			 * update the model and repaint the view.
			 */
			
			//set the data as selected.
			if (currentnode.getLevel() == 1) { //category level.
				tree.setBranchSelected(currentnode, cbcp.getCb().isSelected());
			} else {
				currentdata.setSelected(cbcp.getCb().isSelected());
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) currentnode
						.getParent();
				if (tree.areAllChildrenSelected(parent)) {
					((CBNodeData) parent.getUserObject()).setSelected(true);
				} else {
					((CBNodeData) parent.getUserObject()).setSelected(false);
				}
				tree.repaint();
			}
			
		}
	}

}
