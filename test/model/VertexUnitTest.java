package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import model.Vertex.VertexInstancesCounter;

import org.junit.Test;
import org.sbml.jsbml.Compartment;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.Species;

public class VertexUnitTest {
	@Test
	public void createVertex() {
		Vertex vertex = Vertex.makeVertex();
		assertNotNull(vertex);
	}

	@Test
	public void vertexEmptyNeighboursSet() {
		Vertex vertex = Vertex.makeVertex();
		Set<Vertex> neighbours = vertex.getNeighbours();

		assertNotNull("Neighbours set is null", neighbours);
		assertEquals(0, neighbours.size());
	}

	@Test
	public void containsNeighbours() {
		Vertex vertex = Vertex.makeVertex();
		Vertex a = Vertex.makeVertex();
		Vertex b = Vertex.makeVertex();

		vertex.addNeighbour(a).addNeighbour(b);

		Vertex c = Vertex.makeVertex();

		Set<Vertex> neighbours = vertex.getNeighbours();
		assertEquals(2, neighbours.size());
		assertTrue(neighbours.contains(a));
		assertTrue(neighbours.contains(b));
		assertFalse(neighbours.contains(c));
	}

	@Test
	public void containsReverseNeighbours() {
		Vertex vertex = Vertex.makeVertex();
		Vertex a = Vertex.makeVertex();

		vertex.addNeighbour(a);
		assertTrue(vertex.getNeighbours().contains(a));
		assertFalse(a.getNeighbours().contains(vertex));
	}

	@Test
	public void fluentAddNeighbourReturnTheSameVertex() {
		Vertex vertex = Vertex.makeVertex();
		Vertex a = Vertex.makeVertex();

		Vertex vertex2 = vertex.addNeighbour(a);
		assertSame("The vertex returned by Vertex.addNeighbour(...) "
				+ "is a completely new object.", vertex, vertex2);
	}

	@Test
	public void encapsulatedNeighboursDoApply() {
		Vertex vertex = Vertex.makeVertex();
		Vertex a = Vertex.makeVertex();
		Vertex b = Vertex.makeVertex();

		vertex.addNeighbour(a).addNeighbour(b);

		Vertex c = Vertex.makeVertex();

		final Set<Vertex> collectedNeighbours = new HashSet<Vertex>();

		INeighbourApplier applier = new INeighbourApplier() {

			@Override
			public void apply(Vertex vertex) {
				collectedNeighbours.add(vertex);
			}
		};

		vertex.doOnNeighbours(applier);

		assertEquals(2, collectedNeighbours.size());
		assertTrue(collectedNeighbours.contains(a));
		assertTrue(collectedNeighbours.contains(b));
		assertEquals(vertex.getNeighbours(), collectedNeighbours);
		assertNotSame(vertex.getNeighbours(), collectedNeighbours);
		assertFalse(collectedNeighbours.contains(c));
	}

	@Test
	public void makeVertexWithId() {
		String id = "someId";
		Vertex v = Vertex.makeVertex(id);

		Assert.assertNotNull(v);
		assertSame(id, v.getId());
		assertEquals(id, v.getId());
	}

	@Test
	public void makeVertexWithoutSpecifingId() {
		Vertex v = Vertex.makeVertex();

		Assert.assertNotNull(v);
		Assert.assertNotNull(v.getId());
		assertTrue(v.getId().length() > 0);
	}

	@Test
	public void newVerticesHaveDifferentId() {
		Vertex v1 = Vertex.makeVertex();
		Vertex v2 = Vertex.makeVertex();

		Assert.assertNotSame(v1, v2);
		Assert.assertFalse(v1.getId().equals(v2.getId()));
	}

	@Test
	public void newVerticesHaveConsecutiveId() {

		// catch the count of the vertices added so far
		int count = VertexInstancesCounter.getCount();

		// increment the counter
		Vertex v1 = Vertex.makeVertex();

		// increment the counter another time
		Vertex v2 = Vertex.makeVertex();

		Assert.assertEquals(Vertex.IdPrefix + String.valueOf(count + 1),
				v1.getId());
		Assert.assertEquals(Vertex.IdPrefix + String.valueOf(count + 2),
				v2.getId());
	}

	@Test
	public void emptyNeighborhood() {

		Vertex v1 = Vertex.makeVertex();

		Assert.assertTrue(v1.isYourNeighborhoodEmpty());
		Assert.assertEquals(0, v1.getNeighbours().size());
	}

	@Test
	public void nonEmptyNeighborhood() {

		Vertex v1 = Vertex.makeVertex();
		Vertex v2 = Vertex.makeVertex();

		v1.addNeighbour(v2);

		Assert.assertFalse(v1.isYourNeighborhoodEmpty());
		Assert.assertTrue(v1.getNeighbours().size() > 0);
	}

	@Test
	public void neighborhoodEquals() {

		Vertex v1 = Vertex.makeVertex();
		Vertex v2 = Vertex.makeVertex();
		Vertex v3 = Vertex.makeVertex();
		Vertex v4 = Vertex.makeVertex();

		v1.addNeighbour(v2);
		v1.addNeighbour(v3);
		v1.addNeighbour(v4);

		Set<Vertex> expected = new HashSet<Vertex>();

		expected.add(v2);
		expected.add(v3);
		expected.add(v4);

		Assert.assertFalse(v1.isYourNeighborhoodEmpty());
		Assert.assertTrue(v1.isYourNeighborhoodEquals(expected));
	}

