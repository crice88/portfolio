<?php
namespace UCDavis\Exceptions;

class InvalidController extends UCDavisException
{
	public function __construct($controller)
	{
		$message = "The controller '$controller' does not exist.";

		parent::__construct($message);
	}
}
?>
