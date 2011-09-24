package util;

public class CallbackSignalRecorder {

	private boolean signaled;

	public CallbackSignalRecorder() {
		signaled = false;
	}

	public boolean isSignaled() {
		return signaled;
	}

	public void signal() {
		signaled = true;
	}

}
