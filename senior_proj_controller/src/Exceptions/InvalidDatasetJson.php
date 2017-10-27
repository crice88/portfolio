<?php
namespace UCDavis\Exceptions;

use UCDavis\Exceptions\UCDavisException;

class InvalidDatasetJson extends UCDavisException
{
	public function __construct()
	{
		$message = "Received JSON is invalid.";

		parent::__construct($message);
	}
}
?>
