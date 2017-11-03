<?php
namespace UCDavis\Controllers\Services;

use Khill\Lavacharts\Lavacharts;
use UCDavis\Exceptions\InvalidColumnType;
use UCDavis\Connections\SQLConnection;

/**
 * Service class to assist in chart formatting.
 */
class ChartServices
{
	const LABEL_REGEX = '/(?=[A-Z])|(?=[0-9]{4})/';

	private $queryResult;
	private $dataTable;
	private $keys;
	private $values;
	private $types;
	private $lava;

	/**
	 * Creates a JSON format chart to be consumed by Google Chart API.
	 *
	 * @param PDO[] queryResult Result from PDO query
	 * @param string[] types Data types
	 * @param string title Title of the table
	 * @param string chartType
	 * @return JSON to be consumed by Google Chart API
	 */
	public function createChart($queryResult, $types, $title, $chartType)
	{
		$this->lava = new Lavacharts;

		$this->setData($queryResult, $types);
		$this->setColumns();
		$this->setRowData();

		$chart = $this->lava->$chartType($title, $this->dataTable);

		return $chart->getDataTableJson();
	}

	/**
	 * Set data that is used to generate a chart.
   
 	 * @param string queryResult
   * @param string types
	 */
	private function setData($queryResult, $types)
	{
		$this->queryResult = $queryResult;
		$this->dataTable = $this->lava->DataTable();
		$this->keys = $this->splitLabels(array_keys($this->queryResult[0]));
		$this->values = array_values($this->queryResult);
		$this->types = $types;
	}
	
	/** 
	 * Create each column based on column type and use the array key as a 
	 * header. Keys need to match position with column types. 
	 */
	// TODO: Decrease coupling between column type and key.
	private function setColumns()
	{
		$i = 0;

		foreach ($this->types as $type) {
			switch (preg_replace("/\([^)]+\)/", "", $type['Type'])) {
				case ColumnTypes::COL_VARCHAR:
					$this->dataTable
					     ->addColumn(ColumnTypes::COL_STRING, $this->keys[$i]);
					break;
				case ColumnTypes::COL_TEXT:
					$this->dataTable
						 ->addColumn(ColumnTypes::COL_STRING, $this->keys[$i]);
					break;
				case ColumnTypes::COL_TINYTEXT:
					$this->dataTable
						 ->addColumn(ColumnTypes::COL_STRING, $this->keys[$i]);
					break;
				case ColumnTypes::COL_INT:
					$this->dataTable
					     ->addColumn(ColumnTypes::COL_NUMBER, $this->keys[$i]);
					break;
				case ColumnTypes::COL_DECIMAL:
					$this->dataTable
						 ->addColumn(ColumnTypes::COL_NUMBER, $this->keys[$i]);
					break;
				case ColumnTypes::COL_STRING:
					$this->dataTable
						 ->addColumn(ColumnTypes::COL_STRING, $this->keys[$i]);
					break;
				case ColumnTypes::COL_DATE:
					$this->dataTable
						 ->addColumn(ColumnTypes::COL_DATE, $this->keys[$i]);
					break;
				case ColumnTypes::COL_DATETIME:
					$this->dataTable
						 ->addColumn(ColumnTypes::COL_DATETIME, $this->keys[$i]);
					break;
				default:
					throw new InvalidColumnType($type['Type']);
			}

			$i++;
		}
	}

	/**
	 * Populate data table with data from query.
	 */
	private function setRowData()
	{
		foreach ($this->values as $value) {
			// MySQL driver returns numeric values as strings so parse.
			$this->dataTable->addRow($this->parseNum(array_values($value)));
		}
	}
  /**
	 * Splits column labels
	 */
	private function splitLabels($labels)
	{
		$results = array();

		foreach ($labels as $label) {
			$newLabel = preg_split(self::LABEL_REGEX, $label
				, null, PREG_SPLIT_NO_EMPTY);
			array_push($results, implode(" ", $newLabel));
		}
		
		return $results;
	}
  
	/**
	 * Parses numeric values from PDO response.
	 */
	private function parseNum($rowValues)
	{
		$row = array();

		foreach ($rowValues as $value) {
			if (is_numeric($value)) {
				$number = strpos($value, '.') === false ? intval($value) : floatval($value);
				array_push($row, $number);
			}
			else {
				array_push($row, $value);
			}
		}

		return $row;
	}
}
?>
