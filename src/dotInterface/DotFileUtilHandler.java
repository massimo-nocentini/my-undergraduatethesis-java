package dotInterface;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DotFileUtilHandler {
	public static final String DotOutputFolder = "dot-test-files/tests-output/";

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

	private DotFileUtilHandler() {
	}

	public static DotFileUtilHandler MakeHandler() {
		return new DotFileUtilHandler();
	}

	public void writeDotRepresentationInTestFolder(DotExporter dotExporter,
			String filename) {

		FileWriter outFile;
		String filenameWithExtension = filename.concat(".dot");

		String savingFilename = DotFileUtilHandler.DotOutputFolder
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
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void evaluateDotFilesInOutputFolder() {
		File dir = new File(DotFileUtilHandler.DotOutputFolder);

		for (File file : dir.listFiles()) {

			String dotCommand = "dot -Tsvg " + file.getAbsolutePath() + " -o "
					+ file.getAbsolutePath().replace(".dot", "") + ".svg";

			String command = dotCommand;

			try {
				Runtime.getRuntime().exec(command).waitFor();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
