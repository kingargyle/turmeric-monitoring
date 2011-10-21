package org.ebayopensource.turmeric.monitoring.provider.dao;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Interface IpPerDayAndServiceNameDAO.
 * 
 * @param <SK>
 *           the generic type
 * @param <K>
 *           the key type
 */
public interface IpPerDayAndServiceNameDAO<SK, K> {

   /**
    * Find by date and service name.
    * 
    * @param currentTimeMillis
    *           the current time millis
    * @param serviceName
    *           the service name
    * @return the list
    */
   List<String> findByDateAndServiceName(long currentTimeMillis, String serviceName);

}
