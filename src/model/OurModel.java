package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import tarjan.DfsEventsListener;
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

	/**
	 * Build an empty model: the encapsulated network has both the vertex set
	 * both the edge set equals to the empty set.
	 */
	public static OurModel makeEmptyModel() {
		return new OurModel(new HashSet<Vertex>());
	}

	public static OurModel makeOurModelFrom(Set<Vertex> vertices) {
		return new OurModel(vertices);
	}

	public static OurModel makeOurModelFrom(String path) {
		Connector connector = Connector.makeConnector(path);
		Set<Vertex> vertices = connector.readModel().parseModel();

		return OurModel.makeOurModelFrom(vertices);
	}

	@Override
	public void acceptExporter(DotExporter exporter) {
		for (Vertex vertex : vertices) {
			vertex.acceptExporter(exporter);
		}
	}

	public OurModel runDepthFirstSearch(DfsEventsListener dfsEventListener) {
		dfsEventListener.searchStarted();
		// TODO: decide if sort the vertices or use a datastructure that capture
		// some order. (SortedTreeSet o qualcosa di simile)
		Map<Vertex, BooleanInstance> map = new HashMap<Vertex, OurModel.BooleanInstance>();

		for (Vertex v : vertices) {
			map.put(v, new BooleanInstance(v));
		}

		DfsVertexExplorer vertexExplorer = null;

		for (Vertex v : vertices) {
			map.get(v).ifNotExplored(vertexExplorer);
		}

		dfsEventListener.searchCompleted();
		return this;
	}

	// TODO portare questa interfaccia in un file a se stante
	private interface DfsVertexExplorer {
		void explore(Vertex v);
	}

	// TODO: questa potrebbe diventare un decoratore della classe Vertex
	private static class BooleanInstance {
		private boolean explored;
		private final Vertex vertex;

		BooleanInstance(Vertex v) {
			this.vertex = v;
			this.explored = false;
		}

		public BooleanInstance ifNotExplored(DfsVertexExplorer vertexExplorer) {
			if (explored == false) {
				this.explored = true;
				vertexExplorer.explore(vertex);
			}
			return this;
		}
	}
}
