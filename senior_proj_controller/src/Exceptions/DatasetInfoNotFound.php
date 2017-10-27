<?php
namespace UCDavis\Exceptions;

class DatasetInfoNotFound extends UCDavisException
{
	public function __construct()
	{
		$message = "The dataset info could not be found.";

		parent::__construct($message);
	}
}
?>
