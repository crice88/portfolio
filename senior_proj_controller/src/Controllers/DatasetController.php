<?php
namespace UCDavis\Controllers;

use UCDavis\Connections\SQLConnection;
use UCDavis\DataAccess\MongoDAO;
use UCDavis\DataAccess\DatasetDAO;
use UCDavis\Controllers\Services\ChartServices;
use UCDavis\Controllers\Services\ChartTypes;
use UCDavis\Exceptions\InvalidChartType;

class DatasetController
{
	const DATABASE_NAME = 'charts';
	
	public function getDataTable($datasetId, $chartType) 
	{
		$formattedChartType = $this->formatChartType($chartType);

		$dao = new DatasetDAO(self::DATABASE_NAME);
		$db = new SQLConnection(self::DATABASE_NAME);
		$chart = new ChartServices;
		
		if ($dao->isConnected && $db->isConnected) {
			$queryResult = $dao->getDataset($datasetId);
			
			$typeResult = $db->getColumnInfo($datasetId);	
			
			echo $chart->createChart($queryResult, $typeResult
				, $datasetId, $formattedChartType);
		}
	}

	public function getOptions($datasetId)
	{
		$mongo = new MongoDAO($datasetId);

		$result = $mongo->getOptions();

		echo json_encode($result);
	}

	private function formatChartType($chartType)
	{
		$chartType = strtolower($chartType);

		if (array_key_exists($chartType, ChartTypes::CHART_MAP)) {
			$chartType = ChartTypes::CHART_MAP[$chartType];
		} 
		else {
			throw new InvalidChartType($chartType);
		}

		return $chartType;
	}
}
?>
