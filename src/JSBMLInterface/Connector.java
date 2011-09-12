package JSBMLInterface;

import java.util.HashSet;
import java.util.Set;

import model.Vertex;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SpeciesReference;

public class Connector {

	private Connector() {
	}

	public static Connector makeConnector() {
		return new Connector();
	}

	public Set<Vertex> readReaction(Reaction reaction,
			VertexGenerationWithSourceListener listener) {

		Set<Vertex> reactants = this.convertToVertexSet(
				reaction.getListOfReactants(), new FromReactorDriver(listener));

		Set<Vertex> products = this.convertToVertexSet(
				reaction.getListOfProducts(), new FromProductDriver(listener));

		this.updateNeighbourhoodByCrossProduct(reactants, products);

		if (reaction.isReversible()) {
			this.updateNeighbourhoodByCrossProduct(products, reactants);
		}

		Set<Vertex> result = new HashSet<Vertex>();
		result.addAll(reactants);
		result.addAll(products);

		return result;
	}

	private void updateNeighbourhoodByCrossProduct(Set<Vertex> domainSet,
			Set<Vertex> rangeSet) {
		for (Vertex domainVertex : domainSet) {
			for (Vertex rangeVertex : rangeSet) {
				domainVertex.addNeighbour(rangeVertex);
			}
		}
	}

	public Set<Vertex> convertToVertexSet(
			ListOf<SpeciesReference> listOfSpeciesReference) {

		return this.convertToVertexSet(listOfSpeciesReference,
				new VertexHandlerListenerNullObject());
	}

	public Set<Vertex> convertToVertexSet(
			ListOf<SpeciesReference> listOfSpeciesReference,
			VertexGenerationListener listener) {

		Set<Vertex> result = new HashSet<Vertex>();

		// TODO: do something with sRef, maybe use it for
		// characterize in detail the new vertex.
		for (SpeciesReference sRef : listOfSpeciesReference) {

			Vertex newVertex = Vertex.makeVertex();

			listener.newVertexGenerated(newVertex);

			result.add(newVertex);
		}

		return result;
	}

	public class FromReactorDriver implements VertexGenerationListener {

		private final VertexGenerationWithSourceListener listener;

		public FromReactorDriver(VertexGenerationWithSourceListener listener) {
			this.listener = listener;
		}

		@Override
		public void newVertexGenerated(Vertex vertex) {
			listener.newVertexFromReactor(vertex);
		}
	}

	public class FromProductDriver implements VertexGenerationListener {

		private final VertexGenerationWithSourceListener listener;

		public FromProductDriver(VertexGenerationWithSourceListener listener) {
			this.listener = listener;
		}

		@Override
		public void newVertexGenerated(Vertex vertex) {
			this.listener.newVertexFromProduct(vertex);
		}
	}

}
