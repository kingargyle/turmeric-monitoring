package org.ebayopensource.turmeric.monitoring.provider.dao;

import java.util.List;

public interface IpPerDayAndServiceNameDAO <SK, K>{

   List<String> findByDateAndServiceName(long currentTimeMillis, String serviceName);

   
}
