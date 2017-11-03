<?php
namespace UCDavis\DataAccess;

use UCDavis\Connections\SQLConnection;

/**
 * Provides a data access layer for Chart Types
 * services.
 */
class ChartTypeDAO
{
  const DATABASE_NAME = 'charts';
  
  /**
   * Retreives all chart types for specified
   * chart ID.
   *
   * @param string chartId
   * @return PDO[]
   */
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
  
  /**
   * Gets all available chart types.
   *
   * @return PDO[]
   */
  public function getChartTypes()
  {
    $db = new SQLConnection(self::DATABASE_NAME);

    if ($db->isConnected) {
      $query = 'SELECT * FROM ChartType';

      $queryResult = $db->runQuery($query);

      return $queryResult;
    }
  }
  
  /**
   * Inserts chart type along with its associated dataset Id
   *
   * @param string chartId
   * @param string datasetId
   */
  public function insertChartTypeByDatasetId($chartId, $datasetId)
  {
    $db = new SQLConnection(self::DATABASE_NAME);

    if ($db->isConnected) {
      $query = "INSERT INTO datasetcharttype VALUES('$datasetId','$chartId')";

      //$db->runQuery($query);
      echo $query;
    }
  }
}
?>
