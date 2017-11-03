<?php
namespace UCDavis\Controllers;

use UCDavis\DataAccess\ChartTypeDAO;

/**
 * Gets the chart type from charts database.
 */
class CharttypeController
{
	/**
	 * Get all chart types associated with chartId
	 *
	 * @param string chartId
	 */
	public function getChartTypes($chartId)
	{
		$dao = new ChartTypeDAO;

		$result = $dao->getChartTypesById($chartId);
		// Echos JSON to consuming application
		echo json_encode($result);
	}

  /**
	 * Gets all available chart types.
	 */
	public function getTypes()
	{
		$dao = new ChartTypeDAO;

		$result = $dao->getChartTypes();

		echo json_encode($result);
	}
}
?>
