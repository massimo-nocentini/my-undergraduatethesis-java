package model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.Assert;
import model.Vertex.DoAction;

import org.junit.Test;
import org.sbml.jsbml.Model;

import tarjan.DfsEventsListener;
import tarjan.DfsEventsListenerNullImplementor;
import util.CallbackSignalRecorder;
import dotInterface.DotFileUtilHandler;

public class OurModelUnitTest {

	@Test
	public void creation() {
		OurModel model = OurModel.makeEmptyModel();
		assertNotNull(model);
	}

	@Test
	public void emptyAfterCreation() {
		OurModel model = OurModel.makeEmptyModel();
		assertTrue(model.isEmpty());
	}

	@Test
	public void addingNeighborsOrderIsPreservedByVertex() {
		Vertex vertex = VertexFactory.makeSimpleVertex();
		Vertex a1 = VertexFactory.makeSimpleVertex();
		Vertex a2 = VertexFactory.makeSimpleVertex();
		Vertex a3 = VertexFactory.makeSimpleVertex();

		Set<Vertex> vertices = new TreeSet<Vertex>();
		vertices.add(vertex);
		vertices.add(a1);
		vertices.add(a2);
		vertices.add(a3);

		OurModel customModel = OurModel.makeOurModelFrom(vertices);

		List<Vertex> desiredVerticesSequence = new LinkedList<Vertex>(
				Arrays.asList(vertex, a1, a2, a3));

		final List<Vertex> actualVerticesSequence = new LinkedList<Vertex>();

		customModel.doOnVertices(new VertexLogicApplier() {

			@Override
			public void apply(Vertex neighbourVertex) {
				actualVerticesSequence.add(neighbourVertex);
			}
		});

		Assert.assertEquals(desiredVerticesSequence, actualVerticesSequence);
	}

	/**
	 * This test method establish the protocol of messages which a client have
	 * to send to a OurModel object in order to request the run of a Depth First
	 * Search algorithm on the network encapsulated by the OurModel object. <br>
	 * <br>
	 * This method ensures that the method {@link OurModel}
	 * .runDepthFirstSearch(DfsEventsListener) return a reference to an instance
	 * of the class OurModel and that instance must be the same instance which
	 * receive the message (in other words the method runDepthFirstSearch is
	 * fluent respect to its class).
	 */
	@Test
	public void applyDfsSearchOnAllVertices() {

		OurModel tarjanModel = ModelsRepository.makeTarjanModel();

		OurModel returnedtarjanModel = tarjanModel
				.runDepthFirstSearch(new DfsEventsListenerNullImplementor());

		Assert.assertNotNull(returnedtarjanModel);
		Assert.assertSame(tarjanModel, returnedtarjanModel);

	}

	@Test
	public void applyDfsSearchForSingleVertex() {

		OurModel tarjanModel = ModelsRepository.makeTarjanModel();

		DfsEventsListener dfsEventListener = new DfsEventsListenerNullImplementor();

		OurModel returnedtarjanModel = tarjanModel
				.runDepthFirstSearch(dfsEventListener);

		Assert.assertNotNull(returnedtarjanModel);
		Assert.assertSame(tarjanModel, returnedtarjanModel);

	}

	@Test(expected = NullPointerException.class)
	public void applyDfsSearchWithNullListenerThrowsException() {

		OurModel tarjanModel = ModelsRepository.makeTarjanModel();

		DfsEventsListener dfsEventListener = null;

		tarjanModel.runDepthFirstSearch(dfsEventListener);

	}

	@Test
	public void retrieveVertexAndApplyLogic() {
		String compartment_id = "compartment_id";
		String species_id = "species_id";
		final Vertex v = VertexFactory.makeSimpleVertex(species_id,
				compartment_id);
		Vertex v2 = VertexFactory.makeSimpleVertex();
		Vertex v3 = VertexFactory.makeSimpleVertex();

		v.addNeighbour(v2);

		Set<Vertex> vertices = new HashSet<Vertex>();
		vertices.add(v);
		vertices.add(v2);
		vertices.add(v3);

		final Vertex exampleVertex = VertexFactory.makeSimpleVertex(species_id,
				compartment_id);

		final CallbackSignalRecorder recorder = new CallbackSignalRecorder();

		VertexLogicApplier logicApplier = new VertexLogicApplier() {

			@Override
			public void apply(Vertex vertex) {
				Assert.assertEquals(exampleVertex, vertex);
				Assert.assertNotSame(exampleVertex, vertex);

				Assert.assertFalse(v.isYourNeighborhoodEmpty());
				Assert.assertTrue(exampleVertex.isYourNeighborhoodEmpty());

				recorder.signal();
			}
		};

		OurModel someModel = OurModel.makeOurModelFrom(vertices);
		OurModel outputModel = someModel.findVertexByExampleAndApplyLogicOnIt(
				exampleVertex, logicApplier);

		Assert.assertSame(someModel, outputModel);
		Assert.assertTrue(recorder.isSignaled());
	}

