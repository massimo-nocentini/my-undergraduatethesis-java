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

}
