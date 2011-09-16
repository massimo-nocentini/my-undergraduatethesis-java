package dotInterface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import junit.framework.Assert;
import model.Vertex;

import org.junit.After;
import org.junit.Test;
import org.sbml.jsbml.Species;

public class DotExportableUnitTest {

	private static final String DotOutputFolder = "dot-test-files/tests-output/";

	@Test
	public void simpleNodeWithoutNeighboursDotExporting() {

		String speciesId = "species_id";
		String compartmentId = "compartment_id";

		Species species = new Species(speciesId);
		species.setCompartment(compartmentId);

		Vertex v = Vertex.makeVertex(species);
		DotExportable exportable = v;

		DotExporter exporter = new SimpleExporter();
		exportable.acceptExporter(exporter);

		String dotModel = exporter.getOutput();

		Assert.assertEquals(speciesId.concat(compartmentId), dotModel);

		this.writeDotRepresentationInTestFolder(dotModel,
				"simpleNodeWithoutNeighboursDotExporting");
	}

	private void writeDotRepresentationInTestFolder(String content,
			String filename) {

		FileWriter outFile;
		String filenameWithExtension = filename.concat(".dot");

		String savingFilename = DotExportableUnitTest.DotOutputFolder
				.concat(filenameWithExtension);

		String completedContent = "digraph G {"
				+ System.getProperty("line.separator") + content
				+ System.getProperty("line.separator") + "}";

		try {
			outFile = new FileWriter(savingFilename);
			PrintWriter out = new PrintWriter(outFile);
			out.write(completedContent);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@After
	public void invokeDotCompilationForAllGeneratedFiles() {
		File dir = new File(DotExportableUnitTest.DotOutputFolder);

		for (File file : dir.listFiles()) {

			String dotCommand = "dot -Tsvg " + file.getAbsolutePath() + " -o "
					+ file.getAbsolutePath().replace(".dot", "") + ".svg";

			// String command = "/bin/bash -c \"" + dotCommand + "\"";
			String command = dotCommand;

			try {
				Runtime.getRuntime().exec(command).waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
