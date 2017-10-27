<?php
namespace UCDavis\DataAccess;

use UCDavis\Connections\SQLConnection;

class ChartTypeDAO
{
	const DATABASE_NAME = 'charts';

	public function getChartTypesById($chartId)
	{
		$db = new SQLConnection(self::DATABASE_NAME);

		if ($db->isConnected) {
			$query = 'SELECT dct.chartId, ct.chartName FROM DatasetChartType AS dct '
        		. 'JOIN ChartType AS ct ON ct.ChartID = dct.ChartID '
				. 'WHERE dct.ID = ' . $chartId;
			
			$queryResult = $db->runQuery($query);
			 
			return $queryResult;
		}
	}
}
?>
