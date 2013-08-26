package net.trevize.labelme.gui;

import java.util.Vector;

import javax.swing.JScrollPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import net.trevize.gui.cbtree.CBNodeData;
import net.trevize.gui.cbtree.CBTree;
import net.trevize.gui.cbtree.CBTreeCellEditor;
import net.trevize.gui.cbtree.CBTreeCellRenderer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * LabelMeAnnotationPanel.java - Jul 6, 2009
 */

public class LabelMeAnnotationPanel extends JScrollPane {

	//create a derivation of the CBNodeClass.
	class CBNodeDataAnnotatedObject extends CBNodeData {

		private LabelMeImagePanel lmip;

		private net.trevize.labelthem.jaxb.Object annotatedObject;

		public CBNodeDataAnnotatedObject(LabelMeImagePanel lmip, String name,
				boolean selected) {
			super(name, selected);
			this.lmip = lmip;
		}

		@Override
		public void setSelected(boolean selected) {
			super.setSelected(selected);
			if (annotatedObject != null) {
				lmip.setPolygonVisibility(annotatedObject, selected);
			}
		}

		@Override
		public Object clone() {
			CBNodeDataAnnotatedObject annotatedObjetNode = new CBNodeDataAnnotatedObject(
					lmip, super.getName(), super.isSelected());
			annotatedObjetNode.setAnnotatedObject(annotatedObject);
			return annotatedObjetNode;
		}

		public net.trevize.labelthem.jaxb.Object getAnnotatedObject() {
			return annotatedObject;
		}

		public void setAnnotatedObject(
				net.trevize.labelthem.jaxb.Object annotatedObject) {
			this.annotatedObject = annotatedObject;
		}

	}

	private LabelMeImagePanel lmip;

	//the visualized annotation.
	private net.trevize.labelthem.jaxb.Annotation a;

	private Vector<String> vocabulary;

	private Vector<Vector<DefaultMutableTreeNode>> nodes;

	private CBTree tree;

	private DefaultTreeModel treemodel;

	private DefaultMutableTreeNode root;

	private CBTreeCellRenderer renderer;

	private CBTreeCellEditor editor;

	private JScrollPane scrollPane;

	public LabelMeAnnotationPanel(LabelMeImagePanel lmip,
			net.trevize.labelthem.jaxb.Annotation a) {

		this.lmip = lmip;
		this.a = a;

		init();

	}

	private void init() {

		tree = new CBTree();
		tree.setRootVisible(false);

		//create the model.
		root = new DefaultMutableTreeNode(new CBNodeDataAnnotatedObject(lmip,
				"Named objects", false));
		treemodel = new DefaultTreeModel(root);
		populate(root);
		tree.setModel(treemodel);

		//create the renderer.
		renderer = new CBTreeCellRenderer();
		tree.setCellRenderer(renderer);

		//create the editor.
		editor = new CBTreeCellEditor(tree);
		tree.setCellEditor(editor);
		tree.setEditable(true);

		setViewportView(tree);

	}

	private void populate(DefaultMutableTreeNode root) {

		vocabulary = new Vector<String>();
		nodes = new Vector<Vector<DefaultMutableTreeNode>>();

		for (net.trevize.labelthem.jaxb.Object o : a.getObject()) {

			String o_name = o.getName().getContent();

			if (!vocabulary.contains(o_name)) {
				vocabulary.add(o_name);
				Vector<DefaultMutableTreeNode> annot_instances_list = new Vector<DefaultMutableTreeNode>();
				nodes.add(annot_instances_list);
				CBNodeDataAnnotatedObject annot_instance = new CBNodeDataAnnotatedObject(
						lmip, o_name, true);
				annot_instance.setAnnotatedObject(o);
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(
						annot_instance);
				annot_instances_list.add(node);
			} else {
				int node_list_indice = vocabulary.indexOf(o_name);
				CBNodeDataAnnotatedObject annot_instance = new CBNodeDataAnnotatedObject(
						lmip, o_name, true);
				annot_instance.setAnnotatedObject(o);
				DefaultMutableTreeNode node = new DefaultMutableTreeNode(
						annot_instance);
				nodes.get(node_list_indice).add(node);
			}

		}

		//creating the hierarchy of nodes from the root node.
		for (int i = 0; i < vocabulary.size(); ++i) {

			//if only one node for the current annotation, then no subnodes.
			if (nodes.get(i).size() == 1) {
				root.add(nodes.get(i).get(0));
			}

			else
			//if severals instance for the current annotation, creating a parent node.
			{
				//creating a class node (a parent node for severals subnodes).
				DefaultMutableTreeNode annot_instances_list = new DefaultMutableTreeNode(
						new CBNodeDataAnnotatedObject(lmip, vocabulary.get(i),
								true));

				//adding all annotation instances in the parent node (an annot_instances_list).
				for (int j = 0; j < nodes.get(i).size(); ++j) {
					annot_instances_list.add(nodes.get(i).get(j));
				}

				//add the subtree to the root node.
				root.add(annot_instances_list);
			}
		}

	}

}
