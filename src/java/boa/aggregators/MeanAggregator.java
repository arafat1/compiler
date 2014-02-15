package boa.aggregators;

import boa.io.EmitKey;

/**
 * A Boa aggregator to calculate a mean of the values in a dataset.
 * 
 * @author anthonyu
 */
abstract class MeanAggregator extends Aggregator {
	private long count;

	public void count(final String metadata) {
		if (metadata == null)
			this.count++;
		else
			this.count += Long.parseLong(metadata);
	}

	/** {@inheritDoc} */
	@Override
	public void start(final EmitKey key) {
		super.start(key);

		this.count = 0;
	}

	/**
	 * Return the count of the values in the dataset.
	 * 
	 * @return A long representing the cardinality of the dataset
	 */
	protected long getCount() {
		return this.count;
	}
}