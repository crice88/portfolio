<?php
namespace UCDavis\DataAccess;

use UCDavis\Connections\SQLConnection;

class MapDAO
{
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

	public function getMap($mapId)
	{
			$query = 'SELECT Address, Latitude, Longitude '
				. 'FROM maptwo '
				. 'WHERE MapID= ' . $mapId;

			$result = $this->connection->runQuery($query);

			return $result;
	}
}
?>
