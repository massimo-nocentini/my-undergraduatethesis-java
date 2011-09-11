package model;

import java.util.HashSet;
import java.util.Set;

public class Vertex {

	private Set<Vertex> _neighbours;

	private Vertex() {
		_neighbours = new HashSet<Vertex>();
	}

	public static Vertex makeVertex() {
		return new Vertex();
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

}
