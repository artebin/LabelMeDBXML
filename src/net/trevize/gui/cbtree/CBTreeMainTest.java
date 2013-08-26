package net.trevize.gui.cbtree;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class CBTreeMainTest extends JScrollPane {

	private CBTree tree;

	private DefaultTreeModel treemodel;

	private DefaultMutableTreeNode root;

	public CBTreeMainTest() {
		super();
		tree = new CBTree();
		tree.setRootVisible(false);

		root = new DefaultMutableTreeNode(new CBNodeData("DEC", false));

		treemodel = new DefaultTreeModel(root);

		populate(root);

		tree.setModel(treemodel);

		CBTreeCellRenderer renderer = new CBTreeCellRenderer();

		tree.setCellRenderer(renderer);

		CBTreeCellEditor editor = new CBTreeCellEditor(tree);

		tree.setCellEditor(editor);

		tree.setEditable(true);

		setViewportView(tree);
	}

	public void populate(DefaultMutableTreeNode root) {
		DefaultMutableTreeNode tmp = new DefaultMutableTreeNode(new CBNodeData(
				"carte1", false));
		root.add(tmp);

		tmp = new DefaultMutableTreeNode(new CBNodeData("dossier image", false));
		root.add(tmp);

		DefaultMutableTreeNode tmp2 = new DefaultMutableTreeNode(
				new CBNodeData("image1", false));
		tmp.add(tmp2);

		tmp2 = new DefaultMutableTreeNode(new CBNodeData("image2", false));
		tmp.add(tmp2);

		tmp2 = new DefaultMutableTreeNode(new CBNodeData("image3", false));
		tmp.add(tmp2);

		tmp = new DefaultMutableTreeNode(new CBNodeData("dossier carte", false));
		root.add(tmp);

		tmp2 = new DefaultMutableTreeNode(new CBNodeData("carte1", false));
		tmp.add(tmp2);

		tmp2 = new DefaultMutableTreeNode(new CBNodeData("carte2", false));
		tmp.add(tmp2);

		tmp2 = new DefaultMutableTreeNode(new CBNodeData("carte3", false));
		tmp.add(tmp2);
	}

	public static void main(String[] args) {
		// setting the system look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		JFrame f = new JFrame("MainTest");
		f.setSize(256, 256);
		CBTreeMainTest mt = new CBTreeMainTest();
		f.getContentPane().add(mt);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
	
}
