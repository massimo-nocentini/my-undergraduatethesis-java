package dotInterface;


public class NullObjectLineDecorator implements LineDecorator {

	@Override
	public String decore(String line) {
		return line;
	}

}
