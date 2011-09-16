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

		String completedContent = dotExporter.getCompleteContent();

		try {
			outFile = new FileWriter(savingFilename);
			PrintWriter out = new PrintWriter(outFile);
			out.write(completedContent);
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
