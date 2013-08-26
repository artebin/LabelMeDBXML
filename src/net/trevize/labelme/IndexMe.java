package net.trevize.labelme;

import net.trevize.labelme.dbxml.DBXMLManager;
import net.trevize.labelme.lucene.LuceneLabelMeIndexer;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * IndexMe.java - Jun 2, 2009
 */

public class IndexMe {

	public static void main(String args[]) {
		String annotationsPath = Configuration.getLabelMeAnnotationsPath();

		String imagesPath = Configuration.getLabelMeImagesPath();

		/*
		 * Dataset cleaning and normalization.
		 * cleaning the annotations datasets.
		 * normalizing annotations and images filename.
		 * rewrite image in JPG format and eventually resize it.
		 */

		//int maxImageWidth = 1024;
		//int maxImageHeight = 1024;
		DatasetCleanerNormalizer dcn = new DatasetCleanerNormalizer();

		dcn.cleanLabelMeDataset(annotationsPath, imagesPath, null);

		/*
		 * Create Lucene index.
		 */

		/*
		 * create an index with a KeywordAnalyzer, taking the freetext annotation like one keyword.
		 */

		String indexPath = Configuration
				.getLabelMeLuceneIndex_KeywordAnalyzerPath();
		LuceneLabelMeIndexer llmi = new LuceneLabelMeIndexer(
				LuceneLabelMeIndexer.KEYWORD_ANALYZER);
		llmi.indexLabelMeDataset(annotationsPath, indexPath);

		/*
		 * create an index with a WhitespaceAnalyzer, so the freetext annotation is cut in token considering spaces.
		 */

		indexPath = Configuration
				.getLabelMeLuceneIndex_WhitespaceAnalyzerPath();
		llmi = new LuceneLabelMeIndexer(
				LuceneLabelMeIndexer.WHITESPACE_ANALYZER);
		llmi.indexLabelMeDataset(annotationsPath, indexPath);

		/*
		 * Create DBXML database.
		 */

		//do not forget to add the path to ~dbxml/lib in LD_LIBRARY_PATH.
		DBXMLManager bdbXML = new DBXMLManager(Configuration
				.getLabelMeDbxmlIndexPath());
		bdbXML.indexDataset(annotationsPath, Configuration
				.getDbxmlInstallLibPath()
				+ "/LabelMe.dbxml");
	}

}
