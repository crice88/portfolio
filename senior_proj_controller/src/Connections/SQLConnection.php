<?php
namespace UCDavis\Connections;

use PDO;
use UCDavis\Credentials\SQLCredential;
use UCDavis\DataAccess\DatasetDAO;

class SQLConnection implements IConnection
{
	/**
	 * Connection object
	 *
	 * @var PDO Object
	 */
	private $connection;
	
	private $dbName;

	/**
	 * Connection
	 *
	 * @var boolean
	 */
	public $isConnected;
	
	/**
	 * Connection class constructor.
	 */
	public function __construct($dbName)
	{
		$this->dbName = $dbName;
		$this->connect();
	}

	public function connect()
	{
		try {
			$credentials = new SQLCredential;
			$this->connection = new PDO('mysql:host=' . $credentials->getHost() . ';dbname='
				. $this->dbName, $credentials->getUserName(), $credentials->getPassword());
			$this->isConnected = true;
		} catch (PDOException $e) {
			$this->connection = null;
			$this->isConnected = false;
			echo 'PDO could not connect to client.' . "\n";
		}
	}
	
	/**
	 * Runs passed query against created database connection.
	 *
	 * @param string $query Query to be ran
	 * @return array Returns a PDO associative array
	 */
	public function runQuery($query)
	{
		if ($this->isConnected) {
			$cmd = $this->connection->prepare($query);
			$cmd->execute();
			$result = $cmd->fetchall(PDO::FETCH_ASSOC);
			
			return $result;
		}
	}

	public function getColumnInfo($datasetId)
	{	
		$dao = new DatasetDAO($this->dbName);

		$tableName = $dao->getDatasetName($datasetId);

		if ($this->isConnected) {
			// Get all column info for selected table.
			$query = 'show columns from ' . $tableName;
			$cmd = $this->connection->prepare($query);
			$cmd->execute();
			$result = $cmd->fetchall(PDO::FETCH_ASSOC);

			return $result;
		}
	}
}
?>
