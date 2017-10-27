<?php
namespace UCDavis\DataAccess;

use UCDavis\Connections\MongoConnection;
use UCDavis\DataAccess\DatasetDAO;

class MongoDAO
{
	const DATABASE_NAME = 'charts';

	private $mongo;
	private $datasetName;

	public function __construct($datasetId)
	{
		$dao = new DatasetDAO(self::DATABASE_NAME);

		$this->mongo = new MongoConnection;
		$this->datasetName = $dao->getDatasetName($datasetId);
	}

	public function getOptions()
	{
		if ($this->mongo->isConnected) {
			$collection = $this->mongo->getCollection(self::DATABASE_NAME,
				strtolower($this->datasetName) . 'Options');

			$result = $collection->find([], ['projection' => ['_id' => 0]]);

			$resultArr = iterator_to_array($result);

			return $resultArr[0];
		}
	}
}
?>
