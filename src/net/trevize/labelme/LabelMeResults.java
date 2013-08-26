package net.trevize.labelme;

import java.util.Vector;

public class LabelMeResults {

	private Vector<String> listfiles;

	private Vector<Vector<Object>> data;

	public LabelMeResults() {
		listfiles = new Vector<String>();
		data = new Vector<Vector<Object>>();
	}

	public Vector<String> getListfiles() {
		return listfiles;
	}

	public void setListfiles(Vector<String> listfiles) {
		this.listfiles = listfiles;
	}

	public Vector<Vector<Object>> getData() {
		return data;
	}

	public void setData(Vector<Vector<Object>> data) {
		this.data = data;
	}

	public static void compare(String id0, LabelMeResults results0, String id1,
			LabelMeResults results1) {
		Vector<String> listFiles0 = (Vector<String>) results0.getListfiles()
				.clone();
		Vector<String> listFiles1 = (Vector<String>) results1.getListfiles()
				.clone();

		for (int i = 0; i < listFiles0.size(); ++i) {
			boolean foundIn1 = false;
			for (int j = 0; j < listFiles1.size(); j++) {
				if (listFiles0.get(i).equals(listFiles1.get(j))) {
					listFiles1.remove(j);
					foundIn1 = true;
					break;
				}
			}
			if (foundIn1) {
				listFiles0.remove(i);
				--i;
			}
		}

		System.out.println("in " + id0 + " but missing in " + id1);
		for (int i = 0; i < listFiles0.size(); ++i) {
			System.out.println(listFiles0.get(i));
		}

		System.out.println("in " + id1 + " but missing in " + id0);
		for (int i = 0; i < listFiles1.size(); ++i) {
			System.out.println(listFiles1.get(i));
		}
	}
}
