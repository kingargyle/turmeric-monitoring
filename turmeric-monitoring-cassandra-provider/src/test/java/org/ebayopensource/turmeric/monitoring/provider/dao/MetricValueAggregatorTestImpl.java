/*******************************************************************************
 * Copyright (c) 2006-2011 eBay Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/
package org.ebayopensource.turmeric.monitoring.provider.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.ebayopensource.turmeric.runtime.common.monitoring.MetricCategory;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricClassifier;
import org.ebayopensource.turmeric.runtime.common.monitoring.MetricId;
import org.ebayopensource.turmeric.runtime.common.monitoring.MonitoringLevel;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricComponentType;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricComponentValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValue;
import org.ebayopensource.turmeric.runtime.common.monitoring.value.MetricValueAggregator;
import org.ebayopensource.turmeric.runtime.common.pipeline.MessageContext;

public class MetricValueAggregatorTestImpl implements MetricValueAggregator {
	private final MetricValue metricValue;
	private final MetricCategory category;
	private final MonitoringLevel level;
	private final Map<MetricClassifier, MetricValue> valuesByClassifier;
	private volatile boolean readOnly;

	public MetricValueAggregatorTestImpl(MetricValue metricValue, MetricCategory category, MonitoringLevel level) {
		this(metricValue, category, level, new ConcurrentHashMap<MetricClassifier, MetricValue>());
	}

	public MetricValueAggregatorTestImpl(MetricValue metricValue, MetricCategory category, MonitoringLevel level,
			Map<MetricClassifier, MetricValue> valuesByClassifier) {
		this.metricValue = metricValue;
		this.category = category;
		this.level = level;
		this.valuesByClassifier = valuesByClassifier;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	@Override
	public MetricId getMetricId() {
		return metricValue.getMetricId();
	}

	@Override
	public MetricCategory getCategory() {
		return category;
	}

	@Override
	public MonitoringLevel getLevel() {
		return level;
	}

	@Override
	public Collection<MetricClassifier> getClassifiers() {
		return valuesByClassifier.keySet();
	}

	@Override
	public MetricValue getValue(MetricClassifier classifier) {
		return valuesByClassifier.get(classifier);
	}

	@Override
	public MetricValue getTotalValue() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<MetricComponentType> getAllComponentsTypes() {
		return metricValue.getAllComponentsTypes();
	}

	@Override
	public MetricComponentValue[] getValues() {
		throw new UnsupportedOperationException();
	}

	@Override
	public MetricValue deepCopy(boolean isReadOnly) {
		Map<MetricClassifier, MetricValue> valuesByClassifierCopy = new ConcurrentHashMap<MetricClassifier, MetricValue>();
		for (Map.Entry<MetricClassifier, MetricValue> classifierAndValue : valuesByClassifier.entrySet()) {
			valuesByClassifierCopy.put(classifierAndValue.getKey(), classifierAndValue.getValue().deepCopy(isReadOnly));
		}
		MetricValueAggregatorTestImpl result = new MetricValueAggregatorTestImpl(metricValue, category, level,
				valuesByClassifierCopy);
		result.readOnly = isReadOnly;
		return result;
	}

	@Override
	public MetricValue diff(MetricValue subtrahend, boolean isReadOnly) {
		MetricValueAggregatorTestImpl that = (MetricValueAggregatorTestImpl) subtrahend;
		MetricValueAggregatorTestImpl result = (MetricValueAggregatorTestImpl) deepCopy(false);
		result.valuesByClassifier.clear();
		for (Map.Entry<MetricClassifier, MetricValue> classifierAndValue : valuesByClassifier.entrySet()) {
			MetricClassifier metricClassifier = classifierAndValue.getKey();
			MetricValue difference = getValue(metricClassifier).diff(that.getValue(metricClassifier), isReadOnly);
			result.valuesByClassifier.put(metricClassifier, difference);
		}
		result.readOnly = isReadOnly;
		return result;
	}

	@Override
	public MetricValue add(MetricValue other, boolean isReadOnly) {
		throw new UnsupportedOperationException();
	}

	public void update(MetricClassifier metricClassifier, long value) {
		MetricValue metricValue = valuesByClassifier.get(metricClassifier);
		if (metricValue == null) {
			metricValue = cloneMetricValue();
			valuesByClassifier.put(metricClassifier, metricValue);
		}
		metricValue.update(value);
	}

	private MetricValue cloneMetricValue() {
		try {
			return this.metricValue.getClass().getConstructor(MetricId.class)
					.newInstance(this.metricValue.getMetricId());
		} catch (Exception x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public void update(MessageContext ctx, MetricValue value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(MessageContext ctx, int value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(MessageContext ctx, long value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(MessageContext ctx, float value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(MessageContext ctx, double value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(MetricValue newValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(int value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(long value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(float value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(double value) {
		throw new UnsupportedOperationException();
	}
}
