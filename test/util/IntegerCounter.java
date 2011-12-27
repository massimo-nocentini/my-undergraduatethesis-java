package util;

public class IntegerCounter {

	private int count = 0;

	public IntegerCounter increment() {
		count = count + 1;
		return this;
	}

	public void increment(int offset) {
		for (int i = 0; i < offset; i = i + 1) {
			increment();
		}
	}

	public Integer getCount() {
		return count;
	}

	@Override
	public String toString() {
		return "(COUNT " + String.valueOf(count) + ")";
	}
}