	@Test
	public void retrieveVertexAndApplyLogic_VacouslySearch() {
		String compartment_id = "compartment_id";
		String species_id = "species_id";
		final Vertex v = VertexFactory.makeSimpleVertex(species_id,
				compartment_id);
		Vertex v2 = VertexFactory.makeSimpleVertex();
		Vertex v3 = VertexFactory.makeSimpleVertex();

		v.addNeighbour(v2);

		Set<Vertex> vertices = new HashSet<Vertex>();
		vertices.add(v);
		vertices.add(v2);
		vertices.add(v3);

		final Vertex exampleVertex = VertexFactory.makeSimpleVertex(
				"some_different_species_id", compartment_id);

		final CallbackSignalRecorder recorder = new CallbackSignalRecorder();

		VertexLogicApplier logicApplier = new VertexLogicApplier() {

			@Override
			public void apply(Vertex vertex) {
				Assert.fail("This logic shouldn't be applied, the example vertex is not in the model.");
				recorder.signal();
			}
		};

		OurModel someModel = OurModel.makeOurModelFrom(vertices);

		@SuppressWarnings("unused")
		OurModel outputModel = someModel.findVertexByExampleAndApplyLogicOnIt(
				exampleVertex, logicApplier);

		Assert.assertFalse(recorder.isSignaled());
	}

	@Test
	public void collapseAllSourcesIntoOneOfEmptyModel() {
		OurModel model = OurModel.makeEmptyModel();

		Vertex newSource = model.collapseSources();

		final CallbackSignalRecorder recorder = new CallbackSignalRecorder();

		VertexLogicApplier sourcesRecorder = new VertexLogicApplier() {

			@Override
			public void apply(Vertex vertex) {
				if (vertex.isSource()) {
					recorder.signal();
				}
			}
		};

		model.doOnVertices(sourcesRecorder);

		Assert.assertNull(newSource);
		Assert.assertFalse(recorder.isSignaled());
	}

	@Test
	public void collapseTheSingleSourceIntoANewOne() {

		Set<Vertex> vertices = new HashSet<Vertex>();

		Vertex source = VertexFactory.makeSimpleVertex();
		Vertex sink = VertexFactory.makeSimpleVertex();

		source.addNeighbour(sink);

		vertices.add(source);
		vertices.add(sink);

		OurModel collapsedModel = OurModel.makeOurModelFrom(vertices);

		Vertex newSource = collapsedModel.collapseSources();

		Set<Vertex> expectedVerticesInsideModel = new HashSet<Vertex>();
		expectedVerticesInsideModel.add(newSource);
		expectedVerticesInsideModel.add(sink);

		final CallbackSignalRecorder recorder = new CallbackSignalRecorder();

		VertexLogicApplier sourcesRecorder = new VertexLogicApplier() {

			@Override
			public void apply(Vertex vertex) {
				if (vertex.isSource() == true) {
					recorder.signal();
				}
			}
		};

		Assert.assertNotNull(collapsedModel);

		collapsedModel.doOnVertices(sourcesRecorder);

		Assert.assertTrue(recorder.isCountOfSignals(1));
		Assert.assertTrue(collapsedModel.isVerticesCount(2));

		Assert.assertFalse(collapsedModel.isVertices(vertices));
		Assert.assertTrue(collapsedModel
				.isVertices(expectedVerticesInsideModel));
	}

