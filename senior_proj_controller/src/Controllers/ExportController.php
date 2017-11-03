<?php
namespace UCDavis\Controllers;

use UCDavis\Controllers\Services\SpreadsheetServices;

/**
 * Downloads dataset by id and file type.
 */
class ExportController 
{

	/**
	 * Saves document to ouput stream
	 *
	 * @param string datasetId
	 * @param string fileType
	 */
	public function downloadDataset($datasetId, $fileType)
	{
		$service = new SpreadsheetServices($datasetId, $fileType);

		$service->saveDocument();
	}
}
?>
