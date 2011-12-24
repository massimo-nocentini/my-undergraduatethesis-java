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
import JSBMLInterface.Connector;
import dotInterface.DotExportable;
import dotInterface.DotExporter;

public class OurModel implements DotExportable {

	private final Set<Vertex> vertices;

	private OurModel(Set<Vertex> vertices) {
		this.vertices = new TreeSet<Vertex>(vertices);
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
		Set<Vertex> vertices = connector.parseModel();

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

	// public OurModel runDepthFirstSearch(final DfsExplorer dfsVertexExplorer,
	// final Vertex startingVertex) {
	//
	// final Map<Vertex, ExploreStatedWrapperVertex> map =
	// makeDfsVertexMetadataMap();
	//
	// dfsVertexExplorer.searchStarted(map);
	//
	// findVertexByExampleAndApplyLogicOnIt(startingVertex,
	// new VertexLogicApplier() {
	//
	// @Override
	// public void apply(Vertex vertex) {
	// map.get(vertex).ifNotExplored(dfsVertexExplorer);
	// }
	// });
	//
	// dfsVertexExplorer.searchCompleted(map);
	//
	// return this;
	// }

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

	public Vertex collapseSources() {
		final Set<Vertex> sources = new TreeSet<Vertex>();

		final Vertex newCollapsedSource = VertexFactory.makeSimpleVertex();

		final VertexLogicApplier appendNeighbourToNewSource = new VertexLogicApplier() {

			@Override
			public void apply(Vertex vertex) {
				newCollapsedSource.addNeighbour(vertex);
			}
		};

		VertexLogicApplier sourceCollapser = new VertexLogicApplier() {

			@Override
			public void apply(Vertex source) {

				// keep track of the source that I'm now currently scanning
				sources.add(source);

				// for each neighbor of this current source we add
				// them to the neighborhood of the new source that
				// collapse the others.
				source.doOnNeighbors(appendNeighbourToNewSource);
			}
		};

		this.applyOnSources(sourceCollapser);

		// now we can add the source to the vertices set if
		// it is not a sink (hence must have at least a neighbor)
		if (newCollapsedSource.isSink() == false) {
			this.vertices.add(newCollapsedSource);

			for (Vertex source : sources) {
				// for all neighbors of the source that we want to remove
				// we have to broke the direct ancestor relation, decoupling
				// the neighbor from the source that are going to be deleted.
				source.brokeYourNeighborhoodRelations();

				// now we can erase the source from our model
				vertices.remove(source);
			}
		} else {
			// we haven't introduced any new source so we
			// don't return nothing.
			return null;
		}

		return newCollapsedSource;
	}

	private void applyOnSources(VertexLogicApplier sourceCollapser) {
		for (Vertex vertex : vertices) {
			if (vertex.isSource() == true) {
				sourceCollapser.apply(vertex);
			}
		}
	}

	public boolean isVertices(Set<Vertex> vertices) {
		return this.vertices.equals(vertices);
	}

	public boolean isVerticesCount(int guess) {
		return vertices.size() == guess;
	}

	public OurModel cloneYourself() {

		return OurModel.makeOurModelFrom(vertices);
	}

}
