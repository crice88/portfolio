<?php
namespace UCDavis\Exceptions;

class InvalidChartType extends UCDavisException
{
	public function __construct($chartType)
	{
		$message = "The chart type '$chartType' is not supported.";

		parent::__construct($message);
	}
}
?>
