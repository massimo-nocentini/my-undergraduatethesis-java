package util;

public class CallbackSignalRecorder {

	private int count;

	public CallbackSignalRecorder() {
		count = 0;
	}

	public boolean isSignaled() {
		return count > 0;
	}

	public void signal() {
		count = count + 1;
	}

	public boolean isCountOfSignals(int i) {
		return count == i;
	}
}
