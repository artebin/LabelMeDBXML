package net.trevize.labelme.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import net.trevize.galatee.GEvent;
import net.trevize.galatee.GListener;
import net.trevize.galatee.Galatee;
import net.trevize.gui.layout.CellStyle;
import net.trevize.gui.layout.XGridBag;
import net.trevize.labelme.LabelMeImageViewer;
import net.trevize.labelme.dbxml.DBXMLSearcher;
import net.trevize.utils.SystemCommandHandler;

public class LabelMeBrowserPanel extends JPanel {

	//path to ~LabelMe/database/images
	private String pathToLabelMeImages;

	//path to ~LabelMe/database/annotations
	private String pathToLabelMeAnnotations;

	//the panel containing the Galatee panel.
	private JPanel p5;

	private JTabbedPane tpane;

	private JButton b0, b1, b2;
	public static final String ACTION_COMMAND_LUCENE_QUERY_0 = "ACTION_COMMAND_LUCENE_QUERY_0";
	public static final String ACTION_COMMAND_LUCENE_QUERY_1 = "ACTION_COMMAND_LUCENE_QUERY_1";
	public static final String ACTION_COMMAND_DBXML_XQUERY = "ACTION_COMMAND_DBXML_XQUERY";

	private JTextField textfield_lucene_query_0;
	private JTextField textfield_lucene_query_1;
	private JTextArea textarea_dbxml_xquery;

	private JButton b_previous_results, b_next_results;
	public static final String ACTION_COMMAND_PREVIOUS_RESULTS = "ACTION_COMMAND_PREVIOUS_RESULTS";
	public static final String ACTION_COMMAND_NEXT_RESULTS = "ACTION_COMMAND_NEXT_RESULTS";

	private JLabel l_query_results_info;
	private String query_results_info_text;

	private Galatee galatee;
	private int galateeDescriptionWidth = 256;
	private Dimension galateeImagesDimension = new Dimension(128, 128);
	private int galateeNbCol = 3;

