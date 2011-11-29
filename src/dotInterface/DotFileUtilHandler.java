package dotInterface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import model.OurModel;

public class DotFileUtilHandler {

	private final String filename;
	private File file;

	/**
	 * This method define where is the dot output folder that will contain all
	 * the .dot files generated using this program.
	 * 
	 * @return a string capturing the relative path to the dot output folder.
	 */
	public static String dotOutputFolderPathName() {
		return "dot-test-files".concat(DotFileUtilHandler.getFileSeparator())
				.concat("tests-output")
				.concat(DotFileUtilHandler.getFileSeparator());
	}

	/**
	 * This static method make a File object ready for use, redirecting the
	 * simple file name to a .dot file that (will) resides in the dot output
	 * file folder.
	 * 
	 * @param fileName
	 *            simple file name without information on path and extensions
	 * @return a File instance pointing to a .dot output file in folder
	 *         specified by the constant
	 *         {@link DotFileUtilHandler#dotOutputFolderPathName()}
	 */
	public static File makeDotOutputFile(String fileName) {

		String relativeFileName = DotFileUtilHandler.dotOutputFolderPathName()
				.concat(fileName).concat(".dot");

		return new File(relativeFileName);
	}

	public static String getSbmlExampleModelsFolder() {
		return "sbml-test-files".concat(DotFileUtilHandler.getFileSeparator());
	}

	public static String getNewLineSeparator() {
		return System.getProperty("line.separator");
	}

	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}

	public static String getPathSeparator() {
		return System.getProperty("path.separator");
	}

	public static String getTabString() {
		return "\t";
	}

	public static String getBlankString() {
		return " ";
	}

	public static String getOpeningDotDecorationString() {
		return "[";
	}

	public static String getClosingDotDecorationString() {
		return "]";
	}

	private DotFileUtilHandler(String filename) {
		this.filename = filename;
	}

	public static DotFileUtilHandler MakeHandler(String filename) {
		return new DotFileUtilHandler(filename);
	}

	public static DotFileUtilHandler MakeHandlerForExistingFile(String filename) {
		DotFileUtilHandler dotFileUtilHandler = new DotFileUtilHandler(filename);
		dotFileUtilHandler.file = new File(filename);
		return dotFileUtilHandler;

	}

	public DotFileUtilHandler writeDotRepresentationInTestFolder(
			DotExporter dotExporter) {

		FileWriter outFile;
		String filenameWithExtension = filename.concat(".dot");

		String savingFilename = DotFileUtilHandler.dotOutputFolderPathName()
				.concat(filenameWithExtension);

		try {
			outFile = new FileWriter(savingFilename);
			PrintWriter out = new PrintWriter(outFile);

			out.append("digraph G {".concat(DotFileUtilHandler
					.getNewLineSeparator()));

			dotExporter.collectCompleteContent(EndLineFillerWriter
					.MakeWrapper(out));

			out.append("}");

			out.close();

			// only now we can set the destination, all the dot model generation
			// steps are successfully completed
			file = new File(savingFilename);

		} catch (IOException e) {
			e.printStackTrace();
		}

		return this;
	}

	public static void EvaluateDotFilesInOutputFolder() {
		File dir = new File(DotFileUtilHandler.dotOutputFolderPathName());

		for (File existingFile : dir.listFiles()) {
			DotFileUtilHandler.MakeHandlerForExistingFile(
					existingFile.getAbsolutePath()).produceSvgOutput();
		}
	}

	public void produceSvgOutput() {

		return;

		// String dotCommand = "dot -Tsvg " + file.getAbsolutePath() + " -o "
		// + file.getAbsolutePath().replace(".dot", "") + ".svg";
		//
		// String command = dotCommand;
		//
		// try {
		// Runtime.getRuntime().exec(command).waitFor();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}

	public static String composeSquareBracketsWrapping(String wrappingContent) {

		return getOpeningDotDecorationString().concat(wrappingContent).concat(
				getClosingDotDecorationString());
	}

	public static String composeVertexLabelOutsideBox(String label) {
		return "taillabel=\"".concat(label).concat(
				"\", labelangle=45, labeldistance=1, color=transparent");
	}

	protected static DotFileUtilHandler MakeHandlerForExampleSbmlModel(
			String sbmlExampleSelector) {

		String sourceRelativeFileName = DotFileUtilHandler
				.getSbmlExampleModelsFolder().concat(sbmlExampleSelector)
				.concat(".xml");

		DotExportable exportable = OurModel
				.makeOurModelFrom(sourceRelativeFileName);

		DotExporter exporter = new SimpleExporter();
		exportable.acceptExporter(exporter);

		return DotFileUtilHandler.MakeHandler(sbmlExampleSelector)
				.writeDotRepresentationInTestFolder(exporter);

	}

	public static String getNeighbourRelationInfixToken() {
		return " -> ";
	}

	public static String getPlainTextFilenameExtensionToken() {
		return ".txt";
	}

}
