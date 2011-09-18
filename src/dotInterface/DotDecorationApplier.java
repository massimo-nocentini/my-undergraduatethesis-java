package dotInterface;

public interface DotDecorationApplier {
	// TODO: doesn't seem this class is the right home for this method.
	String decoreWithSourceSinkAttributes(String string);

	String buildInfixNeighborRelation(String vertex, String neighbour);
}
