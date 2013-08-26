package net.trevize.labelme.lucene;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import net.trevize.labelthem.jaxb.Annotation;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.LockObtainFailedException;

/**
 * This class provides a Lucene Indexer for the LabelMe annotation files.
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * LuceneLabelMeIndexer.java - May 20, 2009
 */

public class LuceneLabelMeIndexer {

	public static String KEYWORD_ANALYZER = "KEYWORD_ANALYZER";

	public static String WHITESPACE_ANALYZER = "WHITESPACE_ANALYZER";

	private String datasetPath;

	private String indexPath;

	private JAXBContext jc;

	private Unmarshaller u;

	private XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

	private IndexWriter idxwriter;

	private Analyzer analyzer;

	public LuceneLabelMeIndexer(String analyzer_name) {
		if (analyzer_name.equals(KEYWORD_ANALYZER)) {
			analyzer = new KeywordAnalyzer();
		}

		else

		if (analyzer_name.equals(WHITESPACE_ANALYZER)) {
			analyzer = new WhitespaceAnalyzer();
		}
	}

	/**
	 * To be used for adding one document (not in a batch mode).
	 * @param a
	 * @param indexPath
	 */
	public void addLabelMeDocumentToIndex(Annotation a, String indexPath) {
		try {
			/*
			 * create the directory for storing indexes if not exists
			 * if exists the index will be extended with new documents (take care for copy)
			 */
			boolean create;
			File f = new File(indexPath);
			if (f.exists() && f.isDirectory()) {
				create = false;
			} else {
				create = true;
			}

			//initializing the indexwriter
			idxwriter = new IndexWriter(indexPath, analyzer, create);

			//adding the LabelMe document
			idxwriter.addDocument(LuceneLabelMeDocument.createLabelMeDocument(f
					.getParent(), f.getName(), a));
			idxwriter.optimize();
			idxwriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * For indexing all the LabelMe dataset.
	 * @param datasetPath
	 * @param indexPath
	 */
	public void indexLabelMeDataset(String datasetPath, String indexPath) {
		System.out.println("indexLabelMeDataset begin.");

		this.datasetPath = datasetPath;
		this.indexPath = indexPath;

		//create the JAXBContext.
		try {
			jc = JAXBContext.newInstance("net.trevize.labelthem.jaxb");
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		//instantiate an Unmarshaller.
		try {
			u = jc.createUnmarshaller();
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		//create the directory for storing indexes if not exists
		//if exists the index will be extended with new documents (take care for copy)
		boolean create;
		File f = new File(indexPath);
		if (f.exists() && f.isDirectory()) {
			create = false;
		} else {
			create = true;
		}

		//initializing the indexwriter
		try {
			idxwriter = new IndexWriter(indexPath, analyzer, create);
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		_processDataset();

		try {
			idxwriter.close();
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("indexLabelMeDataset end.");
	}

	private void _processDataset() {
		System.out.println("_processDataset begin.");

		File[] dl = new File(datasetPath).listFiles();

		for (File d : dl) {
			_processDir(d.getName());
		}

		System.out.println("_processDataset end.");
	}

	public void _processDir(String dir_path) {
		System.out.println("\tentering " + datasetPath + "/" + dir_path);

		File dir = new File(datasetPath + "/" + dir_path);

		/*
		 * we store logs in txt files, we have to skip it.
		 */
		if (!dir.isDirectory()) {
			return;
		}

		//iterate on all file on the current directory.
		File[] lf = new File(datasetPath + "/" + dir_path).listFiles();

		//iterate on all XML annotation files in the current directory.
		for (File f : lf) {
			//if the file is not an annotation file then go to the next iteration.
			if (!f.getName().endsWith(".xml")) {
				continue;
			}

			//create an XMLStreamReader on the annotation file.
			XMLStreamReader xmlsr = null;
			try {
				xmlsr = xmlInputFactory
						.createXMLStreamReader(new FileInputStream(f));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (XMLStreamException e1) {
				e1.printStackTrace();
			}

			//load the annotation.
			Annotation a = null;
			try {
				a = (Annotation) u.unmarshal(xmlsr);
			} catch (JAXBException e) {
				e.printStackTrace();
			}

			//adding the LabelMe document to the index.
			try {
				Document doc = LuceneLabelMeDocument.createLabelMeDocument(f
						.getParent(), f.getName(), a);

				//doc == null when there is no object names, and we don't
				//index documents with any object names.
				if (doc == null) {
					continue;
				}

				idxwriter.addDocument(doc);
				idxwriter.optimize();
			} catch (CorruptIndexException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}//end for all file in the current directory.
	}

}