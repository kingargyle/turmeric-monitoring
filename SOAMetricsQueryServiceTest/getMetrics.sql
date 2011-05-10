
SELECT M.*, MC.consumerName, MD.category,  MD.level, MD.name, MV.*
  FROM Metric as M
  inner join MetricDef as MD on M.metricDef_id = MD.id
  inner join MetricValue as MV on M.id = MV.metric_id
  inner join MetricClassifier as MC on MV.metric_id = MC.id
  where M.operationName != 'null' and M.metricDef_id = 1 and M.serviceAdminName = 'ShippingCalculatorService';
  
select sum(metriccomp0_.value) as col_0_0_, metriccomp0_.metricComponentDef_id as col_1_0_, metricvalu1_.serverSide as col_2_0_, machine4_.canonicalHostName as col_3_0_, machinegro5_.name as col_4_0_, metric3_.serviceAdminName as col_5_0_ from MetricComponentValue metriccomp0_ inner join MetricValue metricvalu1_ on metriccomp0_.metricValue_id=metricvalu1_.id inner join MetricClassifier metricclas2_ on metricvalu1_.metricClassifier_id=metricclas2_.id inner join Metric metric3_ on metricvalu1_.metric_id=metric3_.id inner join MetricDef metricdef6_ on metric3_.metricDef_id=metricdef6_.id inner join Machine machine4_ on metricvalu1_.machine_id=machine4_.id left outer join MachineGroup machinegro5_ on machine4_.machineGroup_id=machinegro5_.id
   where metric3_.serviceAdminName = 'ShippingCalculatorService' and metricvalu1_.timeStamp >= 1257058800000 and metricvalu1_.timeStamp < 1257058886401
         and metricvalu1_.aggregationPeriod=3600 and (metric3_.serviceAdminName in ('ShippingCalculatorService'))
   group by metriccomp0_.metricComponentDef_id , metricvalu1_.serverSide , machine4_.canonicalHostName , machinegro5_.name , metric3_.serviceAdminName;  