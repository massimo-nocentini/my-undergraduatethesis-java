package model;

public class DfsWrapperVertex extends WrapperVertex {

	private int preVisitClock;
	private int postVisitClock;

	public DfsWrapperVertex previsitedAt(int instant) {
		preVisitClock = instant;
		return this;
	}

	public boolean isYourPreVisitClock(int otherClock) {
		return preVisitClock == otherClock;
	}

	public DfsWrapperVertex postvisitedAt(int other) {
		postVisitClock = other;
		return this;
	}

	public boolean isYourPostVisitClock(int otherClock) {
		return postVisitClock == otherClock;
	}

	DfsWrapperVertex(Vertex wrappingVertex) {
		super(wrappingVertex);
	}

}