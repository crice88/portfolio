<?php
namespace UCDavis\Controllers;

use UCDavis\DataAccess\DatasetDAO;
use UCDavis\DataAccess\MapDAO;

/**
 * Controller for map services.
 */
class MapController
{
  const DATABASE_NAME = 'charts';
  
  /**
   * Gets dataset map information by mapId
   *
   * @param string mapId
   */
  public function getDataTableByID($mapId)
  {
    $dao = new MapDAO(self::DATABASE_NAME);

    if ($dao->isConnected) {
      $result = $dao->getMap($mapId);

      echo json_encode($result);
    }
  }
  
  /**
   * Gets the raw JSON retreived from PDO response
   *
   * @param string mapId
   */
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
