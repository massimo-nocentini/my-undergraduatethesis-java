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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((members == null) ? 0 : members.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ConnectedComponentWrapperVertex)) {
			return false;
		}
		ConnectedComponentWrapperVertex other = (ConnectedComponentWrapperVertex) obj;
		if (!super.equals(other.getWrappedVertex())) {
			return false;
		}
		if (members == null) {
			if (other.members != null) {
				return false;
			}
		} else if (!members.equals(other.members)) {
			return false;
		}
		return true;
	}

	@Override
	public void publishYourStatsOn(VertexStatsRecorder vertexStatsRecorder) {

		VertexVoteAccepter voteAccepter = vertexStatsRecorder
				.recordConnectedComponent(this.members.size());

		this.voteOn(voteAccepter);

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

	public void publishYourContentOn(
			final ConnectedComponentInfoRecorder recorder) {

		final int cardinality = this.members.size();
		final String modelName = this.findModelName();

		for (final Vertex member : this.members) {

			member.doWithVertexType(new DoAction<VertexType>() {

				@Override
				public void apply(VertexType item, String species_id,
						String species_name, String compartment_id) {

					recorder.putTuple(member.buildVertexUniqueIdentifier(),
							item.toString(), cardinality, modelName);
				}

			});

		}
	}

	public String findModelName() {

		if (this.members.isEmpty()) {
			throw new RuntimeException("Impossible to retrieve the model name.");
		}

		// at least an element is present in the members set
		Vertex aMember = this.members.iterator().next();

		final StringBuilder ourModelName = new StringBuilder();

		// retrieve the model and access its model name
		DoAction<OurModel> action = new DoAction<OurModel>() {

			@Override
			public void apply(OurModel item, String species_id,
					String species_name, String compartment_id) {

				ourModelName.append(item.supplyModelName());
			}
		};

		aMember.doWithParentModel(action);

		return ourModelName.toString();
	}
}
