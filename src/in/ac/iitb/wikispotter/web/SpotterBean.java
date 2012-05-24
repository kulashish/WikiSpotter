package in.ac.iitb.wikispotter.web;

import in.ac.iitb.wikispotter.trie.SearchGraph;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name = "SpotterBean")
@RequestScoped
public class SpotterBean {

	private SpotterResult result;
	private String term;

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public SpotterResult getResult() {
		return result;
	}

	public void setResult(SpotterResult result) {
		this.result = result;
	}

	public String btnSearch_action() {
		try {
			doSearch();
		} catch (IOException e) {
			return "fail";
		}
		return "success";
	}

	private void doSearch() throws IOException {
		HashMap<Integer, String> resultMap = SearchGraph.search(term);
		List<Integer> ids = null;
		if (null != resultMap) {
			Iterator<Entry<Integer, String>> iter = resultMap.entrySet()
					.iterator();
			result = new SpotterResult();
			Entry<Integer, String> entry;
			while (iter.hasNext()) {
				entry = iter.next();
				ids = new ArrayList<Integer>();
				ids.add(entry.getKey());
				result.add(entry.getKey(), entry.getValue(), ids);
			}
		}
	}
}