	@Test
	public void collapseSources() {

		Set<Vertex> vertices = new HashSet<Vertex>();

		Vertex source1 = VertexFactory.makeSimpleVertex();
		Vertex source2 = VertexFactory.makeSimpleVertex();
		Vertex source3 = VertexFactory.makeSimpleVertex();

		Vertex white = VertexFactory.makeSimpleVertex();

		Vertex sink = VertexFactory.makeSimpleVertex();

		source1.addNeighbour(white);
		source2.addNeighbour(white);

		source3.addNeighbour(sink);

		white.addNeighbour(sink);

		vertices.add(source1);
		vertices.add(source2);
		vertices.add(source3);
		vertices.add(white);
		vertices.add(sink);

		OurModel collapsedModel = OurModel.makeOurModelFrom(vertices);

		Vertex newSource = collapsedModel.collapseSources();

		Set<Vertex> expectedVerticesInsideModel = new HashSet<Vertex>();
		expectedVerticesInsideModel.add(newSource);
		expectedVerticesInsideModel.add(white);
		expectedVerticesInsideModel.add(sink);

		final CallbackSignalRecorder recorder = new CallbackSignalRecorder();

		VertexLogicApplier sourcesRecorder = new VertexLogicApplier() {

			@Override
			public void apply(Vertex vertex) {
				if (vertex.isSource() == true) {
					recorder.signal();
				}
			}
		};

		Assert.assertNotNull(collapsedModel);

		collapsedModel.doOnVertices(sourcesRecorder);

		Assert.assertTrue(recorder.isCountOfSignals(1));
		Assert.assertTrue(collapsedModel.isVerticesCount(3));

		Assert.assertFalse(collapsedModel.isVertices(vertices));
		Assert.assertTrue(collapsedModel
				.isVertices(expectedVerticesInsideModel));
	}

	@Test
	public void cloneOurModelWithoutVertices() {
		OurModel model = OurModel.makeEmptyModel();

		OurModel clone = model.cloneYourself();

		Assert.assertNotNull(clone);
		Assert.assertNotSame(model, clone);

		Assert.assertTrue(clone.isEmpty());
	}

	@Test
	public void cloneOurModelWithVertices() {

		Set<Vertex> vertices = new HashSet<Vertex>();
		Vertex v1 = VertexFactory.makeSimpleVertex();
		Vertex v2 = VertexFactory.makeSimpleVertex();
		Vertex v3 = VertexFactory.makeSimpleVertex();
		Vertex v4 = VertexFactory.makeSimpleVertex();

		// make a cycle
		v1.addNeighbour(v2);
		v2.addNeighbour(v3);
		v3.addNeighbour(v1);

		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);
		vertices.add(v4);

		OurModel model = OurModel.makeOurModelFrom(vertices);

		OurModel clone = model.cloneYourself();

		Assert.assertNotNull(clone);
		Assert.assertNotSame(model, clone);

