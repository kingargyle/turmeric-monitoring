package org.ebayopensource.turmeric.monitoring.provider;



import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;

public class SOAMetricsQueryServiceCassandraProviderTest {

    private long now = 0l;
    private long sixtyMinsAgo = 0l;
    private long sixtyOneMinsAgo = 0l;
    private long sixtyTwoMinsAgo = 0l;
    private long oneMinuteAgo = 0l;
    private long twoMinutesAgo = 0l;
    
    @Before
    public void setup() throws Exception {
        now = System.currentTimeMillis();
        createData();
    }

	private void createData() {
		// TODO Auto-generated method stub
	    
	}
    
	@Test
	public void testNothing() {
		assertNull(null);
		
	}
}
