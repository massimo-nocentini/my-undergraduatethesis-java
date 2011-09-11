package model;

import java.util.HashSet;
import java.util.Set;

public class Vertex {

	private Set<Vertex> _neighbours;

	private String id;

	private Vertex(String id) {
		this.id = id;
		_neighbours = new HashSet<Vertex>();
	}

	public static Vertex makeVertex() {
		int id = (int) (Math.random() * 100000000);
		return Vertex.makeVertex("id_" + String.valueOf(id));
	}

	Set<Vertex> getNeighbours() {
		return _neighbours;
	}

	public Vertex addNeighbour(Vertex neigtbour) {
		this._neighbours.add(neigtbour);
		return this;
	}

	public void doOnNeighbours(INeighbourApplier applier) {
		for (Vertex vertex : this._neighbours) {
			applier.apply(vertex);
		}

	}

	public static Vertex makeVertex(String id) {
		return new Vertex(id);
	}

	public String getId() {
		return id;
	}

}
