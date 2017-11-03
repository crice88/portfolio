<?php

namespace UCDavis\Controllers\Services;

use PhpOffice\PhpSpreadsheet\IOFactory;

/**
 * Provides services for uploaded Excel files.
 */
class CsvServices
{
	private $csv;

	public function __construct($csv)
	{
	  // CSV will be Base654 encoded.
		$this->csv = base64_decode($csv);	
	}
  
	/**
	 * Converts uploaded CSV to an array.
	 *
	 * @return string[]
	 */
	public function convertCsvToArray()
	{
	  // TODO: Abstract common file handling functionality.
		$tmpName = tempnam(sys_get_temp_dir(), "TempCSV");
		$fp = fopen($tmpName, "w");

		fwrite($fp, $this->csv);
		fseek($fp, 0);

		$csvArr =  array_map('str_getcsv', file($tmpName));

		fclose($fp);
		
		return $csvArr;
	}
  
	/**
	 * Converts Excel formats to an array for consumption
	 * with export controller.
   *
	 * @return string[]
	 */
	public function convertExcelToArray()
	{
		// TODO: Abstract common file handling functionality.
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
