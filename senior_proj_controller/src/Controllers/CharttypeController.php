<?php
namespace UCDavis\Controllers;

use UCDavis\DataAccess\ChartTypeDAO;

class CharttypeController
{
	public function getChartTypes($chartId)
	{
		$dao = new ChartTypeDAO;

		$result = $dao->getChartTypesById($chartId);

		echo json_encode($result);
	}
}
?>
