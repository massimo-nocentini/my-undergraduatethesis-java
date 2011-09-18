package dotInterface;

public class SimpleDotDecorationApplier implements DotDecorationApplier {

	@Override
	public String decoreWithSourceSinkAttributes(String string) {
		return string.concat(" [color=\"black\", style=filled]");
	}

	@Override
	public String buildInfixNeighborhoodRelation(String vertex, String neighbour) {
		return vertex.concat(" -> ").concat(neighbour);
	}
}
