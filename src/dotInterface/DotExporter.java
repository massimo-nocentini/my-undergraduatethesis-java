package dotInterface;


public interface DotExporter {
	void buildVertexDefinition(VertexDotInfoProvider vertex);

	String getOutput();
}
