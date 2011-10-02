package model;

import java.util.LinkedHashSet;
import java.util.Set;

public class ConnectedComponentWrapperVertex extends
		VertexWithLabelWrapperVertex {

	private final Set<Vertex> members;

	protected ConnectedComponentWrapperVertex() {
		// here we build a completely new vertex
		super(VertexFactory.makeSimpleVertex());

		members = new LinkedHashSet<Vertex>();
	}

	public boolean isMember(Vertex vertex) {
		return members.contains(vertex);
	}

	public void includeMember(Vertex vertex) {
		members.add(vertex);
	}

	@Override
	protected String provideOutsideLabel() {
		return "".concat(String.valueOf(members.size()));
	}

}