	public LabelMeBrowserPanel(String pathToLabelMeAnnotations,
			String pathToLabelMeImages) {

		this.pathToLabelMeAnnotations = pathToLabelMeAnnotations;
		this.pathToLabelMeImages = pathToLabelMeImages;

		setLayout(new GridBagLayout());
		XGridBag xgb = new XGridBag(this);
		setBorder(new EmptyBorder(10, 10, 10, 10));

		tpane = new JTabbedPane();
		CellStyle style0 = new CellStyle(1., .0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0);
		xgb.add(tpane, style0, 0, 0);

		/**********************************************************************
		 * add lucene query 0 in the tpane.
		 */

		JPanel p1 = new JPanel();
		p1.setLayout(new BorderLayout());
		JLabel l0 = new JLabel("Lucene query: ");
		p1.add(l0, BorderLayout.WEST);
		textfield_lucene_query_0 = new JTextField();
		p1.add(textfield_lucene_query_0, BorderLayout.CENTER);
		b0 = new JButton("do query...");
		b0.setActionCommand(ACTION_COMMAND_LUCENE_QUERY_0);
		p1.add(b0, BorderLayout.EAST);
		tpane.addTab("Lucene index 0", null, p1,
				"Lucene index 0 (KeywordAnalyzer)");

		/**********************************************************************
		 * add lucene query 1 in the tpane.
		 */

		JPanel p2 = new JPanel();
		p2.setLayout(new BorderLayout());
		JLabel l1 = new JLabel("Lucene query: ");
		p2.add(l1, BorderLayout.WEST);
		textfield_lucene_query_1 = new JTextField();
		p2.add(textfield_lucene_query_1, BorderLayout.CENTER);
		b1 = new JButton("do query...");
		b1.setActionCommand(ACTION_COMMAND_LUCENE_QUERY_1);
		p2.add(b1, BorderLayout.EAST);
		tpane.addTab("Lucene index 1", null, p2,
				"Lucene index 0 (WhitespaceAnalyzer)");

		/**********************************************************************
		 * add dbxml query in the tpane.
		 */

		JPanel p3 = new JPanel();
		p3.setLayout(new BorderLayout());
		JLabel l2 = new JLabel("dbxml XQuery: ");
		p3.add(l2, BorderLayout.WEST);
		textarea_dbxml_xquery = new JTextArea();
		textarea_dbxml_xquery.setText(DBXMLSearcher.xqueryQueryImagesByObjectName);
		JScrollPane p3sp = new JScrollPane(textarea_dbxml_xquery);
		p3sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		p3.add(p3sp, BorderLayout.CENTER);
		b2 = new JButton("do query...");
		b2.setActionCommand(ACTION_COMMAND_DBXML_XQUERY);
		p3.add(b2, BorderLayout.EAST);
		tpane.addTab("DBXML xquery", null, p3, "DBXML xquery");

		/**********************************************************************
		 * add the info pane.
		 */

		JPanel p4 = new JPanel();
		p4.setLayout(new GridBagLayout());
		XGridBag p4_xgb = new XGridBag(p4);
		l_query_results_info = new JLabel();
		l_query_results_info.setText("query :");
		l_query_results_info.setBackground(Color.BLUE);
		l_query_results_info.setOpaque(true);
		l_query_results_info.setForeground(Color.WHITE);
		CellStyle p4_style0 = new CellStyle(1., 1.,
				GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0);
		p4_xgb.add(l_query_results_info, p4_style0, 0, 0);
		CellStyle p4_style1 = new CellStyle(0., 0.,
				GridBagConstraints.NORTHEAST, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0);
		p4_xgb.add(Box.createHorizontalStrut(5), p4_style1, 0, 1);
		b_previous_results = new JButton("< previous results");
		b_previous_results.setActionCommand(ACTION_COMMAND_PREVIOUS_RESULTS);

		p4_xgb.add(b_previous_results, p4_style1, 0, 2);
		p4_xgb.add(Box.createHorizontalStrut(5), p4_style1, 0, 3);
		b_next_results = new JButton("next results >");
		b_next_results.setPreferredSize(b_previous_results.getPreferredSize());
		b_next_results.setActionCommand(ACTION_COMMAND_NEXT_RESULTS);

		p4_xgb.add(b_next_results, p4_style1, 0, 4);
		xgb.add(p4, style0, 4, 0);

		/**********************************************************************
		 * add the panel containing the Galatee panel.
		 */

		p5 = new JPanel();
		p5.setBackground(Color.WHITE);
		p5.setLayout(new BorderLayout());
		CellStyle style1 = new CellStyle(1., 1., GridBagConstraints.NORTHWEST,
				GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
		xgb.add(p5, style1, 5, 0);

		/**********************************************************************/

	}

	public void addActionListener(ActionListener listener) {
		b0.addActionListener(listener);
		b1.addActionListener(listener);
		b2.addActionListener(listener);
		b_previous_results.addActionListener(listener);
		b_next_results.addActionListener(listener);
	}

	public String getLuceneQuery0() {
		return textfield_lucene_query_0.getText();
	}

	public String getLuceneQuery1() {
		return textfield_lucene_query_1.getText();
	}

	public String getDBXMLQuery() {
		return textarea_dbxml_xquery.getText();
	}

	public void updateGalateePanel(Vector<String> listfiles,
			Vector<Vector<Object>> data) {

		Vector<File> files;
		files = new Vector<File>();

		for (int i = 0; i < listfiles.size(); ++i) {
			files.add(new File(pathToLabelMeImages + "/" + listfiles.get(i)));
		}

		Galatee g0 = new Galatee(files, data, galateeImagesDimension,
				galateeDescriptionWidth, galateeNbCol);

		g0.addGalateeListener(new GListener() {
			class VisuThread extends Thread {
				private String command = "display ";
				private SystemCommandHandler sch = new SystemCommandHandler();
				private String path;

				public VisuThread(String path) {
					this.path = path;
				}

				public void run() {
					openLabelMeImageViewer();
					//openWithImageMagick();
				}

				private void openWithImageMagick() {
					sch.exec(command + path);
				}

				private void openLabelMeImageViewer() {
					/*
					 * Retrieving the image path.
					 * We could only retrieve the image path from the
					 * LabelMeImagePanel, so we retrieve the image relative
					 * path in ~LabelMe/database/images, which is the same for
					 * the annotation path inside ~LabelMe/database/annotations
					 */
					String[] pathExplode = path.split("/");
					String annotationRelativePath = pathExplode[pathExplode.length - 2];

					/*
					 * using the DatasetCleanerNormalizer, the extension of
					 * the image filename is .jpg
					 */
					String image_filename = pathExplode[pathExplode.length - 1];
					String annotationFileName = image_filename.substring(0,
							image_filename.length() - 4)
							+ ".xml";

					new LabelMeImageViewer(
							pathToLabelMeAnnotations + "/"
									+ annotationRelativePath + "/"
									+ annotationFileName, pathToLabelMeImages);
				}
			}

			@Override
			public void focusChanged(GEvent arg0) {
			}

			@Override
			public void itemClicked(GEvent arg0) {
			}

			@Override
			public void itemDoubleClicked(GEvent arg0) {
				new VisuThread(arg0.getSelectedItem().getFile()
						.getAbsolutePath()).start();
			}

			@Override
			public void selectionChanged(GEvent arg0) {
			}
		});

		p5.removeAll();
		p5.add(g0, BorderLayout.CENTER);
		p5.revalidate();

		if (galatee != null) {
			galatee.dispose();
		}

		galatee = g0;

		System.gc();

	}

	public void updateResultsInfoPanel(String query, int nbOfResults,
			int pageOfResultsViewed, int nbOfResultsPerPage) {
		query_results_info_text = "query: "
				+ query
				+ ", "
				+ nbOfResults
				+ " results, "
				+ (pageOfResultsViewed + 1)
				+ "/"
				+ ((nbOfResults % nbOfResultsPerPage == 0) ? (nbOfResults / nbOfResultsPerPage)
						: (nbOfResults / nbOfResultsPerPage + 1));

		l_query_results_info.setText(query_results_info_text);
	}

}
