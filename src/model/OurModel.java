package model;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import tarjan.DfsExplorer;
import tarjan.VertexDfsMetadata;
import JSBMLInterface.Connector;
import dotInterface.DotExportable;
import dotInterface.DotExporter;

public class OurModel implements DotExportable {

	private Set<Vertex> vertices;

	private OurModel(Set<Vertex> vertices) {

		this.vertices = new TreeSet<Vertex>();

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
	public void acceptExporter(final DotExporter exporter) {

		doOnVertices(new VertexLogicApplier() {

			@Override
			public void apply(Vertex vertex) {
				vertex.acceptExporter(exporter);
			}
		});
	}

	public OurModel runDepthFirstSearch(DfsExplorer dfsVertexExplorer) {

		Map<Vertex, VertexDfsMetadata> map = makeDfsVertexMetadataMap();

		dfsVertexExplorer.searchStarted(map);

		for (Entry<Vertex, VertexDfsMetadata> entry : map.entrySet()) {

			entry.getValue().ifNotExplored(dfsVertexExplorer);
		}

		dfsVertexExplorer.searchCompleted(map);

		return this;
	}

	private Map<Vertex, VertexDfsMetadata> makeDfsVertexMetadataMap() {

		final Map<Vertex, VertexDfsMetadata> map = new TreeMap<Vertex, VertexDfsMetadata>();

		doOnVertices(new VertexLogicApplier() {

			@Override
			public void apply(Vertex vertex) {
				map.put(vertex, new VertexDfsMetadata(vertex));
			}
		});

		return Collections.unmodifiableMap(map);
	}

	public void doOnVertices(VertexLogicApplier vertexLogicApplier) {
		for (Vertex vertex : vertices) {
			vertexLogicApplier.apply(vertex);
		}

	}

	public static OurModel makePapadimitriouModel() {

		final Vertex vA = Vertex.makeVertex();
		final Vertex vB = Vertex.makeVertex();
		final Vertex vE = Vertex.makeVertex();
		final Vertex vI = Vertex.makeVertex();
		final Vertex vJ = Vertex.makeVertex();
		final Vertex vC = Vertex.makeVertex();
		final Vertex vD = Vertex.makeVertex();
		final Vertex vG = Vertex.makeVertex();
		final Vertex vH = Vertex.makeVertex();
		final Vertex vK = Vertex.makeVertex();
		final Vertex vL = Vertex.makeVertex();
		final Vertex vF = Vertex.makeVertex();

		vA.addNeighbour(vB);
		vA.addNeighbour(vE);

		vB.addNeighbour(vA);

		vE.addNeighbour(vA);
		vE.addNeighbour(vI);
		vE.addNeighbour(vJ);

		vI.addNeighbour(vE);
		vI.addNeighbour(vJ);

		vJ.addNeighbour(vE);
		vJ.addNeighbour(vI);

		vC.addNeighbour(vD);
		vC.addNeighbour(vH);
		vC.addNeighbour(vG);

		vD.addNeighbour(vH);
		vD.addNeighbour(vC);

		vG.addNeighbour(vH);
		vG.addNeighbour(vC);
		vG.addNeighbour(vK);

		vH.addNeighbour(vD);
		vH.addNeighbour(vC);
		vH.addNeighbour(vG);
		vH.addNeighbour(vK);
		vH.addNeighbour(vL);

		vK.addNeighbour(vH);
		vK.addNeighbour(vG);

		vL.addNeighbour(vH);

		return OurModel.makeOurModelFrom(new TreeSet<Vertex>(
				Arrays.<Vertex> asList(vA, vB, vC, vD, vE, vF, vG, vH, vI, vJ,
						vK, vL)));

	}

}
