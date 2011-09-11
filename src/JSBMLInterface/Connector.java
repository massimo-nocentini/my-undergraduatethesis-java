package JSBMLInterface;

import java.util.Set;

import model.Vertex;

import org.sbml.jsbml.Reaction;

public class Connector {

	private Connector() {
	}

	public static Connector makeConnector() {
		return new Connector();
	}

	public Set<Vertex> readReaction(Reaction reaction) {
		// TODO Auto-generated method stub
		return null;
	}

}