	@Test
	public void neighborhoodNotEquals() {

		Vertex v1 = Vertex.makeVertex();
		Vertex v2 = Vertex.makeVertex();
		Vertex v3 = Vertex.makeVertex();
		Vertex v4 = Vertex.makeVertex();

		v1.addNeighbour(v2);
		v1.addNeighbour(v3);
		v1.addNeighbour(v4);

		Set<Vertex> anotherNeighbourhood = new HashSet<Vertex>();
		Vertex v5 = Vertex.makeVertex();

		anotherNeighbourhood.add(v3);
		anotherNeighbourhood.add(v5);

		Assert.assertFalse(v1.isYourNeighborhoodEmpty());
		Assert.assertFalse(v1.isYourNeighborhoodEquals(anotherNeighbourhood));
	}

	@Test
	public void neighbourhoodEqualsVacouslyTrue() {

		Vertex v1 = Vertex.makeVertex();

		// v1 has empty neighborhood

		Set<Vertex> anotherNeighborhood = new HashSet<Vertex>();
		Vertex v3 = Vertex.makeVertex();
		Vertex v4 = Vertex.makeVertex();
		Vertex v5 = Vertex.makeVertex();

		anotherNeighborhood.add(v3);
		anotherNeighborhood.add(v4);
		anotherNeighborhood.add(v5);

		Assert.assertFalse(v1.isYourNeighborhoodEquals(anotherNeighborhood));
	}

	@Test
	public void neighborhoodEqualsVacouslyTrueReversed() {

		Vertex v1 = Vertex.makeVertex();
		Vertex v2 = Vertex.makeVertex();
		Vertex v3 = Vertex.makeVertex();
		Vertex v4 = Vertex.makeVertex();

		v1.addNeighbour(v2);
		v1.addNeighbour(v3);
		v1.addNeighbour(v4);

		// empty testing neighborhood
		Set<Vertex> anotherNeighborhood = new HashSet<Vertex>();

		Assert.assertFalse(v1.isYourNeighborhoodEquals(anotherNeighborhood));
	}

	@Test
	public void neighborhoodDoesntContainsDoubles() {

		Vertex v1 = Vertex.makeVertex();
		Vertex v2 = Vertex.makeVertex();
		Vertex v3 = Vertex.makeVertex();
		Vertex v4 = Vertex.makeVertex();

		v1.addNeighbour(v2);
		v1.addNeighbour(v3);
		v1.addNeighbour(v4);

		// doubling
		v1.addNeighbour(v2);
		v1.addNeighbour(v3);
		v1.addNeighbour(v4);

		// empty testing neighborhood
		Set<Vertex> anotherNeighborhood = new HashSet<Vertex>();
		anotherNeighborhood.add(v2);
		anotherNeighborhood.add(v3);
		anotherNeighborhood.add(v4);

		Assert.assertEquals(3, v1.getNeighbours().size());
		Assert.assertTrue(v1.isYourNeighborhoodEquals(anotherNeighborhood));
	}

	@Test
	public void fetchSpeciesCharacteristicData() {
		Model sbmlModel = new Model();

		String compartmentId = "compartment_id";
		Compartment compartment = sbmlModel.createCompartment(compartmentId);

		String speciesId = "species_id";
		Species firstAdded = sbmlModel.createSpecies(speciesId, compartment);

		Vertex vertex = Vertex.makeVertex(firstAdded);

		Assert.assertNotNull(vertex);
		Assert.assertTrue(vertex.isYourSpeciesId(speciesId));
		Assert.assertTrue(vertex.isYourCompartmentId(compartmentId));
		Assert.assertFalse(vertex.isYourSpeciesId(""));
		Assert.assertFalse(vertex.isYourCompartmentId(""));
	}

	@Test
	public void vertexEquivalenceSameSpeciesIdDifferentCompartmentId() {

		String compartmentId = "compartment_id";
		Compartment compartment = new Compartment(compartmentId);

		String anotherCompartmentId = "anotherCompartment_id";
		Compartment anotherCompartment = new Compartment(anotherCompartmentId);

		String speciesId = "species_id";
		Species firstAdded = new Species(speciesId);
		firstAdded.setCompartment(compartment);

		Species secondAdded = new Species(speciesId);
		secondAdded.setCompartment(anotherCompartment);

		Vertex vertex = Vertex.makeVertex(firstAdded);
		Vertex anotherVertex = Vertex.makeVertex(secondAdded);

		Assert.assertNotSame(vertex, anotherVertex);
		Assert.assertFalse(vertex.equals(anotherVertex));
	}

	@Test
	public void vertexEquivalenceDifferentSpeciesIdSameCompartmentId() {

		String compartmentId = "compartment_id";
		Compartment compartment = new Compartment(compartmentId);

		Species firstAdded = new Species("species_id");
		firstAdded.setCompartment(compartment);

		Species secondAdded = new Species("another_species_id");
		secondAdded.setCompartment(compartment);

		Vertex vertex = Vertex.makeVertex(firstAdded);
		Vertex anotherVertex = Vertex.makeVertex(secondAdded);

		Assert.assertNotSame(vertex, anotherVertex);
		Assert.assertFalse(vertex.equals(anotherVertex));
	}

	@Test
	public void vertexIsYourOrigin() {

		String compartmentId = "compartment_id";
		Compartment compartment = new Compartment(compartmentId);

		Species firstAdded = new Species("species_id");
		firstAdded.setCompartment(compartment);

		Species secondAdded = new Species("another_species_id");
		secondAdded.setCompartment(compartment);

		Vertex vertex = Vertex.makeVertex(firstAdded);

		Assert.assertTrue(vertex.isYourOrigin(firstAdded));
		Assert.assertFalse(vertex.isYourOrigin(secondAdded));
	}
}
