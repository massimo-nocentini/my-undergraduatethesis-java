package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

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
}
