package net.trevize.labelme.lucene;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;

import net.trevize.labelme.LabelMeResults;
import net.trevize.labelme.LabelMeSearcher;

import org.apache.commons.lang.mutable.MutableInt;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;

/**
 * 
 * 
 * @author Nicolas James <nicolas.james@gmail.com> [[http://njames.trevize.net]]
 * LuceneLabelMeSearcher.java - May 28, 2009
 */

public class LuceneLabelMeSearcher extends LabelMeSearcher {

	private String indexPath;

	private Analyzer analyzer;

	private Hits hits;

	public LuceneLabelMeSearcher(String indexPath, Analyzer analyzer) {
		this.indexPath = indexPath;
		this.analyzer = analyzer;
	}

	public String getIndexPath() {
		return indexPath;
	}

	public void setIndexPath(String indexPath) {
		this.indexPath = indexPath;
	}

	public Hits getHits() {
		return hits;
	}

	public void setHits(Hits hits) {
		this.hits = hits;
	}

	private Hits search(String field, String queryString, String indexPath) {

		System.out.println("\tLuceneLabelMeSearcher.search begin.");
		hits = null;

		try {
			//to prevent org.apache.lucene.search.BooleanQuery$TooManyClauses: maxClauseCount is set to 1024
			BooleanQuery.setMaxClauseCount(Integer.MAX_VALUE);

			Searcher searcher = new IndexSearcher(indexPath);

			QueryParser parser = new QueryParser(field, analyzer);
			parser.setAllowLeadingWildcard(true);
			Query query = parser.parse(queryString);

			hits = searcher.search(query);

			//display number of results.
			System.out.println(hits.length() + " results.");

			//writing query log.
			writeQueryLog(queryString, hits);

		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("\tLuceneLabelMeSearcher.search end.");

		return hits;

	}

	private void writeQueryLog(String query, Hits hits) {

		System.out.println("\tLuceneLabelMeSearcher.writeQueryLog begin.");

		String logFilename = "./lucene-queryresults-logs/query-"
				+ Calendar.getInstance().getTimeInMillis() + ".log";
		System.out.println("writing file " + logFilename);
		FileWriter fw = null;
		BufferedWriter bw = null;

		//write the query result in a file.
		try {
			fw = new FileWriter(logFilename);
			bw = new BufferedWriter(fw);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			bw.write("####\n" + query + "\n####\n");
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (hits.length() > 0) {
			for (int i = 0; i < hits.length(); i++) {
				try {
					bw.write(hits.doc(i).get(
							LuceneLabelMeDocument.FIELD_IMAGE_FOLDER.toString())
							+ "/"
							+ hits.doc(i).get(
									LuceneLabelMeDocument.FIELD_IMAGE_FILENAME
											.toString()) + "\n");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		try {
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("\tLuceneLabelMeSearcher.writeQueryLog end.");
	}

	/***************************************************************************
	 * implementation of LabelMeSearcher.
	 **************************************************************************/

	@Override
	public void doQuery(String query) {
		super.setQuery(query);

		hits = search(LuceneLabelMeDocument.FIELD_OBJECT_NAME, query, indexPath);

		super.setNbOfResults(hits.length());
	}

	@Override
	public LabelMeResults getResults(int idxFrom, int n) {

		System.out.println("\tLuceneLabelMeSearcher.getResults begin.");
		LabelMeResults results = new LabelMeResults();

		if (hits.length() > 0 && idxFrom < hits.length()) {
			for (int i = idxFrom; i < hits.length() && (i - idxFrom) < n; i++) {

				//getting filepath.
				try {
					String filepath = hits.doc(i).get(
							LuceneLabelMeDocument.FIELD_IMAGE_FOLDER)
							+ "/"
							+ hits.doc(i).get(
									LuceneLabelMeDocument.FIELD_IMAGE_FILENAME)
									.toString();
					results.getListfiles().add(filepath);
				} catch (CorruptIndexException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				//getting image metadata.
				try {
					Vector<Object> data = new Vector<Object>();
					results.getData().add(data);

					//adding the image filename.
					data.add(hits.doc(i).get(
							LuceneLabelMeDocument.FIELD_IMAGE_FILENAME).toString());

					//adding the annotation filename.
					data.add(hits.doc(i).get(
							LuceneLabelMeDocument.FIELD_ANNOTATION_FILENAME).toString());
					
					//adding all object names.
					String[] objectnames = hits.doc(i).getValues(
							LuceneLabelMeDocument.FIELD_OBJECT_NAME);

					//if there is one or several named object in the current document.
					if (objectnames != null && objectnames.length != 0) {

						HashMap<String, MutableInt> collapsedKeywords = new HashMap<String, MutableInt>();

						for (int j = 0; j < objectnames.length; ++j) {
							if (!collapsedKeywords.keySet().contains(
									objectnames[j])) {
								collapsedKeywords.put(objectnames[j],
										new MutableInt(1));
							} else {
								collapsedKeywords.get(objectnames[j])
										.increment();
							}
						}

						for (String key : collapsedKeywords.keySet()) {
							data.add(key + "(" + collapsedKeywords.get(key)
									+ ")");
						}

					}//endif there is some named object in the current document.

				} catch (CorruptIndexException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		}

		System.out.println("\tLuceneLabelMeSearcher.getResults end.");

		return results;

	}

}
