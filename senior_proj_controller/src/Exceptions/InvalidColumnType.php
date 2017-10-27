<?php
namespace UCDavis\Exceptions;

class InvalidColumnType extends UCDavisException
{
	public function __construct($columnType)
	{
		$message = "The column type '$columnType' is not a valid column type. "
			. "Please check table schema definition.";

		parent::__construct($message);
	}
}
?>
