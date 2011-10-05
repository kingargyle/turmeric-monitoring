package org.ebayopensource.turmeric.monitoring.aggregation;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AggregatorTest {
	Aggregator aggregator = null;
	Date startTime;
	Date endTime;
	CassandraConnectionInfo realtimeCluster;
	CassandraConnectionInfo offlineCluster;

	@Before
	public void setUp() throws Exception {
		startTime = new Date(System.currentTimeMillis());
		aggregator = new Aggregator(null, null, realtimeCluster, offlineCluster);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
