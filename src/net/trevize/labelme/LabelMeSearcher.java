package net.trevize.labelme;

import java.util.Vector;

public abstract class LabelMeSearcher {

	private String query;

	private int nbOfResults;

	private Vector<String> results;

	public LabelMeSearcher() {
		results = new Vector<String>();
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public int getNbOfResults() {
		return nbOfResults;
	}

	public void setNbOfResults(int nbOfResults) {
		this.nbOfResults = nbOfResults;
	}

	public abstract void doQuery(String query);

	public abstract LabelMeResults getResults(int idxFrom, int nbOfResults);

}
