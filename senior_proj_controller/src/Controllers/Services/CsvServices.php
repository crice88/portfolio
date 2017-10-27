<?php

namespace UCDavis\Controllers\Services;

use PhpOffice\PhpSpreadsheet\IOFactory;

class CsvServices
{
	private $csv;

	public function __construct($csv)
	{
		$this->csv = base64_decode($csv);	
	}

	public function convertCsvToArray()
	{
		$tmpName = tempnam(sys_get_temp_dir(), "TempCSV");
		$fp = fopen($tmpName, "w");

		fwrite($fp, $this->csv);
		fseek($fp, 0);

		$csvArr =  array_map('str_getcsv', file($tmpName));

		fclose($fp);
		
		return $csvArr;
	}

	public function convertExcelToArray()
	{
        $tmpName = tempnam(sys_get_temp_dir(), "TempCSV");
        file_put_contents($tmpName, $this->csv);

        $excel = IOFactory::load($tmpName);
        $csvWriter = IOFactory::createWriter($excel, 'Csv');

        $csvWriter->save("$tmpName");

        $csv = file_get_contents($tmpName);

        $fp = fopen($tmpName, "w");

        fwrite($fp, $csv);
        fseek($fp, 0);

        $csvArr =  array_map('str_getcsv', file($tmpName));

        fclose($fp);
		return $csvArr;
	}
}
?>
