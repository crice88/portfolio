<?php
namespace UCDavis\Connections;

use MongoDB;
use UCDavis\Credentials\MongoCredential;

class MongoConnection implements IConnection
{
	private $connection;

	public $isConnected;
	
	public function __construct()
	{
		$this->connect();
	}

	public function connect()
	{
		try {
			$credentials = new MongoCredential; 
			$this->connection = new MongoDB\Client($credentials->getHost());
			$this->isConnected = true;
		} catch (Exception $e) {
			$this->isConnected = false;
			$this->connection = null;
			
			echo 'Could not connect to MongoDB client.' . "\n";
		}	
	}

	public function getCollection($db, $collectionName)
	{
		if ($this->isConnected) {
			$collection = $this->connection->$db->$collectionName;
		}
		
		return $collection;
	}
}
?>
