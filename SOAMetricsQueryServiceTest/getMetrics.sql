
SELECT M.*, MC.consumerName, MD.category,  MD.level, MD.name, MV.*
  FROM Metric as M
  inner join MetricDef as MD on M.metricDef_id = MD.id
  inner join MetricValue as MV on M.id = MV.metric_id
  inner join MetricClassifier as MC on MV.metric_id = MC.id
  where M.operationName != 'null' and M.metricDef_id = 1 and M.serviceAdminName = 'StorageService';
 