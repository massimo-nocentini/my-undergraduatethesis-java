package dotInterface;

import java.io.IOException;
import java.io.Writer;

public class EndLineFillerWriter extends Writer {

	private final Writer wrappedWriter;

	private EndLineFillerWriter(Writer wrappedWriter) {
		this.wrappedWriter = wrappedWriter;
	}

	public static Writer MakeWrapper(Writer wrappedWriter) {
		return new EndLineFillerWriter(wrappedWriter);
	}

	private String completeLineWithSemicolonAndNewLine(String toCompleteString) {
		return DotFileUtilHandler.getTabString().concat(
				toCompleteString.concat(";").concat(
						DotFileUtilHandler.getNewLineSeparator()));
	}

	@Override
	public void write(int c) throws IOException {
		wrappedWriter.write(completeLineWithSemicolonAndNewLine(String
				.valueOf(c)));
	}

	@Override
	public void write(char[] cbuf) throws IOException {
		wrappedWriter
				.write(completeLineWithSemicolonAndNewLine(cbuf.toString()));
	}

	@Override
	public void write(String str) throws IOException {
		wrappedWriter.write(completeLineWithSemicolonAndNewLine(str));
	}

	@Override
	public void write(String str, int off, int len) throws IOException {
		// here we have to increment the off parameter because we add a '\t'
		// character in front of the string
		wrappedWriter.write(completeLineWithSemicolonAndNewLine(str), off + 1,
				len);
	}

	@Override
	public Writer append(CharSequence csq) throws IOException {
		return wrappedWriter.append(completeLineWithSemicolonAndNewLine(csq
				.toString()));
	}

	@Override
	public Writer append(CharSequence csq, int start, int end)
			throws IOException {

		// here we have to increment the start and end parameter because we add
		// a '\t' character in front of the string
		return wrappedWriter.append(
				completeLineWithSemicolonAndNewLine(csq.toString()), start + 1,
				end + 1);
	}

	@Override
	public Writer append(char c) throws IOException {
		return wrappedWriter.append(completeLineWithSemicolonAndNewLine(String
				.valueOf(c)));
	}

	@Override
	public void write(char[] cbuf, int off, int len) throws IOException {
		// here we have to increment the off parameter because we add a '\t'
		// character in front of the string
		wrappedWriter.write(
				completeLineWithSemicolonAndNewLine(cbuf.toString()), off + 1,
				len);
	}

	@Override
	public void flush() throws IOException {
		wrappedWriter.flush();
	}

	@Override
	public void close() throws IOException {
		wrappedWriter.close();
	}

}
