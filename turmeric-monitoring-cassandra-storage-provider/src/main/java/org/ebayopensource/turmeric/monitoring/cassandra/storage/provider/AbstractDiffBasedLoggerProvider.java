/**
 *  Copyright (c) 2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package org.ebayopensource.turmeric.monitoring.cassandra.storage.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.ebayopensource.turmeric.runtime.common.impl.monitoring.storage.SnapshotLogger;
import org.ebayopensource.turmeric.runtime.common.monitoring.DiffBasedMetricsStorageProvider;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricId;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValueAggregator;

/**
 * This is an abstract implementation for a Diff Based logger.  It extends the abstract implementation
 * for a SnapshotLogger for metrics.   Implementors should extend this and implement the saveMetricValue
 * method for their storage provider.  This also implements the DiffBasedMetricsStorageProvider to
 * reset a particular service admin name metric when called.
 * 
 * @author dcarver
 *
 */

public abstract class AbstractDiffBasedLoggerProvider extends SnapshotLogger implements DiffBasedMetricsStorageProvider {

	private Collection<MetricValueAggregator> m_previousSnapshot;
	private Integer snapshotInterval;
	
	protected Integer getSnapshotInterval() {
		return snapshotInterval;
	}

	protected void recordSnapshot(long snapshotTime, Collection<MetricValueAggregator> snapshot) {
		m_previousSnapshot = snapshot;
	}
	
	@Override
	public void init(Map<String, String> options, String name,
			String collectionLocation, Integer snapshotInterval) {
		super.init(options, name, collectionLocation, snapshotInterval);
		this.snapshotInterval = snapshotInterval;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	public void resetPreviousSnapshot(String adminName) {
		Collection<MetricValueAggregator> savedCopy = m_previousSnapshot;
		m_previousSnapshot = new ArrayList<MetricValueAggregator>(
				m_previousSnapshot.size());
		for (MetricValueAggregator metricValueAggregator : savedCopy) {
			if (!metricValueAggregator.getMetricId().getAdminName().equals(
					adminName)) {
				m_previousSnapshot.add(metricValueAggregator);
			}
		}
	}

	/**
	 * 
	 * {@inheritDoc}
	 * 
	 * This implementation provides a diff based aggregator value.  It takes in the
	 * current value, and provides the difference from the previous snapshot value.
	 * 
	 */
	@Override
	protected MetricValueAggregator getAggregator(MetricValueAggregator current) {
		if (null == m_previousSnapshot) {
			return current;
		}

		MetricId id = current.getMetricId();
		for (MetricValueAggregator value: m_previousSnapshot) {
			if (id.equals(value.getMetricId())) {
				return (MetricValueAggregator)current.diff(value, true);
			}
		}
		return current;
	}
	
}
