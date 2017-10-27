<?php
namespace UCDavis\Controllers;

use UCDavis\DataAccess\DatasetDAO;
use UCDavis\DataAccess\MapDAO;

class MapController
{
	const DATABASE_NAME = 'charts';

  public function getDataTableByID($mapId)
  {
		$dao = new MapDAO(self::DATABASE_NAME);

		if ($dao->isConnected) {
      $result = $dao->getMap($mapId);

      echo json_encode($result);
    }
  }

  public function getRawData($mapId) 
	{
		$dao = new DatasetDAO(self::DATABASE_NAME);

		if ($dao->isConnected) {
			$result = $dao->getDataset($mapId);

      echo json_encode($result);
		}
	}
}
?>
