package model;

import java.util.HashSet;
import java.util.Set;

import dotInterface.DotExportable;
import dotInterface.DotExporter;

public class OurModel implements DotExportable {

	private Set<Vertex> vertices;

	private OurModel() {
		vertices = new HashSet<Vertex>();
	}

	public boolean isEmpty() {
		return vertices.size() == 0;
	}

	public static OurModel makeEmptyModel() {
		return new OurModel();
	}

	public static OurModel makeModel(Set<Vertex> vertices) {
		OurModel ourModel = OurModel.makeEmptyModel();
		ourModel.vertices.addAll(vertices);
		return ourModel;
	}

	@Override
	public void acceptExporter(DotExporter exporter) {
		for (Vertex vertex : vertices) {
			vertex.acceptExporter(exporter);
		}
	}

}
