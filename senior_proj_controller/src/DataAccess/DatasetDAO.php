<?php
namespace UCDavis\DataAccess;

use UCDavis\Connections\SQLConnection;

class DatasetDAO
{
	const BASE_QUERY = 'select * from ';

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

	public function getDataset($datasetId)
	{
		$tableName = $this->getDatasetName($datasetId);

		$sql = self::BASE_QUERY . $tableName;

		$result = $this->connection->runQuery($sql);

		return $result;
	}

	public function getDatasetName($datasetId)
	{
		$sql = 'select tablename from datasetinfo where id=' . $datasetId;

		$result = $this->connection->runQuery($sql);
		
		return $result[0]['tablename'];
	}
	
	public function createDatasetTable($tableName, $headers)
	{
		$sql = "create table $tableName (" . $this->createDatasetColumns($headers)
			. ");";

		echo $sql;
	}

	public function insertDataset($tableName, $data)
	{
		$sql = "insert into $tableName values". $this->createDataString($data);

		echo $sql;
	}

	private function createDatasetColumns($headers)
	{
		$columnName = "";
		
		foreach($headers as $header) {
			$columnName = $columnName . str_replace(" ", "_",$header) . ' VARCHAR(250), ';		
		}

		if (strlen($columnName) > 1) {
			$columnName = chop($columnName, ", ");
		}

		return $columnName;
	}

	private function createDataString($data)
	{
		$insStatement = "";
		
		for ($i = 0; $i < count($data); $i++) {
			$values = "(";

			foreach($data[$i] as $entry) {
				$values = $values . "'$entry',";
			}
			
			if (strlen($values) > 1) {
				$values = chop($values, ",") . "),";
			}

			$insStatement = $insStatement . $values;
		}

		if (strlen($values) > 1) {
			$insStatement = chop($insStatement, ",");
		}

		return $insStatement;
	}
}
?>
