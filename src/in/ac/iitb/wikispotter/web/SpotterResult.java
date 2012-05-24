package in.ac.iitb.wikispotter.web;

import in.ac.iitb.wikispotter.trie.QueryResults;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SpotterResult {

	private List<TrieTerm> terms;
	private int count = 0;

	public List<TrieTerm> getTerms() {
		if (null == terms)
			terms = new ArrayList<SpotterResult.TrieTerm>();
		return terms;
	}

	public void setTerms(List<TrieTerm> terms) {
		this.terms = terms;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public class TrieTerm {
		private String id;
		private String term;
		private List<String> links;

		public TrieTerm(Integer key, String value, ArrayList<String> arrayList) {
			id = key.toString();
			term = value;
			links = arrayList;
			for (String link : links)
				System.out.println(link);
		}

		public List<String> getLinks() {
			return links;
		}

		public void setLinks(List<String> links) {
			this.links = links;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTerm() {
			return term;
		}

		public void setTerm(String term) {
			this.term = term;
		}

	}

	public void add(Integer key, String value, List<Integer> ids)
			throws IOException {
		HashMap<Integer, ArrayList<String>> linksMap = QueryResults
				.get((ArrayList<Integer>) ids);
		getTerms().add(new TrieTerm(key, value, linksMap.get(key)));
		count++;
	}

}
