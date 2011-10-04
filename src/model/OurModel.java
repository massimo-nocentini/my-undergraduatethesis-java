package model;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import model.ExploreStatedWrapperVertex.ExploreStateWrapperVertexMapper;
import tarjan.DfsEventsListener;
import tarjan.DfsExplorer;
import JSBMLInterface.Connector;
import dotInterface.DotExportable;
import dotInterface.DotExporter;

public class OurModel implements DotExportable {

	private final Set<Vertex> vertices;

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

	private OurModel runDepthFirstSearch(DfsExplorer dfsVertexExplorer) {

		Map<Vertex, ExploreStatedWrapperVertex> map = makeDfsVertexMetadataMap();

		dfsVertexExplorer.searchStarted(map);

		for (Entry<Vertex, ExploreStatedWrapperVertex> entry : map.entrySet()) {

			entry.getValue().ifNotExplored(dfsVertexExplorer);
		}

		dfsVertexExplorer.searchCompleted(map);

		return this;
	}

	public OurModel runDepthFirstSearch(DfsEventsListener dfsEventsListener) {

		final Map<Vertex, ExploreStatedWrapperVertex> map = makeDfsVertexMetadataMap();

		dfsEventsListener.searchStarted(map);

		for (Entry<Vertex, ExploreStatedWrapperVertex> entry : map.entrySet()) {

			entry.getValue().ifNotExplored(dfsEventsListener,
					new ExploreStateWrapperVertexMapper() {

						@Override
						public ExploreStatedWrapperVertex map(Vertex vertex) {
							return map.get(vertex);
						}
					});
		}

		dfsEventsListener.searchCompleted(map);

		return this;
	}

	public OurModel runDepthFirstSearch(final DfsExplorer dfsVertexExplorer,
			final Vertex startingVertex) {

		final Map<Vertex, ExploreStatedWrapperVertex> map = makeDfsVertexMetadataMap();

		dfsVertexExplorer.searchStarted(map);

		findVertexByExampleAndApplyLogicOnIt(startingVertex,
				new VertexLogicApplier() {

					@Override
					public void apply(Vertex vertex) {
						map.get(vertex).ifNotExplored(dfsVertexExplorer);
					}
				});

		dfsVertexExplorer.searchCompleted(map);

		return this;
	}

	private Map<Vertex, ExploreStatedWrapperVertex> makeDfsVertexMetadataMap() {

		final Map<Vertex, ExploreStatedWrapperVertex> map = new TreeMap<Vertex, ExploreStatedWrapperVertex>();

		doOnVertices(new VertexLogicApplier() {

			@Override
			public void apply(Vertex vertex) {
				map.put(vertex, new ExploreStatedWrapperVertex(vertex));
			}
		});

		return Collections.unmodifiableMap(map);
	}

	public void doOnVertices(VertexLogicApplier vertexLogicApplier) {
		for (Vertex vertex : vertices) {
			vertexLogicApplier.apply(vertex);
		}

	}

	public static String getDefaultCompartmentId() {
		String compartment_id = "compartment_id";
		return compartment_id;
	}

	public OurModel findVertexByExampleAndApplyLogicOnIt(
			final Vertex exampleVertex, final VertexLogicApplier logicApplier) {

		if (vertices.contains(exampleVertex)) {
			doOnVertices(new VertexLogicApplier() {

				@Override
				public void apply(Vertex vertex) {

					if (vertex.equals(exampleVertex)) {
						logicApplier.apply(vertex);
					}
				}
			});
		}

		return this;
	}

	public static void collectEdgeDefinitionInto(
			Set<String> expectedEdgeDefinitionPart, Vertex source,
			Vertex neighbour) {

		StringWriter identifierWriter = new StringWriter();

		source.collectEdgeDefinitionInto(identifierWriter, neighbour);

		try {
			identifierWriter.close();
			expectedEdgeDefinitionPart.add(identifierWriter.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void collectVertexDefinitionInto(
			Set<String> expectedVertexDefinitionPart, Vertex vertex) {

		Writer identifierWriter = new StringWriter();

		vertex.collectYourDefinitionInto(identifierWriter);

		try {
			identifierWriter.close();
			expectedVertexDefinitionPart.add(identifierWriter.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
