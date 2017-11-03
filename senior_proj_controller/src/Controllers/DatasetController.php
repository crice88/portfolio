<?php
namespace UCDavis\Controllers;

use UCDavis\Connections\SQLConnection;
use UCDavis\DataAccess\MongoDAO;
use UCDavis\DataAccess\DatasetDAO;
use UCDavis\Controllers\Services\ChartServices;
use UCDavis\Controllers\Services\ChartTypes;
use UCDavis\Exceptions\InvalidChartType;
/**
 * Provides consumable services for Chart datasets.
 */
class DatasetController
{
	const DATABASE_NAME = 'charts';
	
	/**
	 * Gets the selected datatable
	 * 
	 * @param string ID of dataset
	 * @param string The chart type of requested dataset
	 */
	public function getDataTable($datasetId, $chartType) 
	{
		$formattedChartType = $this->formatChartType($chartType);

		$dao = new DatasetDAO(self::DATABASE_NAME);
		$db = new SQLConnection(self::DATABASE_NAME);
		$chart = new ChartServices;
		
		if ($dao->isConnected && $db->isConnected) {
			$queryResult = $dao->getDataset($datasetId);
			
			$typeResult = $db->getColumnInfo($datasetId);	
			// Echoes formatted set to consuming service.
			echo $chart->createChart($queryResult, $typeResult
				, $datasetId, $formattedChartType);
		}
	}
	
	/**
	 * Retrieves an options JSON retrieved from 	
	 * 
	 * @param string datasetId
	 */ 
	public function getOptions($datasetId)
	{
		$mongo = new MongoDAO($datasetId);

		$result = $mongo->getOptions();

		echo json_encode($result);
	}
	
	/**
	 * Returns formatted chart type
	 *
	 * @param  string chartType
	 * @return formatted chart type
	 */
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
	
	/**
	 * Gets each dataset by requested chart type
	 *
	 * @param string chartType
	 */
	public function getDatasetsByChartType($chartType) {
		$dao = new DatasetDAO(self::DATABASE_NAME);

		if ($dao->isConnected) {
			$result = $dao->getDatasetChartTypes($chartType);

			echo json_encode($result);
		}
	}
	
	/**
	 * Deletes selected dataset.
	 *
	 * @param string datasetId
	 */
	public function deleteDataset($datasetId) {
		$dao = new DatasetDAO(self::DATABASE_NAME);

		if ($dao->isConnected) {
			$result = $dao->deleteDataset($datasetId);

			print_r($result);
		}
	}
}
?>
