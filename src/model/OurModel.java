package model;

import java.util.HashSet;
import java.util.Set;

import JSBMLInterface.Connector;
import dotInterface.DotExportable;
import dotInterface.DotExporter;

public class OurModel implements DotExportable {

	private Set<Vertex> vertices;

	private OurModel(Set<Vertex> vertices) {
		this.vertices = new HashSet<Vertex>();
		for (Vertex vertex : vertices) {
			this.vertices.add(vertex);
		}
	}

	public boolean isEmpty() {
		return vertices.size() == 0;
	}

	public static OurModel makeEmptyModel() {
		return new OurModel(new HashSet<Vertex>());
	}

	public static OurModel makeModel(Set<Vertex> vertices) {
		return new OurModel(vertices);
	}

	public static OurModel makeOurModel(String path) {
		Connector connector = Connector.makeConnector(path);
		Set<Vertex> vertices = connector.readModel().parseModel();

		return OurModel.makeModel(vertices);
	}

	@Override
	public void acceptExporter(DotExporter exporter) {
		for (Vertex vertex : vertices) {
			vertex.acceptExporter(exporter);
		}
	}

}
