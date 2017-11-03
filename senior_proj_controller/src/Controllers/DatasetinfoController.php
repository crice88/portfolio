<?php
namespace UCDavis\Controllers;

use UCDavis\DataAccess\DatasetInfoDAO;

/**
 * Used to retreive info on a dataset
 */
class DatasetInfoController
{
  const DATABASE_NAME = 'charts';

  public function getDatasetInfo($datasetId = null)
  {
    $dataset = new DatasetInfoDAO(self::DATABASE_NAME);
    
    if ($dataset->isConnected) {
      $info = $dataset->getDatasetInfo($datasetId);
      
      echo json_encode($info);
    }
  }
}
?>
