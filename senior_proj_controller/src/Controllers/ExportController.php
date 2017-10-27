<?php
namespace UCDavis\Controllers;

use UCDavis\Controllers\Services\SpreadsheetServices;

class ExportController 
{
	public function downloadDataset($datasetId, $fileType)
	{
		$service = new SpreadsheetServices($datasetId, $fileType);

		$service->saveDocument();
	}
}
?>
