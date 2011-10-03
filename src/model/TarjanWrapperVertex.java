package model;

public class TarjanWrapperVertex extends WrapperVertex {

	private int exploredInstant;

	private ConnectedComponentWrapperVertex connectedComponent;

	TarjanWrapperVertex(Vertex wrappingVertex) {
		super(wrappingVertex);
	}

	public void exploredAt(int clock) {
		exploredInstant = clock;
	}

	public boolean isYourExploredInstantEquals(int instant) {
		return exploredInstant == instant;
	}

	public boolean areYouMemberInSomeConnectedComponent() {
		return connectedComponent != null && connectedComponent.isMember(this);
	}

	public void joinConnectedComponent(
			ConnectedComponentWrapperVertex connectedComponent) {

		this.connectedComponent = connectedComponent;
		connectedComponent.includeMember(this);
	}

	public boolean hadYouBeenDiscoveredFirstThan(TarjanWrapperVertex otherVertex) {
		return exploredInstant < otherVertex.exploredInstant;
	}

	public void bridgeConnectedComponentOf(TarjanWrapperVertex otherVertex) {

		if (this.areYouMemberInSomeConnectedComponent() == false) {
			throw new RuntimeException(
					"Impossible to haven't a component associated at this "
							+ "wrapper vertex at this point of the computation");
		}

		// if the two vertices belong to the same connected component then
		// we have to skip adding a self loop from the connected component to it
		// self
		if (connectedComponent.equals(otherVertex.connectedComponent)) {
			return;
		}

		connectedComponent.addNeighbour(otherVertex.connectedComponent);
	}

}
