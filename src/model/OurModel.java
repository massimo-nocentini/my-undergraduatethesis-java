package model;

import java.util.HashSet;
import java.util.Set;

public class OurModel {

	private Set<Vertex> vertices;

	private OurModel() {
		vertices = new HashSet<Vertex>();
	}

	public boolean isEmpty() {
		return vertices.size() == 0;
	}

	public static OurModel MakeEmptyModel() {
		return new OurModel();
	}

}
