<?php
namespace UCDavis\Exceptions;

class InvalidAction extends UCDavisException
{
	public function __construct($controller, $action)
	{
		$message = "The method '$action' does not exist "
			. "in the set controller '$controller'.";

		parent::__construct($message);
	}
}
?>
