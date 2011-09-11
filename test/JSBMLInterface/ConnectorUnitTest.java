package JSBMLInterface;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ConnectorUnitTest {

	@Test
	public void creation() {
		Connector connector = Connector.makeConnector();
		assertNotNull(connector);
	}

}
