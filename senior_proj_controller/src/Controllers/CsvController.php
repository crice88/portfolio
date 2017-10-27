<?php
namespace UCDavis\Controllers;

use UCDavis\DataAccess\DatasetDAO;
use UCDavis\DataAccess\DatasetInfoDAO;
use UCDavis\Controllers\Services\CsvServices;

use UCDavis\Exceptions\InvalidDatasetJson;

class CsvController
{
	private $csv;
	private $headers;
	private $datasetJson;

	public function insertDataset()
	{
		$dao = new DatasetDAO('charts');
		$daoInfo = new DatasetInfoDAO('charts');

		$this->getFile();
		$this->getColumnNames();
			
		$dao->createDatasetTable($this->datasetJson->tableName, $this->headers);
		$dao->insertDataset($this->datasetJson->tableName, $this->csv);
		$daoInfo->insertDatasetInfo($this->datasetJson);
	}

	private function getFile()
	{
		$str_json = file_get_contents('php://input');

		if (!isset($str_json)) {
			throw new InvalidDatasetJson();			
		}

		$this->datasetJson = json_decode($str_json);

        $csvService = new CsvServices($this->datasetJson->base64);


        /*
         * Check from $this->datasetJson if file type is xls, xlsx, or csv
         * If none of the above, abort mission
         */

        if ($this->datasetJson->ext == "csv") {
            $this->csv = $csvService->convertCsvToArray();
        }
		else if ($this->datasetJson->ext == "xls" || $this->datasetJson->ext == "xlsx") {
            $this->csv = $csvService->convertExcelToArray();
        }
	}

	private function getColumnNames()
	{
		$this->headers = array_shift($this->csv);	
	}
}
?>
