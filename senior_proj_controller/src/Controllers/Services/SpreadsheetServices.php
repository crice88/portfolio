<?php
namespace UCDavis\Controllers\Services;

use UCDavis\DataAccess\DatasetDAO;
use UCDavis\Exceptions\InvalidFileType;
use PhpOffice\PhpSpreadsheet\IOFactory;
use PhpOffice\PhpSpreadsheet\Spreadsheet;
use PhpOffice\PhpSpreadsheet\Settings;

class SpreadsheetServices
{
	const DB_NAME = 'charts';

	private $dataset;
	private $datasetName;
	private $spreadsheet;
	private $fileType;
	private $rendererName = Settings::PDF_RENDERER_DOMPDF;
	
	public function __construct($datasetId, $fileType)
	{
		$dao = new DatasetDAO(self::DB_NAME);
		
		$this->fileType = strtolower($fileType);
		$this->datasetName = $dao->getDatasetName($datasetId);
		$this->dataset = $dao->getDataset($datasetId);
		$this->spreadsheet = new Spreadsheet();
	}

	public function setDocumentProperties()
	{
		$this->spreadsheet->getProperties()
			->setTitle($this->datasetName);
	}

	public function setDocumentData()
	{
		$this->setSpreadsheetHeader();
		$this->setSpreadsheetData();
	}
	
	public function saveDocument()
	{
		$this->setDocumentProperties();
		$this->setDocumentData();
		$this->httpHeader();
		
		if ($this->fileType == FileTypes::PDF)
			Settings::setPdfRendererName($this->rendererName);

		$writer = IOFactory::createWriter($this->spreadsheet, ucfirst($this->fileType));
		$writer->save('php://output');
		
		exit;
	}
	
	private function httpHeader()
	{
		if (array_key_exists($this->fileType, ContentTypes::CONTENT_TYPE_MAP)) {
			header(ContentTypes::CONTENT_TYPE_MAP[$this->fileType]);
			header(ContentTypes::CONTENT_DISPOSITION .'"' . $this->getFileName() . '"');
			header(ContentTypes::CONTENT_CACHE);
		} else {
			throw new InvalidFileType($this->fileType);
		}
	}

	private function getFileName()
	{
		return $this->datasetName . '.' . $this->fileType;
	}

	private function setSpreadsheetHeader()
	{
		$columnName = 'A';
		$row = '1';

		$headers = array_keys($this->dataset[0]);
		
		foreach ($headers as $header) {
			$this->spreadsheet->setActiveSheetIndex(0)
				->setCellValue($columnName . $row, $header);

			$columnName++;
		}
	}

	private function setSpreadsheetData()
	{
		$row = '2'; // Headers are row 1

		$results = array_values($this->dataset);
		
		foreach($results as $result) {
			$columnName = 'A';
			
			foreach($result as $item) {
				$this->spreadsheet->setActiveSheetIndex(0)
					->setCellValue($columnName . $row, $item);
				
				$columnName++;
			}
			
			$row++;
		}
	}
}
?>
