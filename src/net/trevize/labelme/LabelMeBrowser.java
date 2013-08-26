package net.trevize.labelme;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.trevize.labelme.dbxml.DBXMLSearcher;
import net.trevize.labelme.gui.LabelMeBrowserPanel;
import net.trevize.labelme.lucene.LuceneLabelMeSearcher;

import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * LabelMeBrowser.java - May 25, 2009
 */

public class LabelMeBrowser implements ActionListener {

	//the main frame.
	private JFrame f;

	private LabelMeBrowserPanel lmbp;

	//path to ~LabelMe/database/images
	private String pathToLabelMeImages = Configuration.getLabelMeImagesPath();

	//path to ~LabelMe/database/annotations
	private String pathToLabelMeAnnotations = Configuration
			.getLabelMeAnnotationsPath();

	//path to LabelMe - Lucene indexes.
	private String pathToLuceneIndexWithKeywordAnalyzer = Configuration
			.getLabelMeLuceneIndex_KeywordAnalyzerPath();
	private String pathToLuceneIndexWithWhitespaceAnalyzer = Configuration
			.getLabelMeLuceneIndex_WhitespaceAnalyzerPath();

	//path to LabelMe - dbxml index.
	private String pathToDBXMLIndex = Configuration.getLabelMeDbxmlIndexPath();

	private String query;
	private LabelMeSearcher searcher;
	private LabelMeResults results;
	private int nbOfResults;
	private int pageOfResultsViewed;
	private int nbOfResultsPerPage = 36;

	public LabelMeBrowser() {

		f = new JFrame("LabelMe Browser");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(768, 512);

		lmbp = new LabelMeBrowserPanel(pathToLabelMeAnnotations,
				pathToLabelMeImages);
		lmbp.addActionListener(this);

		f.getContentPane().add(lmbp);

		f.setLocation(128, 128);

		f.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String actionCommand = e.getActionCommand();

		if (actionCommand
				.equals(LabelMeBrowserPanel.ACTION_COMMAND_LUCENE_QUERY_0)) {

			query = lmbp.getLuceneQuery0();
			if (query.equals("")) {
				return;
			}

			//querying with Lucene.
			System.out.println("querying with Lucene (KeywordAnalyzer).");
			String indexPath = pathToLuceneIndexWithKeywordAnalyzer;
			searcher = new LuceneLabelMeSearcher(indexPath,
					new KeywordAnalyzer());

			searcher.doQuery(query);
			nbOfResults = searcher.getNbOfResults();
			pageOfResultsViewed = 0;
			results = searcher.getResults(pageOfResultsViewed
					* nbOfResultsPerPage, nbOfResultsPerPage);

			lmbp.updateResultsInfoPanel(query, nbOfResults,
					pageOfResultsViewed, nbOfResultsPerPage);
			lmbp.updateGalateePanel(results.getListfiles(), results.getData());

		}

		else

		if (actionCommand
				.equals(LabelMeBrowserPanel.ACTION_COMMAND_LUCENE_QUERY_1)) {

			query = lmbp.getLuceneQuery1();
			if (query.equals("")) {
				return;
			}

			//querying with Lucene.
			System.out.println("querying with Lucene (WhitespaceAnalyzer).");
			String indexPath = pathToLuceneIndexWithWhitespaceAnalyzer;
			searcher = new LuceneLabelMeSearcher(indexPath,
					new WhitespaceAnalyzer());
			searcher.doQuery(query);
			nbOfResults = searcher.getNbOfResults();
			pageOfResultsViewed = 0;
			results = searcher.getResults(pageOfResultsViewed
					* nbOfResultsPerPage, nbOfResultsPerPage);

			lmbp.updateResultsInfoPanel(query, nbOfResults,
					pageOfResultsViewed, nbOfResultsPerPage);
			lmbp.updateGalateePanel(results.getListfiles(), results.getData());

		}

		else

		if (actionCommand
				.equals(LabelMeBrowserPanel.ACTION_COMMAND_DBXML_XQUERY)) {

			query = lmbp.getDBXMLQuery();
			if (query.equals("")) {
				return;
			}

			//querying with the dbxml.
			System.out.println("querying with DBXML.");
			searcher = new DBXMLSearcher(pathToDBXMLIndex, "LabelMe.dbxml");

			searcher.doQuery(query);
			nbOfResults = searcher.getNbOfResults();
			pageOfResultsViewed = 0;
			results = searcher.getResults(pageOfResultsViewed
					* nbOfResultsPerPage, nbOfResultsPerPage);

			lmbp.updateResultsInfoPanel(query, nbOfResults,
					pageOfResultsViewed, nbOfResultsPerPage);
			lmbp.updateGalateePanel(results.getListfiles(), results.getData());

		}

		else

		if (actionCommand
				.equals(LabelMeBrowserPanel.ACTION_COMMAND_NEXT_RESULTS)) {
			//System.out.println("ACTION_COMMAND_NEXT_RESULTS");

			if ((pageOfResultsViewed + 1) * nbOfResultsPerPage < nbOfResults) {
				pageOfResultsViewed++;
				results = searcher.getResults(pageOfResultsViewed
						* nbOfResultsPerPage, nbOfResultsPerPage);

				lmbp.updateResultsInfoPanel(query, nbOfResults,
						pageOfResultsViewed, nbOfResultsPerPage);
				lmbp.updateGalateePanel(results.getListfiles(), results
						.getData());
			}
		}

		else

		if (actionCommand
				.equals(LabelMeBrowserPanel.ACTION_COMMAND_PREVIOUS_RESULTS)) {
			//System.out.println("ACTION_COMMAND_PREVIOUS_RESULTS");

			if (pageOfResultsViewed != 0) {
				pageOfResultsViewed--;
				results = searcher.getResults(pageOfResultsViewed
						* nbOfResultsPerPage, nbOfResultsPerPage);

				lmbp.updateResultsInfoPanel(query, nbOfResults,
						pageOfResultsViewed, nbOfResultsPerPage);
				lmbp.updateGalateePanel(results.getListfiles(), results
						.getData());
			}
		}

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

		LabelMeBrowser lmb = new LabelMeBrowser();
	}

}
