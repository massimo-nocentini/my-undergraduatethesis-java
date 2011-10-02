package model;

public class TarjanWrapperVertex extends WrapperVertex {

	private int exploredInstant;

	private Vertex connectedComponent;

	TarjanWrapperVertex(Vertex wrappingVertex) {
		super(wrappingVertex);
	}

	public void exploredAt(int clock) {
		exploredInstant = clock;
	}

	public boolean isYourExploredInstantEquals(int instant) {
		return exploredInstant == instant;
	}

	public boolean haveYouParentComponent() {
		// TODO: qui sarebbe meglio usare un downcast per chiedere alla
		// componente connessa se effettivamente contiene questo vertice
		return connectedComponent != null;
	}

	public void joinConnectedComponent(Vertex connectedComponent) {
		this.connectedComponent = connectedComponent;
	}

	public boolean hadYouBeenDiscoveredFirstThan(TarjanWrapperVertex otherVertex) {

		return exploredInstant < otherVertex.exploredInstant;

	}

}
