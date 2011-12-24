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
				.concat(fileName).concat(getDotFilenameExtension());

		return new File(relativeFileName);
	}

	public static String getDotFilenameExtension() {
		return ".dot";
	}

	public static String getSbmlExampleModelsFolder() {
		return "sbml-test-files".concat(getFileSeparator());
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

	public static DotFileUtilHandler makeHandler(String filename) {
		return new DotFileUtilHandler(filename);
	}

	public static DotFileUtilHandler makeHandlerForExistingFile(String filename) {
		DotFileUtilHandler dotFileUtilHandler = new DotFileUtilHandler(filename);
		dotFileUtilHandler.file = new File(filename);
		return dotFileUtilHandler;

	}

	public DotFileUtilHandler writeDotRepresentationInTestFolder(
			DotExporter dotExporter) {

		FileWriter outFile;
		String filenameWithExtension = filename
				.concat(getDotFilenameExtension());

		String savingFilename = dotOutputFolderPathName().concat(
				filenameWithExtension);

		try {
			outFile = new FileWriter(savingFilename);
			PrintWriter out = new PrintWriter(outFile);

			out.append("digraph G {".concat(getNewLineSeparator()));

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

	public static void evaluateDotFilesInOutputFolder() {

		DotUtilAction<File> action = new DotUtilAction<File>() {

			@Override
			public void apply(File element) {

				makeHandlerForExistingFile(element.getAbsolutePath())
						.produceSvgOutput();
			}
		};

		mapOnFilesInFolderFilteringByExtension(dotOutputFolderPathName(),
				getDotFilenameExtension(), action, false);
	}

	public interface DotUtilAction<T> {
		void apply(T element);
	}

	public static void mapOnFilesInFolderFilteringByExtension(String folder,
			String extensionFilter, DotUtilAction<File> action,
			boolean recursiveDirectoryScanning) {

		File dir = new File(folder);

		for (File existingFile : dir.listFiles()) {

			if (existingFile.isDirectory() == true
					&& recursiveDirectoryScanning == true) {

				// temporary filter to analyze only 'a'-starting folders
				// if (existingFile.getName().startsWith("a") == false) {
				// continue;
				//
				// }

				mapOnFilesInFolderFilteringByExtension(
						existingFile.getAbsolutePath(), extensionFilter,
						action, recursiveDirectoryScanning);

				// when we finish the recursive invocation we
				// continue with the next element in the directory
				// because the current scanned element is a directory
				continue;
			}

			if (extensionFilter != null
					&& existingFile.getName().endsWith(extensionFilter) == false) {

				continue;
			}

			action.apply(existingFile);
		}

	}

	public static void mapOnAllFilesInFolder(String folder,
			DotUtilAction<File> action) {

		// just delegate the work saying that no filter on the file extension
		// must be considered
		mapOnFilesInFolderFilteringByExtension(folder, null, action, false);
	}

	public static void mapOnAllFilesInFolder(String folder,
			DotUtilAction<File> action, boolean recursively) {

		// just delegate the work saying that no filter on the file extension
		// must be considered
		mapOnFilesInFolderFilteringByExtension(folder, null, action,
				recursively);
	}

	public void produceSvgOutput() {

		String dotCommand = "dot -Tsvg " + file.getAbsolutePath() + " -o "
				+ file.getAbsolutePath().replace(getDotFilenameExtension(), "")
				+ ".svg";

		try {
			Runtime.getRuntime().exec(dotCommand).waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String composeSquareBracketsWrapping(String wrappingContent) {

		return getOpeningDotDecorationString().concat(wrappingContent).concat(
				getClosingDotDecorationString());
	}

	// TODO: this method should really be defined here? It seems better to move
	// it in the decoration classes?
	public static String composeVertexLabelOutsideBox(String label) {
		return "taillabel=\"".concat(label).concat(
				"\", labelangle=45, labeldistance=1, color=transparent");
	}

	public static DotFileUtilHandler MakeHandlerForExampleSbmlModel(
			String sbmlExampleSelector) {

		String sourceRelativeFileName = DotFileUtilHandler
				.getSbmlExampleModelsFolder().concat(sbmlExampleSelector)
				.concat(".xml");

		DotExportable exportable = OurModel
				.makeOurModelFrom(sourceRelativeFileName);

		DotExporter exporter = new SimpleExporter();
		exportable.acceptExporter(exporter);

		return DotFileUtilHandler.makeHandler(sbmlExampleSelector)
				.writeDotRepresentationInTestFolder(exporter);

	}

	public static String getNeighbourRelationInfixToken() {
		return " -> ";
	}

	public static String getPlainTextFilenameExtensionToken() {
		return ".txt";
	}

}