		Assert.assertFalse(clone.isEmpty());
		Assert.assertTrue(clone.isVertices(vertices));

	}

	@Test
	public void realBartonellaQuintanaToulouse_Parsing_CollapsingSources() {

		OurModel bartonellaModel = OurModel.makeOurModelFrom(DotFileUtilHandler
				.getSbmlExampleModelsFolder().concat(
						"BartonellaQuintanaToulouse.xml"));

		@SuppressWarnings("unused")
		Vertex collapseSource = bartonellaModel.collapseSources();

		final CallbackSignalRecorder recorder = new CallbackSignalRecorder();

		VertexLogicApplier sourceRecorder = new VertexLogicApplier() {

			@Override
			public void apply(Vertex vertex) {
				if (vertex.isSource()) {
					recorder.signal();
				}
			}
		};

		bartonellaModel.doOnVertices(sourceRecorder);

		Assert.assertTrue(recorder.isCountOfSignals(1));
		Assert.assertFalse(recorder.isCountOfSignals(0));
		Assert.assertFalse(recorder.isCountOfSignals(49));
	}

	@Test
	public void check_setting_model_name_building_from_vertex_set() {

		Model model = new Model();
		String name = "model_name";
		model.setName(name);

		OurModel ourModel = OurModel.makeOurModelFrom(new TreeSet<Vertex>(),
				name);

		Assert.assertTrue(ourModel.isModelNameEquals(name));
		Assert.assertEquals(name, ourModel.supplyModelName());
		Assert.assertFalse(ourModel.isModelNameEquals(name.concat("_Smullyan")));
		Assert.assertFalse(ourModel.isModelNameEquals(""));
	}

	@Test
	public void check_setting_model_name_building_from_existing_models() {

		OurModel bartonella_model = OurModel
				.makeOurModelFrom(DotFileUtilHandler
						.getSbmlExampleModelsFolder().concat(
								"BartonellaQuintanaToulouse.xml"));
		Assert.assertTrue(bartonella_model
				.isModelNameEquals("Bartonella quintana"));
		Assert.assertFalse(bartonella_model.isModelNameEquals(""));

		OurModel candidatus_model = OurModel
				.makeOurModelFrom(DotFileUtilHandler
						.getSbmlExampleModelsFolder().concat(
								"CandidatusSulciamuelleriSMDSEM.xml"));
		Assert.assertTrue(candidatus_model
				.isModelNameEquals("Candidatus Sulcia muelleri"));
		Assert.assertFalse(candidatus_model.isModelNameEquals(""));

		OurModel escherichia_model = OurModel
				.makeOurModelFrom(DotFileUtilHandler
						.getSbmlExampleModelsFolder().concat(
								"EscherichiaColiK12.xml"));
		Assert.assertTrue(escherichia_model
				.isModelNameEquals("Escherichia coli"));
		Assert.assertFalse(escherichia_model.isModelNameEquals(""));

		OurModel mesorhizobium_model = OurModel
				.makeOurModelFrom(DotFileUtilHandler
						.getSbmlExampleModelsFolder().concat(
								"MesorhizobiumLotiMAFF303099.xml"));
		Assert.assertTrue(mesorhizobium_model
				.isModelNameEquals("Mesorhizobium loti"));
		Assert.assertFalse(mesorhizobium_model.isModelNameEquals(""));

		OurModel pseudomonas_model = OurModel
				.makeOurModelFrom(DotFileUtilHandler
						.getSbmlExampleModelsFolder().concat(
								"PseudomonasAeruginosaPAO1.xml"));
		Assert.assertTrue(pseudomonas_model
				.isModelNameEquals("Pseudomonas aeruginosa"));
		Assert.assertFalse(pseudomonas_model.isModelNameEquals(""));

		OurModel rhodobacter_model = OurModel
				.makeOurModelFrom(DotFileUtilHandler
						.getSbmlExampleModelsFolder().concat(
								"RhodobacterSphaeroides241.xml"));
		Assert.assertTrue(rhodobacter_model
				.isModelNameEquals("Rhodobacter sphaeroides"));
		Assert.assertFalse(rhodobacter_model.isModelNameEquals(""));

		OurModel rickettsia_model = OurModel
				.makeOurModelFrom(DotFileUtilHandler
						.getSbmlExampleModelsFolder().concat(
								"RickettsiaMassiliaeMTU5.xml"));
		Assert.assertTrue(rickettsia_model
				.isModelNameEquals("Rickettsia massiliae"));
		Assert.assertFalse(rickettsia_model.isModelNameEquals(""));

	}

	@Test
	public void assigning_container_model_to_vertex_should_return_back_the_same_model() {

		Vertex vertex = VertexFactory.makeSimpleVertex();

		final OurModel model = OurModel.makeEmptyModel();

		vertex.containedIn(model);

		final CallbackSignalRecorder callbackSignalRecorder = new CallbackSignalRecorder();

		vertex.doWithParentModel(new DoAction<OurModel>() {

			@Override
			public void apply(OurModel item, String species_id,
					String species_name, String compartment_id) {

				// register that actually this method has been called
				callbackSignalRecorder.signal();

				Assert.assertSame(model, item);
			}
		});

		// we have to check this also in order to verify that the
		// lambda is actually called.
		Assert.assertTrue(callbackSignalRecorder.isSignaled());
	}

	@Test
	public void building_a_model_from_some_vertices_should_assign_the_model_to_them() {

		Set<Vertex> vertices = new HashSet<Vertex>();
		Vertex v1 = VertexFactory.makeSimpleVertex();
		Vertex v2 = VertexFactory.makeSimpleVertex();
		Vertex v3 = VertexFactory.makeSimpleVertex();
		Vertex v4 = VertexFactory.makeSimpleVertex();

		// make a cycle
		v1.addNeighbour(v2);
		v2.addNeighbour(v3);
		v3.addNeighbour(v1);

		vertices.add(v1);
		vertices.add(v2);
		vertices.add(v3);
		vertices.add(v4);

		final OurModel model = OurModel.makeOurModelFrom(vertices);

		for (Vertex vertex : vertices) {
			vertex.doWithParentModel(new DoAction<OurModel>() {

				@Override
				public void apply(OurModel item, String species_id,
						String species_name, String compartment_id) {

					Assert.assertSame(model, item);
				}
			});
		}
	}

}
