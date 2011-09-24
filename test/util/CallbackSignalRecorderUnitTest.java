package util;

import junit.framework.Assert;

import org.junit.Test;

public class CallbackSignalRecorderUnitTest {

	@Test
	public void testCreation() {
		CallbackSignalRecorder callbackSignalRecorder = new CallbackSignalRecorder();

		Assert.assertNotNull(callbackSignalRecorder);

	}

	@Test
	public void signalNotRecorded() {
		CallbackSignalRecorder callbackSignalRecorder = new CallbackSignalRecorder();

		Assert.assertFalse(callbackSignalRecorder.isSignaled());

	}

	@Test
	public void signalRecorded() {
		CallbackSignalRecorder callbackSignalRecorder = new CallbackSignalRecorder();

		callbackSignalRecorder.signal();

		Assert.assertTrue(callbackSignalRecorder.isSignaled());

	}

	@Test
	public void checkEmptySignalCount() {
		CallbackSignalRecorder callbackSignalRecorder = new CallbackSignalRecorder();

		Assert.assertTrue(callbackSignalRecorder.isCountOfSignals(0));

	}

	@Test
	public void checkNonEmptySignalCount() {
		CallbackSignalRecorder callbackSignalRecorder = new CallbackSignalRecorder();

		callbackSignalRecorder.signal();
		callbackSignalRecorder.signal();
		callbackSignalRecorder.signal();
		callbackSignalRecorder.signal();

		Assert.assertTrue(callbackSignalRecorder.isCountOfSignals(4));

	}

}
