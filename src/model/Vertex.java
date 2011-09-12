package model;

import java.util.HashSet;
import java.util.Set;

public class Vertex {

	static String IdPrefix = "id_";

	static class VertexInstancesCounter {
		private static int count;

		static {
			count = 0;
		}

		synchronized static int makeNewId() {
			count = count + 1;
			return count;
		}

		static int getCount() {
			return count;
		}
	}

	private Set<Vertex> neighbours;

	private String id;

	private Vertex(String id) {
		this.id = id;
		neighbours = new HashSet<Vertex>();
	}

	public static Vertex makeVertex() {
		int id = VertexInstancesCounter.makeNewId();
		return Vertex.makeVertex(IdPrefix + String.valueOf(id));
	}

	Set<Vertex> getNeighbours() {
		return neighbours;
	}

	public Vertex addNeighbour(Vertex neigtbour) {
		this.neighbours.add(neigtbour);
		return this;
	}

	public void doOnNeighbours(INeighbourApplier applier) {
		for (Vertex vertex : this.neighbours) {
			applier.apply(vertex);
		}

	}

	public static Vertex makeVertex(String id) {
		return new Vertex(id);
	}

	public String getId() {
		return id;
	}

	public boolean isYourNeighbourhoodEquals(Set<Vertex> products) {
		return this.neighbours.equals(products);
	}

	public boolean isYourNeighbourhoodEmpty() {
		return this.neighbours.size() == 0;
	}

}
