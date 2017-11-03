<?php
namespace UCDavis\DataAccess;

use UCDavis\Connections\SQLConnection;
use UCDavis\Exceptions\DatasetInfoNotFound;

class DatasetInfoDAO
{
	const BASE_QUERY = 'select id, title, tableName, description, featured from DatasetInfo';

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
				$sql .= ' where ID=' . $datasetID 
					. ' and delete_dataset=0';
			} else {
				$sql .= ' order by featured desc';
			}
		
			$result = $this->connection->runQuery($sql);
			
			return $result;
		}
	}

	public function getDatasetId($datasetName)
	{
		if ($this->isConnected) {
			$sql = 'select id from datasetinfo where tablename=' . "'$datasetName'"
				. ' and delete_dataset=0';
			
			$result = $this->connection->runQuery($sql);

			return $result;
		}
	}

	public function insertDatasetInfo($datasetInfo)
	{
		if ($this->isConnected) {
			$sql = 'insert into datasetinfo(title, description, tablename, featured) values(';

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
				$sql = $sql . "'$datasetInfo->tableName',";
			} else {
				throw new DatasetInfoNotFound();
			}
			if (isset($datasetInfo->featured)) {
				$sql = $sql . "'$datasetInfo->featured')";
			} else {
				throw new DatasetInfoNotFound();
			}
			
			echo $sql;
		}
	}

    public function runSearch($query)
    {
        if ($this->isConnected) {
			$sql = "select ID as id, description, TableName as tableName, title, featured from datasetinfo 
				where description like '%" . $query . "%' or title like '%" . $query . "%'"
				" and delete_dataset=0";

            $result = $this->connection->runQuery($sql);

            return $result;
        }
    }
}
?>
