package dotInterface;

public class DotDecorationApplierNullObject implements DotDecorationApplier {

	@Override
	public String decoreWithSourceSinkAttributes(String string) {
		return "";
	}

	@Override
	public String buildInfixNeighborhoodRelation(String vertex, String neighbour) {
		return "";
	}

}
