<?php
namespace UCDavis\DataAccess;

use UCDavis\Connections\SQLConnection;
use UCDavis\Exceptions\DatasetInfoNotFound;

class DatasetInfoDAO
{
	const BASE_QUERY = 'select id, title, tableName, description from DatasetInfo';

	private $connection;
	
	public $isConnected;

	public function __construct($dbName)
	{
		$this->connection = new SQLConnection($dbName); 

		if ($this->connection->isConnected) {
			$this->isConnected = true;
		} else {
			$this->isConnected = false;
		}
	}

	public function getDatasetInfo($datasetID = null)
	{
		if ($this->isConnected) {
			$sql = self::BASE_QUERY;

			if (isset($datasetID)) {
				$sql .= ' where ID=' . $datasetID;
			}
		
			$result = $this->connection->runQuery($sql);
			
			return $result;
		}
	}

	public function insertDatasetInfo($datasetInfo)
	{
		if ($this->isConnected) {
			$sql = 'insert into datasetinfo(title, description, tablename) values(';

			if (isset($datasetInfo->title)) {
				$sql = $sql . "'$datasetInfo->title',";
			} else {
				throw new DatasetInfoNotFound();
			}
			if (isset($datasetInfo->description)) {
				$sql = $sql . "'$datasetInfo->description',";
			} else {
				throw new DatsetInfoNotFound();
			}
			if (isset($datasetInfo->tableName)) {
				$sql = $sql . "'$datasetInfo->tableName')";
			} else {
				throw new DatasetInfoNotFound();
			}
			
			echo $sql;
		}
	}
}
?>
